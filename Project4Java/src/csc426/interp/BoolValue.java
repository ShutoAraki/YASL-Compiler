package csc426.interp;

public class BoolValue extends Value {

	protected boolean boolValue;
	
	public BoolValue(boolean b) {
		boolValue = b;
	}
	
	public boolean boolValue() {
		return boolValue;
	}

	public int intValue() {
		System.err.println("Cannot get an int value from boolean");
		System.exit(0);
		return 0;
	}
}
