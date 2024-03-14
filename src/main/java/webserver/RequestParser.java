package webserver;

import db.Database;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import model.User;
import utils.Path;

public class RequestParser {
    public static final String REQUEST_DELIMITER = " ";
    public static final String QUERY_DELIMITER = "\\?";
    public static final String ELEMENTS_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";

    private final String[] requestPath;

    /*
     클래스 이름을 바꾸던지, 아니면 분리를 해야 할 것 같은데 분리가 어렵다 ..
    */
    public RequestParser(String requestLine) {
        requestPath = requestLine.split(REQUEST_DELIMITER)[1].split(QUERY_DELIMITER);
    }

    public String getUserInfo() {
        return requestPath[1];
    }

    public String getUserCommand() {
        return requestPath[0];
    }

    /*
     create의 경우에 사용한다 User를 생성한 다음 DB에 저장한다.
    */
    public void createUser() {
        String userInfo = getUserInfo();
        Database.addUser(new User(requestInfoToMap(userInfo)));
    }

    // 해당 요청이 create인지 확인한다.
    public boolean isCreate() {
        final String CREATE_COMMAND = "/create";
        return getUserCommand().equals(CREATE_COMMAND);
    }

    /*
     file 내용을 바이트로 읽어와서 byte[]로 변환하는 메서드이다.
     파일의 크기만큼의 바이트 배열을 만들고, FileInputStream의 read 메서드를 통해 해당 배열에
     파일의 내용을 작성한다.
    */
    public byte[] parseFileToByte() throws IOException {
        File file = new File(parseFileUrl());

        if(isCreate()) {
            createUser();
            return parseFileUrl().getBytes();
        }
        else {
            byte[] result = new byte[(int) file.length()];
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(result);
            return result;
        }
    }

    /*
     응답 헤더를 만들 때 필요한 URL을 생성한다
     */
    public String parseFileUrl() throws IOException {
        Path path = new Path();
        if (isCreate()) {
            return path.buildForRedirection("/login");
        } else {
            return path.buildURL(getUserCommand());
        }
    }

    /*
     회원가입을 위한 정보를 받아 해당 값을 파싱해서 Map으로 저장한다.
     해당 메서드의 결과값은 User를 생성할 때, 생성자로 사용된다.
     */
    public Map<String, String> requestInfoToMap(String info) {
        return Arrays.stream(info.split(ELEMENTS_DELIMITER))
                .map(e -> e.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        key -> key[0],
                        value -> value[1]
                ));
    }
}