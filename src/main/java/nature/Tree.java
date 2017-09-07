package nature;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import model.Game;
import plateau.Character;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import ressources.Map;
import utils.ObjetsList;

public strictfp class Tree extends NaturalObjet {
	
	public static float size = 10f;
	public static float coeffDraw = 0.4f;
	public int type;

	public Tree(float x, float y, int type, Plateau plateau) {
		super(plateau);
		this.type = type;
		this.setName(ObjetsList.get("tree0"+type));
		this.team = plateau.teams.get(0);
		this.setCollisionBox(new Circle(x,y,this.getAttribut(Attributs.size)));
		this.color = Color.gray;
		this.setLifePoints(1.0f);
		this.setX(x);
		this.setY(y);
		plateau.addNaturalObjets(this);
	}
	
	
	public void collision(Objet o){
	}
	

	@Override
	public void collision(Character c, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}
}
