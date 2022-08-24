package bgu.spl.net.srv;
/**
 * thread- safe singletopn containing all the server's data 
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import javax.print.DocFlavor.STRING;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Messages.Message;
import bgu.spl.net.api.bidi.Messages.Notification;

public class DB {
    private ConcurrentHashMap<String,User> namesToUsesrsMap;
    private ConcurrentHashMap<Integer,String> connectionidToUsername;
    private Connections<Message> connections;
    private DB(){
        namesToUsesrsMap = new ConcurrentHashMap<>();
        connectionidToUsername = new ConcurrentHashMap<>();
        connections = ConnectionsImpl.getInstance();
    }
    /**
     * {@link DB} Singleton Holder.
     */

	private static class SingletonHolder {
        private static DB instance = new DB();
    }

	
	/**
     * Retrieves the single instance of this class.
     */

	public static DB getInstance() {
		return SingletonHolder.instance;
	}
    
    public void register(User user) throws IllegalStateException{
        if (!namesToUsesrsMap.containsKey(user.getUsername()))
            namesToUsesrsMap.put(user.getUsername(), user);
        else
            throw new IllegalStateException("User is already registered");
    }

    public void login(String userName,String password, int connectionId) throws IllegalStateException{
        if (!namesToUsesrsMap.containsKey(userName))
            throw new IllegalStateException("User is not registered");
        else {
            User user=namesToUsesrsMap.get(userName);
            if (user.isLoggedIn())
                throw new IllegalStateException("User is already logged in");
            else{
                if(!(user.getPassword().equals(password)))
                    throw new IllegalStateException("Incorrect password");
                else{
                    user.setLoggedIn(true);
                    user.setConnectionId(connectionId);
                    connectionidToUsername.put(connectionId, userName);
                    //TODO check messages
                    for(Notification msg: user.getUnreadMessages()){
                        connections.send(connectionId, msg);
                    }
                }
                    
            }
        }
    }

    public void logout(int connectionId){
        if(!connectionidToUsername.containsKey(connectionId))
            throw new IllegalStateException("User is not logged in");
        User user =namesToUsesrsMap.get(connectionidToUsername.get(connectionId));
        user.setLoggedIn(false);
        user.setConnectionId(-1);
        connectionidToUsername.remove(connectionId);
    }

    public void follow(int connectionId,boolean isFollow,String usernameTofollow) {
        if(!connectionidToUsername.containsKey(connectionId))
            throw new IllegalStateException("User is not logged in");
        User currUser =  namesToUsesrsMap.get(connectionidToUsername.get(connectionId));
        User userToFollow = namesToUsesrsMap.get(usernameTofollow);
        if(userToFollow == null)
            throw new IllegalStateException("User to follow is not registered");
        if(isFollow){
            if(!currUser.addFollower(userToFollow))
                throw new IllegalStateException("User is already being followed");
        }
        else{
            if(!currUser.removeFollower(userToFollow))
                throw new IllegalStateException("User is already not being followed");
        }
       
    }

    public void post(int connectionId, String content) {
        if(!connectionidToUsername.containsKey(connectionId))
            throw new IllegalStateException("User is not logged in");
        User currUser =  namesToUsesrsMap.get(connectionidToUsername.get(connectionId));
        currUser.increamentNumOfPosts();
        Notification n = new Notification(true, currUser.getUsername(), content);
        //send to tagged users
        LinkedList<String> taggedUsers = getTaggedUsers(content);
        for (String username : taggedUsers) {
            //check if tagged user is registered
            User taggedUser = namesToUsesrsMap.get(username);
            if(taggedUser!=null){
                if(taggedUser.isLoggedIn() && connections.send(taggedUser.getConnectionId(), n)){
                    
                }
                else{//user is not logged in or send failed
                    taggedUser.add(n);
                }
                taggedUser.logMessage(content);
            }
        }
        //send to followers
        LinkedList<User> followers = currUser.getFollowers();
        for (User follower : followers) {
            if(follower.isLoggedIn() && connections.send(follower.getConnectionId(), n)){
                
            }
            else{//user is not logged in or send failed
                follower.add(n);
            }
            follower.logMessage(content);
        }

    }

   

    private LinkedList<String> getTaggedUsers(String content){
        LinkedList<String> list = new LinkedList<>();
        String username;
        for (int i = 0; i < content.length()-1; i++) {
            if(content.charAt(i)== '@'){
                int endIndex=i+1;
                while(content.charAt(endIndex)!= ' '){
                    endIndex++;
                }
                username = content.substring(i+1, endIndex);
                list.add(username);
                i=endIndex+1;
            }
        }
        return list;
    }

    public void pm(int connectionId, String username, String content, String time) {
        if(!connectionidToUsername.containsKey(connectionId))
            throw new IllegalStateException("User is not logged in");
        User currUser =  namesToUsesrsMap.get(connectionidToUsername.get(connectionId));
        User recipient =  namesToUsesrsMap.get(username);
        if(recipient == null)
            throw new IllegalStateException("Recipient is not registered");
        Notification n = new Notification(false, currUser.getUsername(), content);
        if(recipient.isLoggedIn() ){
            if(!connections.send(recipient.getConnectionId(), n)){
                throw new IllegalStateException("COULD NOT SEND");
            }
        }
        else{//user is not logged in or send failed
            recipient.add(n);
        }
        recipient.logMessage(content);
    }
    //TODO filter
    private String filter(String content){
        return content;
    }

    public String logstat(int connectionId) {
        if(!connectionidToUsername.containsKey(connectionId))
            throw new IllegalStateException("User is not logged in");
        User currUser =  namesToUsesrsMap.get(connectionidToUsername.get(connectionId));
        return currUser.getBirthday() +" "+currUser.getNumOfPosts()+" "+currUser.getNumOfFolowers()+" "+currUser.getNumOfFolowings();  
    }

    public String stat(int connectionId, LinkedList<String> usernamesList) {
        if(!connectionidToUsername.containsKey(connectionId))
            throw new IllegalStateException("User is not logged in");
        return null;
    }

	public void block(int connectionId, String username) {
        if(!connectionidToUsername.containsKey(connectionId))
            throw new IllegalStateException("User is not logged in");
        User currUser =  namesToUsesrsMap.get(connectionidToUsername.get(connectionId));
        User userToBlock =  namesToUsesrsMap.get(username);
        if(userToBlock == null)
            throw new IllegalStateException("User To Block is not registered");
        userToBlock.blocked(currUser);
	}    
}
