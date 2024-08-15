FROM openjdk:17
ADD target/stockQuote-0.0.1-SNAPSHOT.jar stockQuote-0.0.1-SNAPSHOT.jar
EXPOSE 9099
ENTRYPOINT ["java","-jar","stockQuote-0.0.1-SNAPSHOT.jar"]