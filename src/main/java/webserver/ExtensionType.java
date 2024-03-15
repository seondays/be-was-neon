package webserver;

import java.io.File;
import java.util.Arrays;

public enum ExtensionType {
    HTML("text/html"),
    CSS("text/css"),
    JS("text/jscript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpg"),
    SVG("image/svg+xml");


    private final String contentType;

    ExtensionType(String contentType) {
        this.contentType = contentType;
    }

    /*
     파일의 URL이 주어지면 파일의 확장자 타입 이름에 맞게 Content Type을 반환해줍니다
     fileUrl은 이미 최종적으로 파싱된 주소가 넘어오기 때문에, 따로 디렉토리일 경우를 체크해주지 않습니다
     찾는 확장자가 없을 경우에는 정상적으로 파일을 읽을 수 없는 상황이기 때문에, 404 에러 페이지로 응답하기 위한 text/html를 넘겨줍니다.
     */
    public static String getContentType(String fileUrl) {
        String fileName = new File(fileUrl).getName();
        int extensionIndex = fileName.lastIndexOf('.');
        String extension = fileName.substring(extensionIndex + 1);

        return Arrays.stream(values())
                .filter(type -> type.name().equals(extension.toUpperCase()))
                .map(type -> type.contentType)
                .findFirst()
                .orElse("text/html");
    }
}
