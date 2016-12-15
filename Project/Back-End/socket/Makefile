
all: clean compile

clean:
	rm -rf build/*
	rm -f dist/app.jar
	find . -name "*.class" -exec rm -rf {} \;

app: compile
	cd build ; jar -cvfe ../dist/app.jar SocketTesting .

test: compile app
	java -cp dist/json.jar:dist/restlet-json.jar:dist/restlet.jar:dist/java_websocket.jar:dist/app.jar socket/SocketTesting


compile:
	javac -cp \
	dist/json.jar:\
	dist/restlet-json.jar:\
	dist/restlet.jar:\
	dist/java_websocket.jar \
	-d build \
	src/socket/*.java

docker-build: app
	docker build -t rpssocket .
	docker images
