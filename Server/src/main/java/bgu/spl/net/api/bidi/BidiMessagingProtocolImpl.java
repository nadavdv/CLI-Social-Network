package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.Messages.*;
import bgu.spl.net.api.bidi.Messages.Error;
import bgu.spl.net.api.bidi.Messages.Message.Opcode;
import bgu.spl.net.srv.DB;
import bgu.spl.net.srv.User;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private int ownerId;
    private Connections<Message> connections;
    private DB db = DB.getInstance();
    private boolean shouldTerminate =false;

    public BidiMessagingProtocolImpl(){

    }

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.ownerId = connectionId;
        this.connections= connections;
        
    }

    @Override
    public void process(Message message) {
        System.out.println("OPCODE "+ message.getOpcode());
        switch (message.getOpcode()) {
            case REGISTER:
                register((Register) message);
                break;
            case LOGIN:
                login((Login) message);
                break;
            case LOGOUT:
                logout((Logout) message);
                break;
            case FOLLOW:
                follow((Follow) message);
                break;
            case POST:
                post((Post) message);
                break;
            case PM:
                pm((Pm) message);
                break;
            case LOGSTAT:
                logstat((Logstat) message);
                break;
            case STAT:
                stat((Stat) message);
                break;
            case BLOCK:
                block((Block) message);
                break;
            default:
                break;
        }
        
    }

    

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }

    public void register(Register message){
        System.out.println("REGISTER");
        try {
            db.register(new User(message.getUsername(),message.getPassword(),message.getBirthday()));
            connections.send(ownerId, new Ack(Opcode.REGISTER, "optionalRegister"));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.REGISTER));
        }
    }

    public void login(Login message){
        System.out.println("LOGIN");
        
        try {
            db.login(message.getUsername(),message.getPassword(),ownerId);
            connections.send(ownerId, new Ack(Opcode.LOGIN, ""));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.LOGIN));
        }
    }

    public void logout(Logout message){
        System.out.println("LOGOUT");
        try {
            db.logout(ownerId);
            connections.send(ownerId, new Ack(Opcode.LOGOUT,""));
            connections.disconnect(ownerId);
            shouldTerminate();
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.LOGOUT));
        }
    }
    public void follow(Follow message){
        System.out.println("FOLLOW");
        try {
            db.follow(ownerId, message.isFollow(),message.getUsername());
            connections.send(ownerId, new Ack(Opcode.FOLLOW, message.getUsername()));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.FOLLOW));
        }
    }
    public void post(Post message){
        System.out.println("POST");
        try {
            db.post(ownerId, message.getContent());
            connections.send(ownerId, new Ack(Opcode.POST,""));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.POST));
        }
    }
    public void pm(Pm message){
        System.out.println("PM");
        try {
            db.pm(ownerId,message.getUsername(),message.getContent(),message.getTime());
            connections.send(ownerId, new Ack(Opcode.PM,""));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.PM));
        }
    }
    public void logstat(Logstat message){
        System.out.println("LOGSTAT");
        try {
            String optional= db.logstat(ownerId);
            connections.send(ownerId, new Ack(Opcode.LOGSTAT,optional));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.LOGSTAT));
        }
    }
    public void stat(Stat message){
        System.out.println("STAT");
        try {
            String optional= db.stat(ownerId, message.getUsernamesList());
            connections.send(ownerId, new Ack(Opcode.STAT, optional));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.STAT));
        }
    }
    
    public void block(Block message) {
        System.out.println("BLOCK");
        try {
            db.block(ownerId, message.getUsername());
            connections.send(ownerId, new Ack(Opcode.BLOCK,""));
        } catch (IllegalStateException e) {
            connections.send(ownerId, new Error(Opcode.STAT));
        }
    }
}
