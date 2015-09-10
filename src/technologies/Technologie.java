package technologies;

import java.util.Vector;

import model.Data;
import model.Plateau;
import model.Player;

public abstract class Technologie {

	Technologies tech;
	Plateau p;
	Player player;
	Data data;
	boolean isDiscovered;
	Vector<Technologie> techRequired;
	

	
	public void applyEffect(){
		
		switch(this.tech){
		case Sight100:

			break;
		}
	}
}
