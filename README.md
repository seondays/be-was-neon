# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

### HTTP GET 메서드 [🔗](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/GET)
get 방식은 데이터를 요청할 때만 사용해야 하고, `GET /index.html`와 같이 GET과 요청하는 정보를 보낸다.
성공 시 200 (OK) 성공 코드와, 요청한 리소스를 응답받는다.

### 🧐 전체 동작 구조 분석
#### WebServer 클래스
- 기본 포트 번호는 8080으로 설정되었다.
> 왜 8080으로 설정했을까?
>> 보안 때문에 리눅스나 유닉스에서는 1024 이하의 포트는 유저들이 바인딩 할 수 없게 했다고 한다. (root가 아닌 사용자들)
그래서 사용자가 웹 서버를 실행하기 위해서 8080 포트를 많이 사용하던 것이 관습이 된 모양  
[참고자료](https://www.grc.com/port_8080.htm)

- `ServerSocket listenSocket = new ServerSocket(port)`로 서버소켓을 생성하고 있다.
- `Socket connection`을 가지고 listenSocket.accept()을 null이 아닌 것을 체크하는 중
    `(connection = listenSocket.accept()) != null` 이렇게 되어 있다.  
    &rarr; 요청이 들어오는지 대기ing. 들어오면 Socket이 만들어져 연결되므로, 내부 코드 실행
> ServerSocket과 일반 Socket의 차이는 무엇일까? [🔗](https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html)
> >[ServerSocket](https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html)은
서버 측에서 클라이언트를 기다리기 위해 대기하는 역할을 하는 소켓이다. 포트번호를 바인딩해서 새로운 서버소켓을 만들면 accept 메서드를 통해
해당 서버로 오는 요청을 기다리게 된다.
[Socket](https://docs.oracle.com/javase/8/docs/api/index.html?java/net/Socket.html)은 실제로 클라이언트와 통신하기 위한 객체이다.  
accept 메서드를 통해 대기하고 있던 서버가 클라이언트의 요청을 받게 되고 성공적으로 설정되면, 동일한 로컬 포트에 바인딩되고, 원격 주소와 원격 포트가
클라이언트의 포트로 설정된 Socket 객체를 반환하고, 이제 서버는 이 소켓을 이용하여 클라이언트와 통신할 수 있다.

> 굳이 Socket이라는 connection 변수를 따로 만들어서 체크하는 이유가 뭘까?
> > Socket과 ServerSocket이 다른 역할을 하는 객체이고, 클라이언트와의 연결 수단인 Socket을 가지고 Handler로 넘겨서
통신해야하기 때문에

> main에서 따로 try만 하고 catch 대신 throws Exception을 하는 이유는 뭘까?

- 이후 RequestHandler를 Thread pool에 제출하여 실행한다.

#### RequestHandler 클래스
connection 이라는 Socket을 외부에서 받아서 사용하고 있다.

socket의 getInputStream으로 클라이언트에서 서버쪽으로 오는 요청 데이터를 가져오고,  
socket의 getOutputStream으로 서버에서 클라이언트쪽으로 응답을 보내는 데이터를 가져온다.

```
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```
요청 데이터(request)는 이런 식으로 들어온다. 여기서 GET은 HTTP 요청 메서드로 해당 방식으로 데이터를 받는다는 의미이며
/index.html은 요청하는 리소스의 경로(파일명)를 나타낸다. 마지막으로 HTTP/1.1은 HTTP의 버전을 의미한다.

요청 데이터가 오는 InputStream은 바이트로 이루어져있기 때문에 불편, 그래서 우리가 확인하고 보기 위해 BufferedReader와 함께
활용한다.
첫번째 줄의 데이터에서 path를 분리해서 해당 path에 존재하는 내용을 byte배열로 변경해준다. OutputStream으로 보낼 때도 바이트로 보내야하기
때문이다.

DataOutputStream을 가지고 클라이언트에게로 보낼 응답을 만들 준비를 한다.
먼저 `response200Header` 메서드에서는 응답 헤더를 만드는 역할을 한다.
추가적으로 `responseBody` 메서드를 이용해 본문에 화면에 보여줄 코드 내용을 넣어야 한다. 요청받은 path의 파일 내용을 파라미터로
지정하여 `DataOutputStream`에 이어 담아주면 된다. 해당 메서드에서는 `DataOutputStream`의 write를 이용해 바이트 배열의 내용을
해당 스트림에 쓰고 있다.

그리고 이와 별개로 logger를 이용하여 로그를 찍어서 보여주고 있다.

> OutputStream과 DataOutputStream의 차이는 뭘까? 굳이 한번 DataOutputStream에 넣어서 응답을 만드는 이유는 뭘까?
> > DataOutputStream은 각종 데이터를 바이트 형태로 바꿔줄 수 있도록 하는 기능들을 가지고 있어서, 우리가 필요한 내용들을 작성해서
다시 바이트 형태로 바꿔 줄 수 있기 때문이다.