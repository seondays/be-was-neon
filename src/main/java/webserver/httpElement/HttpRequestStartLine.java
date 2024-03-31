package webserver.httpElement;

import utils.HttpMethods;

public class HttpRequestStartLine {
    private HttpMethods httpMethod;
    private String resource;
    private String query;

    public HttpRequestStartLine(HttpMethods httpMethod, String resource, String query) {
        this.httpMethod = httpMethod;
        this.resource = resource;
        this.query = query;
    }

    public HttpMethods getHttpMethod() {
        return httpMethod;
    }

    public String getResource() {
        return resource;
    }

    public String getQuery() {
        return query;
    }
}