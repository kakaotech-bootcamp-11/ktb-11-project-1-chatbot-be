FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
	apt-utils \
	&& apt-get install -y --no-install-recommends \
	tcpdump \
	iproute2 \
	iputils-ping \
	curl \
	&& apt-get clean \
	&& rm -rf /var/lib/apt/lists/*


ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
