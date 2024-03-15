package utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.FileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    Path path = new Path();

    @Test
    @DisplayName("요청 리소스가 디렉토리일 때, 해당 디렉토리의 index.html 파일로 바로 접근할 수 있는 주소를 생성한다.")
    void buildRegistrationURL() throws FileNotFoundException {
        String resource = "/registration";
        assertThat(path.buildURL(resource)).isEqualTo("src/main/resources/static/registration/index.html");
    }

    @Test
    @DisplayName("요청 리소스가 파일일 때, 해당 파일로 바로 접근할 수 있는 주소를 생성한다.")
    void buildFileURL() throws FileNotFoundException {
        String resource = "/index.html";
        assertThat(path.buildURL(resource)).isEqualTo("src/main/resources/static" + resource);
    }

    @Test
    @DisplayName("현재 요청 리소스로 파일을 찾을 수 없을 때, 예외가 발생한다.")
    void buildURLException() {
        String resource = "home.css";
        assertThatThrownBy(() -> path.buildURL(resource)).isInstanceOf(FileNotFoundException.class);
    }
}