package units;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import bullets.Fireball;
import main.Main;
import model.Data;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;

public class UnitInquisitor extends Character {

	public static float radiusCollisionBox = 20f*Main.ratioSpace;
	
	public UnitInquisitor(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "inquisitor";
		this.printName = "Inquisiteur";
		this.type = UnitsList.Inquisitor;
		this.unitType = INQUISITOR;
		this.maxLifePoints = 60f*data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f*Main.ratioSpace;
		this.attackDuration = 2f;
		this.collisionBox = new Circle(0f,0f,radiusCollisionBox);
		this.selectionBox = new Rectangle(-1.5f*radiusCollisionBox,-2.5f*radiusCollisionBox,3*radiusCollisionBox,3*radiusCollisionBox);
		this.maxVelocity = 90f*Main.ratioSpace*data.speedFactor;
		this.armor = 0f;
		this.damage = 20f*data.damageFactor;
		this.chargeTime = 8f;
		this.weapon = "wand";
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = 200f*Main.ratioSpace;
		this.animStep = 24f;
		this.explosionWhenImmolate = data.explosionWhenImmolate;
		this.spells.add(data.immolation);
		this.spells.add(data.firewall);
		this.spells.add(data.blessedArea);
		
	}

	public UnitInquisitor(UnitInquisitor spearman, float x, float y,int id) {
		super(spearman,x,y,id);	
	}
	
	public void action(){
		if(this.mode==TAKE_BUILDING){
			this.mode = NORMAL;
		}
		mainAction();
	}
	
	
	public void useWeapon(){
		new Fireball(this.p,this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.damage,-1);
		this.state = 0f;
		this.isAttacking = false;
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

//	public Graphics draw(Graphics g){
//
//
//		float r = collisionBox.getBoundingCircleRadius()*1.8f;
//		float direction = 0f;
//
//
//		//Adapted to spearman TODO : Genericity
//		
//		if(this.isImmolating){
//			this.animation = 0;
//			this.orientation = 2;
//		}
//
//		direction = (float)(orientation/2-1);
//		int imageWidth = this.image.getWidth()/5;
//		int imageHeight = this.image.getHeight()/4;
//		float drawWidth = r*imageWidth/Math.min(imageWidth,imageHeight);
//		float drawHeight = r*imageHeight/Math.min(imageWidth,imageHeight);
//		float x1 = this.getX() - drawWidth;
//		float y1 = this.getY() + drawWidth - 2*drawHeight;
//		float x2 = this.getX() + drawWidth;
//		float y2 = this.getY() + drawWidth;
//		y1-=40f*Main.ratioSpace;
//		y2-=40f*Main.ratioSpace;
//		x1+=5f*Main.ratioSpace;
//		x2+=5f*Main.ratioSpace;
//		if(mouseOver && frozen<=0f){
//			Color color = new Color(this.gameteam.color.getRed(),this.gameteam.color.getGreen(),this.gameteam.color.getBlue(),0.4f);
//			Image i = this.image.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
//			i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));
//
//			g.drawImage(i,x1,y1);
//			i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
//			//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
//		}
//		else if(frozen<=0f){
//			g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
//		}else{
//		
//				Color color = Color.darkGray;
//				color = new Color(100,150,255,0.4f);
//				Image i = this.image.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
//				i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));
//
//				g.drawImage(i,x1,y1);
//				i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
//				//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
//		}
//
//
//		// Drawing the health bar
//		if(!isImmolating && this.lifePoints<this.maxLifePoints){
//			drawLifePoints(g,r);
//
//		}
//		//Draw the immolation
//		if(isImmolating){
//			drawImmolation(g,r);
//		}
//		return g;
//	}

}
