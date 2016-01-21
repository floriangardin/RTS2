package model;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Taunt {



	public HashMap<String, Sound> taunts;

	public boolean isPlaying = false;

	public Game game;

	public Taunt(Game game){
		this.game = game;
		taunts = new HashMap<String, Sound>();
		File repertoire = new File("music/taunt/");
		File[] files=repertoire.listFiles();
		try {
			for(int i=0; i<files.length; i++){
				if(files[i].getName().endsWith(".ogg")){
					taunts.put(files[i].getName().substring(0, files[i].getName().length()-4),new Sound("music/taunt/"+files[i].getName()));
					//System.out.println("taunt créé : " + files[i].getName());
				}	
			} 
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void playTaunt(String s){
		System.out.println("taunt demandé: " + s);
		if(taunts.containsKey(s)){
			this.game.musics.imperial.setVolume(0.05f);
			this.game.musics.multi.setVolume(0.05f);
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
		this.game.musics.imperial.setVolume(this.game.options.musicVolume);
		this.game.musics.multi.setVolume(this.game.options.musicVolume);
	}
}
