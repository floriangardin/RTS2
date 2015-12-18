package multiplaying;

import java.net.InetAddress;

public class MultiMessage {

	public String message;
	public InetAddress address;
	
	/**
	 * 0 connexion
	 * 1 input
	 * 2 validation
	 * 3 resynchro
	 * 4 ping
	 * 5 checksum
	 * 6 chat
	 * 
	 */
	
	public MultiMessage(String message, InetAddress address) {
		this.message = message;
		this.address = address;
	}
	
}
