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
		return tab;
	}
	public boolean equals(Checksum c){
		// Checksums that haven't the same round haven't to be compared so return true in this case
		boolean equalities = c==this || c.round!=this.round || c.checksum.equals(this.checksum);
		if(!equalities){
			System.out.println("Desynchro at round : "+c.round);
			System.out.println(c.checksum);
			System.out.println(this.checksum);
			if(c.checksum.split("name").length!=this.checksum.split("name").length){
				System.out.println("Pas le meme nombre d'unites mon petit gillou");
				System.out.println("Number 1 : "+c.checksum.split("name").length);
				System.out.println("Number 2 : "+this.checksum.split("name").length);
			}
			System.out.println("-------------");
		}
		return equalities;
	}
	
	public String toString(){
		return this.checksum;
	}
}
