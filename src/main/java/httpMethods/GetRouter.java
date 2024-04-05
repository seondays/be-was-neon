package httpMethods;

import java.util.HashMap;
import java.util.Map;
import utils.Path;
import webserver.Request;
import webserver.handler.AuthenticationHandler;
import webserver.handler.DynamicHttpBodyHandler;
import webserver.handler.StaticHttpHandler;
import webserver.httpElement.HttpResponseBody;
import webserver.httpElement.HttpResponseHeader;

public class GetRouter implements MethodsHandler {
    private final AuthenticationHandler authenticationHandler;
    private final Request request;
    private HttpResponseBody responseBody;
    private HttpResponseHeader responseHeader;
    private Path path;
    private Map<String, GetHandler> dynamicHanlderMap;

    public GetRouter(Request request) {
        this.request = request;
        path = new Path();
        authenticationHandler = new AuthenticationHandler(request);
        initDynamicHandlerMap();
    }

    public void initDynamicHandlerMap() {
        dynamicHanlderMap = new HashMap<>();
        dynamicHanlderMap.put("/main", new DynamicHttpBodyHandler(request, path, authenticationHandler));
        dynamicHanlderMap.put("/user/list", new DynamicHttpBodyHandler(request, path, authenticationHandler));
        dynamicHanlderMap.put("/article", new DynamicHttpBodyHandler(request, path, authenticationHandler));
    }


    /**
     * 요청에 적합한 핸들러를 찾아서 분류한다.
     * 동적 파일 응답이 필요한 요청이면 동적 핸들러로 보내고, 그렇지 않은 경우에는 정적 핸들러로 보내서 처리한다.
     * @throws Exception
     */
    public void run() throws Exception {
        GetHandler getHandler = dynamicHanlderMap.get(request.getResource());
        if (getHandler == null) {
            getHandler = new StaticHttpHandler(request, path, authenticationHandler);
        }
        getHandler.run();
        responseHeader = getHandler.getResponseHeader();
        responseBody = getHandler.getResponseBody();
    }

    public HttpResponseBody getResponseBody() {
        return responseBody;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }
}