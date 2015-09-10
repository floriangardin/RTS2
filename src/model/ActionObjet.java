package model;

import org.newdawn.slick.Graphics;




public abstract class ActionObjet extends Objet{
	public int id;
	public float maxLifePoints;
	protected float vx;
	protected float vy;
	private Objet target;
	protected Checkpoint checkpointTarget;


	protected void destroy(){
		this.lifePoints = -10;
		this.target = null;
		this.x = -100f;
		this.y = -100f;
	}
	public void action(){


	}
	public void move(){

	}
	
	public Objet getTarget(){
		return this.target;
	}
	public void setTarget(Objet t){
		this.target = t;
		if(t!=null)
			this.checkpointTarget = new Checkpoint(t.getX(),t.getY());
	}
	public void drawIsSelected(Graphics g) {
		
		
	}


}
