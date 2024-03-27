package utils;

import java.util.Arrays;

public enum HttpMethods {
    GET,
    POST;

    /**
     * String 형식으로 들어온 http 메서드를 Enum 타입과 매칭한다.
     * 유효하지 않은 메서드인 경우 예외를 발생시킨다.
     * @param stringMethod
     * @return
     */
    public static HttpMethods matchMethods(String stringMethod) {
        return Arrays.stream(values())
                .filter(httpMethods -> httpMethods.name().equals(stringMethod.toUpperCase()))
                .findFirst()
                .orElseThrow();
    }
}