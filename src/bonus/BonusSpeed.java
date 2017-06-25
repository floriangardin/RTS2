package bonus;


import plateau.Character;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public class BonusSpeed extends Bonus{


	public BonusSpeed( float x , float y, Team team, Plateau plateau){
		super(ObjetsList.BonusSpeed,x,y, team, plateau);
		this.initialize( x, y, plateau);
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

	public void collision(Character c, Plateau plateau){
		
		if(this.bonusPresent && c.getTeam()==this.getTeam()){
			//c.maxVelocity +=this.bonus;
			this.bonusPresent =false;
			this.state = 0f;
			
			this.setTeam(0, plateau);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}




}
