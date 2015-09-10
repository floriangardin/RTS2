package bullets;




import buildings.Building;
import model.ActionObjet;
import multiplaying.OutputModel.OutputBullet;
import units.Character;

public abstract class Bullet extends ActionObjet {
	public float damage;
	public float areaEffect;
	public Character owner;
	public int id;

		
	public void change(OutputBullet ocb){
		this.x = ocb.x;
		this.y = ocb.y;
	}
	
	
	public void collision(Building c){
		
	}

}
