# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17-jdk-slim AS build

# 필수 패키지 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
    apt-utils \
    curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 파일과 소스코드를 복사 (gradlew와 build.gradle 포함)
COPY ./ /app

# Gradle 빌드를 실행 (의존성 다운로드 및 애플리케이션 빌드)
RUN ./gradlew build

# 최종 실행 단계
FROM openjdk:17-jdk-slim

# 빌드 단계에서 생성된 JAR 파일을 최종 이미지로 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 실행 포트 설정
EXPOSE 8080

# 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]