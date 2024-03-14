package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExtensionTypeTest {

    @Test
    @DisplayName("처리 가능한 확장자를 가진 파일 URL이 주어지면, MIME 정보를 정상적으로 파싱합니다")
    void getContentType() {
        String url = "src/main/resources/static/registration/index.css";
        assertThat(ExtensionType.getContentType(url)).isEqualTo("text/css");
    }

    @Test
    @DisplayName("처리할 수 없는 확장자를 가진 파일 URL이 주어지면, MIME 정보는 text.html으로 파싱합니다")
    void getUnsupportedContentType() {
        String url = "src/main/resources/static/registration/index.exe";
        assertThat(ExtensionType.getContentType(url)).isEqualTo("text/html");
    }
}