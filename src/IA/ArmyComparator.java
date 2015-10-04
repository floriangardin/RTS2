package IA;

import units.Character;

public class ArmyComparator {

	
	public ArmyEstimator ally;
	public ArmyEstimator ennemy;
	
	//COMMON PARAMETERS
	public float distance;
	public float concave1;
	public float concave2;
	
	public ArmyComparator(ArmyEstimator a1, ArmyEstimator a2){
		ally = a1;
		ennemy = a2;
		this.computeCommonParameters();
	}
	
	public void computeCommonParameters(){
		distance = (float) Math.sqrt((ally.meanX-ennemy.meanX)*(ally.meanX-ennemy.meanX)+(ally.meanY-ennemy.meanY)*(ally.meanY-ennemy.meanY));
		float maxAngle1=0f, minAngle1=0f, maxAngle2=0f, minAngle2=0f;
		for(Character c : this.ennemy.units){
//			maxAngle1 = Math.max(maxAngle1, Math.acos(a));
//			minAngle1 = Math.min(minAngle1, Math.acos(a));
		}
		for(Character c : this.ally.units){
//			maxAngle2 = Math.max(maxAngle2, Math.acos(a));
//			minAngle2 = Math.min(minAngle2, Math.acos(a));
		}
		concave1 = Math.abs(maxAngle1 - minAngle1);
		concave2 = Math.abs(maxAngle2 - minAngle2);
		
		
	}
	
	public ArmyComparator(){
		
	}
	
	
	
	public  float[][] attribute2Matrix(){
		return null;
	}
}
