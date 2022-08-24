package bgu.spl.net.api.bidi.Messages;

import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.ArrayUtils;

public class Ack  extends Message{

    private Opcode messageOpcode ;
    private String optional;

    public Ack(Opcode messageOpcode, String optional) {
        this.opcode = Opcode.ACK;
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }

    public Opcode getMessageOpcode() {
        return messageOpcode;
    }

    public void setMessageOpcode(Opcode messageOpcode) {
        this.messageOpcode = messageOpcode;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte [] messageOpcodeBytes = shortToBytes(getOpcodeValue(messageOpcode));
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, messageOpcodeBytes);

        if(messageOpcode == Opcode.FOLLOW){
            byte [] usernameBytes = this.optional.getBytes(StandardCharsets.UTF_8);
            encodedMessgae = ArrayUtils.addAll(encodedMessgae, usernameBytes);  
        }

        else if(messageOpcode == Opcode.LOGSTAT){
            String[] fields = optional.split("\\s+"); // splits by whitespace
            int age = Integer.parseInt(fields[0]);
            int numOfPosts = Integer.parseInt(fields[1]);
            int numOfFollowers = Integer.parseInt(fields[2]);
            int numOfFollowings = Integer.parseInt(fields[3]);
            byte[] ageBytes = shortToBytes((short)age);
            byte[] numOfPostsBytes = shortToBytes((short)numOfPosts);
            byte[] numOfFollowersBytes = shortToBytes((short)numOfFollowers);
            byte[] numOfFollowingsBytes = shortToBytes((short)numOfFollowings);
            encodedMessgae = ArrayUtils.addAll(encodedMessgae, ageBytes);  
            encodedMessgae = ArrayUtils.addAll(encodedMessgae, numOfPostsBytes);  
            encodedMessgae = ArrayUtils.addAll(encodedMessgae, numOfFollowersBytes);  
            encodedMessgae = ArrayUtils.addAll(encodedMessgae, numOfFollowingsBytes);  
        }
                
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }
    
}
