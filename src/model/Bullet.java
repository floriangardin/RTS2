package model;




import multiplaying.OutputModel.OutputBullet;

public abstract class Bullet extends ActionObjet {
	protected float damage;
	float areaEffect;
	Character owner;
	int id;

		
	public void change(OutputBullet ocb){
		this.x = ocb.x;
		this.y = ocb.y;
	}
	
	
	public void collision(Building c){
		
	}

}
