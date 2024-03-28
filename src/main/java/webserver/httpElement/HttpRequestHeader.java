package webserver.httpElement;

import java.util.Map;
import java.util.Optional;

public class HttpRequestHeader {
    private static final String SID_DELIMITER = "sid=";
    private static final String COOKIE_KEY = "Cookie";
    private static final String EMPTY_STRING = "";
    private final Map<String, String> headers;
    private String sidCookie;

    public HttpRequestHeader(Map<String, String> headers) {
        this.headers = headers;
        setCookie();
    }

    public String getValueBy(String key) {
        return headers.get(key);
    }

    public String getSidCookie() {
        return sidCookie;
    }

    private void setCookie() {
        String allCookie = Optional.ofNullable(headers.get(COOKIE_KEY))
                .orElse(EMPTY_STRING);

        sidCookie = Optional.of(allCookie)
                .map(cookie -> cookie.split(SID_DELIMITER))
                .filter(sidPart -> sidPart.length > 1)
                .map(piece -> piece[1])
                .orElse(EMPTY_STRING);
    }
}