package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestParserTest {
    RequestParser requestParser;

    @Test
    @DisplayName("들어오는 요청 내용의 http 메서드를 올바르게 파싱해오는지 여부를 확인한다")
    void getMethods() {
        requestParser = new RequestParser("GET /index.html HTTP/1.1");
        assertThat(requestParser.getUserMethod()).isEqualTo("GET");
    }

    @Test
    @DisplayName("들어오는 요청 내용의 쿼리문이 있을 경우 올바르게 파싱해오는지 여부를 확인한다")
    void getQuery() {
        requestParser = new RequestParser("POST /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
        assertThat(requestParser.getUserQuery()).isEqualTo("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
    }

    @Test
    @DisplayName("들어오는 요청의 리소스를 올바르게 파싱해오는지 여부를 확인한다")
    void createAddUser() {
        requestParser = new RequestParser("GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
        assertThat(requestParser.getUserResource()).isEqualTo("/create");
    }
}