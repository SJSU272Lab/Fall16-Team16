
all: clean compile

clean:
	rm -rf build/*
	rm -f dist/app.jar
	find . -name "*.class" -exec rm -rf {} \;

app: compile
	cd build ; jar -cvfe ../dist/app.jar Test .

test: compile app
	java -cp dist/app.jar testing/Test


compile:
	javac \
	-d build \
	src/testing/*.java \
	src/rps/*.java
