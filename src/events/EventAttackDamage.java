package events;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.Main;
import model.Game;
import model.Character;
import model.Objet;
import ressources.Fonts;

public class EventAttackDamage extends Event {

	private int value;
	private int totalRemainingTime = (int) (1.2*Main.framerate);
	private int remainingTime = totalRemainingTime;
	private float x, y, vx, vy;
	
	public EventAttackDamage(Objet parent, int value) {
		super(parent);
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
		Fonts.font_main_red.drawString(x-Fonts.font_main_red.getWidth(""+value)/2, y-Fonts.font_main_red.getHeight(""+value)/2, ""+value);
		try{
			Color color = new Color(1f,0f,0f,(this.remainingTime+10f-this.totalRemainingTime)/10f);
			((Character)(this.parent)).drawFlash(g, color);
		}catch(Exception e){
			
		}
		return this.remainingTime>0;
	}

}
