package bullets;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import buildings.Building;
import main.Main;
import model.Changes;
import model.Plateau;
import multiplaying.OutputModel.OutputBullet;
import units.Character;

public class Arrow extends Bullet{

	protected float angle= 0f;

	public Arrow(Plateau p,Character owner,float damage){
		//MULTI 
		this.changes = new Changes();
		// Parameters
		float size = 2f;
		float Vmax = 250f;
		// 
		this.p = p;
		this.id = p.g.idBullet;
		p.g.idBullet++;
		this.name ="arrow";
		this.damage = damage;
		p.addBulletObjets(this);
		this.lifePoints = 1f;
		this.owner = owner;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),size);
		this.setXY(owner.getX(),owner.getY());
		this.vx = this.owner.getTarget().getX()-this.owner.getX();
		this.vy = this.owner.getTarget().getY()-this.owner.getY();
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*Main.framerate;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.image = p.g.images.arrow.getScaledCopy(1f);
		this.image.rotate(this.angle);
		this.sound = p.g.sounds.arrow;
		this.sound.play(1f,this.p.g.options.soundVolume);
	}
	public Arrow(OutputBullet ocb ,Plateau p){
		// Only used to display on client screen
		// Parameters
		this.p = p;
		this.id = ocb.id;
		p.addBulletObjets(this);
		this.lifePoints = 1f;
		this.collisionBox = new Point(x,y);
		this.setXY(ocb.x,ocb.y);
		this.vx = ocb.vx;
		this.vy = ocb.vy;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.image = p.g.images.arrow.getScaledCopy(1f);
		this.image.rotate(this.angle);
	}

	public void collision(Character c){
		if(c.team!=this.owner.team){
			// Attack if armor<damage and collision
			float damage = this.damage;
			if(c.horse==null){
				damage = damage * this.p.g.players.get(team).data.bonusBowFoot;
			}
			if(c.armor<=damage){
				c.lifePoints+=c.armor-damage;
			}
			this.lifePoints=-1f;
		}

	}
	
	public void collision(Building c){


	}
	public Graphics draw(Graphics g){
		g.drawImage(this.image,this.getX()-5f,this.getY()-5f);
		//g.setColor(Color.white);
		//g.fill(this.collisionBox);
		return g;
	}
	public void action(){
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		if(this.x>this.p.maxX || this.x<0 || this.y>this.p.maxY||this.y<0){
			this.lifePoints=-1f;
		}
	}


	// For MenuArrow
	public Arrow(){

	}
}
