package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

public abstract class Bonus  extends ActionObjet{

	public float bonus=5f;
	public Sound soundTaken;
	public float state=0f;
	public float timeRegen = 50f;
	public boolean bonusPresent=false;

	public float animationStep  = 1f;

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
			g.setColor(new Color(250,0,0,0.8f));
			if(!this.bonusPresent){
				g.fill(new Rectangle(this.getX()-this.selectionBox.getWidth()/2,-34f+this.getY()-this.selectionBox.getWidth(),this.selectionBox.getWidth(),4f));
				float x = this.state*this.selectionBox.getWidth()/this.timeRegen;
				g.setColor(new Color(0,250,0,0.8f));
				g.fill(new Rectangle(this.getX()-this.selectionBox.getWidth()/2,-34f+this.getY()-this.selectionBox.getWidth(),x,4f));
			}
		}
		return g;
	}

}
