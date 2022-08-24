package bgu.spl.net.impl.BGSServer;
import java.util.function.Supplier;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Messages.Message;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        int port = 2090; ;
        // int port = Integer.parseInt(args[0]); ;
        Supplier<BidiMessagingProtocol<Message>> protocolFactory = () -> new BidiMessagingProtocolImpl();
        
        Supplier <MessageEncoderDecoder<Message>>encoderDecoderFactory = () -> new BidiMessageEncoderDecoder();
        // Create tpc server 
        Server<Message> tpcServer = Server.threadPerClient( port, protocolFactory, encoderDecoderFactory);
        tpcServer.serve();
        
        
    }
}
