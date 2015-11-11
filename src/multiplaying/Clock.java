package multiplaying;

import java.io.IOException;
import java.util.Vector;

import model.Game;

public class Clock extends Thread{
	Game game;
	boolean isMaster;
	long originTime;
	Vector<Long> origins;
	//ping to master clock
	long ping;
	
	public Clock(Game g){
		this.game = g;
		this.isMaster = this.game.isHost;
		this.originTime = System.nanoTime();
		this.game.plateau.currentPlayer.originTime = this.originTime;
		this.origins = new Vector<Long>();
	}
	
	
	public void run(){
		while(true){
			this.getPing();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public long getCurrentTime(){
		return System.nanoTime()-this.originTime;
	}
	
	public void updatePing(long messageTime){
		//Get origin time for each player (== delay/2)
		this.ping = this.getCurrentTime()-messageTime ;
	}
	
	public void synchro(long masterClockTime){
		if(!(this.origins.size()<10)){
			this.origins.removeElementAt(0);
		}
		this.origins.addElement(System.nanoTime()-masterClockTime-this.ping/2);
		for(long o : origins){
			this.originTime += o;
		}
		this.originTime/=this.origins.size();
		
	}
	
	public void	getPing(){
		
		//Send a time request for master
		Process p2;
		long time = this.getCurrentTime();
		try {
			p2=Runtime.getRuntime().exec("ping -n 1 "+this.game.addressHost.getHostAddress());
			p2.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ping = (this.getCurrentTime()-time)/5;
		
	}
	
	//We want to find the right origin time
	
	
}
