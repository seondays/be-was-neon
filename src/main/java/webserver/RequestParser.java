package webserver;

public class RequestParser {
    public static final String REQUEST_DELIMITER = " ";
    public static final String QUERY_DELIMITER = "\\?";
    public static final String ELEMENTS_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";
    private final String[] requestPath;

    public RequestParser(String requestLine) {
        requestPath = requestLine.split(REQUEST_DELIMITER)[1].split(QUERY_DELIMITER);
    }
}