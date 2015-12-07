package multiplaying;

import org.newdawn.slick.Color;

import main.Main;

public class ChatMessage {

	public String message;
	public int remainingTime;
	public Color color;
	public int idPlayer;

	public static int basicTime = Main.framerate*2;

	public ChatMessage(String message, int idPlayer) {
		this.message = message;
		this.remainingTime = basicTime;
		this.idPlayer = idPlayer;
		switch(idPlayer){
		case 0: this.color = Color.white;break;
		case 1: this.color = Color.blue; break;
		case 2: this.color = Color.red; break;
		}
	}

	public ChatMessage(String message){
		String[] tab = message.split("\\|");
		this.message = tab[1];
		this.remainingTime = basicTime;
		this.idPlayer = Integer.parseInt(tab[0]);
		switch(idPlayer){
		case 0: this.color = Color.white;break;
		case 1: this.color = Color.blue; break;
		case 2: this.color = Color.red; break;
		}
	}


	public static ChatMessage getById(String s){
		switch(s){
		case "food" : return new ChatMessage("Not enough food", 0);
		case "gold" : return new ChatMessage("Not enough gold", 0);
		case "faith" : return new ChatMessage("Not enough faith", 0);
		case "research" : return new ChatMessage("Research complete", 0);
		case "mana" : return new ChatMessage("Not enough mana", 0);
		case "pop" : return new ChatMessage("Not enough room", 0);
		case "building" : return new ChatMessage("Cannot convert this building", 0);
		default : return null;
		}
	}

	public String toString(){
		String s = "";
		s+=idPlayer+"|"+message+"|";
		return s;
	}

}
