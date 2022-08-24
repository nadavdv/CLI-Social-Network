package bgu.spl.net.srv;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.net.api.bidi.Messages.Notification;

public class User {
    private String username;
    private String password;
    private String birthday;
    private LinkedList<User> followings;
    private LinkedList<User> followers;
    private LinkedList<User> blocked;
    private AtomicInteger numOfPosts;
    private int connectionId;
    private boolean LoggedIn;
    private ConcurrentLinkedDeque<Notification> unreadMessages;
    private ConcurrentLinkedQueue<String> allMessages;

    public User(String username, String password, String birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.followings = new LinkedList<>();
        this.followers = new LinkedList<>();
        this.blocked = new LinkedList<>();
        this.numOfPosts = new AtomicInteger(0);
        this.LoggedIn=false;
        this.unreadMessages = new ConcurrentLinkedDeque<>();
        this.allMessages = new ConcurrentLinkedQueue<String>();
    }

    public ConcurrentLinkedDeque<Notification> getUnreadMessages() {
        return unreadMessages;
    }

    

    public int getNumOfPosts(){
        return numOfPosts.get();
    }
    public void increamentNumOfPosts(){
        numOfPosts.incrementAndGet();
    }


    public Notification remove(){
       return unreadMessages.remove();
    }
    public void addFirst(Notification msg){
        unreadMessages.addFirst(msg);
    }
    public void add(Notification msg){
        unreadMessages.add(msg);
    }

    public boolean isLoggedIn() {return LoggedIn;}

    public void setLoggedIn(boolean LoggedIn) {
        this.LoggedIn = LoggedIn;
        
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getBirthday() {
        return birthday;
    }


    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


   

    public boolean addFollower(User user){
        if(followings.contains(user))
            return false;
        return followings.add(user);
    }
    public boolean removeFollower(User user){
        return followings.remove(user);
    }
    
    
    public LinkedList<User> getFollowing() {
        return followings;
    }
    public void setFollowing(LinkedList<User> followings) {
        this.followings = followings;
    }


    public LinkedList<User> getFollowers() {
        return followers;
    }


    public void setFollowers(LinkedList<User> followers) {
        this.followers = followers;
    }






    public int getConnectionId() {
        return connectionId;
    }


    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void blocked(User currUser) {
        blocked.add(currUser);
    }

	public void logMessage(String message) {
        allMessages.add(message);
	}

    public int getNumOfFolowers() {
        return followers.size();
    }

    public int getNumOfFolowings() {
        return followings.size();
    }
}
