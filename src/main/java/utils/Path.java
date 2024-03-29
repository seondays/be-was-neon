package utils;

import java.io.File;
import java.io.FileNotFoundException;

public class Path {
    public static final String BASE_PATH = "src/main/resources/static";
    public static final String DEFAULT_FILE = "/index.html";

    /**
     * 요청받은 리소스 정보를 가지고 유효한 주소를 만들어준다.
     * 요청 리소스가 디렉토리라면 해당 디렉토리의 index.html 파일의 주소를, 파일이라면 해당 파일의 주소를 생성한 후에, 해당 주소가 유효한지 확인한다.
     * 유효하지 않은 파일일 경우 예외를 발생시킨다.
     */
    public String buildURL(String resource) throws FileNotFoundException {
        StringBuffer result = new StringBuffer(BASE_PATH);

        if (isDirectory(resource)) {
            result.append(resource);
            result.append(DEFAULT_FILE);
        } else {
            result.append(resource);
        }

        if (!isFileExists(result.toString())) {
            throw new FileNotFoundException();
        }
        return result.toString();
    }

    private boolean isDirectory(String resource) {
        File file = new File(BASE_PATH + resource);
        return file.isDirectory();
    }

    private boolean isFileExists(String resource) {
        return new File(resource).exists();
    }
}
