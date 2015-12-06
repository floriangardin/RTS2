package multiplaying;

import model.Game;

public class MultiReceiverResynchro extends MultiReceiver {

	public MultiReceiverResynchro(Game g) {
		super(g, g.portResynchro);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action(String msg) {
		System.out.println("Receive resynchro message");
		this.g.processSynchro = true;
		this.g.toParse= msg;
	}

}
