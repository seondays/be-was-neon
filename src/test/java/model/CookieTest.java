package model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.Request;

class CookieTest {
    private Request request;
    private Cookie cookie;

    @Test
    @DisplayName("request의 헤더에 쿠키가 없는 경우 정상적으로 쿠키 객체에 빈 값이 세팅된다.")
    void validateNoCookie() throws IOException {
        final String httpRequest = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n";
        final String EMPTY_STRING = "";
        request = new Request(new ByteArrayInputStream(httpRequest.getBytes()));
        cookie = new Cookie(request);

        assertThat(cookie.getAllCookie()).isEqualTo(EMPTY_STRING);
        assertThat(cookie.getSid()).isEqualTo(EMPTY_STRING);
    }

    @Test
    @DisplayName("request의 헤더에 쿠키가 있지만, sid 값은 없는 경우 정상적으로 쿠키 객체에 값이 세팅된다.")
    void validateNoSid() throws IOException {
        final String httpRequest = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Cookie: Idea-5e7d8715=b012f8-0404-4d4d-a65b\r\n" +
                "\r\n";
        final String EMPTY_STRING = "";
        final String COOKIE = "Idea-5e7d8715=b012f8-0404-4d4d-a65b";
        request = new Request(new ByteArrayInputStream(httpRequest.getBytes()));
        cookie = new Cookie(request);

        assertThat(cookie.getAllCookie()).isEqualTo(COOKIE);
        assertThat(cookie.getSid()).isEqualTo(EMPTY_STRING);
    }

    @Test
    @DisplayName("request의 헤더에 sid 값이 있는 경우 정상적으로 쿠키 객체에 값이 세팅된다.")
    void validateSidExist() throws IOException {
        final String httpRequest = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Cookie: Idea-5e7d8715=b012f8-0404-4d4d-a65b; sid=12345\r\n" +
                "\r\n";
        final String SID = "12345";
        final String COOKIE = "Idea-5e7d8715=b012f8-0404-4d4d-a65b; sid=12345";
        request = new Request(new ByteArrayInputStream(httpRequest.getBytes()));
        cookie = new Cookie(request);

        assertThat(cookie.getAllCookie()).isEqualTo(COOKIE);
        assertThat(cookie.getSid()).isEqualTo(SID);
    }
}