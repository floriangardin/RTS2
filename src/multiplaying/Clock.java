package multiplaying;

import model.Game;
import model.Player;

public class Clock {
	Game game;
	boolean isMaster;
	long originTime;
	//ping to master clock
	long ping;
	
	public Clock(Game g){
		this.game = g;
		this.isMaster = this.game.isHost;
		this.originTime = System.nanoTime();
		this.game.plateau.currentPlayer.originTime = this.originTime;
	}
	
	public long getCurrentTime(){
		return System.nanoTime()-this.originTime;
	}
	
	public void updatePing(long messageTime){
		//Get origin time for each player (== delay/2)
		this.ping = this.getCurrentTime()-messageTime ;
	}
	
	public void synchro(){
		
	}
	
	public void requestTime(){
		//Send a time request for master
		
	}
	
	//We want to find the right origin time
	
	
}
