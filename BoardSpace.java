package edu.iastate.cs472.proj2;
import java.util.Objects;

public class BoardSpace{
    int row;
    int col;

    BoardSpace(int row, int col){
        this.row = row;
        this.col = col;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Check if both are the same object
        if (obj == null || getClass() != obj.getClass()) return false;  // Check for null and class type
        return ((BoardSpace) obj).row == this.row && ((BoardSpace) obj).col == this.col;
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, col);  // Use the fields to compute hash
    }
}
