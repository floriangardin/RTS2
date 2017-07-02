package render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import display.Camera;
import model.Player;
import plateau.Objet;
import plateau.Plateau;

public class SimpleRenderEngine {

	// Debug rendering
	public static void render(Graphics g, Plateau plateau, Camera camera, Player player){
		
		g.translate(-camera.Xcam, -camera.Ycam);
		// Render everything rawest way ...
		for(Integer o: player.selection.selection){
			g.setColor(Color.green);
			g.draw(plateau.getById(o).selectionBox);
		}
		for(Objet o : plateau.objets.values()){
			g.setColor(o.getTeam().color);
			if(o.collisionBox != null){				
				g.fill(o.collisionBox);
			}
		}
		
		// Rectangle of selection
		if(player.selection.rectangleSelection != null){
			g.setLineWidth(1f);
			g.setColor(Color.green);
			g.drawRect(player.selection.rectangleSelection.getMinX(),
					player.selection.rectangleSelection.getMinY(),
					player.selection.rectangleSelection.getWidth(),
					player.selection.rectangleSelection.getHeight());	
		}
		// Render selection 
		
		g.translate(camera.Xcam, camera.Ycam);
	}
}
