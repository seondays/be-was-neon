package httpResource;

import db.Database;
import httpMethods.PostHandler;
import javax.xml.crypto.Data;
import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.RequestParser;
import webserver.handler.SessionHandler;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class SubmitArticleHandler implements PostHandler {
    private Logger logger = LoggerFactory.getLogger(SubmitArticleHandler.class);
    private final Request request;
    private String sid;
    private HttpResponseBody responseBody;
    private HttpResponseHeader responseHeader;

    public SubmitArticleHandler(Request request) {
        this.request = request;
        sid = request.getHeader().getSidCookie();
    }

    @Override
    public void run() throws Exception {
        Article article = createArticle();
        String userId = SessionHandler.getUserSession(sid).getUserId();
        Database.addArticle(userId, article);
        logger.info(Database.findArticle(userId).toString());

        responseBody = new HttpResponseBody();
        responseHeader = HttpResponseHeader.make302Header(responseBody.length(),"/main/index.html");
    }

    private Article createArticle() {
        Article result = new Article();
        result.setContent(request.getBody().getArticleBody());
        result.setAuthor(SessionHandler.getUserSession(sid));
        return result;
    }

    @Override
    public HttpResponseBody getResponseBody() {
        return responseBody;
    }

    @Override
    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }
}
