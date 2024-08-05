package ui;

import exception.UnknownCommandException;

import java.util.Scanner;

public
class PreLoginUI {
    static final String PRE_LOGIN_PROMPT = "[LOGGED OUT] >>> ";

    /**
     * A type of command that can be given before a user logs in.
     */
    enum PreLoginCommand {
        HELP,
        QUIT,
        LOGIN,
        REGISTER,
        NONE
    }

    /**
     * This method houses the REPL for the pre-login UI.
     */
    public
    void start() {
        Scanner in = new Scanner(System.in);
        PreLoginCommandHandler handler = new PreLoginCommandHandler();
        PreLoginCommand command = PreLoginCommand.NONE;
        do {
            try {
                System.out.print(PRE_LOGIN_PROMPT);

                command = switch (in.next().toLowerCase()) {
                    case "help", "h" -> PreLoginCommand.HELP;
                    case "quit", "q" -> PreLoginCommand.QUIT;
                    case "login", "l" -> PreLoginCommand.LOGIN;
                    case "register", "r" -> PreLoginCommand.REGISTER;
                    default -> throw new UnknownCommandException();
                };

                switch (command) {
                    case HELP -> handler.handleHelp();
                    case LOGIN -> handler.handleLogin(in.nextLine().split(" "));
                    case REGISTER -> handler.handleRegister(in.nextLine().split(" "));
                }
            } catch (UnknownCommandException e) {
                in.nextLine();
                System.out.println(e.getMessage());
            }
        } while (command != PreLoginCommand.QUIT);
    }

    /**
     * Handles delegation and execution of commands that may be given before the user is logged in.
     */
    private static
    class PreLoginCommandHandler {

        private
        void handleHelp() {
            System.out.println("""
                               \tregister <USERNAME> <PASSWORD> <EMAIL> - create an account and login
                               \tlogin <USERNAME> <PASSWORD> - login to play chess
                               \tquit - terminate the application
                               \thelp - show all commands
                               """);
        }

        private
        void handleLogin(String[] args) {
            System.out.println("handle login...");
            System.out.println("SUCCESS!");
            new PostLoginUI().start();
        }

        private
        void handleRegister(String[] args) {
            System.out.println("handle register...");
        }
    }

}
