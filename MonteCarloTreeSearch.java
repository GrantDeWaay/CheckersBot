package edu.iastate.cs472.proj2;

/**
 * 
 * @author Grant DeWaay
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch {


	/**
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * Each legalMove in the input now contains a single move
     * or a sequence of jumps: (rows[0], cols[0]) -> (rows[1], cols[1]) ->
     * (rows[2], cols[2]).
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        // The checker board state can be obtained from this.board,
        // which is an 2D array of the following integers defined below:
    	// 
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
        int iterations = 1000;
        MCNode<CheckersData,CheckersMove> rootNode = new MCNode<CheckersData, CheckersMove>(board);
        rootNode.turn=0;
        MCTree<CheckersData, CheckersMove> CheckersTree = new MCTree<>(rootNode);
        return CheckersTree.GetBestMoveFromRoot(iterations);
    }

    protected CheckersMove[] legalMoves() {

        return board.getLegalMoves(0);
    }

}
