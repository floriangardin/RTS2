package bullets;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import buildings.Building;
import main.Main;
import model.Changes;
import model.Game;
import model.Plateau;
import units.Character;

public class Arrow extends CollisionBullet{

	protected float angle= 0f;
	public Image shadow;
	
	public Arrow(Plateau p,Character owner,float vx,float vy,float damage,int id){
		//MULTI 
		this.changes = new Changes();
		// Parameters
		this.size = 2f*Game.ratioSpace;
		float Vmax = 600f*Game.ratioSpace;

		this.p = p;
		if(id==-1){
			this.id = p.g.idChar;
			p.g.idChar++;
		}else{
			this.id=id;
		}
		this.name ="arrow";
		this.damage = damage;
		p.addBulletObjets(this);
		this.lifePoints = 1f;
		this.owner = owner;
		this.setTeam(owner.getTeam());
		this.collisionBox = new Circle(owner.getX(),owner.getY(),size);
		this.setXY(owner.getX(),owner.getY());
		this.vx = vx;
		this.vy = vy;
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*Main.framerate;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (Math.atan(this.vy/(this.vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.image = p.g.images.arrow.getScaledCopy(2f*Game.ratioSpace);
		this.image.rotate(this.angle);
		
		this.shadow = p.g.images.arrow.getScaledCopy(2f*Game.ratioSpace);
		this.shadow.rotate(this.angle);
	
		this.sound = p.g.sounds.arrow;
		this.sound.play(1f,this.p.g.options.soundVolume);
	}

	public void collision(Character c){
		if(c.getTeam()!=this.owner.getTeam()){
			// Attack if armor<damage and collision
			float damage = this.damage;
			if(c.horse==null){
				damage = damage * this.getGameTeam().data.bonusBowFoot;
			}
			if(c.armor<=damage){
				c.setLifePoints(c.lifePoints+c.armor-damage);
			}
			c.isAttacked();
			this.setLifePoints(-1f);
		}

	}

	public void collision(Building c){
	}
	public Graphics draw(Graphics g){
		g.drawImage(this.image,this.getX()-5f*Game.ratioSpace,this.getY()-75f*Game.ratioSpace);

		shadow.drawFlash(this.getX()-5f*Game.ratioSpace,this.getY()-5f*Game.ratioSpace,shadow.getWidth(),shadow.getHeight(),new Color(0,0,0,0.3f));
		//g.drawImage(i ,this.getX()-5f,this.getY()-5f);
		//g.setColor(Color.white);
		//		g.setColor(new Color(5,5,5,0.2f));
		//		g.draw(this.collisionBox);
		return g;
	}
	public void action(){
		//MULTI 
		this.toKeep = false;
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		if(this.x>this.p.maxX || this.x<0 || this.y>this.p.maxY||this.y<0){
			this.setLifePoints(-1f);
		}
	}


	// For MenuArrow
	public Arrow(){

	}
}
