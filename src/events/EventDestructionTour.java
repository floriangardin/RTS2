package events;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.Main;
import plateau.Building;
import plateau.Objet;
import plateau.Plateau;
import render.RenderBuilding;
import ressources.Images;
import ressources.Map;
import ressources.Sounds;

public strictfp class EventDestructionTour extends Event{
	
	private Vector<Image> images;
	private int totalRemainingTime = (int) (1.8*Main.framerate);
	private int remainingTime = totalRemainingTime;
	private int[] startExplosionTime = new int[]{totalRemainingTime, totalRemainingTime*2/3, totalRemainingTime*1/3};
	private float[] xExplosion;
	private float[] yExplosion;
	private int dureeExplosion =(int) (0.5*Main.framerate);
	private int width, height;

	public EventDestructionTour(Objet parent, Plateau plateau) {
		super(parent, plateau);
		this.topLayer = true;
		width = (int) (Images.get("animation-explosion").getWidth()/5f);
		height = (int) (Images.get("animation-explosion").getHeight()/2f);
		this.images = new Vector<Image>();
		for(int i=0; i<4; i++){
			this.images.add(Images.get("animation-explosion").getSubImage(width*i, 0, width, height));
		}
		xExplosion = new float[]{parent.getX()-Map.stepGrid/3, parent.getX(), parent.getX()+Map.stepGrid/3};
		yExplosion = new float[]{parent.getY()-Map.stepGrid,
				parent.getY()-Map.stepGrid/3,
				parent.getY()-Map.stepGrid*2/3};
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		if(this.remainingTime==this.totalRemainingTime){
			Sounds.playSound("destructionBuilding");
		}
		int startTime;
		int idImage;
		for(int i=0; i<startExplosionTime.length; i++){
			startTime = startExplosionTime[i];
			if(startTime==this.remainingTime){
				Sounds.playSound("explosionBuilding");
				RenderBuilding.drawFlash(g, (Building) parent,Color.white);
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
