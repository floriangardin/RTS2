package plateau;

import main.Main;
import model.Colors;
import model.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class MarkerBuilding extends Checkpoint{

	public Rectangle drawShape;
	public Rectangle drawShape2;
	float maxWidth ;
	float maxHeight ;
	float delta = 20f;
	

	public MarkerBuilding(float x, float y,Building b, Plateau plateau) {
		super(x, y, plateau);
		
		this.x = x;
		this.y = y;
		this.toDraw = true;
		this.alwaysDraw = true;
		this.maxRadius = 200f;
		this.collisionBox = new Circle(x,y,3f);
		maxWidth = ((Rectangle)b.collisionBox).getWidth()+delta;
		maxHeight = ((Rectangle)b.collisionBox).getHeight()+delta;
		this.drawShape = new Rectangle(x,y,maxWidth,maxHeight);
		drawShape.setCenterX(x);
		drawShape.setCenterY(y);
		this.drawShape2 = new Rectangle(x,y,maxWidth-delta,maxHeight-delta);
		drawShape2.setCenterX(x);
		drawShape2.setCenterY(y);
		this.state = this.maxDuration+1f;
		this.selectionBox = null;
		this.setXY(x, y, plateau);
		this.printed=0f;
		
	}

	
	public void action(Plateau plateau){
		this.toDraw = this.state<this.maxDuration;
		if(state<=maxDuration){
			state+=3f*Main.increment;
		}
		
	}
	public Graphics draw(Graphics g, Plateau plateau){
		
		if(!toDraw && !alwaysDraw){
			return g;
		}
		if(this.lastRoundUpdate==plateau.round){
			return g;
		}

		g.setAntiAlias(true);
		g.setColor(Colors.team0);
		if(state<=maxDuration){
			if(color!=null){
				
				g.setLineWidth(2f*Main.ratioSpace);
				
			}
			g.setLineWidth(2f);
			drawShape.setWidth(maxWidth-(delta*state/maxDuration));
			drawShape.setHeight(maxHeight-(delta*state/maxDuration));
			drawShape.setCenterX(x);
			drawShape.setCenterY(y);
			
			drawShape2.setWidth(maxWidth-delta+(delta*state/maxDuration));
			drawShape2.setHeight(maxHeight-delta+(delta*state/maxDuration));
			
			drawShape2.setCenterX(x);
			drawShape2.setCenterY(y);
			g.fill(new Circle(x,y,2f));
			
			g.draw(drawShape2);
			g.setColor(color);
			g.draw(drawShape);
			this.lastRoundUpdate = plateau.round;
		}
		g.setAntiAlias(false);
		return g;
	}
}
