package multiplaying;

import org.newdawn.slick.Color;

import main.Main;
import model.Game;

public class ChatMessage {

	public String message;
	public int remainingTime;
	public Color color;
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

	public ChatMessage(String message){
		String[] tab = message.split("\\|");
		this.message = tab[1];
		this.remainingTime = multiTime;
		this.idPlayer = Integer.parseInt(tab[0]);
		switch(idPlayer){
		case 0: this.color = Color.white;break;
		case 1: this.color = Color.blue; break;
		case 2: this.color = Color.red; break;
		}
	}


	public static ChatMessage getById(String s,Game g){
		switch(s){
		case "food" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough food", 0);
		case "gold" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough gold", 0);
		case "faith" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough faith", 0);
		case "research" : 
			g.sounds.techDiscovered.play(1f,g.options.soundVolume );
			return new ChatMessage("Research complete", 0);
		case "mana" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough mana", 0);
		case "pop" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough room", 0);
		case "building" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Cannot convert this building", 0);
		default : 
			return null;
		}
	}
	
	public static ChatMessage getById(String s){
		switch(s){
		case "food" : 
			return new ChatMessage("Not enough food", 0);
		case "gold" : 
			return new ChatMessage("Not enough gold", 0);
		case "faith" : 
			return new ChatMessage("Not enough faith", 0);
		case "research" : 
			return new ChatMessage("Research complete", 0);
		case "mana" : 
			return new ChatMessage("Not enough mana", 0);
		case "pop" : 
			return new ChatMessage("Not enough room", 0);
		case "building" : 
			return new ChatMessage("Cannot convert this building", 0);
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
