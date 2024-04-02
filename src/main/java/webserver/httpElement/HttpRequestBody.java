package webserver.httpElement;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestBody {
    private Logger logger = LoggerFactory.getLogger(HttpRequestBody.class);
    private Map<String, String> body;

    public HttpRequestBody(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public String getUserId() {
        return body.get("userId");
    }

    public String getPassword() {
        return body.get("password");
    }

    public String getName() {
        return body.get("name");
    }

    public String getEmail() {
        return body.get("email");
    }

    public String getArticleBody() {
        return body.get("articleBody");
    }

    public String getCommentBody() {
        return body.get("commentBody");
    }

    public String getArticleId() {
        return body.get("articleId");
    }
}