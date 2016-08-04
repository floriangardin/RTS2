package bullets;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import buildings.Building;
import main.Main;
import model.Game;
import model.Plateau;
import units.Character;

public class Arrow extends CollisionBullet{

	protected float angle= 0f;
	public Image shadow;
	public float life = 4f;
	public Arrow(Plateau p,Character owner,float vx,float vy,float damage,int id){
		//MULTI 
	
		// Parameters
		this.size = 2f*Main.ratioSpace;
		float Vmax = 600f*Main.ratioSpace;

		this.p = p;
		if(id==-1){
			this.id = Game.g.idChar;
			Game.g.idChar++;
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
		
		this.shadow = Game.g.images.get("arrow").getScaledCopy(2f*Main.ratioSpace);
		this.shadow.rotate(this.angle);
	
		this.sound = Game.g.sounds.get("arrow");
		this.sound.play(1f,Game.g.options.soundVolume);
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
		this.lifePoints = -1f;
	}
	public Graphics draw(Graphics g){
		Game.g.images.get("arrow").rotate(angle);
		g.drawImage(Game.g.images.get("arrow"),this.getX()-5f*Main.ratioSpace,this.getY()-75f*Main.ratioSpace);
		Game.g.images.get("arrow").rotate(-angle);

		shadow.drawFlash(this.getX()-5f*Main.ratioSpace,this.getY()-5f*Main.ratioSpace,shadow.getWidth(),shadow.getHeight(),new Color(0,0,0,0.3f));
		//g.drawImage(i ,this.getX()-5f,this.getY()-5f);
		//g.setColor(Color.white);
		//		g.setColor(new Color(5,5,5,0.2f));
		//		g.draw(this.collisionBox);
		return g;
	}
	public void action(){
		//MULTI 
		this.life  -= Main.increment;
		if(life<0f){
			this.lifePoints = -1f;
		}
		
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		if(this.x>this.p.maxX || this.x<0 || this.y>this.p.maxY||this.y<0){
			this.setLifePoints(-1f);
		}
	}


	// For MenuArrow
	public Arrow(){

	}
}
