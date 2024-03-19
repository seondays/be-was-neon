package webserver;

public class ErrorHandler {
    private byte[] errorBody;
    private String errorHeader;

    /**
     * todo : 404 말고 다른 에러일 때 처리 필요
     */
    public ErrorHandler() {
        set404Error();
    }

    private void set404Error(){
        errorBody = "<h1>404 NOT FOUND!</h1>".getBytes();
        errorHeader = get404Header(errorBody.length);
    }

    /**
     *      우리 서버에서는 404일 경우 정해진 body 타입이 있기 때문에,
     *      일단은 따로 Content Type을 변수로 주지 않아도 된다.
     */
    public String get404Header(int lengthOfBodyContent) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HTTP/1.1 404 Not Found \r\n");
        stringBuffer.append("Content-Type: %s;charset=utf-8\r\n");
        stringBuffer.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public byte[] getErrorBody() {
        return errorBody;
    }

    public String getErrorHeader() {
        return errorHeader;
    }
}
