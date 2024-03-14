# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

### 필요한 전체 기능 정리
- 리퀘스트 요청에서 리소스만 뽑아내는 기능
- 분리한 리소스가 파일인지 디렉토리인지 식별하는 기능
  - 파일인 경우 해당 파일의 주소를 앞에 추가하여 온전한 path를 만들어주는 기능 필요
  - 디렉토리일 경우 해당 디렉토리 내부의 index.html을 찾아가도록 하는 기능 필요
- 요청에 대한 응답 내용을 준비하는 기능
  - 파일이 존재하는 경우, 해당 파일을 전송하기 위해 바이트 배열로 변경하는 기능 필요
  - 요청 결과에 따라 헤더를 추가한다
    - e) 파일이 없는 경우 예외 처리 (헤더를 404로 설정)
    - 성공적으로 응답을 주는 경우 헤더를 200으로 설정

## Step2 GET으로 회원가입
### step2 기능 구현 목록
- [x] 회원가입 메뉴 요청이 들어오면 회원가입 페이지를 보내준다.
- [x] 회원가입 메뉴에서 사용자가 가입한 값을 가져와서 저장한다.
  - [x] 한글 이름이 들어오는 경우 디코딩 해주는 기능
- [x] 저장한 결과에 따라서 적절한 응답 메시지를 보내준다.
- [x] 클라이언트에서 오는 요청이 어떤 경로를 찾아야 하는지 체크해주는 기능

### 설계 및 고민 정리
- 회원가입버튼을 누르면 GET /registration HTTP/1.1 이렇게 요청이 온다. step-1과 다르게 실제 파일명으로 들어오는 것이 아니다.
우리는 /registration/index.html 으로 응답해줘야 한다. 이를 위해 요청-응답을 알맞게 변환해줄 Paths라는 객체가 있으면 좋을 것 같다.


- 회원가입 정보를 입력하고 제출하는 버튼을 눌러도, 서버로 아무런 요청이 오지 않는다.. 어떻게 요청 url을 받을 것인가?
&rarr; html 파일에서 아이디, 닉네임, 비밀번호를 가지고 보내기를 원하는 형식으로 바꾼 다음 [fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch)
를 통해 서버로 요청을 보낸다.


- create 요청을 받았을 때도 지금 수행 후 빈 body 값을 일단 만들도록 구현했는데 이 부분이 적절한 판단이었을까?


- accept 메서드가 호출되면 소켓에 대한 연결을 기다리는 상태가 된다. (연결이 이루어질 때까지 block 상태)
그러다 서버소켓에 요청이 들어오면 서버소켓은 Interrupt를 받아 깨어나고 요청을 받아 소켓을 만듭니다. (현재 프로젝트 코드에서는 connection)
이제 해당 소켓 객체를 통해서 통신이 이루어진다. 내가 착각했던 부분은 connection 소켓 객체가 클라이언트 쪽에서 요청이 오면서 열린 포트 그 자체라고 생각했기 때문에,
이 소켓에 포트번호 정보가 필요 없다고 했을 때 혼동이 있었다.
그치만 포트 번호는 소켓의 식별을 위해 필요한 정보 중 하나이지, 소켓 자체가 포트가 아니다.


- 요청받은 리소스를 찾을 수 없는 경우 어떻게 예외 처리를 해야 할 것이며, 헤더에 그 결과를 어떻게 반영해서 보내줄 수 있을까?
&rarr; 예외를 터트리고, 그 예외를 위로 올려서 catch로 처리하자. catch의 본문에는 404 헤더를 넣는 부분을 추가하자.
&rarr; 그런데 기존 헤더를 생성하는 부분은 DataOutputStream에 헤더를 내보내는 부분이 있어서 여기서도 try가 필요하게 된다.. 그러면 두 기능을 메서드 두개로 나눠보자!

### 궁금한 점 분석
NullPointerException 예외 발생 가능성이 있는 코드가 있었다. 구현되어 있는 내용상 무조건 NullPointerException이 발생하는 상황이었는데,
해당 예외를 핸들링하도록 하는 부분을 작성하지 않았으므로, 해당 예외가 콘솔창에 표시되어야 했다.

그런데 이런 상황에서 thread pool을 만들어서 pool에 submit 하는 방식으로 서버를 구현할 경우, 해당 예외가 발생하더라도 콘솔 창에 예외가 뜨지 않았다.
반면 스레드를 이용해서 바로 start로 실행하는 경우, 혹은 pool에 excute로 실행하는 경우에는 예외가 발생했다고 콘솔 창에서 표시되는데 왜 동작 결과가 다른 것일까?

```
Thread thread = new Thread(new RequestHandler(connection));
thread.start();
```
이렇게 직접 스레드를 가지고 서버를 시작하는 경우 오류가 나는 부분에 도달하면 아래와 같이 표시된다.  
```
Exception in thread "Thread-1" java.lang.NullPointerException
	at java.base/java.util.Objects.requireNonNull(Objects.java:209)
	at utils.Paths.parsePath(Paths.java:21)
	at webserver.RequestParser.parsePath(RequestParser.java:70)
	at webserver.RequestParser.parseFileToByte(RequestParser.java:45)
	at webserver.RequestHandler.responseProcess(RequestHandler.java:54)
	at webserver.RequestHandler.run(RequestHandler.java:32)
	at java.base/java.lang.Thread.run(Thread.java:840)
Exception in thread "Thread-4" java.lang.NullPointerException
	at java.base/java.util.Objects.requireNonNull(Objects.java:209)
	at utils.Paths.parsePath(Paths.java:21)
	at webserver.RequestParser.parsePath(RequestParser.java:70)
	at webserver.RequestParser.parseFileToByte(RequestParser.java:45)
	at webserver.RequestHandler.responseProcess(RequestHandler.java:54)
	at webserver.RequestHandler.run(RequestHandler.java:32)
```

반면 submit을 사용해서 다음과 같이 서버를 실행하면

```
pool.submit(new RequestHandler(connection));
```
![정상 콘솔](<img width="1600" alt="스크린샷 2024-03-13 오후 7 14 17" src="https://github.com/seondays/LeetCode/assets/110711591/b628bf8f-b338-45fe-a6c6-0d5ef8d1998e">)'

콘솔 창에 아무런 예외가 발생하지 않고 있다. 왜일까?

#### submit()와 execute()
> Submits a value-returning task for execution and returns a Future representing the pending results of the task.

> Executes the given command at some time in the future.

~작성중~


### 알아보기
- Objects로 null을 핸들링하는 방법들
- completablefuture