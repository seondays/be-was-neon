package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 지정된 소켓에서 수신 대기할 수 없는 경우 예외가 발생할 것이다. 따라서 try로 감싸준다. (이미 사용중인 포트와 같은 경우)
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            Socket connection;
            // accept 메서드를 실행했기 때문에 메인 스레드는 멈춰 있다. 소켓을 기다리는 block 상태이다. 인터럽트를 받아서 깨어난다.
            while ((connection = listenSocket.accept()) != null) {
                pool.submit(new RequestHandler(connection));
            }
            pool.shutdown();
        }
    }
}
