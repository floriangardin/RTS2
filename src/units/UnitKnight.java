package units;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import model.Data;
import model.GameTeam;
import model.Horse;
import model.Objet;
import model.Plateau;

public class UnitKnight extends Character {

	public UnitKnight(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "knight";
		this.type = UnitsList.Knight;
		this.unitType = KNIGHT;
		this.attackDuration = 2f;
		this.maxLifePoints = 90f*data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 400f*Main.ratioSpace;
		this.size = 40f*Main.ratioSpace;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.selectionBox = new Rectangle(-1.5f*this.image.getWidth()/5,-2.5f*this.image.getHeight()/4,3*this.image.getWidth()/5,3*this.image.getHeight()/4);
		this.maxVelocity = 160f*Main.ratioSpace*data.speedFactor;
		this.armor = 3f;
		this.damage = 8f*data.damageFactor;
		this.chargeTime = 4f;
		this.weapon = "sword";
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f*Main.ratioSpace;
		this.horse = new Horse(p,this);
		this.spells.add(data.immolation);
		this.spells.add(data.fence);
		this.animStep = 32f;
		this.explosionWhenImmolate = data.explosionWhenImmolate;
		if(this.getGameTeam().id==1){
			this.image = this.p.g.images.get("knightBlue");
			//this.animationAttack = this.p.g.images.get("attackSpearmanBlue;
		}
		else{
			this.image = this.p.g.images.get("knightRed");
			//this.animationAttack = this.p.g.images.get("attackSpearmanRed;
		}
		
	}
	
	public UnitKnight(UnitKnight unit, float x, float y,int id) {
		super(unit,x,y,id);
	}

	
	public void useWeapon(){
		if(! (this.target instanceof Character)){
			return ;
		}
		Character c = (Character) this.target;
		// Attack sound
		float damage = this.damage;
		if(this.p.g.sounds!=null)
			this.p.g.sounds.get(this.weapon).play(1f,this.p.g.options.soundVolume);
		if(c.weapon=="bow"){
			damage = damage*this.getGameTeam().data.bonusSwordBow;
		}
		if(c.armor<damage){
			c.setLifePoints(c.lifePoints+c.armor-damage);
		}
		// Reset the state
		this.state = 0f;
		this.isAttacking = false;
		c.isAttacked();
	}
	


	public void moveToward(Objet o){
		if(o==null && this.checkpointTarget==null){
			return;
		}
		float newvx, newvy;
		newvx = o.getX()-this.getX();
		newvy = o.getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float maxVNorm = this.maxVelocity/((float)this.getGameTeam().data.FRAMERATE);
		float vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);

		//Checking if the point is not too close of the target
		if((this.group.size()>1 && vNorm<maxVNorm) || vNorm<maxVNorm){
			// 1st possible call of stop: the target is near
			this.stop();
			return;
		}
		vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);
		if(vNorm>maxVNorm){
			//if the velocity is too large it is reduced to the maxVelocity value
			newvx = newvx*maxVNorm/vNorm;
			newvy = newvy*maxVNorm/vNorm;
		}
		vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);
		float newX,newY;
		newX = this.getX()+newvx;
		newY = this.getY()+newvy;
		//if the new coordinates are beyond the map's limits, it must be reassigned
		if(newX<this.collisionBox.getBoundingCircleRadius()){
			newX = this.collisionBox.getBoundingCircleRadius();
			newvx = Math.max(newvx,0f);
		}
		if(newY<this.collisionBox.getBoundingCircleRadius()){
			newY = this.collisionBox.getBoundingCircleRadius();
			newvy = Math.max(newvy, 0f);
		}
		if(newX>this.p.maxX-this.collisionBox.getBoundingCircleRadius()){
			newX = this.p.maxX-this.collisionBox.getBoundingCircleRadius();
			newvx = Math.min(0f, newvx);
		}
		if(newY>this.p.maxY-this.collisionBox.getBoundingCircleRadius()){
			newY = this.p.maxY-this.collisionBox.getBoundingCircleRadius();
			newvy = Math.min(0f, newvy);
		}

		//eventually we reassign the position and velocity variables
		this.setVXVY(newvx, newvy);

		this.setXY(newX, newY);



		this.animationValue+=this.animStep/(float)this.getGameTeam().data.FRAMERATE;
		if(this.animationValue>=4f){
			this.animationValue = 0f;
			this.animation = (this.animation+1)%5;
			if(this.animation == 0){
				this.animation = 1;
			}

		}

	}

	
	public void setVXVY(float vx, float vy){
		this.vx = vx;
		this.vy = vy;
		int sector = 0;
		if(vx==0 && vy==0){
			//Orientation toward target

			if(this.target!=null){
				vx =this.target.x-this.x;
				vy = this.target.y-this.y;
				if(vx>0f){
					if(vy>vx){
						sector = 2;
					} else if(vy<-vx){
						sector = 8;
					} else {
						sector = 4;
					}
				} else {
					if(vy>-vx){
						sector = 2;
					} else if(vy<vx){
						sector = 8;
					} else {
						sector = 6;
					}
				}
				this.orientation = sector;
			}
			return;
		}
		if(vx>0f){
			if(vy>vx){
				sector = 2;
			} else if(vy<-vx){
				sector = 8;
			} else {
				sector = 4;
			}
		} else {
			if(vy>-vx){
				sector = 2;
			} else if(vy<vx){
				sector = 8;
			} else {
				sector = 6;
			}
		}
		this.orientation = sector;
	}


	public Graphics draw(Graphics g){

		float r = collisionBox.getBoundingCircleRadius()*1.9f;
		float direction = 0f;
		//Adapted to spearman TODO : Genericity
		
		if(this.isImmolating){
			this.animation = 0;
			this.orientation = 2;
		}

		direction = (float)(orientation/2-1);
		int imageWidth = this.image.getWidth()/5;
		int imageHeight = this.image.getHeight()/4;
		float drawWidth = r*imageWidth/Math.min(imageWidth,imageHeight);
		float drawHeight = r*imageHeight/Math.min(imageWidth,imageHeight);
		float x1 = this.getX() - drawWidth;
		float y1 = this.getY() + drawWidth - 2*drawHeight;
		float x2 = this.getX() + drawWidth;
		float y2 = this.getY() + drawWidth;
		y1-=40f*Main.ratioSpace;
		y2-=40f*Main.ratioSpace;
		x1+=5f*Main.ratioSpace;
		x2+=5f*Main.ratioSpace;
		if(mouseOver){
			Color color = new Color(this.gameteam.color.getRed(),this.gameteam.color.getGreen(),this.gameteam.color.getBlue(),0.4f);

			Image i = this.image.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
			i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));

			g.drawImage(i,x1,y1);
			i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
			//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		}
		else{
			g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		}
		if(frozen>0f){
			Color color = Color.darkGray;
			color = new Color(100,150,255,0.4f);
			Image i = this.image.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
			i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));

			g.drawImage(i,x1,y1);
			i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
			//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		}
		else{
			g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		}
		// Drawing the health bar
		if(!isImmolating && this.lifePoints<this.maxLifePoints){
			drawLifePoints(g,r);

		}
		//Draw the immolation
		if(isImmolating){
			drawImmolation(g,r);
		}
		return g;
	
	}

}




