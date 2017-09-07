package plateau;

import main.Main;
import model.Colors;
import model.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public strictfp class MarkerBuilding extends Checkpoint{

	public Rectangle drawShape;
	public Rectangle drawShape2;
	public float maxWidth ;
	public float maxHeight ;
	public float delta = 20f;
	

	public MarkerBuilding(float x, float y,Building b, Plateau plateau) {
		super(x, y, plateau);
		
		this.x = x;
		this.y = y;
		this.team = b.getTeam();
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
		

		return g;
	}
}
