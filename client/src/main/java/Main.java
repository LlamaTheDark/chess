import ui.PreLoginUI;

public
class Main {
    public static
    void main(String[] args) {
        System.out.println("♕ Welcome to This Great Chess Client. Type 'Help' to get started. ♕");

        String url = "ws://localhost:8080/ws";
        if (args.length == 1) {
            url = args[0];
        }

        PreLoginUI preLoginUI = new PreLoginUI(url);
        preLoginUI.start();
    }
}