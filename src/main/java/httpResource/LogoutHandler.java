package httpResource;

import httpMethods.PostHandler;
import webserver.Request;
import webserver.handler.SessionHandler;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class LogoutHandler implements PostHandler {
    private HttpResponseBody responseBody;
    private HttpResponseHeader responseHeader;
    private final Request request;

    public LogoutHandler(Request request) {
        this.request = request;
    }

    public void run() throws Exception {
        String sid = request.getHeader().getSidCookie();
        deleteSession(sid);
        responseBody = new HttpResponseBody();
        responseHeader = HttpResponseHeader.make302DeleteCookieHeader(responseBody.length(),sid);
    }

    // 쿠키값을 읽어서 해당 세션 넘버를 찾아서 삭제
    public void deleteSession(String sid) {
        SessionHandler.deleteSession(sid);
    }

    public HttpResponseBody getResponseBody() {
        return responseBody;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }
}
