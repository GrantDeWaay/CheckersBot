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
        int iterations = 400;
        int rootTurn;
        if (legalMoves == null || legalMoves.length == 0) {
            return null;
        }

        MCNode<CheckersData> root = new MCNode<CheckersData>(board);
        root.turn = 0;
        root.children = new ArrayList<MCNode<CheckersData>>();
        generateActions(root, legalMoves);
        for(int i = 0; i < iterations; i++) {
            MCNode<CheckersData> selectedNode = select(root);
            if (selectedNode == null) {
                return null; // todo: ??
            }
            System.out.println("Iteration: "+ i );
            rollout(selectedNode);
        }
        //PICKING HIGHEST SCORE NODE TO RETURN

        MCNode<CheckersData> highestScoringNode = new MCNode<CheckersData>(new CheckersData());
        highestScoringNode.total_score = Integer.MIN_VALUE;
        for(MCNode<CheckersData> child : root.children){
            if(child.total_score >= highestScoringNode.total_score) {
                highestScoringNode = child;

            }
        }
        System.out.println(board);
        System.out.println();
        return highestScoringNode.checkersMove;
    }

    public MCNode<CheckersData> select(MCNode<CheckersData> root) {
        MCNode<CheckersData> selectedNode = root;
        while(!selectedNode.children.isEmpty()) {

            MCNode<CheckersData> tempNode = selectedNode.children.get(0);
            double MAXubc1 = tempNode.UpperConfidenceBound();
            for(MCNode<CheckersData> child : selectedNode.children){
                if(child.UpperConfidenceBound()> MAXubc1){
                    MAXubc1 = child.UpperConfidenceBound();
                    tempNode = child;
                }
            }
            selectedNode = tempNode;
        }
        if(selectedNode.num_visits == 0) {
            return selectedNode;
        }
        else{
            generateActions(selectedNode);
        }
        if(selectedNode.children.isEmpty()){
            return null;
        }
        return selectedNode.children.getFirst();
    }

    public void generateActions(MCNode<CheckersData> root){
        for(CheckersMove move: root.state.getLegalMoves(root.turn == 1 ? 0 : 1)) {
            CheckersData temp = new CheckersData(deepCopyBoard(board.board));
            temp.makeMove(move);
            MCNode<CheckersData> tempNode = new MCNode<CheckersData>(temp);
            tempNode.checkersMove=move;
            tempNode.parent = root;

            root.children.add(tempNode);
        }
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
    public void generateActions(MCNode<CheckersData> root,CheckersMove[] legalMoves){
        for(CheckersMove move: legalMoves) {
            CheckersData temp = new CheckersData(deepCopyBoard(board.board));
            temp.makeMove(move);
            MCNode<CheckersData> tempNode = new MCNode<CheckersData>(temp);
            tempNode.checkersMove=move;
            tempNode.parent = root;
            tempNode.turn = root.turn == 1 ? 0 : 1;

            root.children.add(tempNode);
        }
    }

    public void rollout(MCNode<CheckersData> root){
        CheckersData iterativeBoard = new CheckersData(deepCopyBoard(root.state.board));
        int turn = root.turn == 1 ? 0 : 1;
        ArrayList<CheckersMove> legalMoves;
        CheckersMove[] moves2 = iterativeBoard.getLegalMoves(turn);
        if (moves2 != null) {
            legalMoves = new ArrayList<CheckersMove>(Arrays.asList(moves2));
        } else {
            legalMoves = new ArrayList<CheckersMove>();
        }
        if(legalMoves.isEmpty()){
            return;
        }
        int index;
        int iterations = 0;
        Random rand = new Random();
        while(!legalMoves.isEmpty()) {
            iterations++;
            index = rand.nextInt(legalMoves.size());
            iterativeBoard.makeMove(legalMoves.get(index));
            CheckersMove[] moves = iterativeBoard.getLegalMoves(turn);
            if (moves != null) {
                legalMoves = new ArrayList<CheckersMove>(Arrays.asList(moves));
            } else {
                legalMoves = new ArrayList<CheckersMove>();
            }
            turn = turn == 1 ? 0 : 1;
        }
        int score;
        if(turn == 0){
            score = 1;
        }
        else{
            score = 0;
        }
        root.total_score = score;
        root.num_visits = root.num_visits + 1;
        MCNode<CheckersData> temp = root;
        while(temp.parent != null){
            temp.parent.num_visits = temp.parent.num_visits + 1;
            temp.parent.total_score = temp.parent.total_score + score;
            temp = temp.parent;
        }

    }
    // TODO
    // 
    // Implement your helper methods here. They include at least the methods for selection,  
    // expansion, simulation, and back-propagation. 
    // 
    // For representation of the search tree, you are suggested (but limited) to use a 
    // child-sibling tree already implemented in the two classes CSTree and CSNode (which  
    // you may feel free to modify).  If you decide not to use the child-sibling tree, simply 
    // remove these two classes. 
    //



}
