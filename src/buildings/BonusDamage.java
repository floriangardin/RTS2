package buildings;

import main.Main;
import model.Plateau;


import units.Character;

public class BonusDamage extends Bonus{

	
	public BonusDamage(Plateau p , float x , float y){
		this.initialize(p, x, y);
		this.bonus = 5f;
		this.image = this.p.g.images.get("bonusDamage").getScaledCopy(Main.ratioSpace);

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
			c.damage += this.bonus;
			this.bonusPresent =false;
			this.state = 0f;
			this.sound.play(1f, this.p.g.options.soundVolume);
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}

}
