package webserver.handler;

import httpMethods.GetHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import utils.Path;
import webserver.Request;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class StaticHttpHandler implements GetHandler {
    private HttpResponseBody responseBody;
    private HttpResponseHeader responseHeader;
    private final AuthenticationHandler authenticationHandler;
    private final Request request;
    private final Path path;
    private Map<String, String> needRedirectionPage;

    public StaticHttpHandler(Request request, Path path, AuthenticationHandler authenticationHandler) {
        this.path = path;
        this.request = request;
        this.authenticationHandler = authenticationHandler;
        initRedirectionPage();
    }

    /**
     * 인증 여부에 따라 리다이렉션이 필요한 특수한 페이지들을 관리한다.
     */
    private void initRedirectionPage() {
        needRedirectionPage = new HashMap<>();
        needRedirectionPage.put("/", "/main");
        needRedirectionPage.put("/registration", "/main");
        needRedirectionPage.put("/login", "/main");
    }


    /**
     * 정적 페이지를 찾아 응답한다.
     * 접근이 불가능한 페이지에 대한 요청인 경우 리다이렉션 한다.
     * @throws Exception
     */
    public void run() throws Exception {
        final String USER_RESOURCE = request.getResource();
        if (authenticationHandler.isAuthenticationUser() && needRedirectionPage.get(USER_RESOURCE) != null) {
            responseBody = new HttpResponseBody();
            responseHeader = HttpResponseHeader.make302Header(responseBody.length(),
                    needRedirectionPage.get(USER_RESOURCE));
            return;
        }
        String fileUrl = path.buildURL(USER_RESOURCE);
        responseBody = new HttpResponseBody(readFileToByte(fileUrl));
        responseHeader = HttpResponseHeader.make200Header(responseBody.length(), fileUrl);
    }

    /**
     * static 인 경우, 응답할 파일 읽어와서 byte[]로 반환
     * todo : 버퍼로 읽어오는 방법으로 수정해보기
     *
     * @return
     * @throws IOException
     */
    private byte[] readFileToByte(String fileUrl) throws IOException {
        File file = new File(fileUrl);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] result = new byte[(int) file.length()];
        fileInputStream.read(result);
        return result;
    }

    public HttpResponseBody getResponseBody() {
        return responseBody;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }
}
