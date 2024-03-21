package webserver;

import java.io.IOException;

public class Response {
    private String header;
    private byte[] body;
    private final ResponseBodyHandler responseBodyHandler;
    private final ResponseHeaderHandler responseHeaderHandler;

    public Response(ResponseBodyHandler responseBodyHandler, ResponseHeaderHandler responseHeaderHandler) throws IOException {
        this.responseBodyHandler = responseBodyHandler;
        this.responseHeaderHandler = responseHeaderHandler;
        setting();
    }

    private void setting() throws IOException {
        body = responseBodyHandler.bodyProcessing();
        header = responseHeaderHandler.createHeader(body.length,responseBodyHandler.getFileUrl());
    }

    public byte[] getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }
}