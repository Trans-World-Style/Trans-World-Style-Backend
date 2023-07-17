FROM eclipse/ubuntu_jdk8:latest

USER root

WORKDIR /app

COPY . .

ENV SSLPATH=/app/src/main/resources/ssl

RUN apt-get update && \
    apt-get install -y libssl-dev openssl && \
    openssl pkcs12 -export -in $SSLPATH/fullchain.pem -inkey $SSLPATH/privkey.pem -out $SSLPATH/keystore.p12 -name ttp -CAfile $SSLPATH/chain.pem -caname root -passout pass:0000

RUN chmod +x ./gradlew

RUN ./gradlew build

EXPOSE 9090

CMD java -jar build/libs/trans-back-0.0.1-SNAPSHOT.jar

#CMD ["java", "-jar", "build/libs/*.jar"]ls

