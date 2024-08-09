package ui.game;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import ui.EscapeSequences;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public
class GamePlayUI implements GameHandler {
    public static
    void observe() {
        printBoard(WHITE);
        printBoard(BLACK);
    }

    public static
    void play() {
        printBoard(WHITE);
        printBoard(BLACK);
    }

    private static
    void printBoard(ChessGame.TeamColor perspective) {
        var board = new ChessBoard();
        board.resetBoard();

        BoardPrinter.printBoard(board, perspective);

    }


    static
    class BoardPrinter {
        static ChessGame.TeamColor nextSquareBackground = WHITE;

        static
        void printBoard(ChessBoard board, int row, int col, int endCol) {
            if (row == 0 || row == 9) {
                if (col == 0 || col == 9) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
                    System.out.print("   ");
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                    System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
                    System.out.print(switch (col) {
                        case 1 -> " a ";
                        case 2 -> " b ";
                        case 3 -> " c ";
                        case 4 -> " d ";
                        case 5 -> " e ";
                        case 6 -> " f ";
                        case 7 -> " g ";
                        case 8 -> " h ";
                        default -> throw new IllegalStateException("Unexpected value: " + col);
                    });
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                    System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                }
            } else if (col == 0 || col == 9) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
                System.out.print(" " + row + " ");
                System.out.print(EscapeSequences.RESET_BG_COLOR);
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            } else {
                if (nextSquareBackground == WHITE) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_BROWN);
                }
                if (col != endCol) {
                    nextSquareBackground = nextSquareBackground == WHITE ? BLACK : WHITE;
                }

                var pieceAtLocation = board.getPiece(new ChessPosition(row, col));
                if (pieceAtLocation == null) {
                    System.out.print("   ");
                } else {
                    if (pieceAtLocation.getTeamColor() == WHITE) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                    }
                    System.out.print(EscapeSequences.SET_TEXT_BOLD);
                    System.out.print(switch (pieceAtLocation.getPieceType()) {
                        case KING -> " K ";
                        case QUEEN -> " Q ";
                        case BISHOP -> " B ";
                        case KNIGHT -> " N ";
                        case ROOK -> " R ";
                        case PAWN -> " P ";
                    });
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                    System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                }
            }
        }

        static
        void printBoard(ChessBoard board, ChessGame.TeamColor colorOnBottom) {
            nextSquareBackground = WHITE;
            if (colorOnBottom == WHITE) {
                for (int row = 9; row >= 0; row--) {
                    for (int col = 0; col <= 9; col++) {
                        printBoard(board, row, col, 8);
                    }
                    System.out.println();
                }
            } else {
                for (int row = 0; row <= 9; row++) {
                    for (int col = 9; col >= 0; col--) {
                        printBoard(board, row, col, 1);
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    @Override
    public
    void updateGame(ChessGame game) {

    }

    @Override
    public
    void printMessage(String message) {

    }
}
