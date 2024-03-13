package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import utils.Paths;

public class RequestParser {
    public static final String REQUEST_DELIMITER = " ";
    public static final String QUERY_DELIMITER = "\\?";
    public static final String ELEMENTS_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";
    private final String[] requestPath;

    public RequestParser(String requestLine) {
        requestPath = requestLine.split(REQUEST_DELIMITER)[1].split(QUERY_DELIMITER);
    }
    /*
     file 내용을 바이트로 읽어와서 byte[]로 변환하는 메서드를 만들어준다.
     파일의 크기만큼의 바이트 배열을 만들고, FileInputStream의 read 메서드를 통해 해당 배열에
     파일의 내용을 작성한다.
    */
    public byte[] parseFileToByte() {
        String filepath = parsePath(requestPath[0]);
        File file = new File(filepath);
        byte[] result = new byte[(int) new File(filepath).length()];
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Paths enum을 사용해 요청 리소스를 실제 리소스 경로와 매칭해주도록 한다.
    private String parsePath(String request) {
        return Paths.parsePath(request).makeResultUrl();
    }
}