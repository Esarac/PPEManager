package exception;

public class AlreadyExistException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	//Constructor
	public AlreadyExistException(){
		super("An object with the same identifier all ready exist");
	}
	
	public AlreadyExistException(String message){
		super(message);
	}
	
}