package message.xmlLevel;

import java.nio.ByteBuffer;

// Message to communicate between Server and client 
public abstract class IOHandler {
	public final static int END = -99999;
	public final ByteBuffer mssgBox;
	
	public abstract int procResponse();

	public IOHandler(ByteBuffer request) {
		this.mssgBox = request;
	}

}
