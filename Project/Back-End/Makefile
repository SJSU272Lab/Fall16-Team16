
all: clean compile

clean:
	rm -rf build/*
	rm -f dist/app.jar
	find . -name "*.class" -exec rm -rf {} \;

app: compile
	cd build ; jar -cvfe ../dist/app.jar RockPaperScissorServer .

test: compile app
	java -cp java -cp dist/cloudant-client-2.5.0.jar:dist/restlet.jar:dist/restlet-json.jar:dist/json.jar:dist/app.jar resources/RockPaperScissorServer


compile:
	javac -cp dist/restlet.jar:dist/restlet-json.jar:dist/json.jar:dist/cloudant-client-2.5.0.jar -d build \
	src/resources/*.java \
	src/rps/*.java

run:
	echo Starting Service at:  http://localhost:8080
	java -cp build:dist/restlet.jar:dist/restlet-json.jar:dist/json.jar api.GumballServer

loadtest:
	echo Starting Load Test on localhost
	java -cp build:dist/restlet.jar:dist/restlet-json.jar:dist/json.jar RunLoadTest 5

docker-build: app
	docker build -t rps .
	docker images

docker-clean:
	docker stop rps
	docker rm rps
	docker rmi rps

docker-run:
	docker run --name rps -td rps
	docker ps

docker-run-host:
	docker run --name rps -td --net=host rps
	docker ps

docker-run-bridge:
	docker run --name rps -td -p 80:8080 rps
	docker ps

docker-network:
	docker network inspect host
	docker network inspect bridge

docker-stop:
	docker stop gumball
	docker rm gumball

docker-shell:
	docker exec -it gumball bash
