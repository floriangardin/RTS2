package buildings;


import main.Main;
import model.Game;
import model.Plateau;
import units.Character;

public class BonusSpeed extends Bonus{


	public BonusSpeed( float x , float y){
		this.name = "BonusSpeed";
		this.initialize( x, y);
		this.bonus = 20f;
	}

	public void action(){
		this.state+=0.1f;
		if(!bonusPresent && this.state>timeRegen){
			this.bonusPresent =true;
			this.state= 0f;
		}
		else if(bonusPresent && this.state>this.animationStep){
			this.animation=(this.animation+1)%4;
			this.state= 0f;
		}
	}

	public void collision(Character c){
		
		if(this.bonusPresent && c.getTeam()==this.getTeam()){
			//c.maxVelocity +=this.bonus;
			this.bonusPresent =false;
			this.state = 0f;
			
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}




}
