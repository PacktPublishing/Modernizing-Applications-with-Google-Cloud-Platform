FROM openjdk:8-jdk
ARG JAR_FILE=build/libs/user-rest-*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /credentials
ENTRYPOINT ["java","-jar","/app.jar"]