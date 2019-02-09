package leavesc.hello.monitor.model;

import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import leavesc.hello.monitor.db.entity.MonitorHttpInformation;
import leavesc.hello.monitor.utils.FormatUtils;
import leavesc.hello.monitor.utils.JsonConverter;
import okhttp3.Headers;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:01
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class HttpInformation {

    public static final int DEFAULT_RESPONSE_CODE = -100;

    private Date requestDate;
    private Date responseDate;
    private long duration;
    private String method;
    private String url;
    private String protocol;

    private Headers requestHeaders;
    private String requestBody;
    private String requestContentType;
    private long requestContentLength;

    private boolean requestBodyIsPlainText = true;

    private int responseCode = DEFAULT_RESPONSE_CODE;
    private Headers responseHeaders;
    private String responseBody;
    private String responseMessage;
    private String responseContentType;
    private long responseContentLength;

    private boolean responseBodyIsPlainText = true;

    private String error;

    public MonitorHttpInformation constModel() {
        MonitorHttpInformation pack = new MonitorHttpInformation();
        pack.setRequestDate(requestDate);
        pack.setResponseDate(responseDate);
        pack.setDuration(duration);
        pack.setMethod(method);
        pack.setUrl(url);
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            pack.setHost(uri.getHost());
            pack.setPath(uri.getPath() + ((uri.getQuery() != null) ? "?" + uri.getQuery() : ""));
            pack.setScheme(uri.getScheme());
        }
        pack.setProtocol(protocol);
        if (requestHeaders != null) {
            List<HttpHeader> httpHeaders = new ArrayList<>();
            for (int i = 0, count = requestHeaders.size(); i < count; i++) {
                httpHeaders.add(new HttpHeader(requestHeaders.name(i), requestHeaders.value(i)));
            }
            pack.setRequestHeaders(JsonConverter.getInstance().toJson(httpHeaders));
        }
        pack.setRequestBody(FormatUtils.formatBody(requestBody, requestContentType));
        pack.setRequestContentType(requestContentType);
        pack.setRequestContentLength(requestContentLength);
        pack.setRequestBodyIsPlainText(requestBodyIsPlainText);
        pack.setResponseCode(responseCode);
        if (responseHeaders != null) {
            List<HttpHeader> httpHeaders = new ArrayList<>();
            for (int i = 0, count = responseHeaders.size(); i < count; i++) {
                httpHeaders.add(new HttpHeader(responseHeaders.name(i), responseHeaders.value(i)));
            }
            pack.setResponseHeaders(JsonConverter.getInstance().toJson(httpHeaders));
        }
        pack.setResponseBody(FormatUtils.formatBody(responseBody, responseContentType));
        pack.setResponseMessage(responseMessage);
        pack.setRequestContentType(responseContentType);
        pack.setResponseContentLength(responseContentLength);
        pack.setResponseBodyIsPlainText(responseBodyIsPlainText);
        pack.setError(error);
        return pack;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Headers getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Headers requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public long getRequestContentLength() {
        return requestContentLength;
    }

    public void setRequestContentLength(long requestContentLength) {
        this.requestContentLength = requestContentLength;
    }

    public boolean isRequestBodyIsPlainText() {
        return requestBodyIsPlainText;
    }

    public void setRequestBodyIsPlainText(boolean requestBodyIsPlainText) {
        this.requestBodyIsPlainText = requestBodyIsPlainText;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Headers responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public long getResponseContentLength() {
        return responseContentLength;
    }

    public void setResponseContentLength(long responseContentLength) {
        this.responseContentLength = responseContentLength;
    }

    public boolean isResponseBodyIsPlainText() {
        return responseBodyIsPlainText;
    }

    public void setResponseBodyIsPlainText(boolean responseBodyIsPlainText) {
        this.responseBodyIsPlainText = responseBodyIsPlainText;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
