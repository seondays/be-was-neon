package webserver.handler;

import webserver.Request;

public class AuthenticationHandler {
    private final Request request;

    public AuthenticationHandler(Request request) {
        this.request = request;
    }

    /**
     * 현재 접속한 사람이 로그인 되어 있는 상태인지 체크한다 (request에서 세션 정보 체크)
     * 리퀘스트에서 쿠키가 있는지, 세션 id에 맞는 세션이 존재하는지 확인한다
     * @return
     */
    public boolean isAuthenticationUser() {
        if (isCookieExist()) {
            return isSessionExist();
        }
        return false;
    }

    private boolean isCookieExist() {
        return request.getHeader().getSidCookie() != null;
    }

    private boolean isSessionExist() {
        return SessionHandler.getUserSession(request.getHeader().getSidCookie()) != null;
    }
}