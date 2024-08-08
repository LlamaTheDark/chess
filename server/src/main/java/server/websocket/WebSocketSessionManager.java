package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// TODO: handle request validation &c
public
class WebSocketSessionManager {
    private final
    ConcurrentHashMap<Integer, HashSet<Session>> sessions = new ConcurrentHashMap<>();

    void addSessionToGame(int gameID, Session session) {
        if (!sessions.containsKey(gameID)) {
            sessions.put(gameID, new HashSet<>());
        }
        sessions.get(gameID).add(session);
    }

    void removeSessionFromGame(int gameID, Session session) {sessions.get(gameID).remove(session);}

    void removeSession(Session session) {}

    Set<Session> getSessionsForGame(int gameID) {return sessions.get(gameID);}
}
