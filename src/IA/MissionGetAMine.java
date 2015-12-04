package IA;

import java.util.HashMap;
import java.util.Vector;

import model.IAPlayer;
import units.Character;

public class MissionGetAMine extends Mission {

	public MissionGetAMine(IAPlayer ia){
		//This mission needs one spearman
		this.initHashMap();
		this.ia = ia;
		this.group = new Vector<Character>();
		this.type = Mission.ECO;
		this.target = ia.getNearestNeutralMine(ia.neutralBuilding,ia.getGameTeam().hq );
	}
	@Override
	public boolean checkRequirement() {
		
		return false;
	}

	@Override
	public boolean isFinished() {
		if(this.target==null){
			return true;
		}
		return this.target.getTeam()==this.ia.getTeam();
	}

	@Override
	public void generateMissionFromRequirement() {
	}

	@Override
	public void initRequirements() {
		this.requirement = new HashMap<Integer,Integer>();
		this.requirement.put(Character.SPEARMAN, 1);
		this.requirement.put(Character.CROSSBOWMAN, 0);
		this.requirement.put(Character.KNIGHT, 0);
		this.requirement.put(Character.INQUISITOR, 0);
		this.requirement.put(Character.PRIEST, 0);
		this.requirement.put(Character.ARCHANGE, 0);
		
	}
}
