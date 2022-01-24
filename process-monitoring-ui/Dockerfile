FROM openjdk:11

ADD ./target/ui-1.0-SNAPSHOT.jar /app/
RUN useradd -m myuser
USER myuser
CMD java -jar -Dspring.profiles.active=prod /app/ui-1.0-SNAPSHOT.jar