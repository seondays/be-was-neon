package webserver.handler;

import httpMethods.MethodsHandler;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class ErrorHandler implements MethodsHandler {
    private HttpResponseBody errorBody;
    private HttpResponseHeader errorHeader;

    public ErrorHandler() throws Exception {
        run();
    }

    public void run() throws Exception {
        errorBody = new HttpResponseBody("<h1>404 NOT FOUND!</h1>".getBytes());
        errorHeader = HttpResponseHeader.make404Header(errorBody.length());
    }

    public HttpResponseBody getResponseBody(){
        return errorBody;
    }

    public HttpResponseHeader getResponseHeader(){
        return errorHeader;
    }
}
