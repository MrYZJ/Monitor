package leavesc.hello.monitor;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import leavesc.hello.monitor.db.MonitorHttpInformationDatabase;
import leavesc.hello.monitor.db.entity.HttpInformation;
import leavesc.hello.monitor.holder.ContextHolder;
import leavesc.hello.monitor.holder.NotificationHolder;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:06
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorInterceptor implements Interceptor {

    private static final String TAG = "MonitorInterceptor";

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private Context context;

    private long maxContentLength = 250000L;

    public MonitorInterceptor(Context context) {
        this.context = context.getApplicationContext();
        ContextHolder.setContext(context);
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        HttpInformation httpInformation = new HttpInformation();
        httpInformation.setRequestDate(new Date());
        httpInformation.setRequestHttpHeaders(request.headers());
        httpInformation.setMethod(request.method());
        String url = request.url().toString();
        httpInformation.setUrl(url);
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            httpInformation.setHost(uri.getHost());
            httpInformation.setPath(uri.getPath() + ((uri.getQuery() != null) ? "?" + uri.getQuery() : ""));
            httpInformation.setScheme(uri.getScheme());
        }
        if (requestBody != null) {
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                httpInformation.setRequestContentType(contentType.toString());
            }
            if (requestBody.contentLength() != -1) {
                httpInformation.setRequestContentLength(requestBody.contentLength());
            }
        }
        httpInformation.setRequestBodyIsPlainText(!bodyHasUnsupportedEncoding(request.headers()));
        if (requestBody != null && httpInformation.isRequestBodyIsPlainText()) {
            BufferedSource source = getNativeSource(new Buffer(), bodyGzipped(request.headers()));
            Buffer buffer = source.buffer();
            requestBody.writeTo(buffer);
            Charset charset;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(CHARSET_UTF8);
            } else {
                charset = CHARSET_UTF8;
            }
            if (isPlaintext(buffer)) {
                httpInformation.setRequestBody(readFromBuffer(buffer, charset));
            } else {
                httpInformation.setResponseBodyIsPlainText(false);
            }
        }
        long id = insert(httpInformation);
        httpInformation.setId(id);
        long startTime = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            httpInformation.setError(e.toString());
            update(httpInformation);
            throw e;
        }
        httpInformation.setResponseDate(new Date());
        httpInformation.setDuration(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
        httpInformation.setRequestHttpHeaders(response.request().headers());
        httpInformation.setProtocol(response.protocol().toString());
        httpInformation.setResponseCode(response.code());
        httpInformation.setResponseMessage(response.message());
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            httpInformation.setResponseContentLength(responseBody.contentLength());
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                httpInformation.setResponseContentType(contentType.toString());
            }
        }
        httpInformation.setResponseHttpHeaders(response.headers());
        httpInformation.setResponseBodyIsPlainText(!bodyHasUnsupportedEncoding(response.headers()));
        if (HttpHeaders.hasBody(response) && httpInformation.isResponseBodyIsPlainText()) {
            BufferedSource source = getNativeSource(response);
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = CHARSET_UTF8;
            if (responseBody != null) {
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(CHARSET_UTF8);
                    } catch (UnsupportedCharsetException e) {
                        update(httpInformation);
                        return response;
                    }
                }
            }
            if (isPlaintext(buffer)) {
                httpInformation.setResponseBody(readFromBuffer(buffer.clone(), charset));
            } else {
                httpInformation.setResponseBodyIsPlainText(false);
            }
            httpInformation.setResponseContentLength(buffer.size());
        }
        update(httpInformation);
        return response;
    }

    private long insert(HttpInformation httpInformation) {
        showNotification(httpInformation);
        return MonitorHttpInformationDatabase.getInstance(context).getHttpInformationDao().insert(httpInformation);
    }

    private void update(HttpInformation httpInformation) {
        showNotification(httpInformation);
        MonitorHttpInformationDatabase.getInstance(context).getHttpInformationDao().update(httpInformation);
    }

    private void showNotification(HttpInformation httpInformation) {
        NotificationHolder.getInstance(context).show(httpInformation);
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    private boolean bodyHasUnsupportedEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null &&
                !contentEncoding.equalsIgnoreCase("identity") &&
                !contentEncoding.equalsIgnoreCase("gzip");
    }

    private String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body;
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
            body = "\\n\\n--- Unexpected end of content ---";
        }
        if (bufferSize > maxContentLength) {
            body += "\\n\\n--- Content truncated ---";
        }
        return body;
    }

    private BufferedSource getNativeSource(Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(maxContentLength).source();
            if (source.buffer().size() < maxContentLength) {
                return getNativeSource(source, true);
            } else {
                Log.e(TAG, "gzip encoded response was too long");
            }
        }
        return response.body().source();
    }

    private BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }

    private boolean bodyGzipped(Headers headers) {
        return "gzip".equalsIgnoreCase(headers.get("Content-Encoding"));
    }

    public MonitorInterceptor maxContentLength(long max) {
        this.maxContentLength = max;
        return this;
    }

}