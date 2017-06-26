package render;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import main.Main;
import model.Colors;
import plateau.Checkpoint;
import plateau.Plateau;

public class RenderCheckpoint {
	public static void render(Checkpoint c, Graphics g, Plateau plateau){
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
			c.drawShape.setCenterX(c.x);
			c.drawShape.setCenterY(c.y);
			
			
			c.drawShape2.setRadius((c.maxRadius)*((c.animationState)/c.maxDuration));
			c.drawShape2.setCenterX(c.x);
			c.drawShape2.setCenterY(c.y);
			g.fill(new Circle(c.x,c.y,2f));
			
			g.draw(c.drawShape2);
			g.setColor(c.color);
			g.draw(c.drawShape);
			c.lastRoundUpdate = plateau.round;
		}
		g.setAntiAlias(false);
		
	}
		
}
