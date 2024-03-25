package webserver.handler;

import java.io.FileNotFoundException;
import utils.Path;
import webserver.Request;

public class AuthenticationHandler {
    private Request request;
    private Path path;

    public AuthenticationHandler(Request request) {
        this.request = request;
        path = new Path();
    }

    // 로그인되어 있지 않은 상태에서 로그인하려고 시도하면 다른 페이지로 리다이렉션 한다
    // todo : 주소들 겹치는 부분 따로 모아서 관리?
    public String buildUrl(String resource) throws FileNotFoundException {
        if (resource.equals("/user/list") && !isAuthenticationUser()) {
            return path.buildURL("/login");
        }
        return path.buildURL(resource);
    }

    /**
     * 현재 접속한 사람이 로그인 되어 있는 상태인지 체크한다 (request에서 세션 정보 체크)
     * 리퀘스트에서 쿠키가 있는지, 세션 id에 맞는 세션이 존재하는지 확인한다
     * @return
     */
    public boolean isAuthenticationUser() {
        if (isCookieExist()) {
            // 이건 쿠키에 있는 sid값 -> sid=04934; 스플릿
            // 쿠키가 sid 말고 다른 값도 포함되어 있을 경우도 체크해야할듯
            // todo : 겹치는 부분 리펙토링 필요
            String sid = request.getHeaderValueBy("Cookie").split("=")[1];
            return isSessionExist(sid);
        }
        return false;
    }

    // 쿠키가 존재하는지 체크
    private boolean isCookieExist() {
        return request.getHeaderValueBy("Cookie") != null;
    }

    // 세션이 존재하는지 체크
    private boolean isSessionExist(String sid) {
        return SessionHandler.getUserSession(sid) != null;
    }
}