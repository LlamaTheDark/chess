package ui;

import exchange.halfduplex.game.CreateGameRequest;
import exchange.halfduplex.game.JoinGameRequest;
import exchange.halfduplex.game.ListGamesRequest;
import exchange.halfduplex.user.LogoutRequest;
import server.ServerFacade;
import server.SessionHandler;
import ui.exception.ForbiddenException;
import ui.exception.UIException;
import ui.exception.UnknownCommandException;

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
        JOIN_GAME,
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
                    case "join", "j", "play", "p" -> PostLoginCommand.JOIN_GAME;
                    case "observe", "o" -> PostLoginCommand.OBSERVE_GAME;
                    default -> throw new UnknownCommandException();
                };

                switch (command) {
                    case HELP -> handler.handleHelp();
                    case LOGOUT -> handler.handleLogout();
                    case CREATE_GAME -> handler.handleCreateGame(in.next());
                    case LIST_GAMES -> handler.handleListGames();
                    case JOIN_GAME -> handler.handleJoinGame(in.next(), in.next());
                    case OBSERVE_GAME -> handler.handleObserveGame(in.next());
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
                               \tjoin <GAME NUMBER> [WHITE|BLACK] - join a game as the WHITE or BLACK team
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
            } catch (UIException e) {
                System.out.println("Failed to log out: " + e.getMessage());
            }
        }

        private
        void handleCreateGame(String gameName) {
            var serverFacade = new ServerFacade();
            try {
                var response = serverFacade.createGame(new CreateGameRequest(gameName));
                System.out.printf("Created game '%s'\n", gameName);
            } catch (UIException e) {
                System.out.println("Failed to create game: " + e.getMessage());
            }
        }

        private
        void handleListGames() {
            var serverFacade = new ServerFacade();
            try {
                var listGamesResponse = serverFacade.listGames(new ListGamesRequest());
                SessionHandler.setGameListMemory(listGamesResponse.getGames());
                if (SessionHandler.games.isEmpty()) {
                    System.out.println(
                            "\tThere are no active games at this time. Type 'create <GAMENAME>' to create one!");
                } else {
                    System.out.println("\tActive games: " + SessionHandler.games.size());
                    for (int i = 0; i < SessionHandler.games.size(); i++) {
                        var game = SessionHandler.games.get(i);
                        System.out.printf(
                                "\t\t(%d) %s \t| WHITE: %s \t| BLACK: %s%n",
                                i + 1,
                                game.gameName(),
                                game.whiteUsername(),
                                game.blackUsername()
                        );
                    }
                }
            } catch (UIException e) {
                System.out.println("Failed to list games: " + e.getMessage());
            }
        }

        private
        void handleJoinGame(String indexInList, String playerColor) { // change this to handleJoinGame
            var serverFacade = new ServerFacade();
            try {
                int index = Integer.parseInt(indexInList);
                serverFacade.joinGame(new JoinGameRequest(
                        playerColor,
                        SessionHandler.getGameIDFromIndex(index)
                ));
            } catch (ForbiddenException e) {
                System.out.println("Failed to join game: the requested player color is taken.");
            } catch (NumberFormatException e) {
                System.out.println(
                        "Failed to join game: your first parameter is not a number.\nSyntax: join <GAME NUMBER> " +
                        "[WHITE|BLACK]");
            } catch (UIException e) {
                System.out.println("Failed to join game: " + e.getMessage());
            }
        }

        private
        void handleObserveGame(String gameIndexInList) {
            try {
                var serverFacade = new ServerFacade();
                serverFacade.observeGame();
            } catch (NumberFormatException e) {
                System.out.println(
                        "Failed to join game: your first parameter is not a number.\nSyntax: join <GAME NUMBER> " +
                        "[WHITE|BLACK]");
            }
        }
    }

}
