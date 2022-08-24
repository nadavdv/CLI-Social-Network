CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude 
LDFLAGS:=-lboost_system -pthread

all: EchoClient
	g++ -o bin/echoExample bin/serverListener.o bin/keyboardListener.o bin/encoderDecoder.o bin/connectionHandler.o bin/echoClient.o $(LDFLAGS) 

EchoClient: bin/serverListener.o bin/keyboardListener.o bin/connectionHandler.o bin/echoClient.o bin/encoderDecoder.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/encoderDecoder.o: src/encoderDecoder.cpp
	g++ $(CFLAGS) -o bin/encoderDecoder.o src/encoderDecoder.cpp
	
bin/keyboardListener.o: src/keyboardListener.cpp
	g++ $(CFLAGS) -o bin/keyboardListener.o src/keyboardListener.cpp

bin/serverListener.o: src/serverListener.cpp
	g++ $(CFLAGS) -o bin/serverListener.o src/serverListener.cpp		
.PHONY: clean
clean:
	rm -f bin/*
	@echo 'finished clean'