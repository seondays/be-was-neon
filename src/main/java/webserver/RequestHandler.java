package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
    private HeaderHandler headerHandler;
    private String header;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        headerHandler = new HeaderHandler();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            byte[] body = responseProcess(in);
            DataOutputStream dos = new DataOutputStream(out);
            headerHandler.writeHeader(dos, header);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /*
     응답을 하기 위한 과정을 진행하는 메서드.
     request의 url 종류를 분석하고, 응답해 줄 body 데이터를 반환한다.
    */
    public byte[] responseProcess(String requestLine) throws IOException {
        RequestParser requestParser = new RequestParser(requestLine);
        byte[] body;

        if (requestParser.isCreate()) {
            body = requestParser.responseCreate();
        } else {
            body = requestParser.parseFileToByte();
        }
        return body;
    }

    /*
     응답할 body와 header를 만든다.
     todo : body를 만드는 동작과, 헤더를 만드는 동작을 분리가 필요해보인다. 어떻게 할 수 있을까?
     */
    private byte[] responseProcess(InputStream in) {
        byte[] body;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            logger.debug("request line : {}", line);
            body =  responseProcess(line);
            header = headerHandler.make200Header(body.length);
            return body;
        } catch (Exception e) {
            logger.error(e.getMessage());
            body = "<h1>404 NOT FOUND! 요청한 페이지를 찾을 수 없습니다.</h1>".getBytes();
            header = headerHandler.make404Header(body.length);
            return body;
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
