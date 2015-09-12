package display;

import org.newdawn.slick.Color;

public class Message {
	
	public String message;
	public int remainingTime;
	public Color color;
	public Message(String message, int remainingTime, Color color) {
		super();
		this.message = message;
		this.remainingTime = remainingTime;
		this.color = color;
	}


}
