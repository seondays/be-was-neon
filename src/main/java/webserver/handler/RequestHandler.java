package webserver.handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.RequestParser;
import webserver.Response;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Response response;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            createResponse(in);
            writeResponse(dos, response);
            dos.flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void createResponse(InputStream in) throws Exception {
        try {
            RequestParser requestParser = new RequestParser(in);
            Request request = new Request(requestParser);

            ResponseHandler responseHandler = new ResponseHandler(request);
            response = responseHandler.responseProcessing();
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler();
            response = new Response(errorHandler);
        }
    }

    private void writeResponse(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getHeader().toString());
            dos.write(response.getBody().getBody());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
