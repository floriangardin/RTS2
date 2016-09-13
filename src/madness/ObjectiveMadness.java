package madness;

import java.util.Vector;

import model.GameTeam;
import model.Objet;

public abstract class ObjectiveMadness {
	
	
	public int current ; // +n or -n (n immolation for having)
	public int objective;
	

	public GameTeam gameTeam;
	
	
	public ObjectiveMadness(GameTeam gameTeam){
		this.gameTeam = gameTeam;
	}
	
	
	public void getReward(){
		
	}

	public boolean isCompleted(){
		return current>=objective;
	}
	
	public abstract void action();
	
	public String toString(){
		return current+"/"+objective+"isCompleted : "+isCompleted()+" "+this.getClass();
	}
}
