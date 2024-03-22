package webserver.handler;

import httpMethods.GetHandler;
import httpMethods.MethodsHandler;
import httpMethods.PostHandler;
import httpResource.CreateHandler;
import httpResource.LoginHandler;
import httpResource.LogoutHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpMethods;
import utils.Path;
import webserver.Request;
import webserver.Response;

public class ResponseHandler {
    /**
     * Response 객체의 body 멤버변수를 위해 Request의 resource가 명령어인지 아니면 파일 요청인지를 처리해서 적절한 body를 만드는 것이 이 객체의 역할이다.
     */
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private final Path path;
    private final Request request;
    private Map<String, PostHandler> functionMap;
    private String fileUrl;

    public ResponseHandler(Request request) throws IOException {
        this.request = request;
        path = new Path();
        initFunctionMap();
    }

    private void initFunctionMap() {
        functionMap = new HashMap<>();
        functionMap.put("/create", new CreateHandler(request));
        functionMap.put("/login", new LoginHandler(request));
        functionMap.put("/logout", new LogoutHandler(request));
    }

    /**
     * body를 어떤 방법으로 처리할지 Method를 통해 분기를 나눈 후 그에 맞게 처리한다.
     *
     * @return
     * @throws IOException
     */
    public Response responseProcessing() throws Exception {
        if (request.getHttpMethod().equals(HttpMethods.GET)) {
            return getProcess();
        }
        return postProcess();
    }

    /**
     * GET method인 경우의 처리
     *
     * @return
     * @throws IOException
     */
    private Response getProcess() throws Exception {
        MethodsHandler getHandler = new GetHandler(request);
        getHandler.run();
        return new Response(getHandler);
    }

    /**
     * POST method인 경우의 처리
     *
     * @return
     */
    private Response postProcess() throws Exception {
        MethodsHandler postHandler = functionMap.get(request.getResource());
        postHandler.run();
        return new Response(postHandler);
    }
}