package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.Messages.Block;
import bgu.spl.net.api.bidi.Messages.Follow;
import bgu.spl.net.api.bidi.Messages.Login;
import bgu.spl.net.api.bidi.Messages.Logout;
import bgu.spl.net.api.bidi.Messages.Logstat;
import bgu.spl.net.api.bidi.Messages.Message;
import bgu.spl.net.api.bidi.Messages.Pm;
import bgu.spl.net.api.bidi.Messages.Post;
import bgu.spl.net.api.bidi.Messages.Register;
import bgu.spl.net.api.bidi.Messages.Stat;
import bgu.spl.net.api.bidi.Messages.Message.Opcode;

public class BidiMessageEncoderDecoder implements MessageEncoderDecoder<Message> {
    
    private byte[] bytes ; //start with 1k
    private int len = 0;

    public BidiMessageEncoderDecoder(){
        
        bytes = new byte[1 << 10];
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            return popMessage();
        } 

        pushByte(nextByte);
        return null; //not a Message yet
    }

    @Override
    public byte[] encode(Message message) {
        return message.encode(message);
    }

    
    
    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }
    public short bytesToShort(byte[] byteArr){
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

   
    
    
     public String bytesToString(byte[] byteArr){
        String result = new String(bytes,StandardCharsets.UTF_8);
        return result;
    }

    public byte[] shortToBytes(short num){
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    } 
    private Message popMessage() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        Message result = null;
        // byte[] t = shortToBytes((short)1);
        short opcode = bytesToShort(new byte[]{bytes[0],bytes[1]});
                
        switch (opcode) {
            case 1:
                result =new Register(bytes);
            break;
            case 2:
                result =new Login(bytes);
            break;
            case 3:
                result =new Logout();
            break;
            case 4:
                result =new Follow(bytes);
            break;
            case 5:
                result =new Post(bytes);
            break;
            case 6:
                result =new Pm(bytes);
            break;
            case 7:
                result =new Logstat(bytes);
            break;
            case 8:
                result =new Stat(bytes);
            break;
            case 12:
                result =new Block(bytes);
            break;
                
            
        }
        
        bytes = new byte[1 << 10];
        len = 0;
        
        return result;
    }
   
    
}
