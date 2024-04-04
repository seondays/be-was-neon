package webserver.handler;

import dynamicBodyModifier.ArticleBodyModifier;
import dynamicBodyModifier.UserListBodyModifier;
import httpMethods.DynamicBodyModifier;
import httpMethods.GetHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import utils.Path;
import webserver.Request;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class DynamicHttpBodyHandler implements GetHandler {
    public static final String USER_NAME_VIEW = "<p class=\"header__username\">안녕하세요 %s 님</p>";
    public static final String NEW_LINE = "\n";
    private final Request request;
    private final Path path;
    private HttpResponseBody responseBody;
    private HttpResponseHeader responseHeader;
    private Map<String, String> needRedirectionPage;
    private Map<String, DynamicBodyModifier> bodyModifier;

    public DynamicHttpBodyHandler(Request request, Path path) {
        this.request = request;
        this.path = path;
        initRedirectionPage();
        initMappingBodyHandler();
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

    private void initMappingBodyHandler() {
        bodyModifier = new HashMap<>();
        bodyModifier.put("/main", new ArticleBodyModifier());
        bodyModifier.put("/user/list", new UserListBodyModifier());
    }

    /**
     * 리소스에 따라 body를 만들어야 하는 부분이 달라지기 때문에 이를 분기하고
     * 로그인 인증 여부에 따라서 리다이렉션이 필요한 리소스들을 관리하여 그곳으로의 접근을 막는다.
     * 따로 modifier가 필요한 요청들은 해당 modifier로 보낸 후 값을 가져오게 하고
     * 사용자 이름 추가하는 로직은 디폴트이기 때문에 bodyModifier에 없는 경우에는 이름만 추가해서 반환
     * @throws Exception
     */
    public void run() throws Exception {
        final String USER_RESOURCE = request.getResource();
        final String fileUrl = path.buildURL(USER_RESOURCE);
        String allFile = readFileAll(fileUrl);
        String addNameBody = readAddNameBody(allFile, getUserName());

        if (needRedirectionPage.get(USER_RESOURCE) != null) {
            responseBody = new HttpResponseBody();
            responseHeader = HttpResponseHeader.make302Header(responseBody.length(),
                    needRedirectionPage.get(USER_RESOURCE));
            return;
        }
        try {
            DynamicBodyModifier modifier = bodyModifier.get(USER_RESOURCE);
            responseBody = new HttpResponseBody(modifier.make(addNameBody));
            responseHeader = HttpResponseHeader.make200Header(responseBody.length(), fileUrl);
        } catch (NullPointerException e) {
            responseBody = new HttpResponseBody(addNameBody.getBytes());
            responseHeader = HttpResponseHeader.make200Header(responseBody.length(), fileUrl);
        }
    }

    /**
     * body로 사용할 파일에 사용자 이름을 넣어 변경해준다.
     *
     * @param allFile
     * @param name
     * @return
     * @throws IOException
     */
    public String readAddNameBody(String allFile, String name) {
        return allFile.replace("<!--        userName-->", String.format((USER_NAME_VIEW), name));
    }

    /**
     * 주어진 경로에 있는 파일을 모두 읽어 String으로 반환한다.
     *
     * @param fileUrl
     * @return
     * @throws IOException
     */
    public String readFileAll(String fileUrl) throws IOException {
        File file = new File(fileUrl);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(NEW_LINE);
        }
        return sb.toString();
    }

    private String getUserName() {
        // todo
        return SessionHandler.getUserSession(request.getHeader().getSidCookie()).getName();
    }

    public HttpResponseBody getResponseBody() {
        return responseBody;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }
}