package csc426.interp;

public class IntValue extends Value {

	protected int intValue;
	
	public IntValue(int n) {
		intValue = n;
	}
	
	public int intValue() {
		return intValue;
	}
	
	public String toString() {
		return Integer.toString(intValue);
	}
}
