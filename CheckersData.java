package edu.iastate.cs472.proj2;

import java.util.*;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */

public class CheckersData implements MCGame<CheckersData, CheckersMove> {

  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    public int[][] board;  // board[r][c] is the contents of row r, column c.


    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    private static int[][] deepCopyBoard(int[][] original) {
        if (original == null) {
            return null;
        }
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    @Override
    public CheckersData clone() {
        int[][] newBoard = deepCopyBoard(board);
        CheckersData clone = new CheckersData(newBoard);
        return clone;

    }

    CheckersData(int[][] board) {
        this.board = board;
    }

    public boolean isTerminal(){
        boolean blackFound = false;
        boolean redFound = false;
        for(int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if (board[row][col] == RED || board[row][col] == RED_KING) {
                    redFound = true;
                }
                if (board[row][col] == BLACK || board[row][col] == BLACK_KING) {
                    blackFound = true;
                }
                if(redFound && blackFound){
                    return false;
                }
            }
        }
        return true;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
        for(int col = 0; col < 8; col++) {
            for(int row = 0; row < 8; row++) {
                board[row][col] = EMPTY;
                if( (row % 2) == (col % 2)){
                    if(row <= 2){
                        board[row][col] = BLACK;
                    }
                    else if(row >= 5){
                        board[row][col] = RED;
                    }
                }
            }
        }
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        if(!isValidBoardSpace(row, col)) {
            return -1;
        }
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    public void makeMove(CheckersMove move) {
        int l = move.rows.size();
        for(int i = 0; i < l-1; i++)
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i+1), move.cols.get(i+1));
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        CheckersMove c = new CheckersMove(fromRow,fromCol,toRow,toCol);
        if (c.isJump()){
            board[fromRow + ((fromRow > toRow) ? -1: 1)][fromCol + ((fromCol > toCol) ? -1: 1)] = EMPTY;
        }
        board[toRow][toCol] = pieceAt(fromRow, fromCol);
        if(toRow == 0 && pieceAt(fromRow, fromCol) == RED){
            board[toRow][toCol] = RED_KING;
        }
        if(toRow == 7 && pieceAt(fromRow, fromCol) == BLACK){
            board[toRow][toCol] = BLACK_KING;
        }
        board[fromRow][fromCol] = EMPTY;
        // TODO
    	// 
    	// Update the board for the given move. You need to take care of the following situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        // 2. if this move is a jump, remove the captured piece
        // 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king
    }



    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    public CheckersMove[] getLegalMoves(int player) {
        int regularPiece, kingPiece, oppRegularPiece, oppKingPiece, direction;
        ArrayList<CheckersMove> legalMoves = new ArrayList<>();
        CheckersMove[] cur;
        for(int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cur = getLegalJumpsFrom(player, row, col);
                if (cur != null) {
                    legalMoves.addAll(Arrays.asList(cur));
                }
            }
        }
        if (legalMoves.isEmpty()) {
            return null;
        }
        List<CheckersMove> outputList = legalMoves.stream()
                .filter(CheckersMove::isJump)
                .toList();
        return !outputList.isEmpty() ? outputList.toArray(new CheckersMove[outputList.size()]) :legalMoves.toArray(new CheckersMove[legalMoves.size()]);
    }

    private boolean isValidBoardSpace(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Note that each CheckerMove may contain multiple jumps. 
     * Each move returned in the array represents a sequence of jumps 
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        int regularPiece, kingPiece, oppRegularPiece, oppKingPiece;
        int direction;
        if (player == 1) {
            regularPiece = RED;
            kingPiece = RED_KING;
            oppRegularPiece = BLACK;
            oppKingPiece = BLACK_KING;
            direction = -1;
        } else {
            regularPiece = BLACK;
            kingPiece = BLACK_KING;
            oppRegularPiece = RED;
            oppKingPiece = RED_KING;
            direction = 1;
        }
        boolean isKing;
        if (pieceAt(row, col) == regularPiece) {
            isKing = false;
        } else if (pieceAt(row, col) == kingPiece) {
            isKing = true;
        } else {
            return null;
        }
        CheckersMove firstPosition = new CheckersMove();
        firstPosition.addMove(row, col);
        CheckersMove[] array = RecursiveMoves(false, firstPosition, new HashSet<BoardSpace>(), player, isKing).toArray(new CheckersMove[0]);
        return array;
    }

    ArrayList<CheckersMove> RecursiveMoves(boolean jumpMode, CheckersMove curMove, HashSet<BoardSpace> taken, int player, boolean isKing) {
        int regularPiece, kingPiece, oppRegularPiece, oppKingPiece;
        int direction;
        if(player == 1){
            regularPiece = RED;
            kingPiece = RED_KING;
            oppRegularPiece = BLACK;
            oppKingPiece = BLACK_KING;
            direction = -1;
        }
        else {
            regularPiece = BLACK;
            kingPiece = BLACK_KING;
            oppRegularPiece = RED;
            oppKingPiece = RED_KING;
            direction = 1;
        }
        BoardSpace mostRecent = curMove.recentMove();
        int row = mostRecent.row;
        int col = mostRecent.col;
        ArrayList<CheckersMove> legalMoves = new ArrayList<>();
        if(!jumpMode) {
            if (isValidBoardSpace(row + direction, col - 1) && pieceAt(row + direction, col - 1) == EMPTY) {
                CheckersMove temp = curMove.clone();
                temp.addMove(row+direction, col-1);
                legalMoves.add(temp);

            }
            if (isValidBoardSpace(row + direction, col + 1) && pieceAt(row + direction, col + 1) == EMPTY) {
                CheckersMove temp = curMove.clone();
                temp.addMove(row+direction, col+1);
                legalMoves.add(temp);
            }
            if(isKing){
                if (isValidBoardSpace(row - direction, col - 1) && pieceAt(row - direction, col - 1) == EMPTY) {
                    CheckersMove temp = curMove.clone();
                    temp.addMove(row-direction, col-1);
                    legalMoves.add(temp);
                }
                if (isValidBoardSpace(row - direction, col + 1) && pieceAt(row - direction, col + 1) == EMPTY) {
                    CheckersMove temp = curMove.clone();
                    temp.addMove(row-direction, col+1);
                    legalMoves.add(temp);
                }
            }
        }
        if ((pieceAt(row+direction, col-1) == oppRegularPiece || pieceAt(row+direction, col-1) == oppKingPiece) && !taken.contains( new BoardSpace(row+direction,col-1))) {
            if(pieceAt(row+(2*direction), col-2) == EMPTY){
                CheckersMove temp = curMove.clone();
                HashSet<BoardSpace> tempTaken = new HashSet<BoardSpace>(taken);
                tempTaken.add( new BoardSpace(row+direction, col-1));
                temp.addMove(row+(2*direction), col-2);
                legalMoves.add(temp);
                legalMoves.addAll(RecursiveMoves(true, temp, tempTaken, player, isKing));
            }
        }

        if ((pieceAt(row+direction, col+1) == oppRegularPiece || pieceAt(row+direction, col+1) == oppKingPiece) && (!taken.contains( new BoardSpace(row+(direction), col+1)))) {
            if(((pieceAt(row+(2*direction), col+2) == EMPTY))){
                CheckersMove temp = curMove.clone();
                HashSet<BoardSpace> tempTaken = new HashSet<BoardSpace>(taken);
                tempTaken.add( new BoardSpace(row+direction, col+1));
                temp.addMove(row+(2*direction), col+2);
                legalMoves.add(temp);
                legalMoves.addAll(RecursiveMoves(true, temp, tempTaken, player, isKing));
            }
        }

        // backwards
        if(isKing){
            direction *= -1;
            if ((pieceAt(row+direction, col-1) == oppRegularPiece || pieceAt(row+direction, col-1) == oppKingPiece) && (!taken.contains( new BoardSpace(row+direction, col-1)))) {
            if(pieceAt(row+(2*direction), col-2) == EMPTY){
                CheckersMove temp = curMove.clone();
                HashSet<BoardSpace> tempTaken = new HashSet<BoardSpace>(taken);
                tempTaken.add( new BoardSpace(row+direction, col-1));
                temp.addMove(row+(2*direction), col-2);
                legalMoves.add(temp);
                legalMoves.addAll(RecursiveMoves(true, temp, tempTaken, player, isKing));
            }
        }

            if ((pieceAt(row+direction, col+1) == oppRegularPiece || pieceAt(row+direction, col+1) == oppKingPiece) && (!taken.contains( new BoardSpace(row+direction, col+1)))) {
                if((pieceAt(row+(2*direction), col+2) == EMPTY)){
                    CheckersMove temp = curMove.clone();
                    HashSet<BoardSpace> tempTaken = new HashSet<BoardSpace>(taken);
                    tempTaken.add( new BoardSpace(row+direction, col+1));
                    temp.addMove(row+(2*direction), col+2);
                    legalMoves.add(temp);
                    legalMoves.addAll(RecursiveMoves(true, temp, tempTaken, player, isKing));
                }
            }
        }
        return legalMoves;
    }
}
