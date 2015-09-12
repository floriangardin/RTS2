package display;

import org.newdawn.slick.Color;

public class Message {
	
	public String message;
	public int remainingTime;
	public Color color;
	public int id;
	
	public Message(int id, String message, int remainingTime, Color color) {
		this.id = id;
		this.message = message;
		this.remainingTime = remainingTime;
		this.color = color;
	}
	
	
	public static Message getById(int id){
		switch(id){
		case 0 : return new Message(id, "Not enough food", 75, Color.white);
		case 1 : return new Message(id, "Not enough gold", 75, Color.white);
		case 2 : return new Message(id, "Not enough faith", 75, Color.white);
		case 3 : return new Message(id, "Research complete", 75, Color.white);
		case 4 : return new Message(id, "Not enough mana", 75, Color.white);
		case 5 : return new Message(id, "Cannot convert this building", 75, Color.white);
		default : return null;
		}
	}

}
