package exchange;

/*
TODO: use records instead of classes here
TODO: what's with all your constructors for your requests? do you even need them?
TODO: I think you just need the variables there with the getters for the requests, then you need the constructors for the responses
 */

public
class Request {
    private String authToken;

    public
    Request() {}

    public
    Request(String authToken) {
        this.authToken = authToken;
    }

    public
    String getAuthToken() {
        return authToken;
    }

    public
    void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}