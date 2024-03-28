package webserver.handler;

import db.Database;
import httpMethods.GetHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import model.User;
import utils.ExtensionType;
import utils.Path;
import webserver.Request;

public class DynamicHttpBodyHandler implements GetHandler {
    public static final String USER_NAME_VIEW = "<p class=\"header__username\">안녕하세요 %s 님</p>";
    public static final String NEW_LINE = "\n";
    private final Request request;
    private final Path path;
    private byte[] responseBody;
    private String responseHeader;

    public DynamicHttpBodyHandler(Request request, Path path) {
        this.request = request;
        this.path = path;
    }

    public void run() throws Exception {
        final String USER_RESOURCE = request.getResource();
        final String fileUrl = path.buildURL(USER_RESOURCE);
        // todo : 분기 줄일 수 있는 방법 고민 필요. map으로 넣으려면 exception 문제 발생.. 또 핸들러로 나눠야 하나?
        if ("/user/list".equals(USER_RESOURCE)) {
            responseBody = readUserListBody(fileUrl);
            responseHeader = get200Header(responseBody.length, fileUrl);
            return;
        }
        responseBody = readAddNameBody(fileUrl, getUserName());
        responseHeader = get200Header(responseBody.length, fileUrl);
    }

    /**
     * body로 사용할 파일에 사용자 이름을 넣어 변경해준다.
     * todo : 따로 플래그를 심어서 그 부분에서 체크하는 것으로 변경해보기
     *
     * @param fileUrl
     * @return
     * @throws IOException
     */
    public byte[] readAddNameBody(String fileUrl, String name) throws IOException {
        File file = new File(fileUrl);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        final String INSERT_INDEX = "<a href=\"/main\">";

        while ((line = br.readLine()) != null) {
            sb.append(line).append(NEW_LINE);
            if (line.contains(INSERT_INDEX)) {
                sb.append(String.format((USER_NAME_VIEW), name)).append(NEW_LINE);
            }
        }
        return sb.toString().getBytes();
    }

    public byte[] readUserListBody(String fileUrl) throws IOException {
        File file = new File(fileUrl);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        final String INSERT_INDEX = "<tbody>";

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.contains(INSERT_INDEX)) {
                for (User u : Database.findAll()) {
                    sb.append("<tr>").append(NEW_LINE);
                    sb.append(String.format("<td>%s</td>", u.getUserId())).append(NEW_LINE);
                    sb.append(String.format("<td>%s</td>", u.getName())).append(NEW_LINE);
                    sb.append("</tr>").append(NEW_LINE);
                }
            }
        }
        return sb.toString().getBytes();
    }

    private String getUserName() {
        return SessionHandler.getUserSession(request.getHeader().getSidCookie()).getName();
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

    public byte[] getResponseBody() {
        return responseBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }
}