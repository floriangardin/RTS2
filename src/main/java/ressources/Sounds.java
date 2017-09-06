package ressources;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import display.Camera;
import model.Options;
import tests.FatalGillesError;
import utils.ObjetsList;

public class Sounds {
	// STORE ALL THE SOUNDS

	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private static HashMap<String, HashMap<String, Vector<Sound>>> soundsUnit;
	public static boolean isInit = false;
	public static void init(){
		// loading sounds
		loadRepertoire("ressources/sounds/");

		// loading soundsUnit
		soundsUnit = new HashMap<String,HashMap<String,Vector<Sound>>>();
		loadSoundsUnit();
		isInit = true;
	}

	public static Sound get(String name) {
		if(sounds.containsKey(name.toLowerCase())){
			return sounds.get(name.toLowerCase());
		} else {
			//System.out.println("Error : trying to load an non-existing sound : "+name);
			try {
				throw new FatalGillesError("non-existing sound : "+name);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static Sound getRandomSoundUnit(String unit2, String soundType2){
		String unit = unit2.toLowerCase(), soundType = soundType2.toLowerCase();
		if(soundsUnit.containsKey(unit) && soundsUnit.get(unit).containsKey(soundType)){
			if(soundsUnit.get(unit).get(soundType).size()>0){
				return soundsUnit.get(unit).get(soundType).get((int)(StrictMath.random()*soundsUnit.get(unit).get(soundType).size()));
			}
		} else {				
			try {
				throw new FatalGillesError("non-existing soundUnit : "+soundType+" "+unit);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Vector<Sound> getSoundVector(ObjetsList unit,String type){
		return soundsUnit.get(unit.name().toLowerCase()).get(type.toLowerCase());
	}

	private static void loadSoundsUnit(){
		// creating vectors
		soundsUnit.put("spearman", new HashMap<String,Vector<Sound>>());
		soundsUnit.put("crossbowman", new HashMap<String,Vector<Sound>>());
		soundsUnit.put("knight", new HashMap<String,Vector<Sound>>());
		soundsUnit.put("priest", new HashMap<String,Vector<Sound>>());
		soundsUnit.put("inquisitor", new HashMap<String,Vector<Sound>>());
		soundsUnit.put("archange", new HashMap<String,Vector<Sound>>());
		for(String s1 : soundsUnit.keySet()){
			soundsUnit.get(s1).put("death", new Vector<Sound>());
			soundsUnit.get(s1).put("selection", new Vector<Sound>());
			soundsUnit.get(s1).put("attack", new Vector<Sound>());
			soundsUnit.get(s1).put("target", new Vector<Sound>());
		}
		// prepare for loading
		File repertoire = new File("ressources/sounds/soundsUnit/");
		File[] files=repertoire.listFiles();
		String s;
		Sound sound;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".ogg")){
					// loading
					s = s.substring(0, s.length()-4);
					sound = new Sound("ressources/sounds/soundsUnit/"+s+".ogg");
					for(String s1 : soundsUnit.keySet()){
						if(s.toLowerCase().contains(s1)){
							soundsUnit.get(s1).get(s.split("_")[0]).add(sound);
						}
					}
				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
//		for(String s1 : this.soundsUnit.keySet()){
//			System.out.println("== unité " + s1);
//			System.out.println("death :" + this.soundsUnit.get(s1).get("death").size());
//			System.out.println("selection :" + this.soundsUnit.get(s1).get("death").size());
//			System.out.println("attack :" + this.soundsUnit.get(s1).get("death").size());
//			System.out.println("target :" + this.soundsUnit.get(s1).get("death").size());
//		}
	}

	private static void loadRepertoire(String name){
		File repertoire = new File(name);
		File[] files=repertoire.listFiles();
		String s;
		Sound sound;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".ogg")){
					// on load le son
					s = s.substring(0, s.length()-4);
					sound = new Sound(name+s+".ogg");
					sounds.put(s.toLowerCase(),sound);
					//					f = Images.class.getField(s);
					//					f.set(this, im);
					//this.images.put(s, new Image(name+s+".png"));
				} else if (!s.contains(".") && !s.equals("soundsUnit")){
					// nouveau répertoire
					loadRepertoire(name+s+"/");

				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public static void playSound(String name){
		if(!isInit){
			return;
		}
		Sound s = get(name);
		if(s!=null){
			s.play(1f, Options.soundVolume);
		}
	}
	public static void playSound(String name, float volume){
		if(!isInit){
			return;
		}
		Sound s = get(name);
		if(s!=null){
			s.play(1f, Options.soundVolume*volume);
		}
	}
	
	public static void playSoundAt(String name, float x, float y){
		float distance =(float)StrictMath.sqrt( (Camera.getCenterX()-x)*(Camera.getCenterX()-x)+(Camera.getCenterY()-y)*(Camera.getCenterY()-y));
		playSound(name, (float)StrictMath.exp(-distance/Camera.resX));
	}
	public static void playSoundAt(String name, float x, float y, float ratio){
		float distance =(float)StrictMath.sqrt( (Camera.getCenterX()-x)*(Camera.getCenterX()-x)+(Camera.getCenterY()-y)*(Camera.getCenterY()-y));
		playSound(name, (float)StrictMath.exp(-distance/Camera.resX)*ratio);
	}
}
