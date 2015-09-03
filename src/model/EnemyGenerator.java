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
		// Add a random weapon to character
		if(n_generated<10){
			Character.createSpearman(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()+2f)*10f);
		}
		else if(n_generated<40){
			if(n_generated%2==0){
				Character.createBowman(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()+2f)*10f);
			}
			else{
				Character.createSpearman(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()+2f)*10f);
			}
		}
		else if(n_generated<80){
			if(n_generated%3==0){
				Character.createWizard(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()+2f)*10f);
			}
			else if(n_generated%3==1){
				Character.createSpearman(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()+2f)*10f);
			}
			else{
				Character.createKnight(p,team,x+((float)Math.random()-0.5f)*40f,y+((float)Math.random()+2f)*10f);
			}
		}



	}
	public Graphics draw(Graphics g){
		g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
		return g;
	}
}
