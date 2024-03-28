package httpMethods;

import httpResource.AddNameHandler;
import httpResource.CreateHandler;
import httpResource.LoginHandler;
import httpResource.LogoutHandler;
import httpResource.MakeUserListHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import utils.Path;
import webserver.Request;
import webserver.handler.AuthenticationHandler;
import webserver.handler.DynamicHttpBodyHandler;
import webserver.handler.StaticHttpHandler;

public class GetRouter implements MethodsHandler {
    private final AuthenticationHandler authenticationHandler;
    private final Request request;
    private byte[] responseBody;
    private String responseHeader;
    Path path;

    public GetRouter(Request request) {
        this.request = request;
        path = new Path();
        authenticationHandler = new AuthenticationHandler(request);
    }

    /**
     * 요청한 클라이언트가 로그인했는지 아닌지 여부를 체크하여 정적, 혹은 동적 페이지를 생성하기 위한 핸들러로 각각 분류하여 처리한다.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        GetHandler getHandler;
        if (authenticationHandler.isAuthenticationUser()) {
            getHandler = new DynamicHttpBodyHandler(request, path);
        } else {
            getHandler = new StaticHttpHandler(request, path);
        }
        getHandler.run();
        responseHeader = getHandler.getResponseHeader();
        responseBody = getHandler.getResponseBody();
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }
}