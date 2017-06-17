package plateau;

import main.Main;
import model.Colors;
import model.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class Checkpoint extends Objet {
	float printed;
	public float maxDuration=30f*60/Main.framerate;
	public float state;
	public float animationState;
	protected transient float maxRadius = 20f;
	int lastRoundUpdate =0;
	Circle drawShape;
	Circle drawShape2;
	public boolean toDraw=false;
	public boolean alwaysDraw = false;
	public boolean neverEnding = false;
	
	public Checkpoint(float x, float y, Plateau plateau){
		super(plateau);
		this.initialize(x, y, plateau);
	}
	
	public void initialize(float x, float y, Plateau plateau){
		this.lifePoints=1f;
		plateau.checkpoints.addElement(this);
		plateau.objets.put(this.id,this);
		//p.addEquipmentObjets(this);
		this.x = x;
		this.y = y;
		color = Colors.team2;
		this.collisionBox = new Circle(x,y,3f);
		this.drawShape = new Circle(x,y,maxRadius);
		drawShape.setCenterX(x);
		drawShape.setCenterY(y);

		this.drawShape2 = new Circle(x,y,0);
		drawShape2.setCenterX(x);
		drawShape2.setCenterY(y);
		this.setXY(x, y, plateau);
		this.selectionBox = null;
		this.x = x;
		this.y = y;
		this.printed=0f;
	}
	
	public Checkpoint(float x, float y,boolean neverEnding, Plateau plateau){
		super(plateau);
		this.initialize(x, y, plateau);
		this.neverEnding = neverEnding;
	}
	

	
	public void action(){

		//toDraw = false;

		if(state<=maxDuration){
			state+=3f*Main.increment;
			animationState+=3f*Main.increment;
		}else if(!neverEnding){
			this.lifePoints=-1f;
		}
		
	}
	
	public Graphics draw(Graphics g){
		if(!toDraw && !alwaysDraw){
			return g;
		}
		if(this.lastRoundUpdate==Game.gameSystem.round){
			return g;
		}

		g.setAntiAlias(true);
		g.setColor(Colors.team0);
		if(state<=maxDuration){
			if(color!=null){
				
				g.setLineWidth(2f*Main.ratioSpace);
				
			}
			g.setLineWidth(2f);
			drawShape.setRadius(maxRadius*(1-2*(animationState)/maxDuration));
			drawShape.setCenterX(x);
			drawShape.setCenterY(y);
			
			
			drawShape2.setRadius((maxRadius)*((animationState)/maxDuration));
			drawShape2.setCenterX(x);
			drawShape2.setCenterY(y);
			g.fill(new Circle(x,y,2f));
			
			g.draw(drawShape2);
			g.setColor(color);
			g.draw(drawShape);
			this.lastRoundUpdate = Game.gameSystem.round;
		}
		g.setAntiAlias(false);
		return g;
	}

	@Override
	public void collision(Character c, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}
}
