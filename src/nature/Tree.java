package nature;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import units.Character;
import model.NaturalObjet;
import model.Objet;
import model.Plateau;
import ressources.Map;

public class Tree extends NaturalObjet {
	
	float size = 10f;
	float coeffDraw = 0.7f;
	public int type;

	public Tree(float x, float y, Plateau p, int type) {
		this.type = type;
		this.collisionBox = new Circle(x-sizeX/2,y-sizeY/2,size);
		this.sizeX = 1*Map.stepGrid;
		this.sizeY = 1*Map.stepGrid;
		this.color = Color.gray;
		this.p = p;
		this.lifePoints = 1.0f;
		this.setXY(x*Map.stepGrid+sizeX/2f, y*Map.stepGrid+sizeY/2f);
		p.addNaturalObjets(this);
		switch(type){
		case 1:
			this.image = this.p.g.images.get("tree01").getScaledCopy(2.5f*sizeX/p.g.images.get("tree01").getWidth());
			break;
		case 2:
			this.image = this.p.g.images.get("tree02").getScaledCopy(2.5f*sizeX/p.g.images.get("tree01").getWidth());
			break;
		case 3:
			this.image = this.p.g.images.get("tree03").getScaledCopy(2.5f*sizeX/p.g.images.get("tree01").getWidth());
			break;
		case 4:
			this.image = this.p.g.images.get("tree04").getScaledCopy(2.5f*sizeX/p.g.images.get("tree01").getWidth());
			break;
		default:
		}
	}
	
	
	public void collision(Objet o){
	}
	
	public Graphics draw(Graphics g){
		float x1 = this.x-coeffDraw*this.image.getWidth()/2f;
		float y1 = this.y-coeffDraw*this.image.getHeight()+2*size;
		float x2 = this.x+coeffDraw*this.image.getWidth()/2f;
		float y2 = this.y+2*size;
		g.drawImage(this.image,x1,y1,x2,y2,0f,0f,this.image.getWidth(),this.image.getHeight());
		//g.setColor(this.color);
		//g.fill(this.collisionBox);
		return g;
	}
	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub
		
	}
}
