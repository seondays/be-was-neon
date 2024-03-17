package webserver;

import java.io.IOException;

public class Response {
    private String header;
    private byte[] body;
    private final ResponseBodyHandler responseBodyHandler;
    private final HeaderHandler headerHandler;

    public Response(ResponseBodyHandler responseBodyHandler) throws IOException {
        this.responseBodyHandler = responseBodyHandler;
        this.headerHandler = new HeaderHandler();
        setting();
    }

    private void setting() throws IOException {
        body = responseBodyHandler.bodyProcessing();
        header = headerHandler.get200Header(body.length, responseBodyHandler.parseFileUrl());
    }

    public byte[] getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }
}