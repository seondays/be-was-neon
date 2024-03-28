package httpMethods;

import webserver.httpElement.HttpResponseHeader;

public interface GetHandler extends MethodsHandler {
    public void run() throws Exception;

    public byte[] getResponseBody();

    public HttpResponseHeader getResponseHeader();
}
