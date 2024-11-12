package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.List;

/**
 * Node type for the Monte Carlo search tree.
 */
public class MCNode<E>
{
    static final double C = 1.41421356;
    MCNode<E> parent;
  int total_score;
  int num_visits;
  E state;
  List<MCNode<E>> children;
  CheckersMove checkersMove;
  int turn;

  MCNode(E state){
      this.state = state;
      children = new ArrayList<MCNode<E>>();
      total_score = 0;
      num_visits = 0;
      parent = null;
      checkersMove = null;
  }

    public double UpperConfidenceBound(){
        if(num_visits == 0){
            return Double.POSITIVE_INFINITY;
        }
        return (double)total_score / (double)num_visits + C * Math.sqrt(Math.log(parent.num_visits)/num_visits);

    }
}

