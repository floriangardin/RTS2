package model;

import org.newdawn.slick.geom.Point;

public class EnnemyGenerator extends ActionObjet {
	// Class which pop ennemies at random time interval

	Game g;
	float spawnTime;
	float state;
	int n_generated ;
	public EnnemyGenerator(Plateau p,Game g,float x, float y){
		p.addEquipmentObjets(this);
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.team=1;
		this.spawnTime = 80f;
		this.state = spawnTime;
		this.lifePoints = 1f;
		this.collisionBox= new Point(x,y);
	}

	public void action(){
		state+=0.1f;
		if(state>=this.spawnTime){
			this.generate();
			this.state=0f;
			n_generated++;
		}

	}
	public void generate(){
		// Create character
		Character c = new Character(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()-0.5f)*40f);
		// Add a random weapon to character
		if(n_generated<10){
			c.collectArmor(new LightArmor(c.getX(),c.getY(),p,c));
			c.collectWeapon(new Sword(p,c));	
		}
		else if(n_generated<40){
			if(n_generated%2==0){
				c.collectArmor(new LightArmor(c.getX(),c.getY(),p,c));
				c.collectWeapon(new Bow(p,c));
			}
			else{
				c.collectArmor(new MediumArmor(c.getX(),c.getY(),p,c));
				c.collectWeapon(new Sword(p,c));	
			}
		}
		else if(n_generated<80){
			if(n_generated%3==0){
				c.collectArmor(new MediumArmor(c.getX(),c.getY(),p,c));
				c.collectWeapon(new Balista(p,c));
			}
			else if(n_generated%3==1){
				c.collectArmor(new MediumArmor(c.getX(),c.getY(),p,c));
				c.collectWeapon(new Sword(p,c));	
			}
			else{
				c.collectArmor(new MediumArmor(c.getX(),c.getY(),p,c));
				c.collectWeapon(new Bow(p,c));
			}
		}



	}
}
