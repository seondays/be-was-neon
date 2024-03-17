package webserver;

public class ErrorHandler {
    private byte[] errorBody;
    private String errorHeader;
    private final HeaderHandler headerHandler;

    /**
     * 404 말고 다른 에러일 때는 어떻게 처리하지?..
     * 어떤 예외이냐에 따라서 처리되어야 할 텐데 방법을 아직 잘 모르겠다.
     */
    public ErrorHandler() {
        this.headerHandler = new HeaderHandler();
        set404Error();
    }

    private void set404Error(){
        errorBody = "<h1>404 NOT FOUND!</h1>".getBytes();
        errorHeader = headerHandler.get404Header(errorBody.length);
    }

    public byte[] getErrorBody() {
        return errorBody;
    }

    public String getErrorHeader() {
        return errorHeader;
    }
}
