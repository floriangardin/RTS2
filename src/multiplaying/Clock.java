package multiplaying;

import java.io.IOException;
import java.util.Vector;

import main.Main;
import model.Game;

public class Clock extends Thread{
	Game game;
	boolean isMaster;
	private long originTime;
	Vector<Long> origins;
	//ping to master clock
	long ping;
	
	public Clock(Game g){
		this.game = g;
		this.isMaster = this.game.host;
		this.originTime = System.nanoTime();
		this.origins = new Vector<Long>();
	}
	
	
	public void run(){
		while(true){
			this.getPing();
			try {
				if(this.game.isInMenu){
					Thread.sleep(50);
				}
				else{
					Thread.sleep(50);
				}
				
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
		if(this.origins.size()>=100){
			this.origins.removeElementAt(0);
		}
		this.origins.addElement(System.nanoTime()-masterClockTime-this.ping/2);
		this.originTime =0;
		for(long o : origins){
			this.originTime += o;
		}
		System.out.println(" 61 Clock " + this.origins.size());
		this.originTime/=this.origins.size();
		//System.out.println("Clock line 61 : Synchro clock");
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
		this.ping = (this.getCurrentTime()-time);
		//System.out.println("Clock line 71 :  ping : "+this.ping);
		
	}
	
	public void setRoundFromTime(){
		long roundDuration = (long) ((1000000000f/Main.framerate));
		this.game.round =(int) (this.getCurrentTime()/roundDuration);
//		this.game.round++;
	}
	
	public long getOrigin(){
		return this.originTime;
	}
	
	
}
