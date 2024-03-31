package webserver;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpMethods;
import webserver.httpElement.HttpRequestHeader;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);
    private static final String BODY_LENGTH_KEY = "Content-Length";
    private RequestParser requestParser;
    private HttpMethods httpMethod;
    private String resource;
    private String query;
    private final HttpRequestHeader header;
    private Map<String, String> body;

    public Request(InputStream in) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        body = new HashMap<>();
        readStartLine(bufferedInputStream);
        header = new HttpRequestHeader(readHeader(bufferedInputStream));
        readBody(bufferedInputStream);
    }

    /**
     * stream에서 \r\n을 기준으로 한 줄을 읽는다.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private String readOneLine(BufferedInputStream stream) throws IOException {
        ByteArrayOutputStream tmp_holder = new ByteArrayOutputStream();
        int nowByte;
        while ((nowByte = stream.read()) != -1) {
            if (nowByte == '\r') {
                continue;
            }
            if (nowByte == '\n') {
                break;
            }
            tmp_holder.write(nowByte);
        }
        return tmp_holder.toString("UTF-8");
    }

    /**
     * request의 start-line을 읽어와서 파싱 후, 멤버변수로 저장한다.
     *
     * @param stream
     * @throws IOException
     */
    private void readStartLine(BufferedInputStream stream) throws IOException {
        String firstLine = readOneLine(stream);
        requestParser = new RequestParser(firstLine);
        logger.debug("request line : {}", firstLine);
        // GET
        httpMethod = requestParser.getUserMethod();
        // ? 이하 쿼리문 (쿼리가 없을 경우 빈칸으로 들어옴)
        query = requestParser.getUserQuery();
        // /create 와 같은 부분
        resource = requestParser.getUserResource();
    }

    /**
     * request의 header를 읽어온 후, map 멤버변수로 저장한다.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private Map<String, String> readHeader(BufferedInputStream stream) throws IOException {
        final String HEADER_DELIMITER = ":";
        final int SPLIT_LIMIT = 2;
        final Map<String, String> bufferedMap = new HashMap<>();
        String nowLine;

        while (!(nowLine = readOneLine(stream)).equals("")) {
            logger.debug("header -> {}", nowLine);
            String[] headerPieces = nowLine.split(HEADER_DELIMITER, SPLIT_LIMIT);
            bufferedMap.put(headerPieces[0].trim(), headerPieces[1].trim());
        }
        return Collections.unmodifiableMap(bufferedMap);
    }


    /**
     * request의 body를 읽어온 후, 멤버변수로 저장한다. 헤더에서 Content-Length 를 가져와서 그 횟수만큼 읽는다.
     *
     * @param stream
     * @throws IOException
     */
    private void readBody(BufferedInputStream stream) throws IOException {
        String contentLength = header.getValueBy(BODY_LENGTH_KEY);
        int bufferSize = 1024;
        byte[] tmp;
        if (contentLength != null) {
            int bodyLength = Integer.parseInt(contentLength);
            tmp = new byte[bodyLength];
            while (bodyLength > 0) {
                int bytesToRead = Math.min(bufferSize, bodyLength);
                stream.read(tmp, 0, bytesToRead);
                bodyLength -= bufferSize;
            }
            try {
                body = requestParser.parseJsonToMap(new String(tmp));
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public Map<String, String> getBody() {
        logger.info(body.toString());
        return body;
    }

    public HttpRequestHeader getHeader() {
        return header;
    }

    public HttpMethods getHttpMethod() {
        return httpMethod;
    }

    public String getResource() {
        return resource;
    }

    public String getQuery() {
        return query;
    }
}

