package nature;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import model.NaturalObjet;
import model.Objet;
import model.Plateau;

public class Tree extends NaturalObjet {
	
	float sizeX = 30f;
	float sizeY = 10f;
	float coeffDraw = 0.7f;
	public int type;

	public Tree(float x, float y, Plateau p, int type) {
		this.type = type;
		this.collisionBox = new Rectangle(x-sizeX/2,y-sizeY/2,sizeX,sizeY);
		this.color = Color.gray;
		this.p = p;
		p.addNaturalObjets(this);
		this.lifePoints = 1.0f;
		this.setXY(x, y);
		switch(type){
		case 1:
			this.image = this.p.images.tree01;
			break;
		case 2:
			this.image = this.p.images.tree02;
			break;
		case 3:
			this.image = this.p.images.tree03;
			break;
		case 4:
			this.image = this.p.images.tree04;
			break;
		default:
		}
	}
	public Tree(float x, float y, int type) {
		float size = 25.0f;
		this.collisionBox = new Rectangle(x-size/2,y-size/2,size,size);
		this.color = Color.gray;
		this.lifePoints = 1.0f;
		this.setXY(x, y);
		String s = "Tree0"+String.valueOf(type);
		try {
			this.image = new Image("pics/"+s+".png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void collision(Objet o){
	}
	
	public Graphics draw(Graphics g){
		float x1 = this.x-coeffDraw*this.image.getWidth()/2f;
		float y1 = this.y-coeffDraw*this.image.getHeight()+2f*sizeY;
		float x2 = this.x+coeffDraw*this.image.getWidth()/2f;
		float y2 = this.y+2f*sizeY;
		g.drawImage(this.image,x1,y1,x2,y2,0f,0f,this.image.getWidth(),this.image.getHeight());
		//g.setColor(this.color);
		//g.fill(this.collisionBox);
		return g;
	}
}
