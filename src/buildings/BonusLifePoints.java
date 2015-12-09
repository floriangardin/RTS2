package buildings;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import com.sun.javafx.css.CssError.StylesheetParsingError;

import model.Map;
import model.Plateau;
import units.Character;
public class BonusLifePoints extends Bonus{




	public BonusLifePoints(Plateau p , float x , float y){
		this.initialize(p, x, y);
		this.image = this.p.g.images.bonusLifePoints;
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
		if(this.bonusPresent && c.getTeam()==this.getTeam() && c.lifePoints<c.maxLifePoints){
			c.setLifePoints(c.lifePoints+this.bonus);
			this.bonusPresent =false;
			this.state = 0f;
			this.sound.play(1f, this.p.g.options.soundVolume);
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}


}
