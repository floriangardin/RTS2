package units;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import model.Data;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;

public class UnitSpearman extends Character {

	public UnitSpearman(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "spearman";
		this.type = UnitsList.Spearman;
		this.attackDuration = 1f;
		this.maxLifePoints = 80f*data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 150f;
		this.size = 40f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.selectionBox = new Rectangle(-1.5f*this.image.getWidth()/5,-2.5f*this.image.getHeight()/4,3*this.image.getWidth()/5,3*this.image.getHeight()/4);
		this.maxVelocity = 100f;
		this.armor = 4f;
		this.damage = 10f*data.damageFactor;
		this.chargeTime = 4f;
		this.weapon = "spear";
		this.animStep = 32f;
		this.soundSetTarget = this.p.g.sounds.orderSpearman;
		this.soundAttack = this.p.g.sounds.attackSpearman;
		this.soundDeath = this.p.g.sounds.death;

		if(this.getGameTeam().id==1){
			this.image = this.p.g.images.spearmanBlue;
			this.animationAttack = this.p.g.images.attackSpearmanBlue;
		}
		else{
			this.image = this.p.g.images.spearmanRed;
			this.animationAttack = this.p.g.images.attackSpearmanRed;
		}
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f;
		this.spells.add(data.immolation);
		//this.updateImage();
	}
	public UnitSpearman(UnitSpearman unit, float x, float y,int id) {
		super(unit,x,y,id);
	}

	public void useWeapon(){
		Character c = (Character) this.target;
		c.changes.lifePoints=true;
		// Attack sound
		float damage = this.damage;
		if(this.p.g.sounds!=null)
			this.p.g.sounds.getByName(this.weapon).play(1f,this.p.g.options.soundVolume);
		if(c.horse!=null)
			damage = damage*this.getGameTeam().data.bonusSpearHorse;

		if(c.armor<damage){
			c.setLifePoints(c.lifePoints+c.armor-damage);
		}
		// Reset the state
		this.state = 0f;
		this.isAttacking = false;
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


		this.changes.orientation = true;

	}


	public Graphics draw(Graphics g){


		float r = collisionBox.getBoundingCircleRadius()*1.5f;
		float direction = 0f;
		//TO draw
		Image toDraw;
		if(isAttacking){
			toDraw = this.animationAttack;
		}
		else{
			toDraw = this.image;
		}

		if(!this.isMobile()){
			this.animation = 0;
			this.animationValue= 0f;
		}

		if(this.isImmolating){
			this.animation = 0;
			this.orientation = 2;
		}

		direction = (float)(orientation/2-1);
		int imageWidth = toDraw.getWidth()/5;
		int imageHeight = toDraw.getHeight()/4;
		float drawWidth = r*imageWidth/Math.min(imageWidth,imageHeight);
		float drawHeight = r*imageHeight/Math.min(imageWidth,imageHeight);
		float x1 = this.getX() - drawWidth;
		float y1 = this.getY() + drawWidth - 2*drawHeight;
		float x2 = this.getX() + drawWidth;
		float y2 = this.getY() + drawWidth;
		y1-=40f;
		y2-=40f;


		if(mouseOver){
			Color color = Color.darkGray;
			if(this.getGameTeam().id==1){
				color = new Color(0,0,205,0.4f);
			}
			else{
				color = new Color(250,0,0,0.4f);
			}
			Image i;
			if(!isAttacking){
				i = toDraw.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
			}
			else{
				i = toDraw.getSubImage(imageWidth*((int)(5*this.attackState/this.attackDuration)),imageHeight*(int)direction,imageWidth,imageHeight);
			}

			i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));

			g.drawImage(i,x1,y1);
			i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
			//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		}
		else{
			if(!isAttacking){
				g.drawImage(toDraw,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
			}
			else{
				g.drawImage(toDraw,x1,y1,x2,y2,imageWidth*((int)(5*this.attackState/this.attackDuration)),imageHeight*direction,imageWidth*((int)(5*this.attackState/this.attackDuration))+imageWidth,imageHeight*direction+imageHeight);
			}
		}
		// Drawing the health bar
		if(!isImmolating && this.lifePoints<this.maxLifePoints){
			//Draw lifepoints
			g.setColor(new Color(250,0,0,0.8f));
			g.fill(new Rectangle(this.getX()-r/2,-46f+this.getY()-r,r,4f));
			float x = this.lifePoints*r/this.maxLifePoints;
			g.setColor(new Color(0,250,0,0.8f));
			g.fill(new Rectangle(this.getX()-r/2,-46f+this.getY()-r,x,4f));

		}
//		//Draw state
//		if(!isImmolating && this.state<this.chargeTime){
//			g.setColor(new Color(255,255,255,0.8f));
//			g.fill(new Rectangle(this.getX()-r/2,-50f+this.getY()-r,r,4f));
//			float x = this.state*r/this.chargeTime;
//			g.setColor(new Color(0,0,0,0.8f));
//			g.fill(new Rectangle(this.getX()-r/2,-50f+this.getY()-r,x,4f));
//		}
//
//		//Draw state
//		if(!isImmolating && this.attackState<this.attackDuration){
//			g.setColor(new Color(255,255,255,0.8f));
//			g.fill(new Rectangle(this.getX()-r/2,-46f+this.getY()-r,r,4f));
//			float x = this.attackState*r/this.attackDuration;
//			g.setColor(new Color(0,0,0,0.8f));
//			g.fill(new Rectangle(this.getX()-r/2,-46f+this.getY()-r,x,4f));
//		}

		//Draw the immolation
		if(isImmolating){
			Image fire = this.p.g.images.explosion;
			r = fire.getWidth()/5f;
			x = this.getX();
			y = this.getY();
			if(this.remainingTime>=65f){
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,0f,0f,r,r);
			}
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


}




