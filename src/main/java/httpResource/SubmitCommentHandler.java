package httpResource;

import db.Database;
import httpMethods.PostHandler;
import model.Article;
import model.Comment;
import model.User;
import webserver.Request;
import webserver.handler.SessionHandler;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class SubmitCommentHandler implements PostHandler {
    private final Request request;
    private final String sid;
    private HttpResponseBody responseBody;
    private HttpResponseHeader responseHeader;

    public SubmitCommentHandler(Request request) {
        sid = request.getHeader().getSidCookie();
        this.request = request;
    }

    @Override
    public void run() throws Exception {
        String commentBody = request.getBody().getCommentBody();
        int articleId = Integer.parseInt(request.getBody().getArticleId());
        User commentAuthor = SessionHandler.getUserSession(sid);
        Article article = Database.findArticle(articleId);
        article.addComment(new Comment(commentAuthor, commentBody, articleId));

        responseBody = new HttpResponseBody();
        responseHeader = HttpResponseHeader.make302Header(responseBody.length(), "/main/index.html");
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