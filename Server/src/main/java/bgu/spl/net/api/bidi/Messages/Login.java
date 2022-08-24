package bgu.spl.net.api.bidi.Messages;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.DateFormatSymbols;

public class Login extends Message {
    private String username;
    private String password;
    private byte captcha;

    public Login(String username, String password, byte captcha) {
        if(captcha==0)
            throw new IllegalStateException("Couldn't log in. Captcha is 0");
        this.opcode = Opcode.LOGIN;
        this.username = username;
        this.password = password;
        this.captcha = captcha;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

   public byte getCaptcha() {
       return captcha;
   }
   public Login(byte[] bytes){
        this.opcode = Opcode.LOGIN;
        int [] zeroIndexes = new int [2];
        int index =0;
        for (int i =2; i < bytes.length &&index <2; i++) {
            if(bytes[i] == '\0' ){
                zeroIndexes[index] = i;
                index+=1;
            }
        }
        username  =  new String(bytes, 2, zeroIndexes[0]-2, StandardCharsets.UTF_8);
        password  =  new String(bytes, zeroIndexes[0]+1, zeroIndexes[1]-(zeroIndexes[0]+1), StandardCharsets.UTF_8);
        captcha  =  1;
    }


    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte [] usernameBytes = this.username.getBytes(StandardCharsets.UTF_8);
        byte seperator ='\0';
        byte [] passwordBytes = this.password.getBytes(StandardCharsets.UTF_8);
        byte captcha = this.captcha;
        encodedMessgae = ArrayUtils.addAll(opcodeBytes,seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, usernameBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, passwordBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, captcha);
        return encodedMessgae;
    }
}
