package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public
class WebSocketConnectionManager {
    /**
     * A concurrent hash map which maps <code>Integer</code> game IDs to a set of all <code>Session</code>s connected to
     * that game.
     */
    private final ConcurrentHashMap<Integer, HashSet<Session>> sessions  = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Session, String>           usernames = new ConcurrentHashMap<>();

    void addSessionToGame(int gameID, Session session, String username) {
        if (!sessions.containsKey(gameID)) {
            sessions.put(gameID, new HashSet<>());
        }
        sessions.get(gameID).add(session);
        usernames.put(session, username);
    }

    void removeSessionFromGame(int gameID, Session session) {
        sessions.get(gameID).remove(session);
    }

    void removeSession(Session session) {
        for (Integer gameID : sessions.keySet()) {
            removeSessionFromGame(gameID, session);
        }
        usernames.remove(session);
    }

    public
    void removeGame(int gameID) {
        sessions.remove(gameID);
    }

    String getUsername(Session session) {
        return usernames.get(session);
    }

    Set<Session> getSessionsForGame(int gameID) {return sessions.get(gameID);}

}
