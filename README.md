# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## ⛹️‍♀️ Step6 동적인 HTML
### ⚒ step6 기능 구현 목록
- [x] 사용자가 로그인 상태인 경우, /index.html에서 사용자 이름 표시하는 기능
- [x] 사용자가 로그인 상태가 아닌 경우, /index.html에서 로그인 버튼을 표시하는 기능
- [x] 사용자가 로그인 상태일 경우, 사용자 목록을 출력하는 기능
- [x] 사용자가 로그인 상태가 아닌 경우, 사용자 목록 페이지에 접근 시 로그인 페이지로 이동하도록 처리하는 기능
- [x] 로그인 여부에 따라 접근할 수 없는 리소스를 요청하면, 다른 리소스로 리다이렉션 해주는 기능

### 🤔 설계 및 고민
#### 로그인이 되어 있는지를 체크하는 부분을 어디에 넣을 것인가?
- get / post 로 나뉘는 부분에서 바로 체크를 하게 되면?
  - 그러면 분기 이후의 행동을 담당할 동적/정적 핸들러가 필요하므로, 이를 만들어야 한다.
- 아니면 더 내부에서 필요한 부분에 넣게 되면? (예를 들면 body를 만드는 부분에서 체크하기)
  - 추가적으로 객체를 만들 필요는 없지만 코드가 길어지고, 너무 아래쪽에서 분기를 하는 느낌이 있다.  
또 추후 로그인 인증 체크가 필요한 부분이 있을 때마다 인증 체크하는 로직 코드를 작성해야 할 것 같다는 생각이 든다.

#### &rarr; static / dynamic 핸들러를 따로 두는 것이 적합하다고 생각해서 각 핸들러를 생성
- GetRouter를 만든 후 여기서 로그인 되어있는 클라이언트인지를 체크해 static / dynamic 핸들러로 분기하도록 내용을 추가했다.
- ResponseHandler에서 Get임을 인식하였으면 GetRouter로 이동 후, 여기서 다시 static / dynamic으로 나누는 흐름으로 되어있고, 가장 안쪽 핸들러에서부터 body와 header값을 위로 전달하면서 올라오게 된다.
- 그런데 한 가지 고민은 지금처럼 GetRouter를 거치는 게 좋은 것일까? 하는 것.

![클래스흐름](https://github.com/seondays/LeetCode/assets/110711591/6953e235-94bc-476f-8e47-f4cfbe940688)

- 굳이 GetRouter를 만들지 않고, 바로 responseHandler에서 해당 동작을 바로 처리하는 방법도 있는데, 하나 클래스를 늘려서 불필요하게 값 전달 과정만 한 단계 더 늘어난 것 아닌지 고민이 되었다.

#### static / dynamic 핸들러 안으로 들어가서도 다시 리다이렉션 여부를 판별해 주어야 하는 특수한 요청 리소스들의 처리는 어떻게?
- 큰 설계가 로그인 인증 여부에 따라서 로그인 상태면 동적 핸들러, 로그아웃 상태면 정적 핸들러로 가도록 되어 있기 때문에
상태에 따라 접근해서는 안되는 요청이 들어올 경우의 처리가 필요하다.
- 예를 들면 로그인 상태에서 다시 `/login`으로 요청을 보낼 경우에는 접근을 막고 `/main`으로 리다이렉션 해주어야 한다.
- static / dynamic 핸들러 바깥에서 한번 걸러서 처리해주고 싶었으나 코드가 꼬여가면서 잘 정리가 되지 않았다.
- 결국 각 핸들러 내부에서 따로 특수한 리소스들을 관리하도록 했다. (정적도 마찬가지) 더 좋은 방법이 있을 것 같지만 일단은 이렇게 정리하였다. 나중에 개선할 수 있다면 개선 예정 

#### 쿠키에 sid가 아닌 내가 원하지 않은 값들이 들어있다..!
- 기존에는 쿠키에 sid의 값만 들어있을 것이라고 생각하고 간단하게 코드를 작성해두었는데, 크롬 시크릿창에서는 이 코드가 잘 작동했다.
- 그런데 시크릿창이 아닌 일반 크롬에서 테스트를 하기 시작하니, 쿠키에 이상한 값들이 추가로 들어오기 시작했다..
- 이 값들을 처리하고 원하는 값만을 가져오기 위해 좀 더 복잡한 처리 메서드들이 필요해지게 되었는데, 그래서 이를 담당할 cookie만을 다루는 객체의 필요성을 느끼게 되었다.

&rarr; 우선 Cookie를 객체로 분리 완료. 그런데 기존 request는 이 Cookie 객체에 관해 전혀 모르고 있는 상태이다. 이런 상태가 괜찮은가? 🤔

- 객체를 분리하고 필요한 곳에 적용한 뒤에 생각해 보니, Cookie는 Request의 Header에서 가져온 내용인데 그 구조가 전혀 반영되어 있지 않은 상황이 되었다.
- 그래서 이 문제의 해결을 위해 기존에는 단순히 Map이었던 Header를 따로 HttpRequestHeader 클래스로 만들어주고, 그 안에 따로 cookie 멤버변수를 선언해서 사용할 수 있도록 수정했다.

#### 동적으로 어떻게 파일을 만들어야 할까?
- 처음에는 html 내에서 동적인 내용을 넣길 원하는 line의 값을 저장해두고, 해당 line을 체크하는 조건을 걸어서 그다음에 원하는 내용을 집어넣도록 구현했다.  
&rarr; `<a href="/main">`이 포함된 줄을 만나면 그 다음줄에 `안녕하세요 User님`을 집어넣도록
- 그런데 피어리뷰를 진행하면서 html 파일 내에 특정 텍스트를 플래그로 미리 집어넣어 두고, 해당 플래그를 만나면 그때 그걸 원하는 값으로 치환하도록 하는 방법을 배우게 되었다.  
&rarr; `<!--      dynamicTextStart-->`와 같이 특정 주석 텍스트를 작성한 뒤에, 해당 줄을 체크해서 동적으로 작성
- 기존 구현 방법은 파일마다 html 구조가 달라지게 되면 유연하게 대처하기가 힘들고, 원하는 대로 결과가 나오지 않을 가능성도 있었는데,
아래 방법을 통해서라면 좀 더 안정적으로 프로그래밍이 가능하게 된다!

#### Response Header를 primitive type이 아닌 reference type을 가지도록 하자. 그리고 응집도를 높이자!
- 기존에는 각 핸들러에서 header를 String으로 따로따로 생성하고 있었는데, 그렇게 되면 변경에 취약해지고 코드를 읽기도 복잡해진다는 사실을 체감하고 있었다.
- 하지만 지금처럼 primitive type 타입으로 종속적으로 코드를 작성해서 사용하게 되면 이 결과를 나중에 변경하는 것이 어렵게 될 수 있다는 것은 리뷰를 받으면서 처음 깨닫게 되었다.
- 그래서 최종적으로 response header를 따로 타입으로 만들어서 관리하도록 했다.

