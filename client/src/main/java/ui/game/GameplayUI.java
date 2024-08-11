package ui.game;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import ui.EscapeSequences;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public
class GameplayUI {
    private final BoardPrinter        printer = new BoardPrinter();
    private final ChessGame.TeamColor teamColor;

    public
    GameplayUI(ChessGame.TeamColor color, String gameName) {
        teamColor = color;
        System.out.print(EscapeSequences.ERASE_SCREEN);
        //        printer.printNotificationsBackground();
        //        printer.printChessGameInformation(gameName, color);
        //        printer.printCommandResponse(" type 'help' for a list of commands", EscapeSequences.SET_TEXT_ITALIC);
    }

    /**
     * A class used to paint the screen with relevant displays.
     * <p>
     * The board coordinates are rows 2 through 11, columns 0 through 30. The first and last 3 columns, as well as the
     * first and last rows, are padding.
     * <p>
     * The notification coordinates are rows 2 through 11, columns 31 through 60. The first and last columns, as well as
     * the first and last rows, are padding.
     */
    public
    class BoardPrinter {
        private final ArrayList<String> recentNotifications       = new ArrayList<>();
        private       int               longestNotificationLength = 0;
        private final ArrayList<String> recentNotificationTimes   = new ArrayList<>();

        private static synchronized
        void resetEscapeSequences() {
            System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_TEXT_ITALIC);
        }

        private static final
        class BoardConstants {
            public static final int CHESS_BOARD_SQUARE_WIDTH  = 3;
            public static final int CHESS_BOARD_SQUARE_HEIGHT = 1;

            public static final int MAX_NOTIFICATIONS         = 9;
            public static final int NOTIFICATIONS_PADDING_ROW = 1;

            // Gameplay View Section Positions
            public static final int CHESS_INFORMATION_HEIGHT = 3;
            public static final int CHESS_INFORMATION_ROW    = 2;
            public static final int CHESS_INFORMATION_COL    = 4;

            public static final int CHESS_BOARD_PADDING_ROW   = 1;
            public static final int CHESS_BOARD_PADDING_COL   = 3;
            public static final int CHESS_BOARD_HEIGHT        = 10;
            public static final int CHESS_BOARD_WIDTH         = 10;
            public static final int CHESS_BOARD_WIDTH_LITERAL = CHESS_BOARD_WIDTH * 3;
            public static final int CHESS_BOARD_INNER_HEIGHT  = 8;
            public static final int CHESS_BOARD_INNER_WIDTH   = 8;

            public static final int NOTIFICATIONS_START_ROW = NOTIFICATIONS_PADDING_ROW + CHESS_INFORMATION_HEIGHT + 1;
            public static final int NOTIFICATIONS_START_COL =
                    (CHESS_BOARD_PADDING_COL * 2) + CHESS_BOARD_WIDTH_LITERAL + 1;
            public static final int NOTIFICATIONS_HEIGHT    = 10;
            public static final int NOTIFICATIONS_TITLE_COL =
                    (CHESS_BOARD_PADDING_COL * 2) + CHESS_BOARD_WIDTH_LITERAL + 3;
            public static final int NOTIFICATIONS_LIST_ROW  = NOTIFICATIONS_START_ROW + 1;
            public static final int NOTIFICATIONS_LIST_COL  = NOTIFICATIONS_TITLE_COL + 1;

            public static final int GAMEPLAY_PROMPT_ROW    =
                    CHESS_INFORMATION_HEIGHT + CHESS_BOARD_HEIGHT + (CHESS_BOARD_PADDING_ROW * 2) + 1;
            public static final int PROMPT_RESPONSE_ROW    = GAMEPLAY_PROMPT_ROW + 1;
            public static final int PROMPT_RESPONSE_HEIGHT = 13;
        }

