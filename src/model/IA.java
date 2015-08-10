package model;

import java.util.Vector;

public class IA {
	
	Plateau p ;
	Player player;
	int team;
	Vector<Character> c;
	public IA(Plateau p,Player player){
		this.p = p;
		this.player=player;
		this.team = player.team;	
		this.c = new Vector<Character>();
	}
	
	public void action(){
		//TODO generate basic action for player ;
	}
	
	
	public void updateCharacters(){
		
	}
	

}
