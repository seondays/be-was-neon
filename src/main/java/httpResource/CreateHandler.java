package httpResource;

import db.Database;
import httpMethods.PostHandler;
import java.io.FileNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExtensionType;
import webserver.Request;
import webserver.httpElement.HttpResponseHeader;

public class CreateHandler implements PostHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreateHandler.class);
    private final Request request;
    private byte[] responseBody;
    private HttpResponseHeader responseHeader;

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
        responseHeader = HttpResponseHeader.make302Header(responseBody.length, "/");
    }

    public void create() {
        User requestUser = new User(request.getBody());
        Database.addUser(requestUser);
        logger.info(Database.findUserById(requestUser.getUserId()).toString());
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }
}