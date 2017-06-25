package events;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import display.Camera;
import main.Main;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import render.RenderCharacter;
import ressources.GraphicElements;

public class EventAttackDamage extends Event {

	private int value;
	private int totalRemainingTime = (int) (1.2*Main.framerate);
	private int remainingTime = totalRemainingTime;
	private float x, y, vx, vy;
	
	public EventAttackDamage(Objet parent, int value, Plateau plateau, Camera camera) {
		super(parent, plateau, camera);
		this.value = value;
		this.x = parent.x;
		this.y = parent.y;
		this.vy = (float) (-2f+0.5f*Math.random()-0.25);
		this.vx = 0.7f*((float) (2f*Math.random()-1f));
	}
	

	@Override
	public boolean play(Graphics g) {
		this.vy += 3f/Main.framerate;
		this.y+=vy;
		this.x+=vx;
		this.remainingTime--;
		GraphicElements.font_red.drawString(x-GraphicElements.font_red.getWidth(""+value)/2, y-GraphicElements.font_red.getHeight(""+value)/2, ""+value);
		try{
			Color color = new Color(1f,0f,0f,(this.remainingTime+10f-this.totalRemainingTime)/10f);
			RenderCharacter.drawFlash(g, color, (Character)(this.parent), plateau);
		}catch(Exception e){
			
		}
		return this.remainingTime>0;
	}

}
