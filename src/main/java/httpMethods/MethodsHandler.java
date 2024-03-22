package httpMethods;

public interface MethodsHandler {
    public void run() throws Exception;

    public byte[] getBody();

    public String getHeader();
}
