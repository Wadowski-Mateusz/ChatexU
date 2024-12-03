FROM openjdk:21-jdk

WORKDIR /server

# copy jar from build to container

COPY ./server/build/libs/server-0.3.0-SNAPSHOT.jar /server/chatexu_server.jar


EXPOSE 8091

CMD ["java", "-jar", "chatexu_server.jar"]