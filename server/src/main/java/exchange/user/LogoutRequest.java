package exchange.user;

import exchange.Request;

public
class LogoutRequest extends Request {
    public
    LogoutRequest(String authToken) {super(authToken);}

    public
    LogoutRequest() {}

}

