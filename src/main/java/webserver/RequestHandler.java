package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // InputStream으로 읽어 오는 요청을 BufferedReader에 담는다
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            logger.debug("request line : {}", line);

            // request의 첫번째 줄에서 path 분리하기
            String[] requestLine = line.split(" ");
            String path = "src/main/resources/static/" + requestLine[1];

            // request 요청은 마지막 끝에 공백문자가 포함되어 오기 때문에, ""을 체크하여 while문을 돈다
            while (!line.equals("")) {
                line = br.readLine();
                logger.debug("header : {}", line);
            }
            DataOutputStream dos = new DataOutputStream(out);
            byte[] mybody = parseFileToByte(path);
            response200Header(dos, mybody.length);
            responseBody(dos, mybody);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /* file 내용을 바이트로 읽어와서 byte[]로 변환하는 메서드를 만들어준다.
       파일의 크기만큼의 바이트 배열을 만들고, FileInputStream의 read 메서드를 통해 해당 배열에
       파일의 내용을 작성한다.
    */
    private byte[] parseFileToByte(String path) {
        File file = new File(path);
        byte[] result = new byte[(int) new File(path).length()];
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
