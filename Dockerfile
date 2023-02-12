FROM openjdk:11

WORKDIR /app
COPY . /app
RUN apt-get update
RUN apt-get install -y unzip
# RUN ls

RUN unzip jpbc-2.0.0.zip
RUN mv jpbc-2.0.0 /usr/local/
RUN rm jpbc-2.0.0.zip

RUN javac -cp /usr/local/jpbc-2.0.0/lib/jpbc-2.0.0.jar Main.java

CMD ["java", "-cp", "/usr/local/jpbc-2.0.0/lib/jpbc-2.0.0.jar:.", "Main"]
