package webserver;

import httpMethods.MethodsHandler;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class Response {
    private HttpResponseHeader header;
    private HttpResponseBody body;
    private final MethodsHandler methodsHandler;

    public Response(MethodsHandler methodsHandler) {
        this.methodsHandler = methodsHandler;
        setting();
    }

    private void setting() {
        body = methodsHandler.getResponseBody();
        header = methodsHandler.getResponseHeader();
    }

    public HttpResponseBody getBody() {
        return body;
    }

    public HttpResponseHeader getHeader() {
        return header;
    }
}