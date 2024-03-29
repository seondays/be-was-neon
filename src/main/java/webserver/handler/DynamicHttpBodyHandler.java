package webserver.handler;

import db.Database;
import httpMethods.GetHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import model.User;
import utils.Path;
import webserver.Request;
import webserver.httpElement.HttpResponseHeader;

public class DynamicHttpBodyHandler implements GetHandler {
    public static final String USER_NAME_VIEW = "<p class=\"header__username\">안녕하세요 %s 님</p>";
    public static final String NEW_LINE = "\n";
    private final Request request;
    private final Path path;
    private byte[] responseBody;
    private HttpResponseHeader responseHeader;
    private Map<String, String> needRedirectionPage;

    public DynamicHttpBodyHandler(Request request, Path path) {
        this.request = request;
        this.path = path;
        initRedirectionPage();
    }

    /**
     * 인증 여부에 따라 리다이렉션이 필요한 특수한 리소스들을 관리한다.
     */
    private void initRedirectionPage() {
        needRedirectionPage = new HashMap<>();
        needRedirectionPage.put("/", "/main");
        needRedirectionPage.put("/login", "/main");
        needRedirectionPage.put("/registration", "/main");
    }

    // todo : 분기 줄일 수 있는 방법 고민 필요
    /**
     * 리소스에 따라 body를 만들어야 하는 부분이 달라지기 때문에 이를 분기하고,
     * 로그인 인증 여부에 따라서 리다이렉션이 필요한 리소스들을 관리하여 그곳으로의 접근을 막는다.
     * @throws Exception
     */
    public void run() throws Exception {
        final String USER_RESOURCE = request.getResource();
        final String fileUrl = path.buildURL(USER_RESOURCE);

        if ("/user/list".equals(USER_RESOURCE)) {
            responseBody = readUserListBody(fileUrl);
            responseHeader = HttpResponseHeader.make200Header(responseBody.length, fileUrl);
            return;
        }
        if (needRedirectionPage.get(USER_RESOURCE) != null) {
            responseBody = makeEmptyBody();
            responseHeader = HttpResponseHeader.make302Header(responseBody.length, needRedirectionPage.get(USER_RESOURCE));
            return;
        }
        responseBody = readAddNameBody(fileUrl, getUserName());
        responseHeader = HttpResponseHeader.make200Header(responseBody.length, fileUrl);
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