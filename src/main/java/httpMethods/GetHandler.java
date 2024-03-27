package httpMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import utils.ExtensionType;
import webserver.Request;
import webserver.handler.AuthenticationHandler;
import webserver.handler.DynamicHttpBodyHandler;

public class GetHandler implements MethodsHandler {
    private static final String USER_LIST_RESOURCE = "/user/list";
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
        DynamicHttpBodyHandler dynamicHttpBodyHandler = new DynamicHttpBodyHandler();
        String fileUrl = authenticationHandler.buildUrl(request.getResource());
        /**
         * 동적 파일을 줘야 하는지에 관해 로그인 여부를 체크하고, 여기서 다시 어떤 파일을 요청했느냐에 따라 바디를 다르게 만들어줘야한다.
         * todo : 이 부분이 AuthenticationHandler의 buildUrl의 분기를 나누는 부분과 로직이 비슷한데.. 개선해 볼 수 없을까?
          */
        if (authenticationHandler.isAuthenticationUser()) {
            if (USER_LIST_RESOURCE.equals(request.getResource())) {
                body = dynamicHttpBodyHandler.readUserListBody(fileUrl);
                header = get200Header(body.length, fileUrl);
                return;
            }
            body = dynamicHttpBodyHandler.readAddNameBody(fileUrl, authenticationHandler.getUserName());
            header = get200Header(body.length, fileUrl);
            return;
        }
        body = readFileToByte(fileUrl);
        header = get200Header(body.length, fileUrl);
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

    public byte[] getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }
}