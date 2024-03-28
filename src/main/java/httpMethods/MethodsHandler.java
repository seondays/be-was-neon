package httpMethods;

import webserver.httpElement.HttpResponseHeader;

public interface MethodsHandler {
    public void run() throws Exception;

    public byte[] getResponseBody();

    public HttpResponseHeader getResponseHeader();
}
