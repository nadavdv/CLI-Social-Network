package bgu.spl.net.api.bidi.Messages;

import java.nio.charset.StandardCharsets;

import javax.print.DocFlavor.STRING;

import org.apache.commons.lang3.ArrayUtils;

public class Notification extends Message {
    private boolean notificationType ;
    private String postingUsername;
    private String content;

    

    public Notification(boolean notificationType, String postingUsername, String content) {
        this.opcode = Opcode.NOTIFICATION;
        this.notificationType = notificationType;
        this.postingUsername = postingUsername;
        this.content = content;
    }


    public boolean isNotificationType() {
        return notificationType;
    }


    public void setNotificationType(boolean notificationType) {
        this.notificationType = notificationType;
    }


    public String getPostingUsername() {
        return postingUsername;
    }


    public void setPostingUsername(String postingUsername) {
        this.postingUsername = postingUsername;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public Notification(byte[] bytes) {
        this.opcode = Opcode.NOTIFICATION;
       
    }




    @Override
    public byte[] encode(Message message) {
        byte [] encodedMessgae;
        byte [] opcodeBytes = shortToBytes(getOpcodeValue());
        byte notificationTypeByte;
        if(notificationType)
        notificationTypeByte = '1';//public
        else
        notificationTypeByte ='0';//pm
        byte [] postingUsernameBytes = this.postingUsername.getBytes(StandardCharsets.UTF_8);
        byte seperator ='\0';
        byte [] contentBytes = this.content.getBytes(StandardCharsets.UTF_8);
        encodedMessgae = ArrayUtils.addAll(opcodeBytes, notificationTypeByte);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, postingUsernameBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, contentBytes);
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, seperator);
        String bye = ";";
        encodedMessgae = ArrayUtils.addAll(encodedMessgae, bye.getBytes(StandardCharsets.UTF_8));
        return encodedMessgae;
    }
}
