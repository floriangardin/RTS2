package ressources;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import model.Options;
import tests.FatalGillesError;

public class Musics {
	
	private static Music musicPlaying = null;
	
	private static HashMap<String, Music> musics;
	
	public static void init(){
		// loading musics
		musics = new HashMap<String, Music>();
		loadRepertoire("ressources/musics/");
	}

	public static Music get(String name) {
		if(musics.containsKey(name.toLowerCase())){
			return musics.get(name.toLowerCase());
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

	private static void loadRepertoire(String name){
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
					musics.put(s.toLowerCase(),music);
				} else if (!s.contains(".")){
					// nouveau répertoire à charger
					loadRepertoire(name+s+"/");
				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public static void playMusic(String name){
		Music music = get(name);
		if(music != null){
			if(musicPlaying != null){
				musicPlaying.stop();
			}
			musicPlaying = music;
			musicPlaying.play(1f, 1f);
		}
	}
	
	public static void playMusicFading(String name){
		int durationFade = 1000;
		Music m = get(name);
		if(m==musicPlaying){
			return;
		}
		if(musicPlaying!=null){
			musicPlaying.fade(durationFade,0,true);
		}
		playMusic(name);
		musicPlaying.setVolume(0);
		musicPlaying.fade(durationFade,Options.musicVolume,false);
	}
	
	public static Music getPlayingMusic(){
		return musicPlaying;
	}
}
