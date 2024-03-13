package webserver;

import db.Database;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import model.User;
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
     응답 시 리소스(파일)을 전달할 필요가 없는 create의 경우에만 사용한다.
     User를 생성한 다음, body가 될 바이트 배열을 비워서 반환한다.
    */
    public byte[] responseCreate() {
        String userInfo = requestPath[1];
        Database.addUser(new User(requestInfoToMap(userInfo)));
        return new byte[0];
    }

    // 해당 요청이 create인지 확인한다.
    public boolean isCreate() {
        return requestPath[0].equals(Paths.CREATE.getRequest());
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

    // 회원가입을 위한 정보가 넘어오면 해당 값을 파싱해서 Map에 저장한다
    private Map<String, String> requestInfoToMap(String info) {
        return Arrays.stream(info.split(ELEMENTS_DELIMITER))
                .map(s -> s.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr[1]
                ));
    }

    // Paths enum을 사용해 요청 리소스를 실제 리소스 경로와 매칭해주도록 한다.
    private String parsePath(String request) {
        return Paths.parsePath(request).makeResultUrl();
    }
}