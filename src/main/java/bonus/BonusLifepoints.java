package bonus;
import data.Attributs;
import plateau.Character;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;
public strictfp class BonusLifepoints extends Bonus{




	public BonusLifepoints(int x , int y, Team team, Plateau plateau){
		super(ObjetsList.BonusLifepoints,x,y, team, plateau);
		this.name = ObjetsList.BonusLifepoints;
//		this.initialize(x, y, plateau);
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

	public void collision(Character c, Plateau plateau){
		if(this.bonusPresent && c.getTeam()==this.getTeam() && c.lifePoints<c.getAttribut(Attributs.maxLifepoints)){
			c.setLifePoints(c.lifePoints+this.bonus, plateau);
			this.bonusPresent =false;
			this.state = 0f;
			this.setTeam(0, plateau);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}


}
