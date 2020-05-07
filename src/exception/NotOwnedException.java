package exception;

public class NotOwnedException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public NotOwnedException(){
		super("You do not owned this item");
	}
	
	public NotOwnedException(String message){
		super(message);
	}
	
}
