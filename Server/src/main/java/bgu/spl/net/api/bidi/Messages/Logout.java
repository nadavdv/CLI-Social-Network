package bgu.spl.net.api.bidi.Messages;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.ArrayUtils;

public class Logout extends Message{

    public Logout() {
        this.opcode = Opcode.LOGOUT;
    }

    

    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }
}