        /**
         * Paints the given string <code>s</code> to the index on the board (0-9 in rows and cols). Prints any specified
         * escape sequences first, and reset text color, background color, and font weight at the end of the function.
         * <b>Any escape sequences that are not reset automatically by this function must be reset after it is
         * called.</b>
         *
         * @param row             the row, specified by the letter corresponding
         * @param col             the column, specified by the letter on the board
         * @param escapeSequences any number of escape sequences to be run before the text is printed.
         */
        private synchronized
        void printToChessBoard(String s, int row, int col, String... escapeSequences) {
            assert s.length() <= BoardConstants.CHESS_BOARD_SQUARE_WIDTH;
            // move the cursor to the specified position
            System.out.print(EscapeSequences.moveCursorToLocation(
                    row + BoardConstants.CHESS_BOARD_PADDING_ROW + BoardConstants.CHESS_INFORMATION_HEIGHT + 1,
                    ((col + 1) * BoardConstants.CHESS_BOARD_SQUARE_WIDTH) + 1
            ));

            // print escape sequences
            for (var escapeSequence : escapeSequences) {
                System.out.print(escapeSequence);
            }

            // print s
            System.out.print(s);

            // reset background color, text color, and font weight
            resetEscapeSequences();
        }

        public synchronized
        void printChessGameInformation(String gameName, ChessGame.TeamColor color) {
            System.out.print(EscapeSequences.moveCursorToLocation(
                    BoardConstants.CHESS_INFORMATION_ROW,
                    BoardConstants.CHESS_INFORMATION_COL
            ));
            System.out.printf("Game Name: %s | Your color: %s", gameName, color);
        }

