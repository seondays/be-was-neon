package httpMethods;

public interface MethodsHandler {
    public void run() throws Exception;

    public byte[] getResponseBody();

    public String getResponseHeader();
}
