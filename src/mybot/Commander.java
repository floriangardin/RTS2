package mybot;

import bot.IA;

public abstract class Commander {

	
	IA ia;
	
	public Commander(IA ia){
		this.ia = ia;
	}
	public abstract  void play();
}
