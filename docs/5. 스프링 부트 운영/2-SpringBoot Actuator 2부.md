# 2. 스프링 부트 Actuator 2부: JMX와 HTTP
https://github.com/freespringlecture/springboot-concept-uses/tree/chap05-02-actuator_jmx_http

## JConsole 사용하기
https://docs.oracle.com/javase/tutorial/jmx/mbeans/

https://docs.oracle.com/javase/7/docs/technotes/guides/management/jconsole.html

1. **console**에서 **jconsole** 입력
2. **Local Process**에서 내가 구동한 **Application**을 선택하고 **connect**
3. **SSL**로 접속을 시도하다가 **SSL**을 적용 안했으므로 **Insecure connection**을 클릭
4. 접속하면 **Overview**에서 **Heap Memory Usage**, **Threads(Thread 개수)**, **Classes(로딩한 Class개수)**, **CPU Usage**를 보여줌
5. 그 외 각각의 정보를 상세히 볼 수 있는 Tab을 제공

## VisualVM 사용하기
https://visualvm.github.io/download.html

- 기존에는 **jvisualvm**이 **java**에서 제공을 했었는데 **java10** 부터 제공되지 않아서 별도로 설치해야됨  
- **Visual** 로 **jconsole** 보다 훨씬 보기 좋은 **UI**를 제공함  

### mbean 플러그인 추가하기
**Tools** - **Plugins** - **Available Plugins** - **VisualVM-Mbeans** 설치

## HTTP 사용하기
**health**와 **info**를 제외한 대부분의 **Endpoint**가 기본적으로 ​비공개​ 상태

`/actuator`

### 공개 옵션 조정  
> 이런 정보들이 밖으로 노출되면 매우 위험하므로 `SpringSecurity` 를 적용해서 `Endpoints` 를 `Admin` 만 접근이 가능하도록 해야함  

#### 모든 EndPoints 모두공개 설정
`management.endpoints.web.exposure.include=*`

#### 일부 EndPoints 비공개 설정  
`management.endpoints.web.exposure.exclude=env,beans` 