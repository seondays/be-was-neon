package httpMethods;

public interface PostHandler extends MethodsHandler {
    public void run() throws Exception;

    public byte[] getResponseBody();

    public String getResponseHeader();
}
