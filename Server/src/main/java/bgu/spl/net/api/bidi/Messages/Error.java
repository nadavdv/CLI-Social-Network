package bgu.spl.net.api.bidi.Messages;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.ArrayUtils;

public class Error extends Message {

    private Opcode messageOpcode ;

    public Error(Opcode messageOpcode) {
        this.opcode = Opcode.ERROR;
        this.messageOpcode = messageOpcode;
    }

    public Opcode getMessageOpcode() {
        return messageOpcode;
    }

    public void setMessageOpcode(Opcode messageOpcode) {
        this.messageOpcode = messageOpcode;
    }

    

    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte [] messageOpcodeBytes = shortToBytes(getOpcodeValue(messageOpcode));
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, messageOpcodeBytes);
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }
    
}
