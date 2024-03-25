package webserver.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionHandler {
    private static final int SESSION_ID_SIZE = 5;
    private static final int START_RANGE = 0;
    private static final int END_RANGE = 10;
    private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);
    private static final Map<String, User> sessions = new HashMap<>();

    // 세션 아이디 만들기
    public static String generateSessionId() {
        return new Random().ints(SESSION_ID_SIZE, START_RANGE, END_RANGE)
                .mapToObj(String::valueOf).collect(Collectors.joining());
    }

    // 세션 아이디가 만들어지면 해당 세션 아이디와 유저 정보 저장하기
    public static void makeSession(String sid, User user) {
        sessions.put(sid, user);
        logger.info(sessions.toString());
    }

    // 세션 삭제
    public static void deleteSession(String key) {
        sessions.remove(key);
    }

    // 세션 검색
    public static User getUserSession(String sid) {
        return sessions.get(sid);
    }
}
