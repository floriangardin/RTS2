package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import units.Character;

public class BonusDamage extends Bonus{
	public float bonus=100f;
	public float state=0f;
	public float timeRegen = 50f;
	public boolean bonusPresent=false;

	public float animationStep  = 1f;


	public BonusDamage(Plateau p , float x , float y){
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
		this.image = this.p.g.images.bonusDamage;
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
		
		if(this.bonusPresent){
			c.damage +=5f;
			this.bonusPresent =false;
			this.state = 0f;
			this.sound.play(1f, this.p.g.options.soundVolume);
		}

	}

	public Graphics draw(Graphics g){

		int imageWidth = this.image.getWidth()/5;

		Color color = Color.darkGray;

		color = new Color(0,0,0,0.4f);
		Image i;
		if(!bonusPresent){
			i = this.image.getSubImage(0,0,imageWidth,this.image.getHeight());
		}
		else{
			i = this.image.getSubImage(imageWidth*(animation+1),0,imageWidth,this.image.getHeight());
		}

		//i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));

		g.drawImage(i,x-i.getWidth()/2,y-i.getHeight()/2);
		if(mouseOver){
			i.drawFlash(x-i.getWidth()/2, y-i.getHeight()/2,i.getWidth(),i.getHeight(),color);
		}



		return g;
	}

}
