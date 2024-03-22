package httpMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import utils.Path;
import utils.ExtensionType;
import webserver.Request;

public class GetHandler implements MethodsHandler {
    private final Request request;
    private byte[] body;
    private String header;
    private final Path path;

    public GetHandler(Request request) {
        this.request = request;
        path = new Path();
    }

    /**
     * get은 응답할 주소를 만들어주는 것이 현재 임무이다.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        String fileUrl = path.buildURL(request.getResource());
        body = parseFileToByte(fileUrl);
        header = get200Header(body.length, fileUrl);
    }

    /**
     * 응답할 파일 읽어와서 byte[]로 반환
     * todo : 버퍼로 읽어오는 방법으로 수정해보기
     *
     * @return
     * @throws IOException
     */
    private byte[] parseFileToByte(String fileUrl) throws IOException {
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