FROM adoptopenjdk/openjdk8

COPY .  .

WORKDIR /Java

RUN javac SimpleFileClient.java

CMD ["java", "SimpleFileClient"]