package httpMethods;

public interface GetHandler extends MethodsHandler {
    public void run() throws Exception;

    public byte[] getResponseBody();

    public String getResponseHeader();
}
