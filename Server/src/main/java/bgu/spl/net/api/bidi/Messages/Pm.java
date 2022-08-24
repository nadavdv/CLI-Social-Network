package bgu.spl.net.api.bidi.Messages;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.DateFormatSymbols;
import java.util.Arrays;

import javax.swing.text.ZoneView;

public class Pm extends Message {
    private String username;
    private String content;
    private String time;//TODO change from String t

    
    public Pm(String username, String content, String time) {
        this.opcode = Opcode.PM;
        this.username = username;
        this.content = content;
        this.time = time;
    }

    public Pm(byte[] bytes){
        this.opcode = Opcode.PM;
        int [] zeroIndexes = new int [3];
        int index =0;
        for (int i =2; i < bytes.length &&index <3; i++) {
            if(bytes[i] == '\0' ){
                zeroIndexes[index] = i;
                index+=1;
            }
        }
        username  =  new String(bytes, 2, zeroIndexes[0]-2, StandardCharsets.UTF_8);
        content  =  new String(bytes, zeroIndexes[0]+1, zeroIndexes[1]-(zeroIndexes[0]+1), StandardCharsets.UTF_8);
        time  =  new String(bytes, zeroIndexes[1]+1, zeroIndexes[2]-(zeroIndexes[1]+1), StandardCharsets.UTF_8);
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setcontent(String content) {
        this.content = content;
    }

    public void settime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }





    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte [] usernameBytes = this.username.getBytes(StandardCharsets.UTF_8);
        byte seperator ='\0';
        byte [] contentBytes = this.content.getBytes(StandardCharsets.UTF_8);
        byte [] timeBytes = this.time.getBytes(StandardCharsets.UTF_8);
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, usernameBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, contentBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, timeBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }

    


    
}

