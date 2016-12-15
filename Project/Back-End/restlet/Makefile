
all: clean compile

clean:
	rm -rf build/*
	rm -f dist/app.jar
	find . -name "*.class" -exec rm -rf {} \;

app: compile
	cd build ; jar -cvfe ../dist/app.jar RockPaperScissorServer .

test: compile app
	java -cp dist/json.jar:dist/restlet.jar:dist/restlet-json.jar:dist/commons-codec-1.6.jar:dist/java_websocket.jar:dist/commons-io-2.4.jar:dist/gson-2.7.jar:dist/cloudant-http-2.7.0.jar:dist/cloudant-client-2.7.0.jar:dist/app.jar resources/RockPaperScissorsServer


compile:
	javac -cp \
	dist/json.jar:\
	dist/restlet.jar:\
	dist/restlet-json.jar:\
	dist/commons-codec-1.6.jar:\
	dist/java_websocket.jar:\
	dist/commons-io-2.4.jar:dist/gson-2.7.jar:dist/cloudant-http-2.7.0.jar:dist/cloudant-client-2.7.0.jar \
	-d build \
	src/resources/*.java \
	src/rps/*.java

docker-build: app
	docker build -t rpsrestlet .
	docker images
