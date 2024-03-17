package webserver;

public class RequestParser {
    public static final String REQUEST_DELIMITER = " ";
    public static final String QUERY_DELIMITER = "\\?";
    private final String[] requestPath;

    public RequestParser(String requestLine) {
        requestPath = requestLine.split(REQUEST_DELIMITER);
    }

    public String getUserMethod() {
        return requestPath[0];
    }

    public String getUserQuery() {
        String[] queryPieces = requestPath[1].split(QUERY_DELIMITER);
        if (queryPieces.length > 1) {
            return queryPieces[1];
        }
        return "";
    }

    public String getUserResource() {
        return requestPath[1].split(QUERY_DELIMITER)[0];
    }
}