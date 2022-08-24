package bgu.spl.net.api.bidi.Messages;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.DateFormatSymbols;
import java.util.Arrays;

import javax.swing.text.ZoneView;

public class Logstat extends Message {
    private Opcode ackOpcode = Opcode.ACK;
    private short age;
    private short numPosts;
    private short numFollowers;
    private short numFollowing;

    
    public Logstat(short age, short numPosts, short numFollowers, short numFollowing) {
        this.opcode= Opcode.LOGSTAT;
        this.age = age;
        this.numPosts = numPosts;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
    }


    public Opcode getAckOpcode() {
        return ackOpcode;
    }


    public void setAckOpcode(Opcode ackOpcode) {
        this.ackOpcode = ackOpcode;
    }


    public short getAge() {
        return age;
    }


    public void setAge(short age) {
        this.age = age;
    }


    public short getNumPosts() {
        return numPosts;
    }


    public void setNumPosts(short numPosts) {
        this.numPosts = numPosts;
    }


    public short getNumFollowers() {
        return numFollowers;
    }


    public void setNumFollowers(short numFollowers) {
        this.numFollowers = numFollowers;
    }


    public short getNumFollowing() {
        return numFollowing;
    }


    public void setNumFollowing(short numFollowing) {
        this.numFollowing = numFollowing;
    }


    public Logstat(byte[] bytes){
        this.opcode = Opcode.LOGSTAT;
        this.age = bytesToShort(new byte[]{bytes[2],bytes[3]});
        this.numPosts = bytesToShort(new byte[]{bytes[4],bytes[5]});
        this.numFollowers = bytesToShort(new byte[]{bytes[6],bytes[7]});
        this.numFollowing = bytesToShort(new byte[]{bytes[8],bytes[9]});
    }





    @Override
    public byte[] encode(Message message) {
        Logstat m = (Logstat)message;
        byte [] encodedMessgae;
        byte [] ackOpcodeBytes = shortToBytes(getOpcodeValue(Opcode.ACK));
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte [] ageBytes = shortToBytes(m.getAge());
        byte [] numPostsBytes = shortToBytes(m.getNumPosts());
        byte [] numFollowersBytes = shortToBytes(m.getNumFollowers());
        byte [] numFollowingBytes = shortToBytes(m.getNumFollowing());

        encodedMessgae = ArrayUtils.addAll(ackOpcodeBytes, opcodeBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, ageBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, numPostsBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, numFollowersBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, numFollowingBytes);
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }

    


    
}
