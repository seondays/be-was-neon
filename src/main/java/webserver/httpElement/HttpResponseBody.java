package webserver.httpElement;

import java.io.IOException;

public class HttpResponseBody {
    private byte[] body;

    public HttpResponseBody() {
        body = new byte[0];
    }

    public HttpResponseBody(byte[] body) throws IOException {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public int length() {
        return body.length;
    }
}
