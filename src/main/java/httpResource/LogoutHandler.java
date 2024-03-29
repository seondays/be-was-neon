package httpResource;

import httpMethods.PostHandler;
import webserver.Request;
import webserver.handler.SessionHandler;
import webserver.httpElement.HttpResponseHeader;

public class LogoutHandler implements PostHandler {
    private byte[] responseBody;
    private HttpResponseHeader responseHeader;
    private final Request request;

    public LogoutHandler(Request request) {
        this.request = request;
    }

    public void run() throws Exception {
        String sid = request.getHeader().getSidCookie();
        deleteSession(sid);
        responseBody = makeEmptyBody();
        responseHeader = HttpResponseHeader.make302DeleteCookieHeader(responseBody.length,sid);
    }

    // 쿠키값을 읽어서 해당 세션 넘버를 찾아서 삭제
    public void deleteSession(String sid) {
        SessionHandler.deleteSession(sid);
    }

    /**
     * post 요청의 경우 빈 배열로 응답하는데, 바로 new로 생성하는 것보다
     * 메서드를 이용해 명시적으로 의미를 전달하고자 함
     * @return 빈 바이트 배열
     */
    private byte[] makeEmptyBody() {
        return new byte[0];
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }
}
