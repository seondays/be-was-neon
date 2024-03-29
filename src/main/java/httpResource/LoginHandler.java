package httpResource;

import db.Database;
import httpMethods.PostHandler;
import webserver.Request;
import webserver.handler.SessionHandler;
import webserver.httpElement.HttpResponseHeader;

public class LoginHandler implements PostHandler {
    private final Request request;
    private byte[] responseBody;
    private HttpResponseHeader responseHeader;

    public LoginHandler(Request request) {
        this.request = request;
    }

    /**
     * 유효한 로그인 시도라면 헤더에 세션ID 정보가 포함된 쿠키를 세팅하고, 연결된 세션을 생성한다.
     */
    @Override
    public void run() {
        if (isValidLogin()) {
            String sid = SessionHandler.generateSessionId();
            responseBody = makeEmptyBody();
            responseHeader = HttpResponseHeader.make302SetCookieHeader(responseBody.length, getSuccessRedirection(), sid);
            SessionHandler.makeSession(sid, Database.findUserById(request.getBody().get("userId")));
        } else {
            responseBody = makeEmptyBody();
            responseHeader = HttpResponseHeader.make302Header(responseBody.length, getFailRedirection());
        }
    }

    /**
     * 로그인 정보가 있는지, 일치하는지 확인한다.
     * map을 탐색하는 과정에서 NullPointerException이 발생하면, 유효한 로그인 상태가 아니다.
     */
    private boolean isValidLogin() {
        try {
            String targetUserId = request.getBody().get("userId");
            String targetPassword = request.getBody().get("password");
            String expectedUserID = Database.findUserById(targetUserId).getUserId();
            String expectedPassword = Database.findUserById(targetUserId).getPassword();

            return targetUserId.equals(expectedUserID) && targetPassword.equals(expectedPassword);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 성공하면 -> 헤더 값을 추가(SID=세션 ID) and 메인으로 리다이렉트 실패하면 -> 실패 창으로 리다이렉트하기위한 주소 생성
     * @return 리다이렉트 주소
     */
    private String getFailRedirection() {
        return "/login/login_failed.html";
    }

    private String getSuccessRedirection() {
        return "main/index.html";
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
