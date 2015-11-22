package model;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import units.Character;
public class BonusLifePoints extends Bonus{

	public float bonus=100f;
	public float state=0f;
	public float timeRegen = 100f;
	public boolean bonusPresent=false;
	
	public float animationStep  = 3f;
	

	public BonusLifePoints(Plateau p , float x , float y){
		this.p = p;
		p.bonus.addElement(this);
		this.size = 100f;
		this.collisionBox = new Circle(x,y,this.size);
		this.x = x;
		this.y = y;
		this.setXY(x, y);
		this.image = this.p.g.images.bonusLifePoints;
		

	}

	public void action(){
		this.state+=0.1f;
		if(!bonusPresent && this.state>timeRegen){
			this.bonusPresent =true;
		}
		else if(bonusPresent && this.state>this.animationStep){
			this.animation=(this.animation+1)%4;
		}
	}

	public void collision(Character c){
		c.setLifePoints(c.lifePoints+this.bonus);
		this.bonusPresent =false;
		this.state = 0f;
	}

	
	public Graphics draw(Graphics g){
		
		
		float r = ((Circle)collisionBox).radius;
		int imageWidth = this.image.getWidth()/5;
		float drawWidth = r*imageWidth/Math.min(imageWidth,this.image.getHeight());
		float drawHeight = r*this.image.getHeight()/Math.min(imageWidth,this.image.getHeight());
		float x1 = this.getX() - drawWidth;
		float y1 = this.getY() + drawWidth - 2*drawHeight;
		float x2 = this.getX() + drawWidth;
		float y2 = this.getY() + drawWidth;
		y1-=40f;
		y2-=40f;


		if(mouseHover){
			Color color = Color.darkGray;
			
			color = new Color(0,0,0,0.4f);
			Image i;
			if(!bonusPresent){
				i = this.image.getSubImage(0,0,imageWidth,this.image.getHeight());
			}
			else{
				i = this.image.getSubImage(imageWidth*(animation+1),0,imageWidth,this.image.getHeight());
			}

			i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));

			g.drawImage(i,x1,y1);
			i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
		}
		
		return g;
	}

}