        public synchronized
        void printMessageToLog(String message, String... escapeSequences) {
            System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);
            recentNotifications.add(message);
            recentNotificationTimes.add(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
            if (message.length() > longestNotificationLength) {longestNotificationLength = message.length();}
            if (recentNotifications.size() > BoardConstants.MAX_NOTIFICATIONS) {
                // TODO: turn this and your specific screen coords into static final variables
                recentNotifications.removeFirst();
                recentNotificationTimes.removeFirst();
            }

            for (var escapeSequence : escapeSequences) {
                System.out.print(escapeSequence);
            }

            for (int i = 0; i < recentNotifications.size(); i++) {
                var notification = recentNotifications.get(i);
                System.out.print(EscapeSequences.moveCursorToLocation(
                        i + BoardConstants.NOTIFICATIONS_LIST_ROW, BoardConstants.NOTIFICATIONS_LIST_COL));

                // print notification
                System.out.printf("[%s] %s", recentNotificationTimes.get(i), notification);
                for (int j = 0; j < longestNotificationLength - notification.length(); j++) {
                    System.out.print(" ");
                }

            }

            resetEscapeSequences();
            System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);
        }

        public synchronized
        void printNotificationsBackground() {
            for (int row = BoardConstants.NOTIFICATIONS_START_ROW;
                 row <= BoardConstants.NOTIFICATIONS_START_ROW + BoardConstants.NOTIFICATIONS_HEIGHT - 1;
                 row++) {
                // move to appropriate location
                System.out.print(EscapeSequences.moveCursorToLocation(row, BoardConstants.NOTIFICATIONS_START_COL));

                // print character
                System.out.print("|");
            }

            // move to title location
            System.out.print(EscapeSequences.moveCursorToLocation(
                    BoardConstants.NOTIFICATIONS_START_ROW,
                    BoardConstants.NOTIFICATIONS_TITLE_COL
            ));

            // print "NOTIFICATIONS:"
            System.out.print(EscapeSequences.SET_TEXT_BOLD);
            System.out.print(EscapeSequences.SET_TEXT_UNDERLINE);

            System.out.print("NOTIFICATIONS:");

            // reset escape sequences
            System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
            System.out.print(EscapeSequences.RESET_TEXT_UNDERLINE);
        }

        public synchronized
        void printChessBoardBackground() {
            for (int row = 0; row < BoardConstants.CHESS_BOARD_HEIGHT; row++) {
                for (int col = 0; col < BoardConstants.CHESS_BOARD_WIDTH; col++) {
                    if (row == 0 || row == 9) {
                        handleColumnLabels(row, col);
                    } else if (col == 0 || col == 9) {
                        // handle row labels
                        printToChessBoard(
                                String.format(
                                        " %d ",
                                        (teamColor == WHITE) ? BoardConstants.CHESS_BOARD_INNER_HEIGHT + 1 - row : row
                                ),
                                row,
                                col,
                                EscapeSequences.SET_BG_COLOR_DARK_GREEN,
                                EscapeSequences.SET_TEXT_COLOR_YELLOW
                        );
                    } else {
                        if (isWhiteSquare(row, col)) {
                            // white
                            printToChessBoard("   ", row, col, EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
                        } else {
                            // black
                            printToChessBoard("   ", row, col, EscapeSequences.SET_BG_COLOR_DARK_BROWN);
                        }
                    }
                }
            }
        }

        public synchronized
        void printChessBoard(ChessBoard board) {
            for (int row = 1; row <= BoardConstants.CHESS_BOARD_INNER_HEIGHT; row++) {
                for (int col = 1; col <= BoardConstants.CHESS_BOARD_INNER_WIDTH; col++) {
                    var pieceAtLocation = board.getPiece(new ChessPosition(
                            (teamColor == WHITE) ? BoardConstants.CHESS_BOARD_INNER_HEIGHT + 1 - row : row,
                            (teamColor == BLACK) ? BoardConstants.CHESS_BOARD_INNER_WIDTH + 1 - col : col
                    ));

                    var backgroundColor = (isWhiteSquare(row, col))
                                          ? EscapeSequences.SET_BG_COLOR_LIGHT_BROWN
                                          : EscapeSequences.SET_BG_COLOR_DARK_BROWN;

                    if (pieceAtLocation == null) {
                        printToChessBoard("   ", row, col, backgroundColor);
                    } else {
                        var textColor = (pieceAtLocation.getTeamColor() == WHITE)
                                        ? EscapeSequences.SET_TEXT_COLOR_WHITE
                                        : EscapeSequences.SET_TEXT_COLOR_BLACK;
                        printToChessBoard(
                                String.format(" %s ", pieceAtLocation),
                                row,
                                col,
                                backgroundColor,
                                textColor,
                                EscapeSequences.SET_TEXT_BOLD
                        );
                    }
                }
            }
        }

        public synchronized
        void printCommandPrompt(String s, String... escapeSequences) {
            for (var escapeSequence : escapeSequences) {
                System.out.print(escapeSequence);
            }

            System.out.print(EscapeSequences.moveCursorToLocation(BoardConstants.GAMEPLAY_PROMPT_ROW, 1));
            System.out.print(EscapeSequences.ERASE_LINE);

            System.out.print(s);

            resetEscapeSequences();
        }

        public synchronized
        void printCommandResponse(String s, String... escapeSequences) {
            for (var escapeSequence : escapeSequences) {
                System.out.print(escapeSequence);
            }

            // clear the response space
            for (int i = 0; i < BoardConstants.PROMPT_RESPONSE_HEIGHT; i++) {
                System.out.print(EscapeSequences.moveCursorToLocation(BoardConstants.PROMPT_RESPONSE_ROW + i, 1));
                System.out.print(EscapeSequences.ERASE_LINE);
            }

            // move to response row
            System.out.print(EscapeSequences.moveCursorToLocation(BoardConstants.PROMPT_RESPONSE_ROW, 1));

            // print the response
            System.out.print(s);

            resetEscapeSequences();
        }

        private synchronized
        void handleColumnLabels(int row, int col) {
            if (col == 0 || col == 9) {
                printToChessBoard(
                        "   ",
                        row,
                        col,
                        EscapeSequences.SET_BG_COLOR_DARK_GREEN,
                        EscapeSequences.SET_TEXT_COLOR_YELLOW
                );
            } else {
                printToChessBoard(
                        switch ((teamColor == WHITE) ? col : BoardConstants.CHESS_BOARD_INNER_WIDTH + 1 - col) {
                            case 1 -> " a ";
                            case 2 -> " b ";
                            case 3 -> " c ";
                            case 4 -> " d ";
                            case 5 -> " e ";
                            case 6 -> " f ";
                            case 7 -> " g ";
                            case 8 -> " h ";
                            default -> throw new IllegalStateException("Unexpected value: " + col);
                        },
                        row,
                        col,
                        EscapeSequences.SET_TEXT_COLOR_YELLOW,
                        EscapeSequences.SET_BG_COLOR_DARK_GREEN
                );
            }
        }

        private
        boolean isWhiteSquare(int row, int col) {
            return (row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1);
        }
    }

    public
    BoardPrinter getPrinter() {
        return printer;
    }


}
