package ui.game;

import chess.ChessBoard;
import chess.ChessGame;
import ui.EscapeSequences;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import static chess.ChessGame.TeamColor.WHITE;

public
class GamePlayUI implements GameHandler {
    private ChessGame game;

    public static
    void main(String[] args) throws InterruptedException {
        System.out.print(EscapeSequences.ERASE_SCREEN);
        new BoardPrinter().printBoardBackground(WHITE);
        new BoardPrinter().printNotificationsBackground();

        String[] messages = {"poop boy has joined the game",
                             "goofus goofus has moved!",
                             "wackus is dead",
                             "don't forget to bring a towel",
                             "so many notifications",
                             "what will happen when there's more than 5?",
                             "I guess we'll find tou",
                             "this is fun haha",
                             "I like this",
                             "I honestly thought it would be harder"
        };

        for (var message : messages) {
            new BoardPrinter().printMessageToLog(message, "");
            Thread.sleep(new Random().nextInt(500, 2000));
        }


        System.out.print(EscapeSequences.moveCursorToLocation(12, 1));
    }

    public
    GamePlayUI() {}

    private static
    void printBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        board.resetBoard();


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
    static
    class BoardPrinter {
        private static final ArrayList<String> recentNotifications       = new ArrayList<>();
        private static       int               longestNotificationLength = 0;
        private static final ArrayList<String> recentNotificationTimes   = new ArrayList<>();

        private static
        void resetColorsAndWeight() {
            System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
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
        private
        void paintToBoard(String s, int row, int col, String... escapeSequences) {
            assert s.length() <= 3;
            // move the cursor to the specified position
            System.out.print(EscapeSequences.moveCursorToLocation(row + 2, ((col + 1) * 3) + 1));

            // print escape sequences
            for (var escapeSequence : escapeSequences) {
                System.out.print(escapeSequence);
            }

            // print s
            System.out.print(s);

            // reset background color, text color, and font weight
            resetColorsAndWeight();
        }

        private
        void printMessageToLog(String message, String... escapeSequences) {
            recentNotifications.add(message);
            recentNotificationTimes.add(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
            if (message.length() > longestNotificationLength) {longestNotificationLength = message.length();}
            if (recentNotifications.size() > 9) {
                // TODO: turn this and your specific screen coords into static final variables
                recentNotifications.removeFirst();
                recentNotificationTimes.removeFirst();
            }

            for (var escapeSequence : escapeSequences) {
                System.out.print(escapeSequence);
            }

            for (int i = 0; i < recentNotifications.size(); i++) {
                var notification = recentNotifications.get(i);
                System.out.print(EscapeSequences.moveCursorToLocation(i + 3, (13 * 3) + 1));

                // print notification
                System.out.printf("[%s] %s", recentNotificationTimes.get(i), notification);
                for (int j = 0; j < longestNotificationLength - notification.length(); j++) {
                    System.out.print(" ");
                }

            }

            resetColorsAndWeight();
        }

        public
        void printNotificationsBackground() {
            for (int row = 2; row <= 11; row++) {
                // move to appropriate location
                System.out.print(EscapeSequences.moveCursorToLocation(row, (13 * 3) - 2));

                // print character
                System.out.print("|");
            }

            // move to title location
            System.out.print(EscapeSequences.moveCursorToLocation(2, 13 * 3));

            // print "NOTIFICATIONS:"
            System.out.print(EscapeSequences.SET_TEXT_BOLD);
            System.out.print(EscapeSequences.SET_TEXT_UNDERLINE);

            System.out.print("NOTIFICATIONS:");

            // reset escape sequences
            System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
            System.out.print(EscapeSequences.RESET_TEXT_UNDERLINE);
        }

        public
        void printBoardBackground(ChessGame.TeamColor colorOnBottom) {
            int rowStart, colStart;
            int rowMax, colMax;
            int rowInc, colInc;
            if (colorOnBottom == WHITE) {
                rowStart = 9;
                rowMax = 0;
                rowInc = -1;

                colStart = 0;
                colMax = 9;
                colInc = +1;
            } else {
                rowStart = 0;
                rowMax = 9;
                rowInc = +1;

                colStart = 0;
                colMax = 0;
                colInc = -1;
            }

            for (int row = rowStart; row != rowMax + rowInc; row += rowInc) {
                for (int col = colStart; col != colMax + colInc; col += colInc) {
                    if (row == 0 || row == 9) {
                        handleColumnLabels(row, col);
                    } else if (col == 0 || col == 9) {
                        // handle row labels
                        paintToBoard(
                                String.format(" %d ", row),
                                row,
                                col,
                                EscapeSequences.SET_BG_COLOR_DARK_GREEN,
                                EscapeSequences.SET_TEXT_COLOR_YELLOW
                        );
                    } else {
                        if (isWhiteSquare(row, col)) {
                            // white
                            paintToBoard("   ", row, col, EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
                        } else {
                            // black
                            paintToBoard("   ", row, col, EscapeSequences.SET_BG_COLOR_DARK_BROWN);
                        }
                    }
                }
            }
        }

        private
        void handleColumnLabels(int row, int col) {
            if (col == 0 || col == 9) {
                paintToBoard(
                        "   ",
                        row,
                        col,
                        EscapeSequences.SET_BG_COLOR_DARK_GREEN,
                        EscapeSequences.SET_TEXT_COLOR_YELLOW
                );
            } else {
                paintToBoard(
                        switch (col) {
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

    @Override
    public
    void updateGame(ChessGame game) {
        printBoard(game.getBoard(), WHITE);
    }

    @Override
    public
    void printMessage(String message) {

    }
}
