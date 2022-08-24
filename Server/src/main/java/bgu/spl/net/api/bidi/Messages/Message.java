package bgu.spl.net.api.bidi.Messages;

import javax.swing.plaf.synth.Region;

public abstract class Message {
    public enum Opcode{
        TEST, REGISTER,LOGIN,LOGOUT,FOLLOW,POST,PM,LOGSTAT,STAT,
        NOTIFICATION,ACK,ERROR,BLOCK
    }

    protected Opcode opcode;


    public abstract byte[] encode(Message message);


    public Opcode getOpcode() {
        return opcode;
    }

    public void setOpcode(Opcode opcode) {
        this.opcode = opcode;
    }

    public short getOpcodeValue() {
        return (short)opcode.ordinal();
    }
    public short getOpcodeValue(Opcode code) {
        return (short)code.ordinal();
    }
   
    public byte[] shortToBytes(short num){
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    public short bytesToShort(byte[] byteArr){
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
   
    

}
