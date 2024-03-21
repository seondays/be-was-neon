# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## Step4 POST로 회원 가입
### step4 기능 구현 목록
- [x] POST로 들어오는 요청에 적절한 응답을 보내는 기능
- [x] create 요청을 처리한 후, 메인 페이지로 redirection하는 기능
- [x] 회원가입 기능 POST로 변경

### 설계 및 고민
- 이번 step에서는 서버로 데이터를 보내기 위한 JS 코드에 관한 여러가지 고민이나 어려움이 있었다.
  - POST 방식으로 클라이언트가 body를 보내는 형식에도 여러가지가 있었는데, 그중 JSON 형식으로 보내도록 JS코드를 변경했고
    서버로 들어와서는 자바의 Map으로 저장하도록 했다.
- Request를 가공해서 Response에 응답할 값으로 저장한 후 이것을 클라이언트로 보내주는 것이 이 서버의 목적이고, 저 둘 사이에서 각종 핸들러들이 목적의 달성을 위해 작업을 하고 있다고 생각하기로 했다.
&rarr; 이번에 헤더를 만드는 과정에서 Response가 Request가 가지고 있는 값을 이용해야 하는 상황이 생겼다. 그런데 이렇게 되면 기존 구조에 맞지 않는다. header를 위한 handler를 새로 만들자!

### 마주친 문제와 해결 과정
#### - Request에서 인코딩에 따른 Content length 크기 문제
기존에 start line만 분석해서 처리하면 됐던 구조에서 이제는 Request body를 읽어서 처리를 해야 하는데 이때 body의 내용을 저장하기 위해 header의 content-length 값을 이용할 수 있다.  
그래서 헤더에서 해당 항목을 가져와서 그 크기만큼 바이트 배열을 만들었고, 이제 이 배열에 Request body 내용을 읽어와서 저장하면 되는 상황이었다.  
그런데 생각한대로 해당 배열에 body 내용(여기서는 `{"userId":"java","name":"kim","password":"123","email":"gmail"}`)을 저장한 후 값을 확인해보니 뒤에 이상한 null값들이 들어오는 것을 발견했다.  
이유가 뭘지 생각하다가 결정적으로 이런 현상은 회원가입시에 한글로 입력이 들어왔을 때만 생긴다는 것을 깨달았고 인코딩 문제라는 것을 알게되었다.
브라우저에서 서버로 전송될 때 한글을 인코딩 해주기 때문에, content-length의 길이가 이 작업의 영향을 받아서 처음 빈 바이트 배열을 만들 때 크기가 더 크게 잡히는 것이 문제로 보였다.
이를 위해 JS에서 데이터를 전달해줄때 인코딩 된 상태로 전달해주면 그에 맞게 length가 올바르게 들어올 것이라고 생각해서 JS코드에서 인코딩 하도록 설정했다.
```
        var userData = {
            userId: encodeURI(userID),
            name: encodeURI(name),
            password: encodeURI(password),
            email: encodeURI(email)
        };
```
이렇게 수정하니까 올바르게 남는 부분 없이 배열이 채워지는 것을 확인할 수 있었다.

#### - 응답을 정상적으로 받았음에도, 브라우저에서 redirection이 되지 않는 문제
결론적으로는 http 통신 시 결과 값 확인하는 법에 미숙했고,
작성한 JS 코드에서 [Response](https://developer.mozilla.org/en-US/docs/Web/API/Response)라는 Fetch API의 인터페이스를 잘 다루지 못해서 생긴 문제였다.

유저 정보를 create하는 POST 요청을 하면 정상적으로 저장되고 성공 메시지도 나오는데, 리다이렉션 부분을 설정했음에도 이동하지 않고 화면에 변화가 없었다.  
디버그로 확인해 보면 헤더랑 바디 내용은 올바르게 보내고 있고, 서버 로그에도 요청이 잘 들어오는데도 반응이 없어서 해결을 위해 자바 서버 코드를 계속 확인하던 중..  

![pic](https://github.com/seondays/LeetCode/assets/110711591/7b96dbd6-9421-4ead-911e-f3ecf198ae1e)  

브라우저에서 이렇게 정상적으로 리다이렉션 이후 페이지까지 요청을 하는 것을 뒤늦게 발견했다.
계속 콘솔에만 값을 찍어가며 확인하다가 헤더 내용을 보고 나니까 서버에서 보내는 데이터 관련 문제는 확실히 아니겠다 싶었고, fetch 부분을 확인해서 수정을 하고 나니까 정상적으로 리다이렉션이 되기 시작했다.

### 학습
### Post Method [📑](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST)
- post 메서드를 이용해 서버로 데이터를 보낼 수 있습니다. 이 때 바디에 데이터를 담아서 보내게 되는데, 이 바디에 담긴 내용의 타입은 헤더에 있는 Content-type에 표시되어 있다.
- post는 서버에 주로 리소스를 생성하는 역할을 하게 되는데, 따라서 멱등성이 보장되지 않는다. (이것이 put과의 차이점)
- 주로 HTML form를 통해 요청을 보내는데, 이때 보낼 수 있는 콘텐츠 유형은 `multipart/form-data`, `text/plain`, `application/x-www-form-urlencoded` 등이 있다.
- fetch() 호출과 같이 HTML 양식 이외의 메서드를 통해 POST 요청을 전송할 수도 있다. 이 경우에는 어떤 타입이든 body에 담길 수 있다.
    
#### 새롭게 접한 것들
- 깃 브랜치 이름 변경하기
  main 브랜치에서 다음과 같은 명령어를 터미널에 입력! `git branch -m old-branch new-branch`
- js.. 기본적인 문법정도는 알면 좋을 것 같은데..