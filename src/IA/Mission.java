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
	public Vector<Character> group;
	public Objet target;
	public HashMap<Integer,Integer> requirement;
	public HashMap<Integer,Integer> units;
	public int type;
	//The higher the better
	public int priority;
	public IAPlayer ia;

	public abstract void initRequirements();
	public abstract boolean isFinished();
	public abstract void generateMissionFromRequirement();
	
	public boolean action(){
		cleanGroup();
		if(isFinished()){
			removeAllFromMission(this.group);
			return true;
		}
		if(!checkRequirement()){
			//On cherche des candidats à la mission
			candidateToMission();
			if(!checkRequirement()){
				generateMissionFromRequirement();
			}
		}
		
		return false;
	}

	public boolean checkRequirement(){
		for(Integer i : this.requirement.keySet()){
			if(this.units.get(i)<this.requirement.get(i)){
				return false;
			}
		}
		return true;
	}

	public void candidateToMission(){
		for(Character c : ia.aliveUnits){
			if(c.mission==null && units.get(c.unitType)<requirement.get(c.unitType)){
				assignToMission(c);
			}
		}
	}

	public void updateNeeded(Character c,boolean add){
		//Update the hashmap
		//Adding a unit
		if(add){
			if(units.containsKey(c.unitType)){
				units.put(c.unitType, units.get(c.unitType)+1);
			}
		}
		//Remove
		else{
			if(units.containsKey(c.unitType)){
				units.put(c.unitType, units.get(c.unitType)-1);
			}
		}
	}

	public void initHashMap(){
		this.units = new HashMap<Integer,Integer>();
		this.units.put(Character.SPEARMAN, 0);
		this.units.put(Character.CROSSBOWMAN, 0);
		this.units.put(Character.KNIGHT, 0);
		this.units.put(Character.INQUISITOR, 0);
		this.units.put(Character.PRIEST, 0);
		this.units.put(Character.ARCHANGE, 0);						
	}

	public void assignToMission(Character c){
		this.group.add(c);
		c.setTarget(this.target,null);
		if(c.mission!=null){
			c.mission.group.remove(c);
		}
		c.mission = this;
		updateNeeded(c,true);
	}

	public void assignAllToMission(Vector<Character> c){
		for(Character d : c){
			assignToMission(d);
		}
	}

	public void removeFromMission(Character c){
		this.group.remove(c);
		updateNeeded(c, false);
	}

	public void removeAllFromMission(Vector<Character> d){
		for(Character c : d){
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
