package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class requires no implementation.  You may use it to create a Monte Carlo search tree, or 
 * you may get the work done using the MCNode class. 
 * 
 * @author Grant DeWaay
 *
 * @param <E,T>
 */
public class MCTree<E extends MCGame<E, T>, T>
{
	MCNode<E, T> root;
	int size;


	public MCTree(MCNode<E, T> root){
		this.root = root;
		size = 1;
	}

	public T GetBestMoveFromRoot(int iterations){
		if(root == null){
			return null;
		}
		root.generateActions();
		for (int i = 0; i < iterations; i++){
			MCNode<E,T> selectedNode = select();
			if(selectedNode == null){continue;}
			int rolloutValue = selectedNode.rollout();
			backPropagate(selectedNode, rolloutValue);
		}
		return getHighestValueChildFromRoot().move;
	}

	private MCNode<E, T> select() {
		MCNode<E, T> selectedNode = root;
		while(!selectedNode.children.isEmpty()) {

			MCNode<E, T> tempNode = selectedNode.children.getFirst();
			double MAXubc1 = tempNode.UpperConfidenceBound();
			for(MCNode<E, T> child : selectedNode.children){
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
		selectedNode.generateActions();
		return selectedNode.children.isEmpty() ? null :selectedNode.children.getFirst();
	}

	private void backPropagate(MCNode<E,T> startingNode, int rolloutValue){
		MCNode<E, T> iteratedNode = startingNode;
		boolean parentReached = false;
		while(!parentReached){
			iteratedNode.total_score += rolloutValue;
			iteratedNode.num_visits++;
			if(iteratedNode.parent == null){
				parentReached = true;
			}
			else{
				iteratedNode = iteratedNode.parent;
			}
		}
	}

	private MCNode<E, T> getHighestValueChildFromRoot(){
		MCNode<E, T> highestScoringNode = null;
		for (MCNode<E, T> child : root.children) {
			if (highestScoringNode == null || child.total_score >= highestScoringNode.total_score) {
				highestScoringNode = child;
			}
		}
		return highestScoringNode;
	}
}
