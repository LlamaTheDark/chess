package exchange.halfduplex;

/**
 * Models a request made to the server.
 */
public
class Request {
    protected String authToken;

    public
    Request(String authToken) {this.authToken = authToken;}

    public
    Request() {}

    /**
     * @return The authorization token associated with this request.
     */
    public
    String getAuthToken() {return this.authToken;}

    /**
     * Sets the authorization token associated with this request to the passed parameter.
     */
    public
    void setAuthToken(String authToken) {this.authToken = authToken;}
}