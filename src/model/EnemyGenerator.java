package model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class EnemyGenerator extends Building {
	// Class which pop ennemies at random time interval

	Game g;
	float spawnTime;
	float state;
	int n_generated ;
	
	public EnemyGenerator(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.team=2;
		this.spawnTime = 40f;
		this.state = spawnTime;
		this.lifePoints = 1f;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
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



	}
	public Graphics draw(Graphics g){
		g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
		return g;
	}
}
