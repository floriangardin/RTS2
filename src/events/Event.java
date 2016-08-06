package events;

import java.util.Random;
import java.util.Vector;

import main.Main;
import model.Game;
import model.Objet;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import utils.Utils;
import units.Character;

public class Event {

	
	Vector<Sound> sounds = new Vector<Sound>();
	Sound soundPlaying;
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
			this.sounds.addElement(Game.g.sounds.get("arrow"));
			this.duration = 20f;
			this.graphics.addElement(GraphicEvent.createGraphicEvent(GraphicEvents.ArrowWind, this));
			break;
		case FireBallLaunched:
			this.sounds.addElement(Game.g.sounds.get("fireball"));
			this.duration = 20f;
			this.graphics.addElement(GraphicEvent.createGraphicEvent(GraphicEvents.ArrowWind, this));
			break;
		case BonusTaken:
			this.sounds.addElement(Game.g.sounds.get("bonusTaken"));
			this.duration = 20f;
			this.graphics.addElement(GraphicEvent.createGraphicEvent(GraphicEvents.BonusTaken, this));
			break;
		case Death:
			// Differentiate by death
			this.sounds.addAll(Game.g.sounds.getSoundVector(parent.name,"death"));
			this.duration = 20f;
			break;
		case Immolation:
			// Differentiate by death
			this.sounds.add(Game.g.sounds.get("fire"));
			this.duration = 10f;
			break;
		case MoveAttack:
			// Differentiate by death
			this.sounds.addAll(Game.g.sounds.getSoundVector(parent.name,"attack"));
			this.duration = 3f;
			break;
		case MoveTarget:
			// Differentiate by death
			this.sounds.addAll(Game.g.sounds.getSoundVector(parent.name,"target"));
			this.duration = 3f;
			break;
		case BuildingSelected:
			// Differentiate by death
			this.sounds.add(Game.g.sounds.get("selection"+parent.name));
			this.duration = 3f;
			break;
		case CharacterSelected:
			// Differentiate by death
			this.sounds.addAll(Game.g.sounds.getSoundVector(parent.name,"selection"));
			this.duration = 3f;
			break;
		case Attack:
			// Differentiate by death
			this.sounds.add(Game.g.sounds.get(((Character)parent).weapon));
			this.duration = 3f;
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
		duration = duration - Main.increment;
		if(duration<=0 && soundPlaying!=null){
			//soundPlaying.stop();
		}
		return duration>0;
	}

	
	public void draw(Graphics g){
		
	}
	
	
  public static Sound getRandomSound (Vector<Sound> v) {
       Random generator = new Random();
       //Filter by not is playing
       
       if(v.size()==1){
    	   return v.get(0);
       }
       int rnd = generator.nextInt(v.size() - 1);
       return v.get(rnd); // Cast the vector value into a String object
   }
  
	public void playSound(){
		if(this.sounds.size()>0 && !playSound){
			soundPlaying = getRandomSound(this.sounds);
			soundPlaying.play(1f,Game.g.options.soundVolume*ratioDistance());
			playSound = true;
		}
	}
	
	private float ratioDistance(){
		return Math.min(1f, Math.max(0f, (200f*Main.ratioSpace)/Utils.distance(parent.x, parent.y, (Game.g.Xcam+Game.g.resX/2), (Game.g.Ycam+Game.g.resY/2))));
	}
}
