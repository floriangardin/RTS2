package madness;

import model.Civilisation.AttributsCiv;
import model.GameTeam;
import java.util.Vector;
import utils.ObjetsList;
public abstract class ObjectiveMadness {
	
	public int current ; // +n or -n (n immolation for having)
	public int objective;
	public AttributsCiv cardList;
	public GameTeam gameTeam;
	public Vector<ObjetsList> techAllowed = new Vector<ObjetsList>();
	public ObjectiveMadness(GameTeam gameTeam,int value,Vector<ObjetsList> list){
		this.gameTeam = gameTeam;
		objective = value;
		techAllowed = list; 
	}
	public boolean isCompleted(){
		return current>=objective;
	}
	public abstract void action();
	public String toString(){
		return current+"/"+objective+"isCompleted : "+isCompleted()+" "+this.getClass();
	}
}
