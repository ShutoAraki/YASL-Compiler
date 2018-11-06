package csc426.interp;

public class VoidValue extends Value {

	public int intValue() {
		System.err.println("Void does not contain any information");
		System.exit(0);
		return 0;
	}

	public boolean boolValue() {
		System.err.println("Void does not contain any information");
		System.exit(0);
		return false;
	}

}
