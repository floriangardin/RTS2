package events;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.Main;
import model.Game;
import model.Building;
import model.Objet;
import ressources.Map;

public class EventDestructionTour extends Event{
	
	private Vector<Image> images;
	private int totalRemainingTime = (int) (1.8*Main.framerate);
	private int remainingTime = totalRemainingTime;
	private int[] startExplosionTime = new int[]{totalRemainingTime, totalRemainingTime*2/3, totalRemainingTime*1/3};
	private float[] xExplosion;
	private float[] yExplosion;
	private int dureeExplosion =(int) (0.5*Main.framerate);
	private int width, height;

	public EventDestructionTour(Objet parent) {
		super(parent);
		width = (int) (Game.g.images.get("animation-explosion").getWidth()/5f);
		height = (int) (Game.g.images.get("animation-explosion").getHeight()/2f);
		this.images = new Vector<Image>();
		for(int i=0; i<4; i++){
			this.images.add(Game.g.images.get("animation-explosion").getSubImage(width*i, 0, width, height));
		}
		xExplosion = new float[]{parent.x-Map.stepGrid/3, parent.x, parent.x+Map.stepGrid/3};
		yExplosion = new float[]{parent.y-Map.stepGrid,
				parent.y-Map.stepGrid/3,
				parent.y-Map.stepGrid*2/3};
	}

	@Override
	public boolean play(Graphics g) {
		if(this.remainingTime==this.totalRemainingTime){
			Game.g.sounds.get("destructionBuilding").play(1f, Game.g.options.soundVolume);
		}
		int startTime;
		int idImage;
		for(int i=0; i<startExplosionTime.length; i++){
			startTime = startExplosionTime[i];
			if(startTime==this.remainingTime){
				Game.g.sounds.get("explosionBuilding").play(1f, Game.g.options.soundVolume);
				((Building)(parent)).drawFlash(g, Color.white);
			}
			if(this.remainingTime<startTime && this.remainingTime>startTime-dureeExplosion){
				idImage = (int)(4*(this.remainingTime - this.startExplosionTime[i])/(-dureeExplosion));
				g.drawImage(this.images.get(idImage),xExplosion[i]-width/2, yExplosion[i]-height/2);
			}
		}
		
		this.remainingTime--;
		return this.remainingTime>0;
	}
	
	

}
