package webserver;

import db.Database;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateHandler {
    public static final String JSON_TEXT_MARK = "\"";
    public static final String JSON_ELEMENTS_DELIMITER = ",";
    public static final String JSON_KEY_VALUE_DELIMITER = ":";
    private static final Logger logger = LoggerFactory.getLogger(CreateHandler.class);
    private final Request request;

    public CreateHandler(Request request) {
        this.request = request;
    }

    public void create() {
        User requestUser = new User(parseJsonToMap(request.getBody()));
        Database.addUser(requestUser);
        logger.info(Database.findUserById(requestUser.getUserId()).toString());
    }

    /**
     * 회원가입을 위한 정보를 JSON으로 받아 해당 값을 파싱해서 Map으로 저장한다. 해당 메서드의 결과값은 User를 생성할 때, 생성자로 사용된다.
     * @param info
     * @return
     */
    public Map<String, String> parseJsonToMap(String info) {
        // 중괄호와 콜론 제거
        info = info.substring(1, info.length() - 1).replaceAll(JSON_TEXT_MARK, "");
        // , 으로 분리 후 다시 : 로 분리
        return Arrays.stream(info.split(JSON_ELEMENTS_DELIMITER))
                .map(pieces -> pieces.split(JSON_KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        key -> key[0],
                        value -> value[1]
                ));
    }
}