package bgu.spl.net.api.bidi.Messages;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.ArrayUtils;

public class Follow extends Message {
    private boolean follow ;
    private String username;

    

    public Follow(boolean follow, String username) {
        this.opcode = Opcode.FOLLOW;
        this.follow = follow;
        this.username = username;
    }


    public Follow(byte[] bytes) {
        this.opcode = Opcode.FOLLOW;
        if(bytes[2]=='0')
            this.follow = true;
        else
            this.follow = false;
        int zeroIndex =-1;
        for (int i = 3; i < bytes.length; i++) {
            if(bytes[i] == '\0'){
                zeroIndex = i;
                break;
            } 
        }   
        this.username  =  new String(bytes, 3, zeroIndex-3, StandardCharsets.UTF_8);
    }


    public boolean isFollow() {
        return follow;
    }


    public void setFollow(boolean follow) {
        this.follow = follow;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte followByte;
        if(follow)
            followByte = 1;
        else
            followByte =0;
        byte [] usernameBytes = this.username.getBytes(StandardCharsets.UTF_8);
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, followByte);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, usernameBytes);
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }
}
