package ui.game.thread;

import ui.game.GameplayUI;

public
class NotificationThread extends Thread {
    private String     message;
    private GameplayUI ui;

    public
    NotificationThread(String message, GameplayUI printer) {
        this.message = message;
        this.ui = printer;
    }

    public
    void run() {
        ui.getPrinter().printMessageToLog(message);
    }
}
