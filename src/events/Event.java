package events;

import java.util.Random;
import java.util.Vector;

import main.Main;
import model.Game;
import model.Objet;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import utils.Utils;


public class Event {

	
	Vector<String> sounds = new Vector<String>();
	Vector<GraphicEvent> graphics = new Vector<GraphicEvent>();
	Events name;
	Objet parent;
	float power;
	float duration;
	boolean playSound = false;
	
	public Event(Events name,final Objet parent){
		this.parent = parent;
		this.name = name;
		
		
		// TODO : Make it generic with dataFile
		switch(name){
		case ArrowLaunched:
			this.sounds.addElement("arrow");
			this.duration = 20f;
			this.graphics.addElement(GraphicEvent.createGraphicEvent(GraphicEvents.ArrowWind, this));
			break;
		case FireBallLaunched:
			this.sounds.addElement("fireball");
			this.duration = 20f;
			this.graphics.addElement(GraphicEvent.createGraphicEvent(GraphicEvents.ArrowWind, this));
			break;
		
		}
	}
	
	
	public boolean play(Graphics g){
		// Calculate intensity in function of options and distance to scene
		playSound();
		
		// Render images
		for(GraphicEvent gr : graphics){
			gr.draw(g);
		}
		duration = duration - 0.1f*Main.increment;
		return duration>0;
	}

	
	public void draw(Graphics g){
		
	}
	
	
  public static String getRandomSound (Vector<String> v) {
       Random generator = new Random();
       if(v.size()==1){
    	   return v.get(0);
       }
       int rnd = generator.nextInt(v.size() - 1);
       return v.get(rnd); // Cast the vector value into a String object
   }
  
	public void playSound(){
		if(this.sounds.size()>0 && !playSound){			
			Game.g.sounds.get(getRandomSound(this.sounds)).play(1f,Game.g.options.soundVolume*ratioDistance());
			playSound = true;
		}
	}
	
	private float ratioDistance(){
		return Math.min(1f, Math.max(0.1f, (400f*Main.ratioSpace)/Utils.distance(parent.x, parent.y, Game.g.Xcam, Game.g.Ycam)));
	}
}
