package multiplaying;

import java.io.IOException;
import java.util.Vector;

import main.Main;
import model.Game;

public class Clock extends Thread{
	Game game;
	boolean isMaster;
	public long originTime;
	Vector<Long> origins;
	//ping to master clock
	public Vector<Long> pings = new Vector<Long>();
	private long ping;
	
	public Clock(Game g){
		this.game = g;
		this.isMaster = this.game.host;
		this.originTime = System.nanoTime();
		this.origins = new Vector<Long>();
	}
	
	
	public void run(){
		boolean b = true;
		while(b){
			
			try {
				if(this.game.isInMenu){
					Thread.sleep(5000);
				}
				else{
					b=false;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				b=false;
			}
		}
	}
	
	public long getCurrentTime(){
		return System.nanoTime()-this.originTime;
	}
	
	public void updatePing(long messageTime){
		//Get origin time for each player (== delay/2)
		this.pings.addElement(this.getCurrentTime()-messageTime);
		if(this.pings.size()>100)
			this.pings.remove(0);
		this.ping = 0;
		for(Long l:this.pings)
			this.ping+=l;
		this.ping /= this.pings.size();
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
		this.originTime/=this.origins.size();
		//System.out.println("Clock line 61 : Synchro clock");
	}
	
	public long	getPing(){
		return this.ping;
	}
	
	public void setRoundFromTime(){
//		long roundDuration = (long) ((1000000000f/Main.framerate));
//		if(this.game.round == (int) (this.getCurrentTime()/roundDuration)){
//			this.game.round++;
//		}
//		else{
//			this.game.round =(int) (this.getCurrentTime()/roundDuration);	
//		}
//		
//		
//		long roundDuration = (long) ((1000000000f/Main.framerate));
//		this.game.round = (int) (this.getCurrentTime()/roundDuration);
		this.game.round++;
		
	}
	
	public long getOrigin(){
		return this.originTime;
	}
	
	
}
