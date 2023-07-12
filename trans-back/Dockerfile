FROM eclipse/ubuntu_jdk8:latest

USER root

WORKDIR /app

COPY . .

RUN ./gradlew build

EXPOSE 9090

CMD ["java", "-jar", "build/libs/*.jar"]