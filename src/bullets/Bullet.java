package bullets;




import java.util.HashMap;

import data.Attributs;
import model.Game;
import plateau.Building;
import plateau.Character;
import plateau.Objet;

public abstract class Bullet extends Objet {
	public float damage;
	public float areaEffect;
	public int owner;
	public float size;
	public String soundLaunch;

	public void collision(Building c){

	}



}
