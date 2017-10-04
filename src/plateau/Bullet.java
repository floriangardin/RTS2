package plateau;




import java.util.HashMap;

import data.Attributs;
import events.EventHandler;
import events.EventNames;
import model.Game;

public abstract strictfp class Bullet extends Objet {
	public Bullet(Plateau plateau) {
		super(plateau);
		// TODO Auto-generated constructor stub
		
	}

	public float damage;
	public float areaEffect;
	public int owner;
	public float size;
	public String soundLaunch;

	public void collision(Objet c, Plateau plateau){

	}



}
