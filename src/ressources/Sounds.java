package ressources;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import tests.FatalGillesError;
import utils.ObjetsList;

public class Sounds {
	// STORE ALL THE SOUNDS

	private HashMap<String, Sound> sounds;
	private HashMap<String, HashMap<String, Vector<Sound>>> soundsUnit;

	public Sounds(){
		// loading sounds
		this.sounds = new HashMap<String, Sound>();
		this.loadRepertoire("ressources/sounds/");

		// loading soundsUnit
		this.soundsUnit = new HashMap<String,HashMap<String,Vector<Sound>>>();
		this.loadSoundsUnit();
	}

	public Sound get(String name) {
		if(this.sounds.containsKey(name.toLowerCase())){
			return this.sounds.get(name.toLowerCase());
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

	public Sound getRandomSoundUnit(String unit2, String soundType2){
		String unit = unit2.toLowerCase(), soundType = soundType2.toLowerCase();
		if(soundsUnit.containsKey(unit) && soundsUnit.get(unit).containsKey(soundType)){
			if(this.soundsUnit.get(unit).get(soundType).size()>0){
				return this.soundsUnit.get(unit).get(soundType).get((int)(Math.random()*this.soundsUnit.get(unit).get(soundType).size()));
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
	
	public Vector<Sound> getSoundVector(ObjetsList unit,String type){

		return this.soundsUnit.get(unit.name().toLowerCase()).get(type.toLowerCase());
	}

	private void loadSoundsUnit(){
		// creating vectors
		this.soundsUnit.put("spearman", new HashMap<String,Vector<Sound>>());
		this.soundsUnit.put("crossbowman", new HashMap<String,Vector<Sound>>());
		this.soundsUnit.put("knight", new HashMap<String,Vector<Sound>>());
		this.soundsUnit.put("priest", new HashMap<String,Vector<Sound>>());
		this.soundsUnit.put("inquisitor", new HashMap<String,Vector<Sound>>());
		this.soundsUnit.put("archange", new HashMap<String,Vector<Sound>>());
		for(String s1 : this.soundsUnit.keySet()){
			this.soundsUnit.get(s1).put("death", new Vector<Sound>());
			this.soundsUnit.get(s1).put("selection", new Vector<Sound>());
			this.soundsUnit.get(s1).put("attack", new Vector<Sound>());
			this.soundsUnit.get(s1).put("target", new Vector<Sound>());
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
					for(String s1 : this.soundsUnit.keySet()){
						if(s.toLowerCase().contains(s1)){
							this.soundsUnit.get(s1).get(s.split("_")[0]).add(sound);
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

	private void loadRepertoire(String name){
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
					this.sounds.put(s.toLowerCase(),sound);
					//					f = Images.class.getField(s);
					//					f.set(this, im);
					//this.images.put(s, new Image(name+s+".png"));
				} else if (!s.contains(".") && !s.equals("soundsUnit")){
					// nouveau répertoire
					this.loadRepertoire(name+s+"/");

				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}
}
