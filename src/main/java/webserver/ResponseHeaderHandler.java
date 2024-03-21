package webserver;

import utils.HttpMethods;

public class ResponseHeaderHandler {
    private Request request;

    public ResponseHeaderHandler(Request request) {
        this.request = request;
    }

    public String createHeader(int lengthOfBodyContent, String fileUrl) {
        if (request.getHttpMethod().equals(HttpMethods.GET)) {
            return get200Header(lengthOfBodyContent, fileUrl);
        }
        return get302Header(lengthOfBodyContent, fileUrl);
    }

    public String get302Header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 302 Found\r\n");
        stringBuffer.append(String.format("Location: %s\r\n", fileUrl));
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public String get200Header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 200 OK\r\n");
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }
}