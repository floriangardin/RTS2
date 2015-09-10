package spells;

import org.newdawn.slick.Image;

import model.ActionObjet;
import model.Checkpoint;
import model.Objet;
import model.Utils;

public class Spell {

	public Image icon;
	public float chargeTime;
	public float state;
	public float range;
	public float damage;
	public ActionObjet owner;
	
	public void clicked(float x, float y){
		if(this.state==this.chargeTime && Utils.distance(new Checkpoint(x,y),this.owner)<this.range){
			this.launch(new Checkpoint(x,y));
			this.state = 0f;
		}
	}
	
	public void launch(Objet target){}
	
	
}
