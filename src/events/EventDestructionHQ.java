package events;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.Main;
import model.Game;
import plateau.Building;
import plateau.Objet;
import ressources.Map;

public class EventDestructionHQ extends Event{
	
	private Vector<Image> images;
	private int totalRemainingTime = (int) (3.8*Main.framerate);
	private int remainingTime = totalRemainingTime;
	private int[] startExplosionTime = new int[]{totalRemainingTime, 
												 totalRemainingTime*7/8, 
												 totalRemainingTime*11/16, 
												 totalRemainingTime*5/8, 
												 totalRemainingTime*9/16, 
												 totalRemainingTime*4/8, 
												 totalRemainingTime*3/8 };
	private float[] xExplosion;
	private float[] yExplosion;
	private int dureeExplosion =(int) (0.5*Main.framerate);
	private int width, height;

	public EventDestructionHQ(Objet parent) {
		super(parent);
		width = (int) (Images.get("animation-explosion").getWidth()/5f);
		height = (int) (Images.get("animation-explosion").getHeight()/2f);
		this.images = new Vector<Image>();
		for(int i=0; i<4; i++){
			this.images.add(Images.get("animation-explosion").getSubImage(width*i, 0, width, height));
		}
		Game.g.Xcam += (Math.random()*3)-1f;
		Game.g.Ycam += (Math.random()*3)-1f;
		xExplosion = new float[]{parent.x-Map.stepGrid, 
								 parent.x, 
								 parent.x+Map.stepGrid*2f,
								 parent.x+15f,
								 parent.x-35f,
								 parent.x,
								 parent.x-Map.stepGrid*1.2f
								 };
		yExplosion = new float[]{parent.y-1.2f*Map.stepGrid,
								 parent.y+Map.stepGrid,
								 parent.y-Map.stepGrid*1.5f,
								 parent.y-Map.stepGrid,
								 parent.y+Map.stepGrid*0.8f,
								 parent.y+Map.stepGrid*1.5f,
								 parent.y-Map.stepGrid};
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
