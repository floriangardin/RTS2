package render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import control.Player;
import data.Attributs;
import display.Camera;
import pathfinding.Case;
import pathfinding.Case.IdTerrain;
import plateau.Building;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;

public strictfp class SimpleRenderEngine {
	
	public static float ux, uy, vx, vy, d, h, signu, signv, old_vx, old_vy;
	public static Circle circle;

	// Debug rendering
	public static void render(Graphics g, Plateau plateau){
		g.setAntiAlias(true);
		g.translate(-Camera.Xcam, -Camera.Ycam);
		g.setColor(Color.white);
		g.fillRect(0, 0, plateau.maxX, plateau.maxY);
		// Render everything rawest way ...
		g.setColor(Color.cyan);
		for(Case c : plateau.mapGrid.idcases.values()){
			if(c.getIdTerrain()==IdTerrain.WATER){
				g.fillRect(c.x, c.y, c.sizeX, c.sizeY);
			}
		}
		for(Integer o: Player.selection){
			Objet obj = plateau.getById(o);
			if(obj!=null){				
				g.setColor(Color.green);
				g.draw(obj.getSelectionBox());
			}
			if(obj instanceof Character){
				Character c = (Character)obj;
				g.setColor(Color.red);
				for(Integer i : c.waypoints){
					Case ca = plateau.mapGrid.idcases.get(i);
					g.drawRect(ca.x,ca.y, ca.sizeX, ca.sizeY);
				}
			}
			
		}
		for(Objet o : plateau.getObjets().values()){
			g.setColor(o.getTeam().color);
			if(o.getCollisionBox() != null){
				if(o instanceof Character){
					g.draw(o.getCollisionBox());
					g.draw(o.getSelectionBox());
					g.fill(o.getCollisionBox());
					if(Player.selection.contains(o.getId())){
						g.setLineWidth(3f);
						g.setColor(Color.red);
						g.drawLine(o.getX(), o.getY(), o.getX()+20*d*signu*ux, o.getY()+20*d*signu*uy);
						g.setColor(Color.green);
						g.drawLine(o.getX(), o.getY(), o.getX()+20*h*signv*vx, o.getY()+20*h*signv*vy);
						g.setLineWidth(4f);
						g.setColor(Color.black);
						g.drawLine(o.getX(), o.getY(), o.getX()+62*old_vx, o.getY()+62*old_vy);
						g.setLineWidth(3f);
						g.setColor(Color.white);
						g.drawLine(o.getX(), o.getY(), o.getX()+60*old_vx, o.getY()+60*old_vy);
					}
					g.setColor(Color.red);
					g.fillRect(o.getCollisionBox().getCenterX()-50, o.getCollisionBox().getY()+10, 100, 10);
					g.setColor(Color.green);
					g.fillRect(o.getCollisionBox().getCenterX()-50, o.getCollisionBox().getY()+10, 100*(o.getLifePoints()/o.getAttribut(Attributs.maxLifepoints)), 10);
				}else if(o instanceof Building){
					Building b = (Building) o;
					g.setColor(Color.gray);
					g.fill(o.getCollisionBox());
					g.setColor(plateau.teams.get(b.potentialTeam).color);
					g.fillRect(o.getCollisionBox().getX(), o.getCollisionBox().getY(), o.getCollisionBox().getWidth()*(b.constructionPoints/o.getAttribut(Attributs.maxLifepoints)), o.getCollisionBox().getHeight());
				}
				else{
					g.fill(o.getCollisionBox());
				}
				if(!(o instanceof Checkpoint)){					
					g.setColor(Color.white);
					if(o.getName()!=null){
						g.drawString(o.getName().toString().substring(0, 3), o.getX()-10, o.getY()-10);
						g.drawString(""+o.getId(), o.getX()-10, o.getY()-20);
					}
				}
				
			}
		}
		if(circle!=null){
			g.setColor(Color.darkGray);
			g.fill(circle);
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
		g.setAntiAlias(false);
	}
}
