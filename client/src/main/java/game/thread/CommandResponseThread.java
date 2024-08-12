package game.thread;

import ui.EscapeSequences;
import ui.GameplayUI;

public
class CommandResponseThread extends Thread {
    private String     message;
    private GameplayUI ui;
    private String     messageSeverity;

    public
    CommandResponseThread(String message, GameplayUI ui, String messageSeverity) {
        this.message = message;
        this.ui = ui;
        this.messageSeverity = messageSeverity;
    }

    public
    void run() {
        System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);
        ui.getPrinter().printCommandResponse(message, messageSeverity);
        System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);
    }

    public static
    class MessageSeverity {
        public static final String ERROR    = EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.SET_TEXT_ITALIC;
        public static final String WARNING  = EscapeSequences.SET_TEXT_COLOR_YELLOW + EscapeSequences.SET_TEXT_ITALIC;
        public static final String STANDARD = EscapeSequences.SET_TEXT_ITALIC;
    }
}
