FROM openjdk:8-jdk
ARG JAR_FILE=build/libs/account-rest-*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /credentials

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]