package madness;

import model.Civilisation.AttributsCiv;
import model.GameTeam;
import java.util.Vector;
import utils.ObjetsList;
public abstract class Objective {
	
	public int current ; // +n or -n (n immolation for having)
	public int[] objective;
	public AttributsCiv cardList;
	public GameTeam gameTeam;
	public Vector<ObjetsList> techAllowed = new Vector<ObjetsList>();
	public Objective(GameTeam gameTeam,int[] value){
		this.gameTeam = gameTeam;
		objective = value; 
	}
	public boolean isCompleted(int act){
		if(act>0 && act<objective.length){
			return current>=objective[act];
		} else {
			return false;
		}
	}
	public abstract void action();

}
