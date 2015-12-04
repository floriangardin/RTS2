package IA;

import java.util.HashMap;
import java.util.Vector;

import model.IAPlayer;
import model.Objet;
import units.Character;

public abstract class Mission {

	public static int ECO = 0;
	public static int OFFENSIVE =1;
	public static int DEFENSIVE = 2;

	public Vector<Mission> submissions;
	protected Vector<Character> group;
	protected Objet target;
	public int type;
	//The higher the better
	public int priority;
	public IAPlayer ia;

	protected abstract boolean checkMissionStillPossible();
	public abstract void initialize(IAPlayer ia);
	public abstract boolean isFinished();
	public abstract boolean hasFailed();

	public boolean action(){
		cleanGroup();
		if(isFinished()){
			removeAllFromMission(this.group);
			return true;
		}
		return false;
	}

	public void abortMission(){
		removeAllFromMission(group);
	}
	public void candidateToMission(){
		for(Character c : ia.aliveUnits){
			if(c.mission==null){
				assignToMission(c);
			}
		}
	}

	public void assignToMission(Character c){

		if(c.mission!=null){
			c.mission.removeFromMission(c);
		}
		this.group.add(c);
		c.setTarget(this.target,null);
		c.mission = this;
	}

	public void assignAllToMission(Vector<Character> c){
		for(Character d : c){
			assignToMission(d);
		}
	}

	public void removeFromMission(Character c){
		this.group.remove(c);
		c.mission = null;
		c.setTarget(null);
	}

	public void removeAllFromMission(Vector<Character> d){
		Vector<Character> copyD = new Vector<Character>();
		copyD.addAll(d);
		for(Character c : copyD){
			removeFromMission(c);
		}
	}

	public void cleanGroup(){
		Vector<Character> toRemove = new Vector<Character>();
		for(Character c : group){
			if(!ia.aliveUnits.contains(c)){
				toRemove.add(c);
			}
		}
		removeAllFromMission(toRemove);
	}

}
