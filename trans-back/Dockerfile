FROM eclipse/ubuntu_jdk8:latest

WORKDIR /app

COPY . /app

RUN trans-back/gradlew build

EXPOSE 9090

CMD ["java", "-jar", "build/libs/*.jar"]