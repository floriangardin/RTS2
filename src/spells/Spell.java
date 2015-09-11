package spells;

import org.newdawn.slick.Image;

import model.ActionObjet;
import model.Checkpoint;
import model.Objet;
import model.Player;
import model.Utils;
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
	

	
	public void launch(Objet target, Character launcher){}
	
	
}
