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


	public static ChatMessage getById(String s,Game g){
		switch(s){
		case "food" :
<<<<<<< HEAD
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough food", 0,Color.red);
		case "building taken":
			g.sounds.buildingTaken.play(1f,g.options.soundVolume );
			return new ChatMessage("Building Taken", 0,Color.green);
		case "gold" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough gold", 0,Color.red);
		case "faith" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough faith", 0,Color.red);
		case "research" : 
			g.sounds.techDiscovered.play(1f,g.options.soundVolume );
			return new ChatMessage("Research complete", 0,Color.green);
		case "mana" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough mana", 0,Color.red);
		case "pop" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough room", 0,Color.red);
		case "building" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Cannot convert this building", 0,Color.red);
		case "attack" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("You are being attacked", 0,Color.red);
		case "unit created" :
			g.sounds.messageWrong.play(1f,g.options.soundVolume );
			return new ChatMessage("Unit created", 0,Color.green);
=======
			g.sounds.get("messageWrong").play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough food", 0);
		case "building taken":
			g.sounds.get("buildingTaken").play(1f,g.options.soundVolume );
			return new ChatMessage("Building Taken", 0);
		case "gold" :
			g.sounds.get("messageWrong").play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough gold", 0);
		case "faith" :
			g.sounds.get("messageWrong").play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough faith", 0);
		case "research" : 
			g.sounds.get("techDiscovered").play(1f,g.options.soundVolume );
			return new ChatMessage("Research complete", 0);
		case "mana" :
			g.sounds.get("messageWrong").play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough mana", 0);
		case "pop" :
			g.sounds.get("messageWrong").play(1f,g.options.soundVolume );
			return new ChatMessage("Not enough room", 0);
		case "building" :
			g.sounds.get("messageWrong").play(1f,g.options.soundVolume );
			return new ChatMessage("Cannot convert this building", 0);
>>>>>>> 02bf43613b3b838a40501709366385dc0c739800
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
			Game.g.sounds.messageWrong.play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Not enough room", 0,Color.red);
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
