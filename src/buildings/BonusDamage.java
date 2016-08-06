package buildings;

import main.Main;
import model.Game;
import model.Plateau;
import units.Character;

public class BonusDamage extends Bonus{

	
	public BonusDamage(float x , float y){
		this.name = "BonusDamage";
		this.initialize( x, y);
		this.bonus = 5f;
		this.image = Game.g.images.get("bonusDamage").getScaledCopy(Main.ratioSpace);

	}

	public void action(){
		this.state+=0.1f*30/Main.framerate;
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
			//c.damage += this.bonus;
			this.bonusPresent =false;
			this.state = 0f;
			Game.g.playSound(this.soundTaken);
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}

}
