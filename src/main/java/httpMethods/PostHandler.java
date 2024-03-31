package httpMethods;

import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public interface PostHandler extends MethodsHandler {
    public void run() throws Exception;

    public HttpResponseBody getResponseBody();

    public HttpResponseHeader getResponseHeader();
}
