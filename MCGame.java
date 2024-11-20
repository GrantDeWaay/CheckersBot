package edu.iastate.cs472.proj2;

public interface MCGame<E, T> {
    T[] getLegalMoves(int player);
    void makeMove(T move);
    MCGame<E, T> clone();


}
