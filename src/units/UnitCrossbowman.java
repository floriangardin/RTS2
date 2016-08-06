package units;

import bullets.Arrow;
import data.Attributs;
import model.Objet;
import utils.UnitsList;


public class UnitCrossbowman extends Character {

	int bonusArrow = 0;
	
	public UnitCrossbowman(float x, float y, int team) {
		super(x,y,UnitsList.Crossbowman, team);
	}

	public void action(){
		if(this.mode==TAKE_BUILDING){
			this.mode = NORMAL;
		}
		mainAction();
	}
	
	public void useWeapon(){
		if(! (this.target instanceof Character)){
			return ;
		}
		new Arrow(this,this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.getAttribut(Attributs.damage),-1);
		this.state = 0f;
		this.isAttacking = false;
		
	}
	
	public void useWeapon(Objet target){
		new Arrow(this,target.getX()-this.getX(),target.getY()-this.getY(),this.getAttribut(Attributs.damage)+bonusArrow,-1);
		this.state = 0f;
	}
	
	

}
