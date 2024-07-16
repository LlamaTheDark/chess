package exchange.user;

import exchange.Response;

public class RegisterResponse extends Response {
    String username;
    String authToken;

    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
