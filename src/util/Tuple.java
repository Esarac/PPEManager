package util;

public class Tuple <T1, T2>{

	private T1 val1;
	private T2 val2;
	
	public Tuple() {
	}
	
	public Tuple(T1 v1, T2 v2) {
		val1 = v1;
		val2 = v2;
	}

	public T1 getVal1() {
		return val1;
	}

	public void setVal1(T1 val1) {
		this.val1 = val1;
	}

	public T2 getVal2() {
		return val2;
	}

	public void setVal2(T2 val2) {
		this.val2 = val2;
	}
	
	public String toString() {
		
		return val1 + " and " + val2;
		
	}
	
}
