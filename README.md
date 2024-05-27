# MVC(Model, View, Controller) Design Pattern

> #### MVC (Model-View-Controller) 패턴을 사용하여 애플리케이션을 개발할 때 일반적인 생성 순서
> 1. Model 클래스 작성: 데이터베이스의 테이블과 매핑될 객체를 정의하는 모델 클래스를 작성합니다. 이 클래스는 주로 엔터프라이즈 애플리케이션의 비즈니스 로직을 담고 있습니다.
> 2. Repository 인터페이스 작성: Spring Data JPA를 사용하거나 직접 구현한 데이터 액세스 계층의 인터페이스를 작성합니다. 이 인터페이스는 데이터베이스와 상호 작용하기 위한 CRUD(Create, Read, Update, Delete) 메서드를 정의합니다.
> 3. Service 클래스 작성: 비즈니스 로직을 담당하는 서비스 클래스를 작성합니다. 이 클래스는 Repository를 주로 사용하여 데이터 액세스를 수행하고, Controller와 Model 사이에서 데이터를 처리하고 전달합니다.
> 4. Controller 클래스 작성: HTTP 요청을 처리하는 컨트롤러 클래스를 작성합니다. 이 클래스는 클라이언트로부터의 HTTP 요청을 받아서 해당 요청을 처리하고, 필요한 서비스를 호출하여 비즈니스 로직을 실행하고, 결과를 HTTP 응답으로 반환합니다.
