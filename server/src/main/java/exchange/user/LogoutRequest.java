package exchange.user;

import exchange.Request;

public class LogoutRequest extends Request {
    public LogoutRequest() {}
    public LogoutRequest(String authToken) {
        super(authToken);
    }
}

