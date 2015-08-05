package model;

import org.newdawn.slick.geom.Point;

public class EnnemyGenerator extends ActionObjet {
// Class which pop ennemies at random time interval

	Game g;
	float spawnTime;
	float state;
	public EnnemyGenerator(Plateau p,Game g,float x, float y){
		p.addEquipmentObjets(this);
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.team=1;
		this.spawnTime = 40f;
		this.state = spawnTime;
		this.lifePoints = 1f;
		this.collisionBox= new Point(x,y);
	}
	
	public void action(){
		state+=0.1f;
		if(state>=this.spawnTime){
			this.generate();
			this.state=0;
		}
	}
	public void generate(){
		
		// Create character
		
		Character c = new Character(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()-0.5f)*40f);
		// Add a random weapon to character
		c.collectArmor(new LightArmor(c.getX(),c.getY(),p,c));
		c.collectWeapon(new Sword(p,c));
		
	}
}
