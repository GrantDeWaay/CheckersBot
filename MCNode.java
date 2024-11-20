package edu.iastate.cs472.proj2;

import java.util.*;

/**
 * Node type for the Monte Carlo search tree.
 */
public class MCNode<E extends MCGame<E, T>, T>
{
    static final double C = 1.41421356;
    MCNode<E, T> parent;

  MCGame<E,T> state;
  List<MCNode<E, T>> children;
    int total_score;
    int num_visits;
  T move;
  int turn;

  MCNode(MCGame<E,T> state){
      this.state = state;
      children = new ArrayList<MCNode<E, T>>();
      total_score = 0;
      num_visits = 0;
      parent = null;
      move = null;
  }
    MCNode(MCGame<E,T> state, T move, MCNode<E, T> parent, int turn){
        this.state = state;
        children = new ArrayList<MCNode<E, T>>();
        total_score = 0;
        num_visits = 0;
        this.parent = parent;
        this.move = move;
        this.turn = turn;
    }

    public double UpperConfidenceBound(){
        if(num_visits == 0){
            return Double.POSITIVE_INFINITY;
        }
        return (double)total_score / (double)num_visits + C * Math.sqrt(Math.log(parent.num_visits)/num_visits);

    }

    public void generateActions(){
      T[] legalMoves = this.state.getLegalMoves(this.turn);
      if(legalMoves == null){return;}
        for(T move: legalMoves) {
            MCGame<E, T> temp = this.state.clone();
            temp.makeMove(move);
            MCNode<E, T> tempNode = new MCNode<E,T>(temp, move, this, this.turn == 0 ? 1 : 0);
            this.children.add(tempNode);
        }
    }

    public int rollout(){
        MCGame<E,T> iterativeBoard = this.state.clone();
        int turn = this.turn;
        ArrayList<T> legalMoves = new ArrayList<>(Optional.ofNullable(iterativeBoard.getLegalMoves(turn)).map(Arrays::asList).orElse(Collections.emptyList()));
        if(legalMoves.isEmpty()){
            return turn == 0 ? -1 : 1;
        }
        int index;
        Random rand = new Random();
        while(!legalMoves.isEmpty()) {
            index = rand.nextInt(legalMoves.size());
            iterativeBoard.makeMove(legalMoves.get(index));
            T[] moves = iterativeBoard.getLegalMoves(turn);
            if (moves != null) {
                legalMoves = new ArrayList<T>(Arrays.asList(moves));
            } else {
                legalMoves = new ArrayList<T>();
            }
            turn = turn == 1 ? 0 : 1;
        }
        int score = turn == 0 ? 1 : -1;
        return score;

    }
}

