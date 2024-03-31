package httpMethods;

import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public interface MethodsHandler {
    public void run() throws Exception;

    public HttpResponseBody getResponseBody();

    public HttpResponseHeader getResponseHeader();
}
