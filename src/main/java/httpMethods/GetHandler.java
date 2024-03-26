package httpMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import utils.ExtensionType;
import webserver.Request;
import webserver.handler.AuthenticationHandler;
import webserver.handler.DynamicHttpHandler;

public class GetHandler implements MethodsHandler {
    private final Request request;
    private byte[] body;
    private String header;

    public GetHandler(Request request) {
        this.request = request;
    }

    /**
     * get은 응답할 주소를 만들어주는 것이 현재 임무이다.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(request);
        DynamicHttpHandler dynamicHttpHandler = new DynamicHttpHandler();
        String fileUrl = authenticationHandler.buildUrl(request.getResource());
        // 동적 파일을 줘야 하는지에 관해 체크한다.
        if (authenticationHandler.isAuthenticationUser()) {
            body = dynamicHttpHandler.readBodyAddUserName(fileUrl, authenticationHandler.getUserName());
            header = get200Header(body.length, fileUrl);
            return;
        }
        body = readFileToByte(fileUrl);
        header = get200Header(body.length, fileUrl);
    }

    /**
     * 응답할 파일 읽어와서 byte[]로 반환
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

    public byte[] getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }
}