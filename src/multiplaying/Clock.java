package multiplaying;

import model.Game;
import model.Player;

public class Clock {
	Game game;
	long originTime;
	
	
	public Clock(Game g){
		this.game = g;
		this.originTime = System.nanoTime();
		this.game.plateau.currentPlayer.originTime = this.originTime;
	}
	
	public long getCurrentTime(){
		return System.nanoTime()-this.originTime;
	}
	
	public void synchronize(long delay, Player p){
		//Get origin time for each player (== delay/2)
		p.originTime = this.originTime-delay/2;
	}
	
	
}
