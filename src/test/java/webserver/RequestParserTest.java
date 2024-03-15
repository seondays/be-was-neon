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

    @Test
    @DisplayName("create 요청에 응답하는 경우, DB에 유저가 정상적으로 저장되는지 확인한다.")
    void createAddUser() {
        requestParser = new RequestParser("GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
        requestParser.responseCreate();
        assertThat(Database.findUserById("javajigi")).isNotNull();
    }

    @Test
    @DisplayName("응답을 위해 파일 내용을 바이트 배열로 올바르게 변환하는지 확인한다.")
    void parseFileToByte() throws IOException {
        requestParser = new RequestParser("GET /index.html HTTP/1.1");
        byte[] body = Files.readAllBytes(new File("src/main/resources/static/index.html").toPath());
        assertThat(body).isEqualTo(requestParser.parseFileToByte());
    }
}