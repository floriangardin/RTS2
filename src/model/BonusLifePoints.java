package model;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import units.Character;
public class BonusLifePoints extends Bonus{




	public BonusLifePoints(Plateau p , float x , float y){
		this.p = p;
		this.lifePoints = 10f;
		p.bonus.addElement(this);
		this.sight = 200f;
		this.size = 30f;
		this.collisionBox = new Circle(x,y,this.size);
		this.selectionBox = new Circle(x,y,this.size+50f);
		this.x = x;
		this.y = y;
		this.setXY(x, y);
		this.image = this.p.g.images.bonusLifePoints;
		this.bonus = 50f;
		this.sound = this.p.g.sounds.bonus;

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
		
		if(this.bonusPresent && c.lifePoints<c.maxLifePoints){
			c.setLifePoints(c.lifePoints+this.bonus);
			this.bonusPresent =false;
			this.state = 0f;
			this.sound.play(1f, this.p.g.options.soundVolume);
		}

	}


}
