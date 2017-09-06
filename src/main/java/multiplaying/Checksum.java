package multiplaying;

import java.io.Serializable;

import plateau.Plateau;

public class Checksum implements Serializable {
	public int round;
	public String checksum;
	
	
	public Checksum(){};
	public Checksum(String checksum){
		String[] tab = checksum.split("\\|");
		this.round = Integer.parseInt(tab[0]);
		this.checksum = tab[1];
	}
	public Checksum(Plateau plateau){
		this.round = plateau.round;
		this.checksum = plateau.toString();
	}
	public boolean[] comparison(Checksum c){
		boolean[] tab = new boolean[2];
		tab[0] = (c.round==this.round);
		tab[1] = (!c.checksum.equals(this.checksum));
		if(tab[0] && tab[1]){
			System.out.println("Desynchro at round : "+c.round);
			System.out.println(c.checksum);
			System.out.println(this.checksum);
			System.out.println("-------------");
			
		}
		return tab;
	}
	public boolean equals(Checksum c){
		// Checksums that haven't the same round haven't to be compared so return true in this case
		return c==this || c.round!=this.round || c.checksum.equals(this.checksum);
	}
	
	public String toString(){
		return this.checksum;
	}
}
