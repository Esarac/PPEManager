package exception;

public class NotRemovableException  extends Exception{

	private static final long serialVersionUID = 1L;

	public NotRemovableException(){
		super("This item can not be removed");
	}
	
	public NotRemovableException(String message){
		super(message);
	}
	
}
