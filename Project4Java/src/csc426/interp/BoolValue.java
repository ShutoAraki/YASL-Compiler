package csc426.interp;

public class BoolValue extends Value {

	protected boolean boolValue;
	
	public BoolValue(boolean b) {
		boolValue = b;
	}
	
	public boolean boolValue() {
		return boolValue;
	}
	
	public String toString() {
		return Boolean.toString(boolValue);
	}
}
