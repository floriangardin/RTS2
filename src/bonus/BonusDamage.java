package bonus;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import main.Main;
import plateau.Character;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public class BonusDamage extends Bonus{

	public BonusDamage(int x, int y,Team team, Plateau plateau) {
		super(ObjetsList.BonusDamage, x, y,team, plateau);
		// TODO Auto-generated constructor stub
//		this.initialize(x, y, plateau);
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

	public void collision(Character c, Plateau plateau){
		if(this.bonusPresent && c.getTeam()==this.getTeam()){
			c.attributsChanges.add(new AttributsChange(Attributs.damage, Change.ADD, 3f, 0f));
			this.bonusPresent =false;
			this.state = 0f;
			
			this.setTeam(0, plateau);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}

}
