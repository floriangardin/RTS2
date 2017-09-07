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

public strictfp class RenderNaturalObjet {
	
	
	public static void render(NaturalObjet n, Graphics g, Plateau plateau){
		Image i = Images.get(n.name.name());
		float x1 = n.x-i.getWidth()/2f;
		float y1 = n.y+n.getAttribut(Attributs.size)/2+n.sizeY/2-i.getHeight();
		g.drawImage(i,x1,y1);
	}
	
}
