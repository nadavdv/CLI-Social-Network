package bgu.spl.net.api.bidi.Messages;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.text.ZoneView;

public class Stat extends Message {
    private LinkedList<String> usernamesList;

    
    public Stat(LinkedList<String> usernamesList) {
        this.opcode = Opcode.STAT;
        this.usernamesList = usernamesList;
    }

    

    public LinkedList<String> getUsernamesList() {
        return usernamesList;
    }


    public void setUsernamesList(LinkedList<String> usernamesList) {
        this.usernamesList = usernamesList;
    }






    public Stat(byte[] bytes){
        this.opcode = Opcode.STAT;
        int [] zeroIndexes = new int [3];
        int zeroIndex =-1;
        String username ="";
        for (int i = 3; i < bytes.length&& bytes[i] != '\0'; i++) {
            if(bytes[i] == '|'){
                usernamesList.add( username);
                username = "";
            }
            else
                username+=bytes[i];
        } 
    }




    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        String usernamesString = "";
        for (String username : usernamesList) {
            usernamesString+=username+"|";
        }
        byte [] usernamesListBytes = usernamesString.getBytes(StandardCharsets.UTF_8);
        byte seperator ='\0';

        encodedMessgae = ArrayUtils.addAll(opcodeBytes, usernamesListBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }

    


    
}
