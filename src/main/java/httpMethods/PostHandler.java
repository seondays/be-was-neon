package httpMethods;

public interface PostHandler extends MethodsHandler {
    public void run() throws Exception;

    public byte[] getBody();

    public String getHeader();
}
