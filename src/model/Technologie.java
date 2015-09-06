package model;

import java.util.Vector;

public class Technologie {

	Technologies tech;
	Plateau p;
	Player player;
	Data data;
	boolean isDiscovered;
	Vector<Technologie> techRequired;
	
	public Technologie(Plateau p, Player player,Technologies tech){
		this.tech = tech;
		this.p = p;
		this.player = player;
		this.data = this.player.data;

	}
	
	
	public void applyEffect(){
		
		switch(this.tech){
		case Sight100:
			this.player.data.smSight*=2;
			this.player.data.bmSight*=2;
			this.player.data.ktSight*=2;
			this.player.data.wzSight*=2;
			this.player.data.prSight*=2;
			break;
		}
	}
}
