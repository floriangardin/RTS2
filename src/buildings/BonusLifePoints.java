package buildings;
import data.Attributs;
import main.Main;
import model.Game;
import units.Character;
public class BonusLifePoints extends Bonus{




	public BonusLifePoints(float x , float y){
		this.name = "BonusLifepoints";
		this.initialize(x, y);
		this.image = Game.g.images.get("bonusLifePoints").getScaledCopy(Main.ratioSpace);
		this.bonus = 50f;

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
		if(this.bonusPresent && c.getTeam()==this.getTeam() && c.lifePoints<c.getAttribut(Attributs.maxLifepoints)){
			c.setLifePoints(c.lifePoints+this.bonus);
			this.bonusPresent =false;
			this.state = 0f;
			
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}


}
