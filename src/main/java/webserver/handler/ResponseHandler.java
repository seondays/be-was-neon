package webserver.handler;

import httpMethods.GetRouter;
import httpMethods.MethodsHandler;
import httpMethods.PostHandler;
import httpResource.CreateHandler;
import httpResource.LoginHandler;
import httpResource.LogoutHandler;
import httpResource.SubmitArticleHandler;
import httpResource.SubmitCommentHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import utils.HttpMethods;
import webserver.Request;
import webserver.Response;

public class ResponseHandler {
    private final Request request;
    private Map<String, PostHandler> functionMap;

    public ResponseHandler(Request request) throws IOException {
        this.request = request;
        initFunctionMap();
    }

    private void initFunctionMap() {
        functionMap = new HashMap<>();
        functionMap.put("/create", new CreateHandler(request));
        functionMap.put("/login", new LoginHandler(request));
        functionMap.put("/logout", new LogoutHandler(request));
        functionMap.put("/submit-article", new SubmitArticleHandler(request));
        functionMap.put("/submit-comment", new SubmitCommentHandler(request));
    }

    /**
     * body를 어떤 방법으로 처리할지 Method를 통해 분기를 나눈 후 그에 맞게 처리한다.
     *
     * @return 응답 객체 생성
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
     * @return 응답 객체 생성
     * @throws Exception
     */
    private Response getProcess() throws Exception {
        MethodsHandler getHandler = new GetRouter(request);
        getHandler.run();
        return new Response(getHandler);
    }

    /**
     * POST method인 경우의 처리
     * 리소스 요청 종류에 따라서 map에서 알맞은 핸들러를 가져와 처리한다
     *
     * @return 응답 객체 생성
     * @throws Exception
     */
    private Response postProcess() throws Exception {
        MethodsHandler postHandler = functionMap.get(request.getResource());
        postHandler.run();
        return new Response(postHandler);
    }
}