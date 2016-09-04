package bullets;




import java.util.HashMap;

import data.Attributs;
import model.Building;
import model.Character;
import model.Game;
import model.Objet;

public abstract class Bullet extends Objet {
	public float damage;
	public float areaEffect;
	public Objet owner;
	public float size;
	public String soundLaunch;

	public void collision(Building c){

	}



}
