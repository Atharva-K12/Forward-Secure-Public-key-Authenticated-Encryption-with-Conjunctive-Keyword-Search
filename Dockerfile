FROM ubuntu:latest

# install java and openjdk
WORKDIR /app
COPY . /app
RUN apt-get update
RUN apt-get install -y unzip
RUN apt-get install -y openjdk-11-jdk
# RUN ls

RUN unzip -u jpbc-2.0.0.zip
RUN ls
# RUN mv jpbc-2.0.0 /usr/local/
# RUN rm jpbc-2.0.0.zip

RUN javac -cp jpbc-2.0.0/lib/jpbc-2.0.0.jar Main.java


RUN java -cp jpbc-2.0.0/lib/jpbc-2.0.0.jar:. Main
# CMD ["java", "-cp", "jpbc-2.0.0/lib/jpbc-2.0.0.jar:.", "Main"]
