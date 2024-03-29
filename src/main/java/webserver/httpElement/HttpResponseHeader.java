package webserver.httpElement;

import java.util.HashMap;
import java.util.Map;
import utils.ExtensionType;

public class HttpResponseHeader {

    // 응답 헤더 구조를 만들기 위한 객체이다
    private String httpVersion;
    private String statusCode;
    private final static String NEW_LINE = "\r\n";
    private final static String SPACE = " ";
    private final Map<String, String> headers = new HashMap<>();

    private HttpResponseHeader() {}

    public static HttpResponseHeader make200Header(int lengthOfBodyContent, String fileUrl) {
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.set200header(lengthOfBodyContent, fileUrl);
        return httpResponseHeader;
    }

    public static HttpResponseHeader make302Header(int lengthOfBodyContent, String fileUrl) {
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.set302header(lengthOfBodyContent, fileUrl);
        return httpResponseHeader;
    }

    public static HttpResponseHeader make302SetCookieHeader(int lengthOfBodyContent, String fileUrl, String sid) {
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.set302SetCookieHeader(lengthOfBodyContent, fileUrl, sid);
        return httpResponseHeader;
    }

    public static HttpResponseHeader make302DeleteCookieHeader(int lengthOfBodyContent, String sid) {
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.set302DeleteCookieHeader(lengthOfBodyContent, sid);
        return httpResponseHeader;
    }

    public static HttpResponseHeader make404Header(int lengthOfBodyContent) {
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.set404Header(lengthOfBodyContent);
        return httpResponseHeader;
    }

    private void set200header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        httpVersion = "HTTP/1.1";
        statusCode = "200 OK";
        headers.put("Content-Type", String.format("%s;charset=utf-8", contentType));
        headers.put("Content-Length", String.valueOf(lengthOfBodyContent));
    }

    private void set302header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        httpVersion = "HTTP/1.1";
        statusCode = "302 Found";
        headers.put("Content-Type", String.format("%s;charset=utf-8", contentType));
        headers.put("Content-Length", String.valueOf(lengthOfBodyContent));
        headers.put("Location", fileUrl);
    }

    private void set302SetCookieHeader(int lengthOfBodyContent, String fileUrl, String sid) {
        String contentType = ExtensionType.getContentType(fileUrl);
        httpVersion = "HTTP/1.1";
        statusCode = "302 Found";
        headers.put("Content-Type", String.format("%s;charset=utf-8", contentType));
        headers.put("Content-Length", String.valueOf(lengthOfBodyContent));
        headers.put("Location", fileUrl);
        headers.put("Set-Cookie",String.format("sid=%s; Path=/", sid));
    }

    private void set302DeleteCookieHeader(int lengthOfBodyContent, String sid) {
        String redirection = "/index.html";
        String contentType = ExtensionType.getContentType(redirection);

        httpVersion = "HTTP/1.1";
        statusCode = "302 Found";
        headers.put("Content-Type", String.format("%s;charset=utf-8", contentType));
        headers.put("Content-Length", String.valueOf(lengthOfBodyContent));
        headers.put("Location", redirection);
        headers.put("Set-Cookie",String.format("sid=%s; Max-Age= 0; Path=/", sid));
    }

    private void set404Header(int lengthOfBodyContent) {
        httpVersion = "HTTP/1.1";
        statusCode = "404 Not Found";
        headers.put("Content-Type", "%s;charset=utf-8");
        headers.put("Content-Length", String.valueOf(lengthOfBodyContent));
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpVersion).append(SPACE).append(statusCode).append(NEW_LINE);

        headers.entrySet().stream()
                .forEach(entry ->
                        stringBuffer.append(entry.getKey())
                                .append(":")
                                .append(SPACE)
                                .append(entry.getValue())
                                .append(NEW_LINE));
        stringBuffer.append(NEW_LINE);
        return stringBuffer.toString();
    }
}
