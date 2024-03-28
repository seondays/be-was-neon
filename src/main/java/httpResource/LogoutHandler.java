package httpResource;

import httpMethods.PostHandler;
import utils.ExtensionType;
import webserver.Request;
import webserver.handler.SessionHandler;

public class LogoutHandler implements PostHandler {
    private byte[] responseBody;
    private String responseHeader;
    private final Request request;

    public LogoutHandler(Request request) {
        this.request = request;
    }

    public void run() throws Exception {
        String sid = request.getHeader().getSidCookie();
        deleteSession(sid);
        responseBody = makeEmptyBody();
        responseHeader = getCookieDeleteHeader(responseBody.length,sid);
    }

    // 쿠키값을 읽어서 해당 세션 넘버를 찾아서 삭제
    public void deleteSession(String sid) {
        SessionHandler.deleteSession(sid);
    }

    // 쿠키를 삭제한다는 것은 헤더에 다시 만료된 헤더를 담어주는 것
    public String getCookieDeleteHeader(int lengthOfBodyContent, String sid) {
        String redirection = "/index.html";
        String contentType = ExtensionType.getContentType(redirection);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 302 Found\r\n");
        stringBuffer.append(String.format("Location: %s\r\n", redirection));
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append(String.format("Set-Cookie: sid=%s; Max-Age= 0; Path=/", sid));
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    private byte[] makeEmptyBody() {
        return new byte[0];
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }
}
