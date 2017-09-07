package render;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import main.Main;
import model.Colors;
import plateau.Checkpoint;
import plateau.MarkerBuilding;
import plateau.Plateau;

public strictfp class RenderCheckpoint {
	public static void render(Checkpoint c, Graphics g, Plateau plateau){
		if(c instanceof MarkerBuilding){
			renderMarkerBuilding((MarkerBuilding) c ,g,  plateau);
		}else{
			renderCheckpoint(c, g, plateau);
		}
	}
	
	public static void renderCheckpoint(Checkpoint c, Graphics g, Plateau plateau){
		if(!c.toDraw && !c.alwaysDraw){
			return ;
		}
		if(c.lastRoundUpdate==plateau.round){
			return ;
		}
		g.setAntiAlias(true);
		g.setColor(Colors.team0);
		if(c.state<=c.maxDuration){
			if(c.color!=null){
				g.setLineWidth(2f*Main.ratioSpace);
				
			}
			g.setLineWidth(2f);
			c.drawShape.setRadius(c.maxRadius*(1-2*(c.animationState)/c.maxDuration));
			c.drawShape.setCenterX(c.getX());
			c.drawShape.setCenterY(c.getY());
			
			c.drawShape2.setRadius((c.maxRadius)*((c.animationState)/c.maxDuration));
			c.drawShape2.setCenterX(c.getX());
			c.drawShape2.setCenterY(c.getY());
			g.fill(new Circle(c.getX(),c.getY(),2f));
			
			g.draw(c.drawShape2);
			g.setColor(c.color);
			g.draw(c.drawShape);
			c.lastRoundUpdate = plateau.round;
		}
		g.setAntiAlias(false);
	}
	public static void renderMarkerBuilding(MarkerBuilding c, Graphics g, Plateau plateau){
		if(!c.toDraw && !c.alwaysDraw){
			return ;
		}
		if(c.lastRoundUpdate==plateau.round){
			return ;
		}
		g.setAntiAlias(true);
		g.setColor(Colors.team0);
		if(c.state<=c.maxDuration){
			if(c.color!=null){
				
				g.setLineWidth(2f*Main.ratioSpace);
				
			}
			g.setLineWidth(2f);
			c.drawShape.setWidth(c.maxWidth-(c.delta*c.state/c.maxDuration));
			c.drawShape.setHeight(c.maxHeight-(c.delta*c.state/c.maxDuration));
			c.drawShape.setCenterX(c.getX());
			c.drawShape.setCenterY(c.getY());
			
			c.drawShape2.setWidth(c.maxWidth-c.delta+(c.delta*c.state/c.maxDuration));
			c.drawShape2.setHeight(c.maxHeight-c.delta+(c.delta*c.state/c.maxDuration));
			
			c.drawShape2.setCenterX(c.getX());
			c.drawShape2.setCenterY(c.getY());
			g.fill(new Circle(c.getX(),c.getY(),2f));
			
			g.draw(c.drawShape2);
			g.setColor(c.color);
			g.draw(c.drawShape);
			c.lastRoundUpdate = plateau.round;
		}
		g.setAntiAlias(false);
	}
		
}
