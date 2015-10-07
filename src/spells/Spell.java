package spells;

import org.newdawn.slick.Image;

import model.ActionObjet;
import model.Objet;
import model.Plateau;
import model.Player;
import units.Character;;

public class Spell {

	public Image icon;
	public float chargeTime;
	public float range;
	public float damage;
	public ActionObjet owner;
	public String name;
	public Player player;
	public boolean needToClick;
	public Plateau p;

	public void cast(Objet target, Character launcher, int number){
		
	}
	
	public void launch(Objet target, Character launcher){}
	
	
}
