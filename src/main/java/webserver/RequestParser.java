package webserver;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import utils.HttpMethods;

public class RequestParser {
    public static final String REQUEST_DELIMITER = " ";
    public static final String QUERY_DELIMITER = "\\?";
    public static final String JSON_TEXT_MARK = "\"";
    public static final String JSON_ELEMENTS_DELIMITER = ",";
    public static final String JSON_KEY_VALUE_DELIMITER = ":";
    private final String[] requestPath;

    public RequestParser(String requestLine) {
        requestPath = requestLine.split(REQUEST_DELIMITER);
    }

    public HttpMethods getUserMethod() {
        return HttpMethods.matchMethods(requestPath[0]);
    }

    public String getUserQuery() {
        String[] queryPieces = requestPath[1].split(QUERY_DELIMITER);
        if (queryPieces.length > 1) {
            return queryPieces[1];
        }
        return "";
    }

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

    public String getUserResource() {
        return requestPath[1].split(QUERY_DELIMITER)[0];
    }
}