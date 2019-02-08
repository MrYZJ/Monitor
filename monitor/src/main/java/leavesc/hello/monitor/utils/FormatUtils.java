package leavesc.hello.monitor.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import leavesc.hello.monitor.model.HttpHeader;
import leavesc.hello.monitor.model.HttpInformation;
import okhttp3.Headers;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:04
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class FormatUtils {

    public static String formatHeaders(List<HttpHeader> httpHeaders, boolean withMarkup) {
        String out = "";
        if (httpHeaders != null) {
            for (HttpHeader header : httpHeaders) {
                out += ((withMarkup) ? "<b>" : "") + header.getName() + ": " + ((withMarkup) ? "</b>" : "") +
                        header.getValue() + ((withMarkup) ? "<br />" : "\n");
            }
        }
        return out;
    }

    public static String formatBody(String body, String contentType) {
        if (contentType != null && contentType.toLowerCase().contains("json")) {
            return FormatUtils.formatJson(body);
        } else if (contentType != null && contentType.toLowerCase().contains("xml")) {
            return FormatUtils.formatXml(body);
        } else {
            return body;
        }
    }


    public static String formatByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String formatJson(String json) {
        try {
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(json);
            return JsonConverter.getInstance().toJson(je);
        } catch (Exception e) {
            return json;
        }
    }

    public static String formatXml(String xml) {
        try {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
        } catch (Exception e) {
            return xml;
        }
    }

//    public static String getShareText(Context context, HttpInformation transaction) {
//        String text = "";
//        text += "Url" + ": " + v(transaction.getUrl()) + "\n";
//        text += "Method" + ": " + v(transaction.getMethod()) + "\n";
//        text += "Protocol" + ": " + v(transaction.getProtocol()) + "\n";
//        text += "Status" + ": " + v(transaction.getStatus().toString()) + "\n";
//        text += "Response" + ": " + v(transaction.getResponseSummaryText()) + "\n";
//        text += "SSL" + ": " + transaction.isSsl() + "\n";
//        text += "\n";
//        text += "Request Time" + ": " + v(transaction.getRequestDateString()) + "\n";
//        text += "Response Time" + ": " + v(transaction.getResponseDateString()) + "\n";
//        text += "Duration" + ": " + v(transaction.getDurationString()) + "\n";
//        text += "\n";
//        text += "Request Size" + ": " + v(transaction.getRequestSizeString()) + "\n";
//        text += "Response Size" + ": " + v(transaction.getResponseSizeString()) + "\n";
//        text += "Total Size" + ": " + v(transaction.getTotalSizeString()) + "\n";
//        text += "\n";
//        text += "---------- " + "Request" + " ----------\n\n";
//        String headers = formatHeaders(transaction.getRequestHeaders(), false);
//        if (!TextUtils.isEmpty(headers)) {
//            text += headers + "\n";
//        }
//        text += (transaction.requestBodyIsPlainText()) ? v(transaction.getFormattedRequestBody()) :
//                "encoded or binary body omitted";
//        text += "\n\n";
//        text += "---------- " + "Response" + " ----------\n\n";
//        headers = formatHeaders(transaction.getResponseHeaders(), false);
//        if (!TextUtils.isEmpty(headers)) {
//            text += headers + "\n";
//        }
//        text += (transaction.responseBodyIsPlainText()) ? v(transaction.getFormattedResponseBody()) :
//                "(encoded or binary body omitted)";
//        return text;
//    }

    public static String getShareCurlCommand(HttpInformation transaction) {
        boolean compressed = false;
        String curlCmd = "curl";
        curlCmd += " -X " + transaction.getMethod();
        Headers headers = transaction.getRequestHeaders();
        List<HttpHeader> headerList = new ArrayList<>();
        for (int i = 0, count = headers.size(); i < count; i++) {
            headerList.add(new HttpHeader(headers.name(i), headers.value(i)));
        }
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headerList.get(i).getName();
            String value = headerList.get(i).getValue();
            if ("Accept-Encoding".equalsIgnoreCase(name) && "gzip".equalsIgnoreCase(value)) {
                compressed = true;
            }
            curlCmd += " -H " + "\"" + name + ": " + value + "\"";
        }
        String requestBody = transaction.getRequestBody();
        if (requestBody != null && requestBody.length() > 0) {
            // try to keep to a single line and use a subshell to preserve any line breaks
            curlCmd += " --data $'" + requestBody.replace("\n", "\\n") + "'";
        }
        curlCmd += ((compressed) ? " --compressed " : " ") + transaction.getUrl();
        return curlCmd;
    }

    private static String v(String string) {
        return (string != null) ? string : "";
    }

}
