package model;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;

/**
 * 쿠키에 여러가지 값이 담길 수 있기 때문에 안정적으로 sid의 값을 분리해서 가지고 있도록 하는 cookie 클래스
 * todo : request 객체에서는 이 쿠키 객체를 모르고 있는데 괜찮은걸까?
 */
public class Cookie {
    private static final Logger logger = LoggerFactory.getLogger(Cookie.class);
    private static final String SID_DELIMITER = "sid=";
    private static final String COOKIE_KEY = "Cookie";
    private static final String EMPTY_STRING = "";
    private final Request request;
    private String sid;
    private String allCookie;

    /**
     * 쿠키의 값들 중에서 필요한 값만을 정확하게 가져오기 위해서 따로 객체 생성
     */
    public Cookie(Request request) {
        this.request = request;
        setCookie();
    }

    /**
     * 현재 쿠키에서 필요한 값은 sid이기 때문에, 해당 부분을 따로 세팅해주는 메서드 작성
     */
    private void setCookie() {
        allCookie = Optional.ofNullable(request.getHeaderValueBy(COOKIE_KEY))
                        .orElse(EMPTY_STRING);

        sid = Optional.of(allCookie)
                .map(cookie -> cookie.split(SID_DELIMITER))
                .filter(sidPart -> sidPart.length > 1)
                .map(piece -> piece[1])
                .orElse(EMPTY_STRING);
    }

    public String getSid() {
        return sid;
    }

    public String getAllCookie() {
        return allCookie;
    }
}