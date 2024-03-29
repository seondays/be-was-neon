package webserver.handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.Response;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private String responseHeader;
    private byte[] responseBody;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            createResponse(in);
            writeHeader(dos, responseHeader);
            writeBody(dos, responseBody);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 응답할 내용을 만드는 과정을 진행한다. Request 객체에 요청을 집어넣고, 필요한 일들을 처리 후 Response 객체에 담는다.
     * 이후 최종적으로 Response에서 값을 가져와서 응답한다.
     */
    private void createResponse(InputStream in) {
        try {
            Request request = new Request(in);
            ResponseHandler responseHandler = new ResponseHandler(request);
            Response response = responseHandler.responseProcessing();
            responseBody = response.getBody();
            responseHeader = response.getHeader().toString();
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler();
            responseHeader = errorHandler.getErrorHeader();
            responseBody = errorHandler.getErrorBody();
        }
    }

    public void writeHeader(DataOutputStream dos, String header) {
        try {
            dos.writeBytes(header);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
