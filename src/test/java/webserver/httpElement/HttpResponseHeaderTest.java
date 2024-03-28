package webserver.httpElement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import utils.ExtensionType;

class HttpResponseHeaderTest {

    HttpResponseHeader httpResponseHeader;

    @Test
    void test200headerToString() {
        String fileUrl = "src/main/resources/static/index.html";
        int lengthOfBodyContent = 0;

        httpResponseHeader = HttpResponseHeader.make200Header(lengthOfBodyContent, fileUrl);

        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 200 OK\r\n");
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");

        assertThat(httpResponseHeader.toString().split("\r\n")).contains(stringBuffer.toString().split("\r\n"));
    }

    @Test
    void test302headerToString() {
        String fileUrl = "src/main/resources/static/index.html";
        int lengthOfBodyContent = 0;

        httpResponseHeader = HttpResponseHeader.make302Header(lengthOfBodyContent, fileUrl);

        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 302 Found\r\n");
        stringBuffer.append(String.format("Location: %s\r\n", fileUrl));
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");

        assertThat(httpResponseHeader.toString().split("\r\n")).contains(stringBuffer.toString().split("\r\n"));
    }
}