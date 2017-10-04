package plateau;

import org.newdawn.slick.geom.Circle;

import main.Main;
import model.Colors;
import utils.ObjetsList;

public strictfp class Checkpoint extends Objet {
	float printed;
	public float maxDuration=30f*60/Main.framerate;
	public float state;
	public float animationState;
	public float maxRadius = 20f;
	public int lastRoundUpdate =0;
	public Circle drawShape;
	public Circle drawShape2;
	public boolean toDraw=false;
	public boolean alwaysDraw = false;
	public boolean neverEnding = false;
	
	public Checkpoint(float x, float y, Plateau plateau){
		super(plateau);
		this.initialize(x, y, plateau);
	}
	public Checkpoint(float x, float y){
		this.setX(x);
		this.setY(y);
	}
	protected void initialize(float x, float y, Plateau plateau){
		this.setLifePoints(1f);
		this.setName(ObjetsList.Checkpoint);
		plateau.addObjet(this);
		this.setX(x);
		this.setY(y);
		color = Colors.team2;
		this.setCollisionBox(new Circle(x,y,3f));
		this.drawShape = new Circle(x,y,maxRadius);
		drawShape.setCenterX(x);
		drawShape.setCenterY(y);
		this.drawShape2 = new Circle(x,y,0);
		drawShape2.setCenterX(x);
		drawShape2.setCenterY(y);
		this.setXY(x, y, plateau);
		this.setSelectionBox(null);
		this.printed=0f;
	}
	
	public Checkpoint(float x, float y,boolean neverEnding, Plateau plateau){
		super(plateau);
		this.initialize(x, y, plateau);
		this.neverEnding = neverEnding;
	}
	
	public void action(Plateau plateau){
		//toDraw = false;
		if(state<=maxDuration){
			state+=3f*Main.increment;
			animationState+=3f*Main.increment;
		}else if(!neverEnding){
			this.setLifePoints(-1f);
		}	
	}

	@Override
	public void collision(Character c, Plateau plateau) {
		// TODO Auto-generated method stub
	}
}
