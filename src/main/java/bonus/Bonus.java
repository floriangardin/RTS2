package bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import data.Attributs;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Colors;
import plateau.Building;
import plateau.Plateau;
import plateau.Team;
import ressources.Images;
import ressources.Map;
import utils.ObjetsList;

public abstract class Bonus extends Building{

	public Bonus(ObjetsList name, int i, int j,Team team, Plateau plateau) {
		super(name, i, j, team, plateau);
		// TODO Auto-generated constructor stub
	}

	public float bonus=5f;
	public String soundTaken;
	public float state=0f;
	public float timeRegen = 50f;
	public boolean bonusPresent=false;
	public float hitBoxSize;
	public Circle hitBox;

	public float animationStep  = 1f;

	public void initialize(Plateau p , float x , float y){
		this.lifePoints = 1f;
		this.constructionPoints=0f;
		this.setTeam(0, p);
		this.collisionBox = new Circle(x*Main.ratioSpace,y*Main.ratioSpace,this.getAttribut(Attributs.size));
		this.selectionBox = new Rectangle(x*Main.ratioSpace,y*Main.ratioSpace,collisionBox.getWidth(),collisionBox.getHeight());
		this.hitBoxSize = 30f*Main.ratioSpace;
		this.hitBox = new Circle(x*Main.ratioSpace,y*Main.ratioSpace,this.hitBoxSize);
		this.setXY(x*Map.stepGrid, y*Map.stepGrid, p);
		this.soundTaken = "bonusTaken";
		//EventHandler.addEvent(EventNames.BonusTaken, this);
	}
	
	public Graphics draw(Graphics g){
		Image im = Images.get(this.name.name()).getScaledCopy(Main.ratioSpace);
		int imageWidth = im.getWidth()/5;
		float r =this.getAttribut(Attributs.size);
		Color color = Colors.team0;
		color = new Color(0,0,0,0.4f);
		Image i;
		if(!bonusPresent){
			i = im.getSubImage(0,0,imageWidth,im.getHeight());
		}
		else{
			i = im.getSubImage((int) (imageWidth*(animation+1)),0,imageWidth,im.getHeight());
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
		if(this.constructionPoints<this.getAttribut(Attributs.maxLifepoints)  && this.constructionPoints>0){
//			System.out.println("Bonus taking");
//			System.out.println(size+ " "+this.getX()+" "+this.getY() );
			g.setColor(new Color(0,0,0));
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,360);
			g.fill(new Rectangle(-1f+this.getX()-getAttribut(Attributs.size)/4,-1f+this.getY()-3*this.getAttribut(Attributs.size)/4,getAttribut(Attributs.size)/2+2f,12f));
			float x = this.constructionPoints/this.getAttribut(Attributs.maxLifepoints);
			if(this.potentialTeam==1)
				g.setColor(Colors.team1);
			else if(this.potentialTeam==2)
				g.setColor(Colors.team2);
			else if(this.potentialTeam==0){
				g.setColor(Colors.team0);
			}
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,x*360);
			g.fill(new Rectangle(this.getX()-getAttribut(Attributs.size)/4,this.getY()-3*this.getAttribut(Attributs.size)/4,x*getAttribut(Attributs.size)/2,10f));
		}
		return g;
	}

}
