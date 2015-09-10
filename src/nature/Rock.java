package nature;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import model.NaturalObjet;
import model.Objet;
import model.Plateau;

public class Rock extends NaturalObjet {

	public Rock(float x, float y, Plateau p) {
		float size = 25.0f;
		this.collisionBox = new Rectangle(x-size/2,y-size/2,size,size);
		this.color = Color.gray;
		this.p = p;
		p.addNaturalObjets(this);
		this.lifePoints = 1.0f;
		this.setXY(x, y);
		try {
			this.image = new Image("pics/Tree01.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void collision(Objet o){
		//TODO: collision
	}
	
	public Graphics draw(Graphics g){
		g.drawImage(this.image,this.getX()-this.image.getWidth()/2f,this.getY()-7f*this.image.getHeight()/8f);
		//g.setColor(this.color);
		//g.fill(this.collisionBox);
		return g;
	}
}
