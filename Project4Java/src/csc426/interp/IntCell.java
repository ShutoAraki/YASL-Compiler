package csc426.interp;

public class IntCell extends IntValue {
	
	public IntCell(int n) {
		super(n);
	}
	
	public void set(int x) {
		intValue = x;
	}
	
	public String toString() {
		return Integer.toString(intValue);
	}

}
