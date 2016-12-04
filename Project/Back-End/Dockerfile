FROM openjdk
EXPOSE 8180
ADD ./dist/app.jar /srv/app.jar
ADD ./dist/restlet.jar /srv/restlet.jar
ADD ./dist/restlet-json.jar /srv/restlet-json.jar
ADD ./dist/json.jar /srv/json.jar
ADD ./dist/cloudant-client-2.5.0.jar /srv/cloudant-client-2.5.0.jar
CMD java -cp /srv/cloudant-client-2.5.0.jar:/srv/restlet.jar:/srv/restlet-json.jar:/srv/json.jar:/srv/app.jar resources/RockPaperScissorServer
