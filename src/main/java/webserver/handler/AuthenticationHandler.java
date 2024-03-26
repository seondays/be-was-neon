package webserver.handler;

import java.io.FileNotFoundException;
import model.Cookie;
import utils.Path;
import webserver.Request;

public class AuthenticationHandler {
    private final Path path;
    private final Cookie cookie;

    public AuthenticationHandler(Request request) {
        cookie = new Cookie(request);
        path = new Path();
    }

    /**
     * 인증 수준이 로그인인 페이지가 있는 경우,
     * 로그인되어 있지 않은 상태에서 해당 페이지에 접근하려고 하면 다른 페이지로 리다이렉션 한다.
     * @param resource
     * @return
     * @throws FileNotFoundException
     */
    public String buildUrl(String resource) throws FileNotFoundException {
        if (resource.equals("/user/list") && !isAuthenticationUser()) {
            return path.buildURL("/login");
        }
        return path.buildURL(resource);
    }

    /**
     * 접속한 사람의 이름 확인
     */
    public String getUserName() {
        return SessionHandler.getUserSession(cookie.getSid()).getName();
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

    // 쿠키가 존재하는지 체크
    private boolean isCookieExist() {
        return cookie.getSid() != null;
    }

    // 세션이 존재하는지 체크
    private boolean isSessionExist() {
        return SessionHandler.getUserSession(cookie.getSid()) != null;
    }
}