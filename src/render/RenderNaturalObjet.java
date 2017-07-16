package render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import model.Colors;
import plateau.Building;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import utils.ObjetsList;

public class RenderNaturalObjet {
	
	
	public static void render(NaturalObjet n, Graphics g, Plateau plateau){
		Image i = Images.get(n.name.name());
		float x1 = n.x-i.getWidth()/2f;
		float y1 = n.y-i.getHeight()+2*n.getAttribut(Attributs.size);
		g.drawImage(i,x1,y1);
	}
	
}