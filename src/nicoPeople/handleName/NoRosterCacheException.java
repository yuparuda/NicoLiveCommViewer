package nicoPeople.handleName;

public class NoRosterCacheException extends Exception {

	private static final long serialVersionUID = 1L;

	public final int errCode;

	public NoRosterCacheException(int code, String message) {
		super(message);
		this.errCode = code;
	}

}
