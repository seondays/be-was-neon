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

## Step3 다양한 컨텐츠 타입 지원
### step3 기능 구현 목록
- [x] 리소스의 컨텐츠 타입 구분을 위해 확장자를 확인하고 식별하는 기능
- [x] 확장자에 알맞게 헤더의 컨텐츠 타입을 변경하는 기능

### 설계 및 고민 정리
- 응답 헤더에 content type을 유동적으로 변경해야 한다. 그래서 응답해 줄 최종 URL을 파라미터로 받아서 해당 파일의 mime 타입을 반환하도록 하는 함수를 만들었다.  
이렇게 단순 리소스(/index.html)로 바로 진행하지 않고, 전체 URL(src/main/resources/static/registration/index.html)로 변경한 후에 mime을 찾도록 한 이유는
들어온 리소스가 디렉토리일 경우 뒤에 파일을 추가하는 부분도 필요하고, create처럼 특별한 파싱이 필요할 수도 있으므로 안정성을 위해서 최종 URL을 받아서 처리하게 했다.


- 회원 가입이 끝나면 바로 로그인 창으로 redirection 시키고 싶었다.  
이를 위해 클라이언트 쪽에 200 헤더와 login.html의 파일 내용이 담긴 응답을 보내보았다. 그런데 then으로 자바스크립트에서 이후 동작을 지시하고 있음에도 페이지는 alert만 뜨고 반응이 없었다..  
**[이유]** 작동하지 않았던 두가지 중요한 이유가 있었다. 첫번째로 자바스크립트 코드에서 `window.location.href = newUrl;` 이런 식으로 새로운 주소로 이동하도록 하는 부분이 필요했다.
응답으로 로그인 창에 대한 정보를 보낸다고 해도 클라이언트(브라우저) 쪽에서 그 응답을 받아서 처리하지 않으면 아무런 의미가 없기 때문이다.
두번째 이유는 body에 파일 내용이 담긴 응답을 보낸 것이 잘못이었다.  
나는 일반적으로 `GET /global.css` 이렇게 파일 요청이 들어왔을 때처럼, 화면에 표시할 파일의 내용을 담아서 보내주었는데, 그게 아니고 리다이렉션할 `주소`를 보내주어야 했던 것.
그래야 브라우저는 해당 주소로 이동하고, 우리 서버로 GET 요청이 와서 응답이 가능해진다. 이 점을 혼동하는 바람에 해결하느라 시간이 좀 많이 들었다.


- 내가 봐도 코드가 리펙토링이 필요한데.. 고칠수록 꼬여간다 ㅠㅠ 애당초 큰 그림을 잘 그려서 설계했다면 좋았을 텐데 아직 이런 관계들을 어떻게 구성할것인지가 어렵다.

### 학습

#### [MIME Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types)
- mime는 문서나 파일, 바이트 배열의 성격과 형식을 나타낸다. 즉 주고받는 컨텐츠가 어떤 종류의 파일인지 식별이 가능하다.
주로 HTML 헤더에 Content-Type 필드에서 사용된다.
- 주로 `타입/서브타입` 형식으로 표시된다. 예를 들면 텍스트 타입을 나타내는 mime 중 하나는 `text/html` 이렇게 표시한다.
타입은 좀 더 일반적인 범주를 의미하며(text, image, application), 서브타입은 그 중의 정확한 데이터 종류를 의미하게 된다.  
예를 들면 image 타입 내의 여러 서브타입들 png, gif 등등..


#### javadoc 스타일의 주석이란 무엇일까? [🔗](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)
- javadoc란 `/** ... */` 형식을 가지는 특수한 주석들의 API 문서를 생성해주는 JDK 도구입니다.
- 해당 형식으로 주석을 작성 후 javadoc 명령어를 실행하면 API 문저가 생성되는 것을 확인할 수 있습니다.
- @ 태그를 이용하여 설명을 추가할 수 있습니다

```
/**
* javadoc style annotation
* @param  url  an absolute URL giving the base location of the image
* @param  name the location of the image, relative to the url argument
* @return      the image at the specified URL
* @see         Image
*/
```

#### 새롭게 접한 것들
- String의 lastIndexOf(ch target) : target이 해당 문자열에서 마지막으로 나타난 위치 Index를 반환하는 메서드
- StringBuffer에 append 하는 경우, 변수값을 주고 싶을 때 -> String.format과 함께 사용하자