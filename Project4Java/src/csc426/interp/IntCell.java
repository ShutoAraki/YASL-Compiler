package csc426.interp;

public class IntCell extends IntValue {
	
	private int intValue;

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

//public class IntCell extends Value {
//	
//	private int intValue;
//
//	public IntCell(int n) {
//		intValue = n;
//	}
//	
//	public int intValue() {
//		return intValue;
//	}
//	
//	public void set(int x) {
//		intValue = x;
//	}
//	
//	public String toString() {
//		return Integer.toString(intValue);
//	}
//
//}
