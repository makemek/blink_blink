package helper;

import java.util.ArrayList;

public class Pair <Left, Right> {
	private final Left left;
	private final Right right;
	
	public Pair(Left left, Right right)
	{
		this.left = left;
		this.right = right;
	}
	
	public Left getLeft() {return left;}
	public Right getRight() {return right;}
	
	public int hashCode() { return left.hashCode() ^ right.hashCode(); }
	
	public boolean equals(Object o) {
	    if (o == null) return false;
	    if (!(o instanceof Pair)) return false;
	    Pair pairo = (Pair) o;
	    return this.left.equals(pairo.getLeft()) &&
	           this.right.equals(pairo.getRight());
	}
}
