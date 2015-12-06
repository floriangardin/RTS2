package multiplaying;

import java.util.HashMap;

import model.Game;
import model.Objet;

public class MultiReceiverConnexion extends MultiReceiver{

	public MultiReceiverConnexion(Game g) {
		super(g, g.portConnexion);
	}

	@Override
	public void action(String msg) {
		if(!this.g.host){
			this.g.addressHost = packet.getAddress();
		}
		//HashMap<String, String> map = Objet.preParse(msg.substring(1));
		this.g.receivedConnexion.add(msg);
	}

}
