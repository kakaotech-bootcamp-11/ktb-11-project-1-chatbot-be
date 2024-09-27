# 1단계: 빌드 환경 설정
FROM amazoncorretto:17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle wrapper 및 빌드 스크립트만 먼저 복사해서 캐시를 활용 (의존성 캐싱)
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle /app/

# Gradle 실행 권한 부여
RUN chmod +x gradlew

# 의존성 미리 다운로드
RUN ./gradlew dependencies --no-daemon || return 0

# 2단계: 애플리케이션 소스코드 복사 및 빌드
COPY ./ /app

# Gradle 빌드 실행 (테스트 생략으로 빌드 속도 향상 가능)
RUN ./gradlew build --no-daemon -x test

# 3단계: 최종 실행 단계
FROM amazoncorretto:17

# 빌드 단계에서 생성된 JAR 파일을 최종 이미지로 복사
COPY --from=build /app/build/libs/*.jar /app/

# 애플리케이션 실행 포트 설정
EXPOSE 8080

# 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app/chatbotBe-0.0.1-SNAPSHOT.jar"]
