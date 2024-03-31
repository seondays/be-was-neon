package webserver;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpMethods;
import webserver.httpElement.HttpRequestBody;
import webserver.httpElement.HttpRequestHeader;
import webserver.httpElement.HttpRequestStartLine;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);
    private static final String BODY_LENGTH_KEY = "Content-Length";
    private final HttpRequestStartLine startLine;
    private final HttpRequestHeader header;
    private final HttpRequestBody body;

    public Request(RequestParser requestParser) throws IOException {
        startLine = requestParser.parseStartLine();
        header = requestParser.parseHeader();
        body = requestParser.parseBody(header.getValueBy(BODY_LENGTH_KEY));
    }

    public HttpRequestBody getBody() {
        logger.info(body.toString());
        return body;
    }

    public HttpRequestHeader getHeader() {
        return header;
    }

    public HttpMethods getHttpMethod() {
        return startLine.getHttpMethod();
    }

    public String getResource() {
        return startLine.getResource();
    }
}

