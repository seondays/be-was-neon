package utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamReader {
    private Logger logger = LoggerFactory.getLogger(StreamReader.class);
    private BufferedInputStream targetStream;

    public StreamReader(BufferedInputStream targetStream) {
        this.targetStream = targetStream;
    }

    /**
     * stream에서 \r\n을 기준으로 한 줄을 읽는다.
     * @return
     * @throws IOException
     */
    public String readOneLine() throws IOException {
        ByteArrayOutputStream tmp_holder = new ByteArrayOutputStream();
        int nowByte;
        while ((nowByte = targetStream.read()) != -1) {
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
     * request의 header를 읽어온 후, map 멤버변수로 저장한다.
     * @return
     * @throws IOException
     */
    public Map<String, String> readHeader() throws IOException {
        final String HEADER_DELIMITER = ":";
        final int SPLIT_LIMIT = 2;
        final Map<String, String> bufferedMap = new HashMap<>();
        String nowLine;

        while (!(nowLine = readOneLine()).equals("")) {
            logger.debug("header -> {}", nowLine);
            String[] headerPieces = nowLine.split(HEADER_DELIMITER, SPLIT_LIMIT);
            bufferedMap.put(headerPieces[0].trim(), headerPieces[1].trim());
        }
        return Collections.unmodifiableMap(bufferedMap);
    }

    /**
     * request의 body를 읽어온 후, 멤버변수로 저장한다. 헤더에서 Content-Length 를 가져와서 그 횟수만큼 읽는다.
     * @param contentLength
     * @return
     * @throws IOException
     */
    public String readBody(int contentLength) throws IOException {
        int bufferSize = 1024;
        byte[] tmp;
        tmp = new byte[contentLength];
        while (contentLength > 0) {
            int bytesToRead = Math.min(bufferSize, contentLength);
            targetStream.read(tmp, 0, bytesToRead);
            contentLength -= bufferSize;
        }
        return new String(tmp);
    }
}