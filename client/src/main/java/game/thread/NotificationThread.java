package game.thread;

import ui.EscapeSequences;
import ui.GameplayUI;

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
        System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);
        ui.getPrinter().printMessageToLog(message);
        System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);
    }
}
