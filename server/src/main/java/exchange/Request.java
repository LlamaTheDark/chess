package exchange;

/*
TODO: use records instead of classes here
 */

public class Request {
    private String authToken;
    public Request(){ }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}