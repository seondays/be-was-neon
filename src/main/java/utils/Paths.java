package utils;

import java.util.Arrays;
import java.util.Objects;

public enum Paths {
    HOME("/index.html", "/index.html"),
    REGISTER("/registration", "/registration/index.html"),
    CREATE("/create","");

    private final String request;
    private final String response;

    private Paths(String request, String response) {
        this.request = request;
        this.response = response;
    }

    //체크해서 어디 루트인지 찾아주기
    public static Paths parsePath(String request) {
        return Objects.requireNonNull(Arrays.stream(Paths.values())
                        .filter(paths -> paths.request.equals(request))
                        .findFirst()
                        .orElse(null));
    }

    public String makeResultUrl() {
        final String BASE_PATH = "src/main/resources/static/";
        return BASE_PATH + response;
    }

    public String getRequest() {
        return request;
    }
}
