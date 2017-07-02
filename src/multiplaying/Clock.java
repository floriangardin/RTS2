package multiplaying;

import java.io.IOException;
import java.util.Vector;

import main.Main;
import model.Game;

public class Clock {
	Game game;
	boolean isMaster;
	public long originTime;
	Vector<Long> origins;
	//ping to master clock
	public Vector<Long> pings = new Vector<Long>();
	public long ping;
	public long lastPing;
	
	public Clock(){
//		this.isMaster = Game.host;
		this.originTime = System.nanoTime();
		this.origins = new Vector<Long>();
	}
	
	
	public long getCurrentTime(){
		return System.nanoTime()-this.originTime;
	}
	
	public void updatePing(long messageTime){
		//Get origin time for each player (== delay/2)
		long calculatedPing = this.getCurrentTime()-messageTime;
		if((calculatedPing>40*1e6)|| calculatedPing<0 ){
//			System.out.println("Ping de batard : "+calculatedPing);
			
			return;
		}

		this.pings.addElement(calculatedPing);
		if(this.pings.size()>8)
			this.pings.removeElementAt(0);
		this.ping = 0;
		for(Long l:this.pings)
			this.ping+=l;
		this.ping /= this.pings.size();
	}
	
	public void synchro(long masterClockTime){
		if(this.origins.size()>=100){
			this.origins.removeElementAt(0);
		}
		this.origins.addElement(System.nanoTime()-(masterClockTime+this.ping/2));
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
		
		
	}
	
	public long getOrigin(){
		return this.originTime;
	}
	
	
}
