package multiplaying;


public class Checksum {
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
