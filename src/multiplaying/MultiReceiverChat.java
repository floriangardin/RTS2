package multiplaying;

import model.Game;

public class MultiReceiverChat extends MultiReceiver {

	public MultiReceiverChat(Game g) {
		super(g, g.portChat);
	}

	@Override
	public void action(String msg) {
		// TODO Auto-generated method stub
		
	}

}
