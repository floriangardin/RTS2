package display;

import org.newdawn.slick.Color;

public class Message {
	
	public String message;
	public int remainingTime;
	public Color color;
	public int id;
	
	public Message(String message, int remainingTime, Color color) {
		this.message = message;
		this.remainingTime = remainingTime;
		this.color = color;
	}
	
	
	public static Message getById(String s){
		switch(s){
		case "food" : return new Message("Not enough food", 75, Color.white);
		case "gold" : return new Message("Not enough gold", 75, Color.white);
		case "faith" : return new Message("Not enough faith", 75, Color.white);
		case "research" : return new Message("Research complete", 75, Color.white);
		case "mana" : return new Message("Not enough mana", 75, Color.white);
		case "building" : return new Message("Cannot convert this building", 75, Color.white);
		default : return null;
		}
	}

}
