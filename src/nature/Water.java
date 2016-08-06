package nature;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import units.Character;
import model.Game;
import model.NaturalObjet;
import model.Objet;
import model.Plateau;

public class Water extends NaturalObjet {
	
	
	
	public Water(float x, float y, float sizeX, float sizeY) {
		this.name="water";
		this.setTeam(0);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
//		this.image= this.p.g.images.get("water");
		this.collisionBox = new Rectangle(x-sizeX/2,y-sizeY/2,sizeX,sizeY);
		this.color = Color.blue;
		this.lifePoints = 1.0f;
		this.setXY(x, y);
		Game.g.plateau.addNaturalObjets(this);
	}
	
	public void collision(Objet o){
		//TODO: collision
	}
	
	public Graphics draw(Graphics g){
		float x = this.getX()-sizeX/2f;
		float y = this.getY()-sizeY/2f;
//		for(int i=0; i<(int)(this.sizeX/this.image.getWidth())+1;i++)
//			for(int j=0; j<(int)(this.sizeY/this.image.getHeight())+1;j++)
//				if(-32f<x+this.image.getWidth()*i && x+this.image.getWidth()*i<this.p.maxX+32f)
//					if(-32f<y+this.image.getHeight()*j && y+this.image.getHeight()*j<this.p.maxY+32f)
//						g.drawImage(this.image,x+this.image.getWidth()*i,y+this.image.getHeight()*j);
		g.setColor(Color.green);
		
		return g;
	}

	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub
		
	}
}
