package IA;

import java.util.HashMap;
import java.util.Vector;

import model.Checkpoint;
import model.GameTeam;
import model.IAPlayer;
import model.Objet;
import model.Plateau;
import model.Utils;
import spells.SpellConversion;
import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;
import units.UnitsList;
import buildings.Building;
import buildings.BuildingBarrack;
import buildings.BuildingProduction;
import buildings.BuildingTech;


public class IABasic extends IAPlayer {

	public IABasic(Plateau p, int id, String name, GameTeam gameteam,int resX, int resY) {
		super(p, id, name, gameteam, resX, resY);
	}	


	public void update(){
		//give a mission in function of.
		updateMission();
		//Product if possible
		product();
	}

	public void product(){
		//Production in barrack if possible
		Vector<Building> barracks = getBarrack(myBuildings);
		Vector<BuildingProduction> d = new Vector<BuildingProduction>();
		for(Building b : barracks){
			d.add((BuildingProduction) b);
		}
		//Sort by queue size
		if(barracks.size()>0){
			for(BuildingProduction b : d){
				if(b.queue.size()==0){
					//Product in function of composition
					int n_spearman = getSpearman(aliveUnits).size() ;
					int n_crossbowman = getCrossbowman(aliveUnits).size() ;
					//Equilibrate army
					if(n_spearman>n_crossbowman && this.getMine(myBuildings).size()>0 && b.product(BuildingBarrack.CROSSBOWMAN)){
					}
					else{
						b.product(BuildingBarrack.SPEARMAN);
					}
				}
			}
		}
	}
	
	public void updateMission(){
		if(this.getMill(myBuildings).size()==0 && this.getIdleSpearman(aliveUnits).size()>0){
			Mission m = new MissionGetABuilding(this,this.getNearestNeutralMill(neutralBuilding,this.getGameTeam().hq));
			m.assignAllToMission(this.getIdleSpearman(aliveUnits));	
		}
		if(this.getMine(myBuildings).size()==0 && this.getIdleSpearman(aliveUnits).size()>0){
			Mission m = new MissionGetABuilding(this,this.getNearestNeutralMine(neutralBuilding,this.getGameTeam().hq));
			m.assignAllToMission(this.getIdleSpearman(aliveUnits));	
		}
		if(this.getBarrack(myBuildings).size()==0 && this.getIdleSpearman(aliveUnits).size()>0){
			Mission m = new MissionGetABuilding(this,this.getNearestNeutralBarrack(neutralBuilding,this.getGameTeam().hq));
			m.assignAllToMission(this.getIdleSpearman(aliveUnits));	
		}
		if(this.getBarrack(myBuildings).size()==0 && this.getIdleSpearman(aliveUnits).size()>0){
			Mission m = new MissionGetABuilding(this,this.getNearestNeutralBarrack(neutralBuilding,this.getGameTeam().hq));
			m.assignAllToMission(this.getIdleSpearman(aliveUnits));	
		}
		if(aliveUnits.size()>5){
			abortAllMission();
			//Guerre
			Mission m = new MissionGetABuilding(this,this.getNearestHQToConquer(buildingToConquer,this.getGameTeam().hq));
			m.assignAllToMission(aliveUnits);
		}
		//EMERGENCY MISSIONS
	}

	@Deprecated
	public void handleCrossbowman(Vector<Character> ennemies,Vector<Character> units){

		for(Character charac : units){
			Character c = IAUtils.nearestUnit(ennemies, charac);
			if(c!=null){
				charac.setTarget(c);
			}
			else{
				//charac.setTarget(currentObjective);
			}
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(p,0f,0f), charac);
				}
			}

			//TODO : handle hit and run
			//Given ennemies considering charge run away from ennemies ( stay at range)
			//Get first and second nearest ennemy, move in orthogonal direction
			if(charac.state<charac.chargeTime){
				Character c1 = IAUtils.nearestUnit(ennemies, charac);
				if( Utils.distance(c1, charac)==-1f || Utils.distance(c1, charac)>charac.range){
					continue;
				}
				ennemies.remove(c1);
				Character c2 = IAUtils.nearestUnit(ennemies, charac);
				if(c2!=null){
					float norm = Utils.distance(c1, c2);
					float dirX = c1.getY()-c2.getY();
					float dirY = c2.getX()-c1.getX();
					dirX /=norm;
					dirY /= norm;

					//Check if this is 

					charac.setTarget(new Checkpoint(p,charac.getX()+10f*dirX,charac.getY()+10f*dirY));
				}
			}

		}
	}


}
