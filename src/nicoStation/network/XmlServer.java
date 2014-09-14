package nicoStation.network;

import java.net.InetSocketAddress;

public class XmlServer {
	public final InetSocketAddress address;
	private final String thread;
	
	// bad code, depend on OS  -> \"
	public String reqToStart() {
		return "<thread thread=\"" + this.thread
				+ "\" version=\"20061206\" res_from=\"-0\" scores=\"1\" />\0";
	}
	
	public XmlServer(InetSocketAddress addr, String thread) {
		this.address = addr;
		this.thread = thread;
	}
}
