package render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.Player;
import display.Camera;
import plateau.Objet;
import plateau.Plateau;
import plateau.Character;

public class SimpleRenderEngine {

	// Debug rendering
	public static void render(Graphics g, Plateau plateau){
		g.setColor(Color.white);
		g.fillRect(0, 0, plateau.maxX, plateau.maxY);
		g.translate(-Camera.Xcam, -Camera.Ycam);
		// Render everything rawest way ...
		for(Integer o: Player.selection){
			Objet obj = plateau.getById(o);
			if(obj!=null){				
				g.setColor(Color.green);
				g.draw(obj.selectionBox);
			}
		}
		for(Objet o : plateau.objets.values()){
			g.setColor(o.getTeam().color);
			if(o.collisionBox != null){
				if(o instanceof Character){
					g.setAntiAlias(true);
					g.draw(o.collisionBox);
					g.setAntiAlias(false);
					g.fill(o.collisionBox);
				}else{
					g.fill(o.collisionBox);
				}
			}
		}
		// Rectangle of selection
		if(Player.rectangleSelection != null){
			g.setLineWidth(1f);
			g.setColor(Color.green);
			g.drawRect(Player.rectangleSelection.getMinX(),
					Player.rectangleSelection.getMinY(),
					Player.rectangleSelection.getWidth(),
					Player.rectangleSelection.getHeight());	
		}
		// Render selection 
		g.translate(Camera.Xcam, Camera.Ycam);
	}
}
