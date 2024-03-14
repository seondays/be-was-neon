package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeaderHandler {
    private static final Logger logger = LoggerFactory.getLogger(HeaderHandler.class);

    /*
     헤더를 만드는 동작과 DataOutputStream에 데이터를 쓰는 동작이 두 메서드로 나눠진다.
     어떤 헤더를 만들 것인지 예외를 catch하는 부분에서 분기가 갈리는데,
     DataOutputStream와 해당 내용이 함께 메서드에 있을 경우 또다시 try-catch로 감싸주어야 해서
     처리가 어려워졌기 때문에 두 동작을 분리하게 되었다.
     */
    public void writeHeader(DataOutputStream dos, String header) {
        try {
            dos.writeBytes(header);
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String make200Header(int lengthOfBodyContent) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 200 OK \r\n");
        stringBuffer.append("Content-Type: text/html;charset=utf-8\r\n");
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public String make404Header(int lengthOfBodyContent) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 404 Not Found \r\n");
        stringBuffer.append("Content-Type: text/html;charset=utf-8\r\n");
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }
}