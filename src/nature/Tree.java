package nature;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import model.Character;
import model.Game;
import model.NaturalObjet;
import model.Objet;
import model.Plateau;
import ressources.Map;
import utils.ObjetsList;

public class Tree extends NaturalObjet {
	
	public static float size = 10f;
	public static float coeffDraw = 0.4f;
	public int type;

	public Tree(float x, float y, int type) {
		this.type = type;
		this.name = ObjetsList.get("tree0"+type);
		this.setTeam(0);
		this.collisionBox = new Circle(x-sizeX/2,y-sizeY/2,size);
		this.sizeX = 1*Map.stepGrid;
		this.sizeY = 1*Map.stepGrid;
		this.color = Color.gray;
		this.lifePoints = 1.0f;
		this.setXY(x*Map.stepGrid+sizeX/2f, y*Map.stepGrid+sizeY/2f);
		Game.g.plateau.addNaturalObjets(this);
	}
	
	
	public void collision(Objet o){
	}
	
	public void drawBasicImage(Graphics g){
		Image i = Game.g.images.get(this.name.name());
		float x1 = this.x-i.getWidth()/2f;
		float y1 = this.y-i.getHeight()+2*size;
		g.drawImage(i,x1,y1);
	}
	
	public Graphics draw(Graphics g){
		this.drawBasicImage(g);
		//g.setColor(this.color);
		//g.fill(this.collisionBox);
		return g;
	}
	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub
		
	}
}
