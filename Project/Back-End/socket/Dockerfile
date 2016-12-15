FROM openjdk
EXPOSE 8990
ADD ./dist/json.jar /srv/json.jar
ADD ./dist/restlet-json.jar /srv/restlet-json.jar
ADD ./dist/restlet.jar /srv/restlet.jar
ADD ./dist/java_websocket.jar /srv/java_websocket.jar
ADD ./dist/app.jar /srv/app.jar
ADD ./src/Config.properties /srv/Config.properties
CMD java -cp srv/json.jar:srv/restlet-json.jar:srv/restlet.jar:srv/java_websocket.jar:srv/app.jar socket/SocketTesting
