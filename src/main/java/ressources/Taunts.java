package ressources;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import model.Options;

public class Taunts {



	private static HashMap<String, Sound> taunts;

	public static boolean isPlaying = false;

	private static boolean isInit;


	public static void init(){
		taunts = new HashMap<String, Sound>();
		File repertoire = new File("ressources/taunts/");
		File[] files=repertoire.listFiles();
		try {
			for(int i=0; i<files.length; i++){
				if(files[i].getName().endsWith(".ogg")){
					taunts.put(files[i].getName().substring(0, files[i].getName().length()-4),new Sound("ressources/taunts/"+files[i].getName()));
					//System.out.println("taunt créé : " + files[i].getName());
				}	
			} 
			isInit = true;
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static void playTaunt(String s){
		if(taunts.containsKey(s) && !isPlaying){
			if(Musics.getPlayingMusic()!=null){
				Musics.getPlayingMusic().setVolume(0.05f);
			}
//			this.game.musics.multi.setVolume(0.05f);
			taunts.get(s).play(1f, Options.musicVolume);
			isPlaying = true;
		}
	}

	public static void update(){
		if(isPlaying==false){
			return;
		}
		for(Sound s : taunts.values()){
			if(s.playing()){
				return;
			}
		}
		isPlaying = false;
		if(Musics.getPlayingMusic()!=null){
			Musics.getPlayingMusic().setVolume(Options.musicVolume);
		}
	}

	public static boolean isInit() {
		// TODO Auto-generated method stub
		return isInit;
	}
}
