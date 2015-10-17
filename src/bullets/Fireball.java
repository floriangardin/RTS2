package bullets;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import buildings.Building;
import main.Main;
import model.Changes;
import model.Checkpoint;
import model.Plateau;
import multiplaying.OutputModel.OutputBullet;
import units.Character;

public class Fireball extends Bullet {

	protected float altitude;
	protected int animation;
	protected float angle;
	protected Image image1, image2, boom;
	protected boolean explosion= false;

	public Fireball(Plateau p,Character owner,float damage){
		//MULTI 
		this.changes = new Changes();
		// Parameters
		this.altitude = 0f;
		this.areaEffect = 40f;
		float Vmax = 120f;
		float size = 10f;
		this.name = "fireball";
		//
		this.p = p;
		this.id = p.g.idBullet;
		p.g.idBullet++;
		p.addBulletObjets(this);
		this.p = p;
		this.damage = damage;
		this.image = (this.p.g.images.fireball).getSubImage(0, 150, 75, 75);
		this.image1 = (this.p.g.images.fireball).getSubImage(75, 150, 75, 75);
		this.image2 = (this.p.g.images.fireball).getSubImage(150, 150, 75, 75);
		this.boom = this.p.g.images.explosion;
		this.animation = 0;
		this.lifePoints = 30f;
		this.owner = owner;
		this.team = this.owner.team;
		this.setTarget(new Checkpoint(owner.getTarget().getX(),owner.getTarget().getY()));
		this.collisionBox = new Circle(owner.getX(),owner.getY(),size);
		this.setXY(owner.getX(),owner.getY()-altitude);
		this.vx = this.owner.getTarget().getX()-this.owner.getX();
		this.vy = this.owner.getTarget().getY()-this.owner.getY()+altitude;
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
		this.image.rotate(this.angle);
		this.image1.rotate(this.angle);
		this.image2.rotate(this.angle);
//		this.sound = p.g.sounds.fireball;
//		this.sound.play(1f,this.p.g.options.soundVolume);
	}
	public Fireball(OutputBullet ocb, Plateau p){
		// Parameters
		this.p = p;
		p.addBulletObjets(this);
		this.id = ocb.id;
		this.image = (this.p.g.images.fireball).getSubImage(0, 150, 75, 75);
		this.image1 = (this.p.g.images.fireball).getSubImage(75, 150, 75, 75);
		this.image2 = (this.p.g.images.fireball).getSubImage(150, 150, 75, 75);
		this.boom = this.p.g.images.explosion;
		this.animation = 0;
		this.lifePoints = 1f;
		this.collisionBox = new Point(ocb.x,ocb.y);
		this.setXY(ocb.x,ocb.y);
		this.vx = ocb.vx;
		this.vy = ocb.vy;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.image.rotate(this.angle);
		this.image1.rotate(this.angle);
		this.image2.rotate(this.angle);
//		this.sound = p.g.sounds.fireball;
//		this.sound.play(1f,this.p.g.options.soundVolume);
	}
	public Fireball(){}
	public void action(){
		if(explosion){
			this.lifePoints-=1f;
			return;
		}
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		this.animation+=1;
		if(this.animation>=9)
			this.animation = 0;
		if(this.collisionBox.contains(this.getTarget().collisionBox)){
			this.explode();
		}
	}
	public void explode(){
		Circle area = new Circle(this.getX(),this.getY(),this.areaEffect);

		for(Character c : this.p.characters){
			if(c.collisionBox.intersects(area) && c.team!=this.owner.team){
				this.boom(c);

			}
		}
		
		this.explosion = true;
	}
	public void boom(Character c){
		float damage = this.damage;
		if(c.weapon!= null && c.weapon == "bow")
			damage = damage * this.p.g.players.get(team).data.bonusBowFoot;
		c.lifePoints-=this.damage;
		
	}
	
	public void boom(Building c){
	
	}

	public Graphics draw(Graphics g){
		if(this.explosion){
			float r = this.boom.getWidth()/5f;
			if(lifePoints>=24f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,0f,0f,r,r);
			else if(lifePoints>=18f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,r,0f,2*r,r);
			else if(lifePoints>=12f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,2*r,0f,3*r,r);
			else if(lifePoints>=6f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,3*r,0f,4*r,r);
			else 
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,4*r,0f,5*r,r);

		} else {
			if(animation<3)	
				g.drawImage(this.image, this.getX()-28f, this.getY()-28f, this.getX()+28f, this.getY()+28f,0f,0f,this.image.getWidth(),this.image.getHeight());
			else if(animation<6)	
				g.drawImage(this.image1, this.getX()-28f, this.getY()-28f, this.getX()+28f, this.getY()+28f,0f,0f,this.image.getWidth(),this.image.getHeight());
			else	
				g.drawImage(this.image2, this.getX()-28f, this.getY()-28f, this.getX()+28f, this.getY()+28f,0f,0f,this.image.getWidth(),this.image.getHeight());
		}
		//g.setColor(Color.black);
		//g.fill(new Circle(this.collisionBox.getCenterX(),this.collisionBox.getCenterY(),this.collisionBox.getBoundingCircleRadius()));
		return g;
	}
}
