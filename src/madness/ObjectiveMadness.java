package madness;

import java.util.Vector;

import model.GameTeam;
import model.Objet;

public abstract class ObjectiveMadness {
	
	
	public int reward ; // +n or -n (n immolation for having)
	public GameTeam gameTeam;
	
	
	public ObjectiveMadness(GameTeam gameTeam){
		this.gameTeam = gameTeam;
	}
	
	
	
	public void getReward(){
		
	}
	
	public boolean action(){
		
		if(checkIfCompleted()){
			gameTeam.addMadness(reward);
			return true;
		}
		return false;
		
	}
	
	public abstract boolean checkIfCompleted();
}
