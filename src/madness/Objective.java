package madness;

import model.Civilisation.AttributsCiv;
import model.GameTeam;
import java.util.Vector;
import utils.ObjetsList;
public abstract class Objective {
	
	public int[] objective;
	public boolean madness;
	public AttributsCiv cardList;
	public GameTeam gameTeam;
	public Vector<ObjetsList> techAllowed = new Vector<ObjetsList>();
	public Objective(GameTeam gameTeam,int[] value, boolean madness){
		this.gameTeam = gameTeam;
		objective = value; 
		this.madness = madness;
	}
	public boolean isCompleted(int act){
		int current = (madness ? 1 : -1)*this.gameTeam.civ.madness;
		if(act<objective.length){
			return current>=objective[act];
		} else {
			return false;
		}
	}
	public abstract boolean action();

}
