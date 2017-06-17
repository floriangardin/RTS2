package plateau;




import java.util.HashMap;

import data.Attributs;
import model.Game;

public abstract class Bullet extends Objet {
	public Bullet(Plateau plateau) {
		super(plateau);
		// TODO Auto-generated constructor stub
	}

	public float damage;
	public float areaEffect;
	public int owner;
	public float size;
	public String soundLaunch;

	public void collision(Building c){

	}



}
