# :pushpin: 세탁해조

> 세탁 서비스 플랫폼

</br>

## 1. 제작 기간 & 참여 인원

-   2023년 08월 21일 ~ 09월 15일
-   팀 프로젝트(7인)

</br>

## 2. 사용 기술

#### `Back-end`

-   Java 11
-   Spring Boot 2.7.14
-   Gradle
-   Tymeleaf 3.1.2
-   MyBatis 2.3.1
-   Oracle 11g

#### `Front-end`

-   HTML5
-   CSS3
-   Javascript

</br>

## 3. ERD 설계

![](https://private-user-images.githubusercontent.com/140701897/270906158-458434d6-77a5-433f-9278-095c5f6ccc52.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTEiLCJleHAiOjE2OTU4MDE4ODcsIm5iZiI6MTY5NTgwMTU4NywicGF0aCI6Ii8xNDA3MDE4OTcvMjcwOTA2MTU4LTQ1ODQzNGQ2LTc3YTUtNDMzZi05Mjc4LTA5NWM1ZjZjY2M1Mi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBSVdOSllBWDRDU1ZFSDUzQSUyRjIwMjMwOTI3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDIzMDkyN1QwNzU5NDdaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xOWMxZDA4NGI1OGZiZmUyZGRiN2RlYzQzMDlmYzJjYmI4Y2UwN2FjNDc4MzdiNmE2MTIyZDNhN2RlYWQ3MzA3JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.c8m8Or9RPKdmQ1Ti0ApOD72CUEou9nSb3NdHmIeC5DI)

## 4. 핵심 기능

이 서비스의 핵심 기능은 컨텐츠 등록 기능입니다.  
사용자는 단지 컨텐츠의 카테고리를 선택하고, URL만 입력하면 끝입니다.  
이 단순한 기능의 흐름을 보면, 서비스가 어떻게 동작하는지 알 수 있습니다.

<details>
<summary><b>핵심 기능 설명 펼치기</b></summary>
<div markdown="1">

### 4.1. 전체 흐름

![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow1.png)

### 4.2. 사용자 요청

![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_vue.png)

-   **URL 정규식 체크** :pushpin: [코드 확인](https://github.com/Integerous/goQuality/blob/b587bbff4dce02e3bec4f4787151a9b6fa326319/frontend/src/components/PostInput.vue#L67)

    -   Vue.js로 렌더링된 화면단에서, 사용자가 등록을 시도한 URL의 모양새를 정규식으로 확인합니다.
    -   URL의 모양새가 아닌 경우, 에러 메세지를 띄웁니다.

-   **Axios 비동기 요청** :pushpin: [코드 확인]()
    -   URL의 모양새인 경우, 컨텐츠를 등록하는 POST 요청을 비동기로 날립니다.

### 4.3. Controller

![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_controller.png)

-   **요청 처리** :pushpin: [코드 확인](https://github.com/Integerous/goQuality/blob/b2c5e60761b6308f14eebe98ccdb1949de6c4b99/src/main/java/goQuality/integerous/controller/PostRestController.java#L55)

    -   Controller에서는 요청을 화면단에서 넘어온 요청을 받고, Service 계층에 로직 처리를 위임합니다.

-   **결과 응답** :pushpin: [코드 확인]()
    -   Service 계층에서 넘어온 로직 처리 결과(메세지)를 화면단에 응답해줍니다.

### 4.4. Service

![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_service1.png)

-   **Http 프로토콜 추가 및 trim()** :pushpin: [코드 확인]()

    -   사용자가 URL 입력 시 Http 프로토콜을 생략하거나 공백을 넣은 경우,  
        올바른 URL이 될 수 있도록 Http 프로토콜을 추가해주고, 공백을 제거해줍니다.

-   **URL 접속 확인** :pushpin: [코드 확인]()

    -   화면단에서 모양새만 확인한 URL이 실제 리소스로 연결되는지 HttpUrlConnection으로 테스트합니다.
    -   이 때, 빠른 응답을 위해 Request Method를 GET이 아닌 HEAD를 사용했습니다.
    -   (HEAD 메소드는 GET 메소드의 응답 결과의 Body는 가져오지 않고, Header만 확인하기 때문에 GET 메소드에 비해 응답속도가 빠릅니다.)

    ![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_service2.png)

-   **Jsoup 이미지, 제목 파싱** :pushpin: [코드 확인]()
    -   URL 접속 확인결과 유효하면 Jsoup을 사용해서 입력된 URL의 이미지와 제목을 파싱합니다.
    -   이미지는 Open Graphic Tag를 우선적으로 파싱하고, 없을 경우 첫 번째 이미지와 제목을 파싱합니다.
    -   컨텐츠에 이미지가 없을 경우, 미리 설정해둔 기본 이미지를 사용하고, 제목이 없을 경우 생략합니다.

### 4.5. Repository

![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_repo.png)

-   **컨텐츠 저장** :pushpin: [코드 확인]()
    -   URL 유효성 체크와 이미지, 제목 파싱이 끝난 컨텐츠는 DB에 저장합니다.
    -   저장된 컨텐츠는 다시 Repository - Service - Controller를 거쳐 화면단에 송출됩니다.

</div>
</details>

</br>

## 5. 핵심 트러블 슈팅

### 5.1. 자동 로그인을 위한 인터셉터 구현

-   이 서비스는 모바일 앱 기반으로 만들어졌습니다. 모바일 앱은 한 번 로그인하면 서비스를 종료해도 일정 기간동안 로그인 상태가 유지됩니다. 그래서 저도 이 자동로그인을 구현해보고 싶었습니다.

-   이 과정에서 쿠키와 세션에 대한 이해가 필요했고 인터셉터에 걸리는 경로를 신중하게 설정해야 하는 어려움이 있었습니다.

-   자동로그인 구현: User가 로그인 한 경우 세션에 memberId를 저장함과 동시에 쿠키(loginCookie)도 생성하여 세션의 ID를 저장합니다. 그리고 이 세션의 ID값과 쿠키 유효시간을 DB에 저장합니다. 그 후 사용자가 서비스를 종료 후 다시 돌아오면 세션은 만료되고 쿠키는 아직 남아있으면 쿠키에 등록된 새션의 ID를 이용하여 memberId를 가져와 세션에 저장하여 로그인 상태를 유지하도록 합니다.(**기존코드** 참고)

<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

`loginController.java`

```java
session.setAttribute(SessionConstant.LOGIN_MEMBER, userDto.getMemberId());
Cookie cookie = new Cookie("loginCookie", session.getId());
cookie.setPath("/");
int amount = 60 * 60 * 24 * 7;
// 단위는 (초)임으로 7일정도로 유효시간을 설정해 준다.
cookie.setMaxAge(amount);
// 쿠키를 적용해 준다.
response.addCookie(cookie);
Date limit = new Date(System.currentTimeMillis() + (1000*amount));
// 현재 세션 id와 유효시간을 사용자 테이블에 저장한다.
service.keepLogin(session.getId(), limit, userDto.getMemberId());

```

`WebConfig.java`

```java
registry.addInterceptor(new LoginCheckInterceptor(loginService))
.order(1)
.addPathPatterns("/laundry/**")
.excludePathPatterns("/css/**", "/images/**", "/js/**", "/font/**", "/","/members//**",
"/login/**", "/price/**", "/subscription/**","/register", "/check/**");
```

`LoginCheckInterceptor.java`

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String requestURI = request.getRequestURI();
    log.info("인증 체크 인터셉터 실행 {}", requestURI);
    HttpSession session = request.getSession(true);

    // 세션이 없거나 세션에 저장된 memberId가 없는 경우
    if (session == null || session.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
    // 웹에 있는 쿠키를 가져온다.
    Cookie loginCookie = WebUtils.getCookie(request,"loginCookie");

    // loginCookie가 있는 경우 쿠키에 저장된 세션ID값을 이용해 memberId를 가져와 세션에 저장
    if(loginCookie != null){
        String sessionId = loginCookie.getValue();
        MemberDto memberDto = loginService.checkUserWithSessionId(sessionId);
        if(memberDto != null){
            session.setAttribute(SessionConstant.LOGIN_MEMBER, memberDto.getMemberId());
            return true;
        }
    }else{
        log.info("인증되지않은 사용자 요청");
        response.sendRedirect("/login?redirectURL=" + requestURI);
        return false;
        }
    }
    log.info("인증된 사용자");
    return true;
}
```

</div>
</details>

-   **문제1**: 사용자가 임의로 세션을 지우고 '마이페이지'나 '주문내역'에 들어갈 경우 쿠키가 있음에도 세션이 생성되지 않았습니다.
-   그 이유는 '주문내역'에 접속하려면 세션에 있는 memberId가 필요한데 세션을 지웠기 때문에 오류가 발생하고 '마이페이지'의 접속은 매핑된 주소에 memberId가 포함되어 있어 memberId가 존재하지 않아 컨트롤러에 접근이 되지 않아 404 오류가 발생했습니다.
-   **문제2**: 쿠키, 세션 모두 존재하지 않은 상태에서 '마이페이지'에 접속하는 경우 '로그인 페이지'로 이동하고 마이페이지의 redirect 경로도 전달됩니다. 이 때 로그인 하는 경우 redirect 경로가 올바르지 않아 오류가 발생하게 됩니다.
-   그래서 아래 **개선된 코드1**와 같이 필터를 활용해서 컨트롤러에 진입하기 전에 memberId가 저장된 세션을 생성하도록 유도했습니다.
<details>
<summary><b>개선된 코드1</b></summary>
<div markdown="1">

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    Cookie loginCookie = WebUtils.getCookie(req,"loginCookie");
    if(loginCookie != null){
        HttpSession session = req.getSession();
        String sessionId = loginCookie.getValue();
        MemberDto memberDto = loginService.checkUserWithSessionId(sessionId);
        if(memberDto != null){
            session.setAttribute(SessionConstant.LOGIN_MEMBER, memberDto.getMemberId());
        }else{
            return;
        }
    }
    chain.doFilter(request, response);
}
```

</div>
</details>

-   **개선된 코드1**을 추가함으로써 **문제1**을 해결했지만 **문제2**는 여전히 해결되지 않았습니다.
-   '마이페이지'의 접속은 footer에서 마이페이지를 클릭하여 이루어지고, **개선된 코드2**를 보시면 thymeleaf를 이용해서 세션이 없으면 로그인 페이지로 이동하도록 했습니다.
-   또한 인터셉터가 발동되어야 하는 경로와 발동되면 안되는 경로를 더욱 구체화 시켰습니다.

<details>
<summary><b>개선된 코드2</b></summary> 
<div markdown="1">

`project_footer.html` :pushpin: [코드확인](https://github.com/geonwoo05/laundry_project/blob/develop/src/main/resources/templates/common/project_footer.html#L29)

```html
<th:block th:if="${session.memberId != null}">
    <a th:href="@{/members/{memberId}/mypage(memberId=${session.memberId})}">
        <button type="button" class="mypage">
            <!-- 선택된 버튼 -->
            <svg xmlns="http://www.w3.org/2000/svg" th:if="${footer == 'mypage'}" viewBox="0 0 448 512">
                <path
                    id="mypageButton1"
                    d="M224 256A128 128 0 1 0 224 0a128 128 0 1 0 0 256zm-45.7 48C79.8 304 0 383.8 0 482.3C0 498.7 13.3 512 29.7 512H418.3c16.4 0 29.7-13.3 29.7-29.7C448 383.8 368.2 304 269.7 304H178.3z"
                />
            </svg>
            <!-- 선택되지 않은 버튼 -->
            <svg xmlns="http://www.w3.org/2000/svg" th:if="${footer != 'mypage'}" viewBox="0 0 448 512">
                <path
                    id="mypageButton2"
                    d="M336 128a112 112 0 1 0 -224 0 112 112 0 1 0 224 0zM96 128a128 128 0 1 1 256 0A128 128 0 1 1 96 128zM16 482.3c0 7.6 6.1 13.7 13.7 13.7H418.3c7.6 0 13.7-6.1 13.7-13.7C432 392.7 359.3 320 269.7 320H178.3C88.7 320 16 392.7 16 482.3zm-16 0C0 383.8 79.8 304 178.3 304h91.4C368.2 304 448 383.8 448 482.3c0 16.4-13.3 29.7-29.7 29.7H29.7C13.3 512 0 498.7 0 482.3z"
                />
            </svg>
            <span>마이페이지</span>
        </button>
    </a>
</th:block>
<th:block th:if="${session.memberId == null}">
    <a th:href="@{/login}">
        <button type="button" class="mypage">
            <!-- 선택된 버튼 -->
            <svg xmlns="http://www.w3.org/2000/svg" th:if="${footer == 'mypage'}" viewBox="0 0 448 512">
                <path
                    id="mypageButton1"
                    d="M224 256A128 128 0 1 0 224 0a128 128 0 1 0 0 256zm-45.7 48C79.8 304 0 383.8 0 482.3C0 498.7 13.3 512 29.7 512H418.3c16.4 0 29.7-13.3 29.7-29.7C448 383.8 368.2 304 269.7 304H178.3z"
                />
            </svg>
            <!-- 선택되지 않은 버튼 -->
            <svg xmlns="http://www.w3.org/2000/svg" th:if="${footer != 'mypage'}" viewBox="0 0 448 512">
                <path
                    id="mypageButton2"
                    d="M336 128a112 112 0 1 0 -224 0 112 112 0 1 0 224 0zM96 128a128 128 0 1 1 256 0A128 128 0 1 1 96 128zM16 482.3c0 7.6 6.1 13.7 13.7 13.7H418.3c7.6 0 13.7-6.1 13.7-13.7C432 392.7 359.3 320 269.7 320H178.3C88.7 320 16 392.7 16 482.3zm-16 0C0 383.8 79.8 304 178.3 304h91.4C368.2 304 448 383.8 448 482.3c0 16.4-13.3 29.7-29.7 29.7H29.7C13.3 512 0 498.7 0 482.3z"
                />
            </svg>
            <span>마이페이지</span>
        </button>
    </a>
</th:block>
```

<br/>

`WebConfig.java`

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    // 로그인 체크 인터셉터
    registry.addInterceptor(new LoginCheckInterceptor(loginService))
                .order(1)
                .addPathPatterns("/laundry/**", "/orders/**", "/members/**")
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/", "/font/**", "/members//**","/orders/*/payment/webhook");

}
```

</div>
</details>

</br>

## 6. 그 외 트러블 슈팅

<details>
<summary>RequestBody를 생성할 때 JSON파라미터를 쿼리 문자열로 변경해야하는 문제</summary>
<div markdown="1">

-   okHttp에서 제공하는 HttpUrl.Builder를 이용하여 URL의 쿼리 문자열을 반환하는 'encodeParameters'메서드를 만들어서 해결

```java
public String encodeParameters(JSONObject params) {
    HttpUrl.Builder urlBuilder = HttpUrl.parse(KAKAO_REDIRECT_URL).newBuilder();
    for (String key : params.keySet()) {
        urlBuilder.addQueryParameter(key, params.getString(key));
    }
    return urlBuilder.build().encodedQuery();
}

```

</div>
</details>

<details>
<summary>카카오 로그인 시 사용자의 정보를 DTO에 담아야하는 문제</summary>
<div markdown="1">

-   ObjectMapper 객체를 이용하여 JSON데이터를 java객체에 저장하면 된다.
-   DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES 설정은 JSON데이터에 java 클래스에 없는 속성이 있어도 예외를 던지지 않게 하는 설정이다.

```java
ObjectMapper obMapper = new ObjectMapper();
obMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
KakaoProfile kakaoProfile = null;
try {
    kakaoProfile = obMapper.readValue(body.string(), KakaoProfile.class);
}catch(JsonMappingException e){
    e.printStackTrace();

}catch (JsonProcessingException e){
    e.printStackTrace();
}
```

</div>
</details>

</br>

## 7. 회고 / 느낀점

>
