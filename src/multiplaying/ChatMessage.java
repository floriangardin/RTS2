package multiplaying;

import org.newdawn.slick.Color;

import main.Main;
import model.Game;

public class ChatMessage {

	public String message;
	public int remainingTime;
	public Color color;
	public Color colorBody = Color.white;
	public int idPlayer;
	
	public static int basicTime = Main.framerate*2;
	public static int multiTime = Main.framerate*7;

	public ChatMessage(String message, int idPlayer) {
		this.message = message;
		this.idPlayer = idPlayer;
		switch(idPlayer){
		case 0: this.color = Color.white; this.remainingTime = basicTime; break;
		case 1: this.color = Color.blue; this.remainingTime = multiTime; break;
		case 2: this.color = Color.red; this.remainingTime = multiTime; break;
		}
	}
	
	public ChatMessage(String message, int idPlayer, Color color) {
		this.message = message;
		this.idPlayer = idPlayer;
		this.colorBody = color;
		switch(idPlayer){
		case 0: this.color = Color.white; this.remainingTime = basicTime; break;
		case 1: this.color = Color.blue; this.remainingTime = multiTime; break;
		case 2: this.color = Color.red; this.remainingTime = multiTime; break;
		}
	}

	public ChatMessage(String message){
		String[] tab = message.split("\\|");
		this.message = tab[1];
		this.remainingTime = multiTime;
		this.idPlayer = Integer.parseInt(tab[0]);
		switch(idPlayer){
		case 0: this.color = Color.white;break;
		case 1: this.color = Color.blue; break;
		case 2: this.color = Color.red; break;
		case 3: this.color = Color.green ; break;
		}
	}


	public static ChatMessage getById(String s){
		switch(s){
		case "food" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Pas assez de nourriture", 0,Color.red);
		case "building taken":
			Game.g.sounds.get("buildingTaken").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Bâtiment capturé", 0,Color.green);
		case "gold" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Pas assez d'or", 0,Color.red);
		case "faith" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Pas assez de foi", 0,Color.red);
		case "research" : 
			Game.g.sounds.get("techDiscovered").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Technologie découverte", 0,Color.green);
		case "mana" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Pas assez de mana", 0,Color.red);
		case "pop" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Limite de population atteinte", 0,Color.red);
		case "building" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Capture impossible", 0,Color.red);
		case "attack" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Vous êtes attaqué !", 0,Color.red);
		case "unit created" :
			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Unité produite", 0,Color.green);
		default : 
			return null;
		}
	}
	

	public String toString(){
		String s = "";
		s+=idPlayer+"|"+message+"|";
		return s;
	}

}
