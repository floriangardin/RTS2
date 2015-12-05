package model;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import units.Character;

public class BonusDamage extends Bonus{



	public BonusDamage(Plateau p , float x , float y){
		this.p = p;
		this.g = p.g;
		this.lifePoints = 10f;
		this.maxLifePoints = 20f;
		this.lifePoints = 1f;
		this.constructionPoints=0f;
		this.setTeam(0);
		p.bonus.addElement(this);
		this.sight = 200f;
		this.size = 100f;
		this.collisionBox = new Circle(x,y,this.size);
		this.selectionBox = this.collisionBox;
		this.hitBoxSize = 30f;
		this.hitBox = new Circle(x,y,this.hitBoxSize);
		this.x = x;
		this.y = y;
		this.setXY(x, y);
		this.image = this.p.g.images.bonusDamage;
		this.sound = this.p.g.sounds.bonus;

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
			c.damage +=5f;
			this.bonusPresent =false;
			this.state = 0f;
			this.sound.play(1f, this.p.g.options.soundVolume);
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}

}
