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
    private final AuthenticationHandler authenticationHandler;
    private HttpResponseBody responseBody;
    private HttpResponseHeader responseHeader;
    private Map<String, String> needRedirectionPage;
    private Map<String, DynamicBodyModifier> bodyModifier;

    public DynamicHttpBodyHandler(Request request, Path path, AuthenticationHandler authenticationHandler) {
        this.request = request;
        this.path = path;
        this.authenticationHandler = authenticationHandler;
        initRedirectionPage();
        initMappingBodyHandler();
    }

    /**
     * 인증 여부에 따라 리다이렉션이 필요한 특수한 리소스들을 관리한다.
     */
    private void initRedirectionPage() {
        needRedirectionPage = new HashMap<>();
        needRedirectionPage.put("/user/list", "/");
        needRedirectionPage.put("/main", "/");
        needRedirectionPage.put("/article", "/");
    }

    /**
     * 요청 리소스 URL에 따라 적절한 modifier를 매핑해준다.
     */
    private void initMappingBodyHandler() {
        bodyModifier = new HashMap<>();
        bodyModifier.put("/main", new ArticleBodyModifier());
        bodyModifier.put("/user/list", new UserListBodyModifier());
    }


    public void run() throws Exception {
        final String USER_RESOURCE = request.getResource();
        final String fileUrl = path.buildURL(USER_RESOURCE);
        String allFile = readFileAll(fileUrl);

        if (authenticationHandler.isAuthenticationUser()) {
            String addNameBody = readAddNameBody(allFile, getUserName());
            try {
                DynamicBodyModifier modifier = bodyModifier.get(USER_RESOURCE);
                responseBody = new HttpResponseBody(modifier.make(addNameBody));
                responseHeader = HttpResponseHeader.make200Header(responseBody.length(), fileUrl);
            } catch (NullPointerException e) {
                responseBody = new HttpResponseBody(addNameBody.getBytes());
                responseHeader = HttpResponseHeader.make200Header(responseBody.length(), fileUrl);
            }
        } else {
            responseBody = new HttpResponseBody();
            responseHeader = HttpResponseHeader.make302Header(responseBody.length(),
                    needRedirectionPage.get(USER_RESOURCE));
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