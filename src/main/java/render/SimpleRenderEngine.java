package render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import bot.IA;
import control.Player;
import data.Attributs;
import display.Camera;
import plateau.Building;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;

public class SimpleRenderEngine {

	// Debug rendering
	public static void render(Graphics g, Plateau plateau){
		g.setColor(Color.white);
		g.setAntiAlias(true);
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
					g.draw(o.collisionBox);
					g.fill(o.collisionBox);
					g.setColor(Color.red);
					g.fillRect(o.collisionBox.getCenterX()-50, o.collisionBox.getY()+10, 100, 10);
					g.setColor(Color.green);
					g.fillRect(o.collisionBox.getCenterX()-50, o.collisionBox.getY()+10, 100*(o.lifePoints/o.getAttribut(Attributs.maxLifepoints)), 10);
				}else if(o instanceof Building){
					Building b = (Building) o;
					g.setColor(Color.gray);
					g.fill(o.collisionBox);
					g.setColor(plateau.teams.get(b.potentialTeam).color);
					g.fillRect(o.collisionBox.getX(), o.collisionBox.getY(), o.collisionBox.getWidth()*(b.constructionPoints/o.getAttribut(Attributs.maxLifepoints)), o.collisionBox.getHeight());
				}
				else{
					g.fill(o.collisionBox);
				}
				if(!(o instanceof Checkpoint)){					
					g.setColor(Color.white);
					if(o.name!=null){
						g.drawString(o.name.toString().substring(0, 3), o.x-10, o.y-10);
						g.drawString(""+o.id, o.x-10, o.y-20);
					}
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
		if(IA.isInit){
			IA.ias.stream().forEach(x-> x.draw(g));
		}
		// Render selection 
		g.translate(Camera.Xcam, Camera.Ycam);
		g.setAntiAlias(false);
	}
}
