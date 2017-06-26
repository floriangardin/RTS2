package events;

import java.util.Random;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import display.Camera;
import main.Main;
import model.Game;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;
import utils.Utils;

public abstract class Event {

	Vector<String> sounds = new Vector<String>();
	String soundPlaying;
	EventNames name;
	int roundLaunched;
	public Objet parent;
	float power;
	float duration;
	boolean playSound = false;
	Plateau plateau;
	Camera camera;

	public Event(final Objet parent, Plateau plateau, Camera camera){
		this.parent = parent;
		this.roundLaunched = plateau.round;
		this.plateau = plateau;
		this.camera = camera;
		// TODO : Make it generic with dataFile

	}

	public String toString(){
		return this.getName().name();
	}
	public boolean isNewEvent(){
		return plateau.round-roundLaunched==0;
	}

	public int getGameTeam(){
		return parent.getTeam().id;
	}


	public abstract boolean play(Graphics g);


	//	public abstract void draw(Graphics g);


	public static String getRandomSound (Vector<String> v) {
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
			if(soundPlaying != null){
				if(this instanceof EventDeath){
					Sounds.playSound(soundPlaying, ratioDistance(((EventDeath)this).x,((EventDeath)this).y));
				} else {
					Sounds.playSound(soundPlaying,ratioDistance());
				}
			}
		playSound = true;
		}
	}

	protected float ratioDistance(){
		return Math.min(1f, Math.max(0f, (500f*Main.ratioSpace)/Utils.distance(parent.x, parent.y, (camera.Xcam+camera.resX/2), (camera.Ycam+camera.resY/2))));
	}
	protected float ratioDistance(float x, float y){
		return Math.min(1f, Math.max(0f, (500f*Main.ratioSpace)/Utils.distance(x, y, (camera.Xcam+camera.resX/2), (camera.Ycam+camera.resY/2))));
	}

	public EventNames getName(){
		return name;
	}

}
