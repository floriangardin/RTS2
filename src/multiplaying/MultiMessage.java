package multiplaying;

import java.net.InetAddress;

public class MultiMessage {

	public String message;
	public int port;
	public InetAddress address;
	public MultiMessage(String message, int port, InetAddress address) {
		this.message = message;
		this.port = port;
		this.address = address;
	}
	
}
