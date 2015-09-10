package technologies;

import java.util.Vector;

import model.Data;
import model.Plateau;
import model.Player;

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

			break;
		}
	}
}