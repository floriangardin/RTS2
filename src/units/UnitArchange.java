package units;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import model.Data;
import model.GameTeam;
import model.Plateau;
import model.Player;


public class UnitArchange extends Character {

	public UnitArchange(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "archange";
		this.type = UnitsList.Archange;
		this.maxLifePoints = 200f*data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.selectionBox = new Rectangle(-1.5f*this.image.getWidth()/5,-2.5f*this.image.getHeight()/4,3*this.image.getWidth()/5,3*this.image.getHeight()/4);
		this.maxVelocity = 60f;
		this.armor = 5f;
		this.damage = 20f*data.damageFactor;
		this.chargeTime = 12f;
		this.weapon = "sword";
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f;

		this.spells.add(data.instantDeath);
		this.spells.add(data.instantHealth);
		if(this.getTeam()==1)
			this.image = this.p.g.images.archangeBlue;
		else
			this.image = this.p.g.images.archangeRed;
	}

	public UnitArchange(UnitArchange archange, float x, float y,int id) {
		super(archange,x,y,id);
	}

	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		float direction = 0f;
		direction = (float)(orientation/2-1);
		int imageWidth = this.image.getWidth()/4;
		int imageHeight = this.image.getHeight()/4;
		float drawWidth = r*imageWidth/Math.min(imageWidth,imageHeight);
		float drawHeight = r*imageHeight/Math.min(imageWidth,imageHeight);
		float x1 = this.getX() - drawWidth*2f;
		float y1 = this.getY() + (drawWidth - 2*drawHeight)*3f;
		float x2 = this.getX() + drawWidth*2f;
		float y2 = this.getY() + drawWidth;
		g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		// Drawing the health bar
		if(!isImmolating && this.lifePoints<this.maxLifePoints){
			g.setColor(Color.red);
			g.fill(new Rectangle(this.getX()-r,this.getY()-r,2*r,2f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.fill(new Rectangle(this.getX()-r,this.getY()-r,x,2f));
		}
		//Draw the immolation
		if(isImmolating){
			Image fire = this.p.g.images.explosion;
			r = fire.getWidth()/5f;
			x = this.getX();
			y = this.getY();
			if(this.remainingTime>=65f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,0f,0f,r,r);
			else if(this.remainingTime>=55f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,r,0f,2*r,r);
			else if(this.remainingTime>=45f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,2*r,0f,3*r,r);
			else if(this.remainingTime>=40f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=35f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,5*r,r);
			else if(this.remainingTime>=40f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=35f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,3*r,r);
			else if(this.remainingTime>=30f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=25f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,5*r,r);
			else if(this.remainingTime>=20f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=15f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,3*r,r);
			else if(this.remainingTime>=10f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=5f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,5*r,r);
			else 
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
		}
		return g;
	}
	public void drawIsSelected(Graphics g){
		g.setColor(Color.green);
		if(this.horse!=null){
			g.drawImage(this.selection_circle.getScaledCopy(this.size/20f),-22f+this.getX()-this.collisionBox.getBoundingCircleRadius()/2f,-8f+this.getY()-this.collisionBox.getBoundingCircleRadius()/2f);

		} else {
			g.drawImage(this.selection_circle.getScaledCopy(this.size/20f),-22f+this.getX()-this.collisionBox.getBoundingCircleRadius()/2f,-8f+this.getY()-this.collisionBox.getBoundingCircleRadius()/2f);
			//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));
		}
	}

	public void useWeapon(){
		Character c = (Character) this.target;
		c.changes.lifePoints=true;
		// Attack sound
		float damage = this.damage;
	
		if(this.p.g.sounds!=null)
			this.p.g.sounds.getByName(this.weapon).play(1f,this.p.g.options.soundVolume);

		if(c.armor<damage){
			c.setLifePoints(c.lifePoints+c.armor-damage);
		}
		// Reset the state
		this.state = 0f;
		this.isAttacking = false;
		
	}


}
