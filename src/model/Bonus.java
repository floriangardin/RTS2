package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import buildings.Building;

public abstract class Bonus  extends Building{

	public float bonus=5f;
	public Sound soundTaken;
	public float state=0f;
	public float timeRegen = 50f;
	public boolean bonusPresent=false;
	public float hitBoxSize;
	public Circle hitBox;

	public float animationStep  = 1f;

	public Graphics draw(Graphics g){

		int imageWidth = this.image.getWidth()/5;
		float r =((Circle) this.collisionBox).radius;
		Color color = Colors.team0;

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
				g.fill(new Rectangle(this.getX()-this.selectionBox.getWidth()/2,this.getY()-r-60f,this.selectionBox.getWidth(),6f));
				float x = this.state*this.selectionBox.getWidth()/this.timeRegen;
				g.setColor(new Color(0,250,0,0.8f));
				g.fill(new Rectangle(this.getX()-this.selectionBox.getWidth()/2,this.getY()-r-60f,x,6f));
			}
		}
		// Construction points
		if(this.constructionPoints<this.maxLifePoints && this.visibleByCurrentPlayer && this.constructionPoints>0){
			g.setColor(Color.white);
			g.fill(new Rectangle(this.getX()-r,this.getY()-r-50f,2*r,6f));
			float x = this.constructionPoints*2f*r/this.maxLifePoints;
			if(this.potentialTeam==1)
				g.setColor(Colors.team1);
			else
				g.setColor(Colors.team2);
			g.fill(new Rectangle(this.getX()-r,this.getY()-r-50f,x,6f));
		}
		return g;
	}

}
