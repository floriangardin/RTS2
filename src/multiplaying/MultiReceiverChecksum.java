package multiplaying;

import model.Game;

public class MultiReceiverChecksum extends MultiReceiver {

	public MultiReceiverChecksum(Game g) {
		super(g, g.portChecksum);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action(String msg) {
		this.g.mutexChecksum.lock();
		this.g.receivedChecksum.addElement(new Checksum(msg));
		this.g.mutexChecksum.unlock();
	}
	
	public static class Checksum {
		public int round;
		public String checksum;
		public Checksum(String checksum){
			String[] tab = checksum.split("\\|");
			this.round = Integer.parseInt(tab[0]);
			this.checksum = tab[1];
		}
		public boolean[] comparison(Checksum c){
			boolean[] tab = new boolean[2];
			tab[0] = (c.round==this.round);
			tab[1] = (!c.checksum.equals(this.checksum));
			return tab;
		}
	}
}
