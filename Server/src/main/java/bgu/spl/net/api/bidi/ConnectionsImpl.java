package bgu.spl.net.api.bidi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

import bgu.spl.net.srv.ConnectionHandler;

public class ConnectionsImpl<T> implements Connections<T> {

    private HashMap<Integer , ConnectionHandler<T>> clientConnectionsMap;

    
    private ConnectionsImpl(){
        clientConnectionsMap = new HashMap<Integer , ConnectionHandler<T>>();
    }
    
    /**
     * {@link MessageBusImpl} Singleton Holder.
     */

	private static class SingletonHolder {
        private static ConnectionsImpl instance = new ConnectionsImpl();
    }

	
	/**
     * Retrieves the single instance of this class.
     */

	public static ConnectionsImpl getInstance() {
		return SingletonHolder.instance;
	}
    public void addConnection(int connectionId, ConnectionHandler<T> connectionHandler){
        clientConnectionsMap.put(connectionId, connectionHandler);
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if(!clientConnectionsMap.containsKey(connectionId) )
            return false;
        clientConnectionsMap.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        for (Integer  clientID : clientConnectionsMap.keySet()) {
            clientConnectionsMap.get(clientID).send(msg);
        }
        
    }

    @Override
    public void disconnect(int connectionId) {
        if( clientConnectionsMap.remove(connectionId) == null)
            throw new IllegalArgumentException("User Not Connected!");

        
    }
    
}