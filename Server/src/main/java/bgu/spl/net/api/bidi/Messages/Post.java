package bgu.spl.net.api.bidi.Messages;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.ArrayUtils;

public class Post extends Message {

    private String content ;

    public Post(String content) {
        this.opcode = Opcode.POST;
        this.content = content;
    }
    public Post(byte[]bytes){
        this.opcode = Opcode.POST;
        int zeroIndex =-1;
        for (int i = 3; i < bytes.length; i++) {
            if(bytes[i] == '\0'){
                zeroIndex = i;
                break;
            } 
        }
        this.content=  new String(bytes, 2, zeroIndex-2, StandardCharsets.UTF_8);
        System.out.println();
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }





    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte [] contentBytes = this.content.getBytes(StandardCharsets.UTF_8);
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, contentBytes);
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }
    
}
