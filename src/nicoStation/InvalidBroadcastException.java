package nicoStation;

public class InvalidBroadcastException extends Exception {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final int errCode;

	public InvalidBroadcastException(int code, String message) {
		super(message);
		this.errCode = code;
	}

}
