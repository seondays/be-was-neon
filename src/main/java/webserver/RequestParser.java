package webserver;

import db.Database;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Path;

public class RequestParser {
    public static final String REQUEST_DELIMITER = " ";
    public static final String QUERY_DELIMITER = "\\?";
    public static final String ELEMENTS_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    private final String[] requestPath;

    /*
     RequestParser 클래스는 String 형식의 request를 받아와서 필요한 양식으로 변경한다.
    */
    public RequestParser(String requestLine) {
        requestPath = requestLine.split(REQUEST_DELIMITER)[1].split(QUERY_DELIMITER);
    }

    /*
     응답 시 리소스(파일)을 전달할 필요가 없는 create의 경우에만 사용한다.
     User를 생성한 다음 DB에 저장하고, body가 될 바이트 배열을 비워서 반환한다.
     todo : body를 만드는 동작과, db에 저장하는 동작을 분리하는 것이 좋아보이긴 한다.
    */
    public byte[] responseCreate() {
        String userInfo = requestPath[1];
        Database.addUser(new User(requestInfoToMap(userInfo)));
        return new byte[0];
    }

    // 해당 요청의 리소스가 create인지 확인한다.
    public boolean isCreate() {
        final String CREATE_COMMAND = "/create";
        return requestPath[0].equals(CREATE_COMMAND);
    }

    /*
     file 내용을 바이트로 읽어와서 byte[]로 변환하는 메서드이다.
     파일의 크기만큼의 바이트 배열을 만들고, FileInputStream의 read 메서드를 통해 해당 배열에
     파일의 내용을 작성한다.
     todo : create랑 함께 쓸 수 있도록 파일 path는 외부에서 받아오는 걸로 수정해보자
    */
    public byte[] parseFileToByte() throws IOException {
        Path path = new Path();
        String filepath = path.buildURL((requestPath[0]));
        File file = new File(filepath);
        byte[] result = new byte[(int) file.length()];
        FileInputStream fileInputStream;
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(result);
        return result;
    }

    /*
     회원가입을 위한 정보를 받아 해당 값을 파싱해서 Map으로 저장한다.
     해당 메서드의 결과값은 User를 생성할 때, 생성자로 사용된다.
     */
    private Map<String, String> requestInfoToMap(String info) {
        return Arrays.stream(info.split(ELEMENTS_DELIMITER))
                .map(s -> s.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr[1]
                ));
    }
}