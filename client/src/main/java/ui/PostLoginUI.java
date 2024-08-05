package ui;

import exception.UnknownCommandException;
import exchange.game.CreateGameRequest;
import exchange.game.JoinGameRequest;
import exchange.game.ListGamesRequest;
import exchange.user.LogoutRequest;
import server.ServerFacade;
import server.SessionHandler;

import java.util.Scanner;

public
class PostLoginUI {
    static final String POST_LOGIN_PROMPT = "[LOGGED IN] >>> ";

    /**
     * A type of command that can be given after a user logs in.
     */
    enum PostLoginCommand {
        HELP,
        LOGOUT,
        CREATE_GAME,
        LIST_GAMES,
        PLAY_GAME,
        OBSERVE_GAME,
        NONE
    }

    /**
     * This method houses the REPL for the pre-login UI.
     */
    public
    void start() {
        Scanner in = new Scanner(System.in);
        var handler = new PostLoginCommandHandler();
        PostLoginCommand command = PostLoginCommand.NONE;

        do {
            try {
                System.out.print(POST_LOGIN_PROMPT);

                command = switch (in.next().toLowerCase()) {
                    case "help", "h" -> PostLoginCommand.HELP;
                    case "logout", "l" -> PostLoginCommand.LOGOUT;
                    case "create", "c" -> PostLoginCommand.CREATE_GAME;
                    case "list", "li" -> PostLoginCommand.LIST_GAMES;
                    case "join", "j", "play", "p" -> PostLoginCommand.PLAY_GAME;
                    case "observe", "o" -> PostLoginCommand.OBSERVE_GAME;
                    default -> throw new UnknownCommandException();
                };

                switch (command) {
                    case HELP -> handler.handleHelp();
                    case LOGOUT -> handler.handleLogout();
                    case CREATE_GAME -> handler.handleCreateGame(in.next());
                    case LIST_GAMES -> handler.handleListGames();
                    case PLAY_GAME -> handler.handlePlayGame();
                    case OBSERVE_GAME -> handler.handleObserveGame();
                }
            } catch (UnknownCommandException e) {
                in.nextLine();
                System.out.println(e.getMessage());
            }
        } while (command != PostLoginCommand.LOGOUT);
    }

    /**
     * Handles delegation and execution of commands that may be given before the user is logged in.
     */
    private static
    class PostLoginCommandHandler {

        private
        void handleHelp() {
            System.out.println("""
                               \tcreate <NAME> - create a new game
                               \tlist - list all games
                               \tjoin <ID> [WHITE|BLACK] - join a game as the WHITE or BLACK team
                               \tobserve <ID> observe a game
                               \tlogout - return to start screen
                               \thelp - display list of possible commands
                               """);
        }

        private
        void handleLogout() {
            var serverFacade = new ServerFacade();
            try {
                serverFacade.logout(new LogoutRequest());
                SessionHandler.authToken = "";
            } catch (Exception e) {
                System.out.println("Failed to log out.");
            }
        }

        private
        void handleCreateGame(String gameName) {
            var serverFacade = new ServerFacade();
            try {
                var response = serverFacade.createGame(new CreateGameRequest(gameName));
                System.out.printf("Created game '%s' with ID %d\n", gameName, response.getGameID());
            } catch (Exception e) {
                System.out.println("Failed to create game.");
            }
        }

        private
        void handleListGames() {
            var serverFacade = new ServerFacade();
            try {
                var listGamesResponse = serverFacade.listGames(new ListGamesRequest());
                for (var game : listGamesResponse.getGames()) {
                    System.out.printf(
                            "\t[ID: %d] %s | WHITE: %s | BLACK: %s%n",
                            game.gameID(),
                            game.gameName(),
                            game.whiteUsername(),
                            game.blackUsername()
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println("Failed to list games.");
            }
        }

        private
        void handlePlayGame() {
            var serverFacade = new ServerFacade();
            try {
                serverFacade.joinGame(new JoinGameRequest());
            } catch (Exception e) {
                System.out.println("Failed to log in.");
            }
            System.out.println("handle play game...");
        }

        private
        void handleObserveGame() {
            /*
            TODO: ???
             */
        }
    }

}
