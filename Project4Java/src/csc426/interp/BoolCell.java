package csc426.interp;

public class BoolCell extends BoolValue {
	
	public BoolCell(boolean b) {
		super(b);
	}
	
	public void set(boolean x) {
		boolValue = x;
	}
	
	public String toString() {
		return Boolean.toString(boolValue);
	}

}
