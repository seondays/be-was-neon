package webserver;

import httpMethods.MethodsHandler;
import webserver.httpElement.HttpResponseHeader;

public class Response {
    private HttpResponseHeader header;
    private byte[] body;
    private final MethodsHandler methodsHandler;

    public Response(MethodsHandler methodsHandler) {
        this.methodsHandler = methodsHandler;
        setting();
    }

    private void setting() {
        body = methodsHandler.getResponseBody();
        header = methodsHandler.getResponseHeader();
    }

    public byte[] getBody() {
        return body;
    }

    public HttpResponseHeader getHeader() {
        return header;
    }
}