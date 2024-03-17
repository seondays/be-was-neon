package webserver;

import db.Database;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Path;

public class ResponseBodyHandler {
    /**
     * Response 객체의 body 멤버변수를 위해 Request의 resource가 명령어인지 아니면 파일 요청인지를 처리해서 적절한 body를 만드는 것이 이 객체의 역할이다.
     */
    public static final String ELEMENTS_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";
    private static final Logger logger = LoggerFactory.getLogger(ResponseBodyHandler.class);
    private final Request request;
    private Map<String, Runnable> functionMap;
    private final String fileUrl;

    public ResponseBodyHandler(Request request) throws IOException {
        this.request = request;
        this.fileUrl = parseFileUrl();
        initFunctionMap();
    }

    private void initFunctionMap() {
        functionMap = new HashMap<>();
        functionMap.put("/create", this::create);
    }

    /**
     * body를 어떤 방법으로 처리할지 Method를 통해 분기를 나눈 후 그에 맞게 처리한다.
     *
     * @return
     * @throws IOException
     */
    public byte[] bodyProcessing() throws IOException {
        if (request.getHttpMethod().equals("GET")) {
            return getProcess();
        }
        return new byte[0];
    }

    /**
     * GET method인 경우의 처리
     *
     * @return
     * @throws IOException
     */
    private byte[] getProcess() throws IOException {
        if (isCreate()) {
            functionMap.get(request.getResource()).run();
            return parseFileUrl().getBytes();
        } else {
            return parseFileToByte();
        }
    }

    // 해당 요청이 create인지 확인한다.
    private boolean isCreate() {
        final String CREATE_COMMAND = "/create";
        return request.getResource().equals(CREATE_COMMAND);
    }

    // 파일 읽어와서 byte[]로 반환
    private byte[] parseFileToByte() throws IOException {
        File file = new File(fileUrl);
        byte[] result = new byte[(int) file.length()];
        FileInputStream fileInputStream;
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(result);
        return result;
    }

    // 최종 응답을 위한 URL 생성
    public String parseFileUrl() throws IOException {
        Path path = new Path();
        if (isCreate()) {
            return path.buildForRedirection("/login");
        } else {
            return path.buildURL(request.getResource());
        }
    }

    private void create() {
        User requestUser = new User(requestInfoToMap(request.getQuery()));
        Database.addUser(requestUser);
        logger.info(Database.findUserById(requestUser.getUserId()).toString());
    }

    /**
     * 회원가입을 위한 정보를 받아 해당 값을 파싱해서 Map으로 저장한다. 해당 메서드의 결과값은 User를 생성할 때, 생성자로 사용된다.
     *
     * @param info
     * @return
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
