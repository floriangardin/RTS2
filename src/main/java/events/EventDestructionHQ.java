package events;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import display.Camera;
import main.Main;
import model.Game;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import ressources.Map;
import ressources.Sounds;

public strictfp class EventDestructionHQ extends Event{
	
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

	public EventDestructionHQ(Objet parent, Plateau plateau) {
		super(parent, plateau);
		this.topLayer = true;
		width = (int) (Images.get("animation-explosion").getWidth()/5f);
		height = (int) (Images.get("animation-explosion").getHeight()/2f);
		this.images = new Vector<Image>();
		for(int i=0; i<4; i++){
			this.images.add(Images.get("animation-explosion").getSubImage(width*i, 0, width, height));
		}
		Camera.Xcam += ((StrictMath.random()-0.5f)*3);
		Camera.Ycam += ((StrictMath.random()-0.5f)*3);
		xExplosion = new float[]{parent.getX()-Map.stepGrid, 
								 parent.getX(), 
								 parent.getX()+Map.stepGrid*2f,
								 parent.getX()+15f,
								 parent.getX()-35f,
								 parent.getX(),
								 parent.getX()-Map.stepGrid*1.2f
								 };
		yExplosion = new float[]{parent.getY()-1.2f*Map.stepGrid,
								 parent.getY()+Map.stepGrid,
								 parent.getY()-Map.stepGrid*1.5f,
								 parent.getY()-Map.stepGrid,
								 parent.getY()+Map.stepGrid*0.8f,
								 parent.getY()+Map.stepGrid*1.5f,
								 parent.getY()-Map.stepGrid};
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
