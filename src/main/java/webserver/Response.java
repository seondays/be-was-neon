package webserver;

import httpMethods.MethodsHandler;

public class Response {
    private String header;
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

    public String getHeader() {
        return header;
    }
}