package bgu.spl.net.api.bidi.Messages;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.ArrayUtils;

public class Block extends Message {
    private String username;

    

    public Block(boolean follow, String username) {
        this.opcode = Opcode.FOLLOW;
        this.username = username;
    }


    public Block(byte[] bytes) {
        this.opcode = Opcode.FOLLOW;
        int zeroIndex =-1;
        for (int i = 2; i < bytes.length; i++) {
            if(bytes[i] == '\0'){
                zeroIndex = i;
                break;
            } 
        }   
        this.username  =  new String(bytes, 2, zeroIndex-2, StandardCharsets.UTF_8);
    }



    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public byte[] encode(Message message) {
        return null;
    }
}
