package webserver;

public class HeaderHandler {

    public String get200Header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 200 OK \r\n");
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    /**
     *      우리 서버에서는 404일 경우 정해진 body 타입이 있기 때문에,
     *      일단은 따로 Content Type을 변수로 주지 않아도 된다.
     */
    public String get404Header(int lengthOfBodyContent) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 404 Not Found \r\n");
        stringBuffer.append("Content-Type: %s;charset=utf-8\r\n");
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }
}