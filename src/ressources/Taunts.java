package ressources;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import model.Game;

public class Taunts {



	private HashMap<String, Sound> taunts;

	public boolean isPlaying = false;

	private Game game;

	public Taunts(Game game){
		this.game = game;
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
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void playTaunt(String s){
		if(taunts.containsKey(s)){
			this.game.musicPlaying.setVolume(0.05f);
//			this.game.musics.multi.setVolume(0.05f);
			taunts.get(s).play(1f, game.options.musicVolume);
			game.taunts.isPlaying = true;
		}
	}

	public void update(){
		if(isPlaying==false){
			return;
		}
		for(Sound s : this.taunts.values()){
			if(s.playing()){
				return;
			}
		}
		this.isPlaying = false;
		this.game.musicPlaying.setVolume(this.game.options.musicVolume);
	}
}
