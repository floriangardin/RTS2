package bonus;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import main.Main;
import plateau.Character;
import utils.ObjetsList;

public class BonusDamage extends Bonus{

	
	public BonusDamage(float x , float y){
		
		super(ObjetsList.BonusDamage,x,y);
		this.name = ObjetsList.BonusDamage;
		this.initialize( x, y);
		this.bonus = 5f;

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
			c.attributsChanges.add(new AttributsChange(Attributs.damage, Change.ADD, 3f, 0f));
			this.bonusPresent =false;
			this.state = 0f;
			
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}

}
