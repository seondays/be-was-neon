package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import db.Database;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestParserTest {
    RequestParser requestParser;

    @Test
    @DisplayName("들어오는 요청 내용이 /create 요청인지 여부를 확인한다.")
    void isCreate() {
        RequestParser createRequest = new RequestParser("GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
        RequestParser otherRequest = new RequestParser("GET /index.html HTTP/1.1");
        assertThat(createRequest.isCreate()).isTrue();
        assertThat(otherRequest.isCreate()).isFalse();
    }
}