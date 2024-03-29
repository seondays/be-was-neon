package webserver.handler;

import webserver.httpElement.HttpResponseHeader;

public class ErrorHandler {
    private byte[] errorBody;
    private HttpResponseHeader errorHeader;

    public ErrorHandler() {
        set404Error();
    }

    private void set404Error(){
        errorBody = "<h1>404 NOT FOUND!</h1>".getBytes();
        errorHeader = HttpResponseHeader.make404Header(errorBody.length);
    }

    public byte[] getErrorBody() {
        return errorBody;
    }

    public HttpResponseHeader getErrorHeader() {
        return errorHeader;
    }
}
