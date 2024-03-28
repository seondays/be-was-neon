package webserver.handler;

import httpMethods.GetHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import utils.ExtensionType;
import utils.Path;
import webserver.Request;

public class StaticHttpHandler implements GetHandler {
    private byte[] responseBody;
    private String responseHeader;
    private final Request request;
    private final Path path;
    private Map<String, String> needRedirectionPage;

    public StaticHttpHandler(Request request, Path path) {
        this.path = path;
        this.request = request;
        initRedirectionPage();
    }

    /**
     * 인증 여부에 따라 리다이렉션이 필요한 특수한 페이지들을 관리한다.
     */
    private void initRedirectionPage() {
        needRedirectionPage = new HashMap<>();
        needRedirectionPage.put("/user/list", "/login");
        needRedirectionPage.put("/main", "/");
    }

    /**
     * resource를 가져와 응답할 내용을 만든다.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        final String USER_RESOURCE = request.getResource();
        if (needRedirectionPage.get(USER_RESOURCE) != null) {
            responseBody = new byte[0];
            responseHeader = get302Header(responseBody.length, needRedirectionPage.get(USER_RESOURCE));
            return;
        }
        String fileUrl = path.buildURL(USER_RESOURCE);
        responseBody = readFileToByte(fileUrl);
        responseHeader = get200Header(responseBody.length, fileUrl);
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

    public String get200Header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 200 OK\r\n");
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public String get302Header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 302 Found\r\n");
        stringBuffer.append(String.format("Location: %s\r\n", fileUrl));
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
