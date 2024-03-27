package httpResource;

import db.Database;
import httpMethods.PostHandler;
import java.io.FileNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExtensionType;
import webserver.Request;

public class CreateHandler implements PostHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreateHandler.class);
    private final Request request;
    private byte[] responseBody;
    private String responseHeader;

    public CreateHandler(Request request) {
        this.request = request;
    }

    /**
     * 유저를 db에 저장해야 함
     */
    @Override
    public void run() throws FileNotFoundException {
        create();
        responseBody = new byte[0];
        responseHeader = get302Header(responseBody.length, "/");
    }

    public void create() {
        User requestUser = new User(request.getBody());
        Database.addUser(requestUser);
        logger.info(Database.findUserById(requestUser.getUserId()).toString());
    }

    public String get302Header(int lengthOfBodyContent, String fileUrl) {
        String contentType = ExtensionType.getContentType(fileUrl);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 302 Found\r\n");
        stringBuffer.append(String.format("Location: %s\r\n", fileUrl));
        stringBuffer.append(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }
}