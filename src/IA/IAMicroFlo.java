package IA;

import java.util.Vector;

import units.Character;
import units.UnitInquisitor;
import model.GameTeam;
import model.IAPlayer;
import model.Plateau;


public class IAMicroFlo extends IAPlayer {

	public IAMicroFlo(Plateau p, int id, String name, GameTeam gameteam,int resX, int resY) {
		super(p, id, name, gameteam, resX, resY);
		
	}
	
	
	public void update(){
		this.makeUnitGroup(this.getMyAliveUnits(), 0);
		Vector<Character> ch = getUnitsGroup(0);
		
		//Get ennemy units
		Vector<Character> ennemies =  getEnnemyUnitsInSight();
		
		//Aim at nearest character for each charac
		for(Character charac : ch){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
		}
		
		//Aim the wizard if possible
		
		
		
	}

}
