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
	public long ping;
	
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
		this.ping = (this.getCurrentTime()-time)/1;
		//System.out.println("Clock line 71 :  ping : "+this.ping);
		
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
