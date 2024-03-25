package webserver.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.Request;

class AuthenticationHandlerTest {

    Request request;
    AuthenticationHandler authenticationHandler;

    @Test
    @DisplayName("로그인되지 않은 클라이언트가 /user/list 로 접근할 경우 로그인 페이지로 이동시키기 위한 파일주소를 생성한다.")
    void buildUrlUnauthentication() throws IOException {
        String httpRequest = "GET /user/list HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n";

        request = new Request(new ByteArrayInputStream(httpRequest.getBytes()));
        authenticationHandler = new AuthenticationHandler(request);

        assertThat(authenticationHandler.buildUrl(request.getResource())).isEqualTo(
                "src/main/resources/static/login/index.html");
    }

    @Test
    @DisplayName("로그인된 클라이언트가 /user/list로 접근할 경우 정상적인 파일주소를 생성한다.")
    void buildUrlAuthentication() throws IOException {
        String httpRequest = "GET /user/list HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Cookie: sid=12345\r\n" +
                "\r\n";
        request = new Request(new ByteArrayInputStream(httpRequest.getBytes()));
        authenticationHandler = new AuthenticationHandler(request);
        SessionHandler.makeSession("12345", new User("id", "password", "name", "email"));

        assertThat(authenticationHandler.buildUrl(request.getResource())).isEqualTo(
                "src/main/resources/static/user/list/index.html");
    }
}