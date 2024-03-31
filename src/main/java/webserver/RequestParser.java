package webserver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpMethods;
import utils.StreamReader;
import webserver.httpElement.HttpRequestBody;
import webserver.httpElement.HttpRequestHeader;
import webserver.httpElement.HttpRequestStartLine;

public class RequestParser {
    private Logger logger = LoggerFactory.getLogger(RequestParser.class);
    public static final String QUERY_DELIMITER = "\\?";
    public static final String SPACE = " ";
    public static final String JSON_TEXT_MARK = "\"";
    public static final String JSON_ELEMENTS_DELIMITER = ",";
    public static final String JSON_KEY_VALUE_DELIMITER = ":";
    private StreamReader streamReader;
    private String[] firstLinePiece;

    public RequestParser(InputStream in) {
        BufferedInputStream stream = new BufferedInputStream(in);
        streamReader = new StreamReader(stream);
    }

    // make start-line
    public HttpRequestStartLine parseStartLine() throws IOException {
        String firstLine = streamReader.readOneLine();
        firstLinePiece = firstLine.split(SPACE);
        logger.debug("request line : {}", firstLine);
        // GET
        HttpMethods httpMethod = getUserMethod();
        // /create 와 같은 부분
        String resource = getUserResource();
        // ? 이하 쿼리문 (쿼리가 없을 경우 빈칸으로 들어옴)
        String query = getUserQuery();
        return new HttpRequestStartLine(httpMethod, resource, query);
    }

    // make header
    public HttpRequestHeader parseHeader() throws IOException {
        return new HttpRequestHeader(streamReader.readHeader());
    }

    // make body
    public HttpRequestBody parseBody(String contentLength) throws IOException {
        int bodyLength;
        if (contentLength != null) {
            bodyLength = Integer.parseInt(contentLength);
            String bodyString = streamReader.readBody(bodyLength);
            try {
                return new HttpRequestBody(parseJsonToMap(bodyString));
            } catch (IndexOutOfBoundsException e) {
                logger.error(e.getMessage());
            }
        }
        return new HttpRequestBody(new HashMap<>());
    }

    public HttpMethods getUserMethod() {
        return HttpMethods.matchMethods(firstLinePiece[0]);
    }

    public String getUserQuery() {
        String[] queryPieces = firstLinePiece[1].split(QUERY_DELIMITER);
        if (queryPieces.length > 1) {
            return queryPieces[1];
        }
        return "";
    }

    // todo : 위치 고민 필요
    public Map<String, String> parseJsonToMap(String info) {
        // 중괄호와 콜론 제거
        info = info.substring(1, info.length() - 1).replaceAll(JSON_TEXT_MARK, "");
        // , 으로 분리 후 다시 : 로 분리
        return Arrays.stream(info.split(JSON_ELEMENTS_DELIMITER))
                .map(pieces -> pieces.split(JSON_KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        key -> key[0],
                        value -> value[1]
                ));
    }

    public String getUserResource() {
        return firstLinePiece[1].split(QUERY_DELIMITER)[0];
    }
}