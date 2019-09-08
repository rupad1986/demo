#!/bin/bash
exec nohup sh /opt/sharedFile/analytics-agent/bin/analytics-agent.sh $ANALYTICAL_AGENT_STATUS &
exec java -jar $JAVA_OPTS -Dserver.port=8182  demo-0.0.1-SNAPSHOT.jar