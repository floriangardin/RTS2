package model;

public class Technologie {

	Technologies tech;
	Plateau p;
	Player player;
	Data data;
	
	public Technologie(Plateau p, Player player,Technologies tech){
		this.tech = tech;
		this.p = p;
		this.player = player;
		this.data = this.player.data;
	}
	
}
