package technologies;

import java.util.Vector;

import org.newdawn.slick.Image;

import model.Data;
import model.Plateau;
import model.Player;

public abstract class Technologie {

	public Technologies tech;
	public Plateau p;
	public Player player;
	public Data data;
	public boolean isDiscovered;
	public Technologie techRequired;
	public Image icon;
	public String name;
	
	public void applyEffect(){
		

	}
}
