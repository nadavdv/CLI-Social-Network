package bgu.spl.net.impl.BGSServer;

import java.io.*;
import java.net.*;
import java.util.function.Supplier;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Messages.Message;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        int port = 2090; ;
        int nthreads = 5;
        // int port = Integer.parseInt(args[0]); ;
        // int nthreads = Integer.parseInt(args[1]);
        Supplier<BidiMessagingProtocol<Message>> protocolFactory = () -> new BidiMessagingProtocolImpl();
        
        Supplier <MessageEncoderDecoder<Message>>encoderDecoderFactory = () -> new BidiMessageEncoderDecoder();
        // Create reactor server 
        Server<Message> reactorServer = Server.reactor(nthreads, port, protocolFactory, encoderDecoderFactory);
        reactorServer.serve();
        
    }
}
