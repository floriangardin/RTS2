package display;

import org.newdawn.slick.Color;

public enum Message {

	NotEnoughFood(0,"Not enough food",75,Color.white),
	NotEnoughGold(1,"Not enough gold",75,Color.white),
	NotEnoughFaith(2,"Not enough faith",75,Color.white),
	ResearchComplete(3,"Research complete",75,Color.white),
	NotEnoughMana(3,"Not enough mana",75,Color.white);
	
	public String message;
	public int remainingTime;
	public Color color;
	public int id;
	
	Message(int id, String message, int remainingTime, Color color) {
		this.id = id;
		this.message = message;
		this.remainingTime = remainingTime;
		this.color = color;
	}
	
	public static Message getById(int id){
		switch(id){
		case 0 : return Message.NotEnoughFood;
		case 1 : return Message.NotEnoughGold;
		case 2 : return Message.NotEnoughFaith;
		case 3 : return Message.ResearchComplete;
		default : return null;
		}
	}

}
