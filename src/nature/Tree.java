package nature;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import model.Game;
import model.NaturalObjet;
import model.Objet;
import model.Plateau;
import ressources.Map;
import units.Character;

public class Tree extends NaturalObjet {
	
	public static float size = 10f;
	public static float coeffDraw = 0.4f;
	public int type;

	public Tree(float x, float y, Plateau p, int type) {
		this.type = type+1;
		this.collisionBox = new Circle(x-sizeX/2,y-sizeY/2,size);
		this.sizeX = 1*Map.stepGrid;
		this.sizeY = 1*Map.stepGrid;
		this.color = Color.gray;
		this.p = p;
		this.lifePoints = 1.0f;
		this.setXY(x*Map.stepGrid+sizeX/2f, y*Map.stepGrid+sizeY/2f);
		p.addNaturalObjets(this);
		this.name = "tree0"+type;
	}
	
	
	public void collision(Objet o){
	}
	
	public Graphics draw(Graphics g){
		Image i = Game.g.images.get(this.name);
		float x1 = this.x-i.getWidth()/2f;
		float y1 = this.y-i.getHeight()+2*size;
		g.drawImage(i,x1,y1);
		//g.setColor(this.color);
		//g.fill(this.collisionBox);
		return g;
	}
	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub
		
	}
}
