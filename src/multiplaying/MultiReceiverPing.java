package multiplaying;

import model.Game;

public class MultiReceiverPing extends MultiReceiver{

	public MultiReceiverPing(Game g) {
		super(g, g.portPing);
	}

	@Override
	public void action(String msg) {
		System.out.println("Ping !");
		String[] valMessage = msg.split("\\|");
		int id = Integer.parseInt(valMessage[1]);
		if(g.host){
			g.pingSender.changeAddress(g.getPlayerById(id).address);
			g.toSendPing.add(msg);
		} else {
			long time =Long.parseLong(valMessage[0]);
			this.g.clock.updatePing(time);
		}
	}

}
