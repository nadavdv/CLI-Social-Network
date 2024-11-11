# A command line social network.

Server-side implemented in java. Server -side has 2 implementations:

Reactor - Non-Blocking server with fixed amount of worker threads. Based on Reactor Design Pattern.
TPC - Thread - Per - Client. Blocking.
Client-side implemented in C++ with boost library.

HOW TO RUN: Server: mvn clean install

Reactor Server: mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args=" "

TPC Server: mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args=""

Client: make ./BGSclient 127.0.0.1 2090

Example Commands:

REGISTER NADAV 1234 24-02-1996 (birthday seperated by "-")

LOGIN NADAV 1234

LOGOUT

POST @OFEK HOW ARE YOU?

PM OFEK HOW ARE YOU?

BLOCK OFEK

FOLLOW 0 OFEK

LOGSTAT

STAT OFEK|SHALOM|RONI

(*)

FILTERED WORDS STORED IN - Server/..../api/bidi/Messages/Pm
