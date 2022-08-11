# Redis 와 MySQL 을 이용해서 만든 세션

* N 대의 서버가 동작하는 상황에서 서버의 세션은 공유가 불가능하다.
* 데이터베이스를 세션처럼 사용하여 서버를 운영해보자.

## profile 에 따라 Database 선택 가능

1Redis
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=redis 
```

2. RDB (MySQL)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=rdb 
```

## AOP 를 이용하여 HttpSession 과 비슷하게 구현

### 메서드에서 `Session` 클래스 파라미터로 요청 시 AOP 를 이용하여 주입

```java
public interface SessionAspect {

    Object session(ProceedingJoinPoint pjp) throws Throwable;

}
```

## ThreadLocal 을 사용하여 쿠키에 존재하는 Session ID 공유

### ThreadLocal
```java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionContext {

    private static final ThreadLocal<Optional<String>> threadLocal = new ThreadLocal<>();

    public static void set(String sessionId) {
        threadLocal.set(Optional.ofNullable(sessionId));
    }

    public static Optional<String> get() {
        return Optional.ofNullable(threadLocal.get())
                       .orElse(Optional.empty());
    }

    public static void remove() {
        threadLocal.remove();
    }

}
```

### Interceptor 를 통해 ThreadLocal 초기화 및 삭제

```java
public class CookieInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        SessionContext.set(getSessionId(request.getCookies()));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {

        SessionContext.remove();
    }
    
}
```