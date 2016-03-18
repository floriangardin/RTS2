package ressources;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import tests.FatalGillesError;

public class Musics {
	
	private HashMap<String, Music> musics;
	
	public Musics(){
		// loading musics
		this.musics = new HashMap<String, Music>();
		this.loadRepertoire("ressources/musics/");
	}


	public Music get(String name) {
		if(this.musics.containsKey(name.toLowerCase())){
			return this.musics.get(name.toLowerCase());
		} else {
			//System.out.println("Error : trying to load an non-existing sound : "+name);
			try {
				throw new FatalGillesError("non-existing music : "+name);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void loadRepertoire(String name){
		File repertoire = new File(name);
		File[] files=repertoire.listFiles();
		String s;
		Music music;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".ogg")){
					// on charge la musique
					s = s.substring(0, s.length()-4);
					music = new Music(name+s+".ogg");
					this.musics.put(s.toLowerCase(),music);
				} else if (!s.contains(".")){
					// nouveau répertoire à charger
					this.loadRepertoire(name+s+"/");
				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

}
