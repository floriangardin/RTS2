package IA;

import java.util.Vector;

import battleIA.Mission;
import model.IAPlayer;
import model.Objet;
import units.Character;
import buildings.Building;

public class MissionAttackABuilding extends Mission {

	public Character ennemy;
	

	public MissionAttackABuilding(IAPlayer ia,Building target){
		initialize(ia);
		this.target = target;
	}
	
	@Override
	public boolean isFinished() {
		if(this.target==null){
			return true;
		}
		if(hasFailed()){
			return true;
		}
		//Define success case
		return this.target.getTeam()==this.ia.getTeam();
	}

	public boolean action(){
		cleanGroup();
		if(isFinished()){
			removeAllFromMission(this.group);
			return true;
		}
		return false;
	}
	
	public void assignToMission(Character c){

		if(c.mission!=null){
			c.mission.removeFromMission(c);
		}
		this.group.add(c);
		c.setTarget(this.target,null,Character.TAKE_BUILDING);
		c.mission = this;
	}
	@Override
	public void initialize(IAPlayer ia) {
		
		this.ia = ia;
		this.ia.missions.addElement(this);
		this.group = new Vector<Character>();
		this.type = Mission.OFFENSIVE;
		
	}
	
	protected boolean checkMissionStillPossible(){
		
		return false;
		
	}

	@Override
	public boolean hasFailed() {
		//Reussir ou mourir
		return this.group.size()==0 ;
		
	}
}
