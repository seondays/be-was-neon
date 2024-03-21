package utils;

import java.util.Arrays;

public enum HttpMethods {
    GET,
    POST,
    ERROR;

    public static HttpMethods matchMethods(String stringMethod) {
        return Arrays.stream(values())
                .filter(httpMethods -> httpMethods.name().equals(stringMethod.toUpperCase()))
                .findFirst()
                .orElse(ERROR);
    }
}