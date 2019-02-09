package leavesc.hello.monitor.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
@Entity(tableName = "monitor_httpInformation")
public class HttpInformation {

    private static final int DEFAULT_RESPONSE_CODE = -100;

    public enum Status {Requested, Complete, Failed}

    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date requestDate;
    private Date responseDate;
    private long duration;
    private String method;
    private String url;
    private String host;
    private String path;
    private String scheme;
    private String protocol;

    private String requestHeaders;
    private String requestBody;
    private String requestContentType;
    private long requestContentLength;
    private boolean requestBodyIsPlainText = true;

    private int responseCode = DEFAULT_RESPONSE_CODE;
    private String responseHeaders;
    private String responseBody;
    private String responseMessage;
    private String responseContentType;
    private long responseContentLength;
    private boolean responseBodyIsPlainText = true;

    private String error;

    public void setRequestHttpHeaders(Headers headers) {
        if (headers != null) {
            List<HttpHeader> httpHeaders = new ArrayList<>();
            for (int i = 0, count = headers.size(); i < count; i++) {
                httpHeaders.add(new HttpHeader(headers.name(i), headers.value(i)));
            }
            setRequestHeaders(JsonConverter.getInstance().toJson(httpHeaders));
        } else {
            setRequestHeaders(null);
        }
    }

    public void setResponseHttpHeaders(Headers headers) {
        if (headers != null) {
            List<HttpHeader> httpHeaders = new ArrayList<>();
            for (int i = 0, count = headers.size(); i < count; i++) {
                httpHeaders.add(new HttpHeader(headers.name(i), headers.value(i)));
            }
            setResponseHeaders(JsonConverter.getInstance().toJson(httpHeaders));
        } else {
            setResponseHeaders(null);
        }
    }

    public HttpInformation.Status getStatus() {
        if (error != null) {
            return HttpInformation.Status.Failed;
        } else if (responseCode == HttpInformation.DEFAULT_RESPONSE_CODE) {
            return HttpInformation.Status.Requested;
        } else {
            return HttpInformation.Status.Complete;
        }
    }

    public String getNotificationText() {
        switch (getStatus()) {
            case Failed:
                return " ! ! !  " + path;
            case Requested:
                return " . . .  " + path;
            default:
                return String.valueOf(responseCode) + " " + path;
        }
    }

    public String getResponseSummaryText() {
        switch (getStatus()) {
            case Failed:
                return error;
            case Requested:
                return null;
            default:
                return String.valueOf(responseCode) + " " + responseMessage;
        }
    }

    private List<HttpHeader> getRequestHeaderList() {
        return JsonConverter.getInstance().fromJson(requestHeaders,
                new TypeToken<List<HttpHeader>>() {
                }.getType());
    }

    public String getRequestHeadersString(boolean withMarkup) {
        return FormatUtils.formatHeaders(getRequestHeaderList(), withMarkup);
    }

    public String getFormattedRequestBody() {
        return FormatUtils.formatBody(requestBody, requestContentType);
    }

    public String getFormattedResponseBody() {
        return FormatUtils.formatBody(responseBody, responseContentType);
    }

    private List<HttpHeader> getResponseHeaderList() {
        return JsonConverter.getInstance().fromJson(responseHeaders,
                new TypeToken<List<HttpHeader>>() {
                }.getType());
    }

    public String getResponseHeadersString(boolean withMarkup) {
        return FormatUtils.formatHeaders(getResponseHeaderList(), withMarkup);
    }

    public String getDurationFormat() {
        return duration + " ms";
    }

    public boolean isSsl() {
        return "https".equalsIgnoreCase(scheme);
    }

    public String getTotalSizeString() {
        return FormatUtils.formatBytes(requestContentLength + responseContentLength);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
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

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpInformation that = (HttpInformation) o;
        return id == that.id &&
                duration == that.duration &&
                requestContentLength == that.requestContentLength &&
                requestBodyIsPlainText == that.requestBodyIsPlainText &&
                responseCode == that.responseCode &&
                responseContentLength == that.responseContentLength &&
                responseBodyIsPlainText == that.responseBodyIsPlainText &&
                Objects.equals(requestDate, that.requestDate) &&
                Objects.equals(responseDate, that.responseDate) &&
                Objects.equals(method, that.method) &&
                Objects.equals(url, that.url) &&
                Objects.equals(host, that.host) &&
                Objects.equals(path, that.path) &&
                Objects.equals(scheme, that.scheme) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(requestHeaders, that.requestHeaders) &&
                Objects.equals(requestBody, that.requestBody) &&
                Objects.equals(requestContentType, that.requestContentType) &&
                Objects.equals(responseHeaders, that.responseHeaders) &&
                Objects.equals(responseBody, that.responseBody) &&
                Objects.equals(responseMessage, that.responseMessage) &&
                Objects.equals(responseContentType, that.responseContentType) &&
                Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestDate, responseDate, duration, method, url, host, path, scheme, protocol, requestHeaders, requestBody, requestContentType, requestContentLength, requestBodyIsPlainText, responseCode, responseHeaders, responseBody, responseMessage, responseContentType, responseContentLength, responseBodyIsPlainText, error);
    }

}