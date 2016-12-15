FROM openjdk
EXPOSE 8080
ADD ./dist/json.jar /srv/json.jar
ADD ./dist/restlet.jar /srv/restlet.jar
ADD ./dist/restlet-json.jar /srv/restlet-json.jar
ADD ./dist/commons-codec-1.6.jar /srv/commons-codec-1.6.jar
ADD ./dist/java_websocket.jar /srv/java_websocket.jar
ADD ./dist/commons-io-2.4.jar /srv/commons-io-2.4.jar
ADD ./dist/gson-2.7.jar /srv/gson-2.7.jar
ADD ./dist/cloudant-http-2.7.0.jar /srv/cloudant-http-2.7.0.jar
ADD ./dist/cloudant-client-2.7.0.jar /srv/cloudant-client-2.7.0.jar
ADD ./dist/app.jar /srv/app.jar
ADD ./src/Config.properties /srv/Config.properties
CMD java -cp srv/json.jar:srv/restlet.jar:srv/restlet-json.jar:srv/commons-codec-1.6.jar:srv/java_websocket.jar:srv/commons-io-2.4.jar:srv/gson-2.7.jar:srv/cloudant-http-2.7.0.jar:srv/cloudant-client-2.7.0.jar:srv/app.jar resources/RockPaperScissorsServer
