FROM eclipse/ubuntu_jdk8:latest

USER root

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew build

EXPOSE 9090

CMD java -jar build/libs/trans-back-0.0.1-SNAPSHOT.jar

#CMD ["java", "-jar", "build/libs/*.jar"]ls

FROM debian:latest

RUN apt-get update && \
    apt-get install -y libssl-dev openssl

COPY ./test.sh .

CMD ["sh", "test.sh"]
