package ressources;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import model.Options;
import tests.FatalGillesError;

public class Musics {
	
	public static Music musicPlaying = null;
	public static String musicPlayingName = null;
	private final static HashMap<String, Music> menusMusics = new HashMap<String, Music>();

	public static void init(){
		// loading musics
		loadRepertoire("ressources/musics/ingame/", menusMusics);
		loadRepertoire("ressources/musics/menus/", menusMusics);
	}

	public static Music get(String name) {
		if(menusMusics.containsKey(name.toLowerCase())){
			return menusMusics.get(name.toLowerCase());
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
	
	
	private static void loadRepertoire(String name, HashMap<String, Music> res){
		
		File repertoire = new File(name);
		File[] files= repertoire.listFiles();
		String s;
		Music music;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".ogg")){
					// on charge la musique
					s = s.substring(0, s.length()-4);
					music = new Music(name+s+".ogg");
					res.put(s.toLowerCase(),music);
				} else if (!s.contains(".")){
					// nouveau répertoire à charger
					loadRepertoire(name+s+"/", res);
				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public static void playMusic(String name){
		Music music = get(name);
		if(music != null && music != musicPlaying){
			if(musicPlaying != null){
				musicPlaying.stop();
			}
			musicPlaying = music;
			musicPlaying.loop(1f, 1f);
			musicPlayingName = name;
		}
	}
	
	public static void stopMusic(){
		if(musicPlaying != null){
			musicPlaying.stop();
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
	public static String getPlayingMusicName(){
		return musicPlayingName;
	}
}
