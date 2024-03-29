package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        body = new HashMap<>();
        readStartLine(br);
        header = new HttpRequestHeader(readHeader(br));
        readBody(br);
    }

    /**
     * request의 start-line을 읽어와서 파싱 후, 멤버변수로 저장한다.
     *
     * @param br
     * @throws IOException
     */
    private void readStartLine(BufferedReader br) throws IOException {
        String firstLine = br.readLine();
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
     * @param br
     * @throws IOException
     */
    private Map<String, String> readHeader(BufferedReader br) throws IOException {
        final String HEADER_DELIMITER = ":";
        final int SPLIT_LIMIT = 2;
        String line = br.readLine();
        final Map<String, String> bufferedMap = new HashMap<>();
        while (!line.equals("")) {
            logger.debug("header -> {}", line);
            String[] headerPieces = line.split(HEADER_DELIMITER, SPLIT_LIMIT);
            bufferedMap.put(headerPieces[0].trim(), headerPieces[1].trim());
            line = br.readLine();
        }
        return Collections.unmodifiableMap(bufferedMap);
    }

    /**
     * request의 body를 읽어온 후, 멤버변수로 저장한다. 헤더에서 Content-Length 를 가져와서 그 횟수만큼 읽는다. 만일 해당 항목이 없을 경우, 비워져 있는 배열을 반환한다.
     *
     * @param
     * @throws IOException
     */
    private void readBody(BufferedReader br) throws IOException {
        String contentLength = header.getValueBy(BODY_LENGTH_KEY);
        if (contentLength != null) {
            int bodyLength = Integer.parseInt(contentLength);
            char[] charBuffer = new char[bodyLength];
            br.read(charBuffer, 0, bodyLength);
            try {
                body = requestParser.parseJsonToMap(new String(charBuffer));
            } catch (ArrayIndexOutOfBoundsException e) {
                // todo : 추후 흐름 다르게 분기 필요
                logger.debug(e.getMessage());
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

