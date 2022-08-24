package bgu.spl.net.api.bidi.Messages;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.DateFormatSymbols;
import java.util.Arrays;

import javax.swing.text.ZoneView;

public class Register extends Message {
    private String username;
    private String password;
    private String birthday;//TODO change from String t

    
    public Register(String username, String password, String birthday) {
        this.opcode = Opcode.REGISTER;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    } 

    public Register(byte[] bytes){
        this.opcode = Opcode.REGISTER;
        int [] zeroIndexes = new int [3];
        int index =0;
        for (int i =2; i < bytes.length &&index <3; i++) {
            if(bytes[i] == '\0' ){
                zeroIndexes[index] = i;
                index+=1;
            }
        }
        username  =  new String(bytes, 2, zeroIndexes[0]-2, StandardCharsets.UTF_8);
        password  =  new String(bytes, zeroIndexes[0]+1, zeroIndexes[1]-(zeroIndexes[0]+1), StandardCharsets.UTF_8);
        birthday  =  new String(bytes, zeroIndexes[1]+1, zeroIndexes[2]-(zeroIndexes[1]+1), StandardCharsets.UTF_8);
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }





    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte [] usernameBytes = this.username.getBytes(StandardCharsets.UTF_8);
        byte seperator ='\0';
        byte [] passwordBytes = this.password.getBytes(StandardCharsets.UTF_8);
        byte [] birthdayBytes = this.birthday.getBytes(StandardCharsets.UTF_8);
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, usernameBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, passwordBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, birthdayBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }

    


    
}
