package httpResource;

import db.Database;
import httpMethods.PostHandler;
import utils.ExtensionType;
import webserver.Request;
import webserver.handler.SessionHandler;

public class LoginHandler implements PostHandler {
    private final Request request;
    private byte[] responseBody;
    private String responseHeader;

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
            responseBody = makeBody();
            responseHeader = getSuccessHeader(responseBody.length, getSuccessRedirection(), sid);
            SessionHandler.makeSession(sid, Database.findUserById(request.getBody().get("userId")));
        } else {
            responseBody = makeBody();
            responseHeader = getFailHeader(responseBody.length, getFailRedirection());
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

    public String getSuccessHeader(int lengthOfBodyContent, String fileUrl, String sid) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 302 Found\r\n");
        stringBuffer.append(String.format("Location: %s\r\n", fileUrl));
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append(String.format("Set-Cookie: sid=%s; Path=/", sid));
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public String getFailHeader(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 302 Found\r\n");
        stringBuffer.append(String.format("Location: %s\r\n", fileUrl));
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    /**
     * 성공하면 -> 헤더 값을 추가(SID=세션 ID) and 메인으로 리다이렉트 실패하면 -> 실패 창으로 리다이렉트하기위한 주소 생성
     */
    private String getFailRedirection() {
        return "/login/login_failed.html";
    }

    private String getSuccessRedirection() {
        return "main/index.html";
    }

    public byte[] makeBody() {
        return new byte[0];
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }
}
