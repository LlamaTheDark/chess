package exchange;

/*
TODO: use records instead of classes here
TODO: what's with all your constructors for your requests? do you even need them?
TODO: I think you just need the variables there with the getters for the requests, then you need the constructors for
  the responses
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