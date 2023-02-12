# Use the OpenJDK 11 image as the base image
FROM openjdk:11

# Set the working directory to /app
WORKDIR /app
COPY . /app
# Install the JPBC library
RUN apt-get update
RUN apt-get install -y jar
RUN ls
# unzip jpbc-2.0.0.zip
RUN jar xf jpbc-2.0.0.zip
RUN mv jpbc-2.0.0 /usr/local/
RUN rm jpbc-2.0.0.zip

# Copy the Java PBC code files to the container

