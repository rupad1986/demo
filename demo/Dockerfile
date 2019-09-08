FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest

USER root

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8597 available to the world outside this container
EXPOSE 8182

#COPY . .
ADD *.jar demo.jar

RUN mkdir /deployments/logs
RUN chmod -R 777 /opt/

# Run the jar file 
COPY run.sh .
RUN chmod  777 run.sh
RUN chmod  777 demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["sh","run.sh"]
