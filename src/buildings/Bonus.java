package buildings;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import events.Events;
import main.Main;
import model.Colors;
import model.Game;
import model.Plateau;
import ressources.Map;

public abstract class Bonus extends Building{

	public float bonus=5f;
	public String soundTaken;
	public float state=0f;
	public float timeRegen = 50f;
	public boolean bonusPresent=false;
	public float hitBoxSize;
	public Circle hitBox;
	public Image image;

	public float animationStep  = 1f;

	public void initialize(Plateau p , float x , float y){
		this.lifePoints = 10f;
		this.maxLifePoints = 20f;
		this.lifePoints = 1f;
		this.constructionPoints=0f;
		this.setTeam(0);
		p.bonus.addElement(this);
		this.sight = 200f*Main.ratioSpace;
		this.size = 100f*Main.ratioSpace;
		this.collisionBox = new Circle(x*Main.ratioSpace,y*Main.ratioSpace,this.size);
		this.selectionBox = new Rectangle(x*Main.ratioSpace,y*Main.ratioSpace,collisionBox.getWidth(),collisionBox.getHeight());
		this.hitBoxSize = 30f*Main.ratioSpace;
		this.hitBox = new Circle(x*Main.ratioSpace,y*Main.ratioSpace,this.hitBoxSize);
		this.setXY(x*Map.stepGrid, y*Map.stepGrid);
		this.soundTaken = "bonusTaken";
		Game.g.events.addEvent(Events.BonusTaken, this);
	}
	
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
//			System.out.println("Bonus taking");
//			System.out.println(size+ " "+this.getX()+" "+this.getY() );
			g.setColor(new Color(0,0,0));
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,360);
			g.fill(new Rectangle(-1f+this.getX()-size/4,-1f+this.getY()-3*this.size/4,size/2+2f,12f));
			float x = this.constructionPoints/this.maxLifePoints;
			if(this.potentialTeam==1)
				g.setColor(Colors.team1);
			else if(this.potentialTeam==2)
				g.setColor(Colors.team2);
			else if(this.potentialTeam==0){
				g.setColor(Colors.team0);
			}
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,x*360);
			g.fill(new Rectangle(this.getX()-size/4,this.getY()-3*this.size/4,x*size/2,10f));
		}
		return g;
	}

}
