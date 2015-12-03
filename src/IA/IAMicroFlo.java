package IA;

import java.util.Vector;

import buildings.Building;
import buildings.BuildingBarrack;
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


public class IAMicroFlo extends IAPlayer {


	Vector<Integer> priorities ;
	int mode;
	//Groups of units
	
	static int SPEARMAN=0;
	static int CROSSBOWMAN=1;
	static int KNIGHT=0;
	static int INQUISITOR=0;
	static int PRIEST=1;
	static int ARCHANGE=5;

	//GROUP OF BUILDING
	static int MILL=6;
	static int MINE = 7;
	static int BARRACK = 8;
	static int STABLE= 9;
	static int ACADEMY = 10;
	static int UNIVERSITY = 11;


	Strategy strategy ;
	int action;
	
	//OBJECTIVE ATTRIBUTE
	Objet currentObjective ;
	int productionBarrack;
	int productionStable ;
	int productionHQ;
	int productionAcademy;
	
	//Units attributes
	
	Building toControl;
	Building toProtect;

	public IAMicroFlo(Plateau p, int id, String name, GameTeam gameteam,int resX, int resY) {
		super(p, id, name, gameteam, resX, resY);
		this.priorities = new Vector<Integer>();
		this.strategy = new Strategy(Strategy.NORMAL);

		this.action = Strategy.NO_ACTION;
	}	


	public void update(){

		//MAKE UNITS IN CORRESPONDING GROUPS
		makeUnitGroups();
		//Define the mode for this round 
		aliveUnits = this.getMyAliveUnits();
		myBuildings = this.getMyBuildings();
		neutralBuilding = this.getNeutralBuildings();
		buildingToConquer = this.getEnnemyBuildings();
		
		//Get ennemy units
		Vector<Character> ennemies =  getEnnemyUnitsInSight();
		
		//Update action
		updateAction( ennemies, myBuildings);
		
		
		//Define priorities, which define objective attributes
		switch(action){
		case Strategy.GET_FOOD:
			if(this.getSpearman(aliveUnits).size()>0){
				this.toControl=getNearestNeutralMill(neutralBuilding, this.getSpearman(aliveUnits).get(0));
				this.currentObjective = this.toControl;
			}
			break;
		case Strategy.GET_GOLD:
			if(this.getSpearman(aliveUnits).size()>0){
				this.toControl=getNearestNeutralMine(neutralBuilding, this.getSpearman(aliveUnits).get(0));
				this.currentObjective = this.toControl;
			}
			break;
		case Strategy.GET_BARRACK:
			if(this.getSpearman(aliveUnits).size()>0){
				this.toControl=getNearestNeutralBarrack(neutralBuilding, this.getSpearman(aliveUnits).get(0));
				this.currentObjective = this.toControl;
			}
			break;
		case Strategy.CONQUER_ENNEMY_BARRACK:
			if(this.getSpearman(aliveUnits).size()>0){
				this.toControl=getNearestBarrackToConquer(buildingToConquer, this.getSpearman(aliveUnits).get(0));
				this.currentObjective = this.toControl;
			}
			break;
		case Strategy.CONQUER_ENNEMY_MILL:
			if(this.getSpearman(aliveUnits).size()>0){
				this.toControl=getNearestMillToConquer(buildingToConquer, this.getSpearman(aliveUnits).get(0));
				this.currentObjective = this.toControl;
			}
			break;
		case Strategy.CONQUER_ENNEMY_MINE:
			if(this.getSpearman(aliveUnits).size()>0){
				this.toControl=getNearestMineToConquer(buildingToConquer, this.getSpearman(aliveUnits).get(0));
				this.currentObjective = this.toControl;
			}
			break;
		case Strategy.CONQUER_ENNEMY_HQ:
			if(this.getSpearman(aliveUnits).size()>0){
				this.toControl=getNearestHQToConquer(buildingToConquer, this.getSpearman(aliveUnits).get(0));
				this.currentObjective = this.toControl;
			}
			break;
		default:
			break;

		}
		
		
		//Selon le mode on fait des choses differentes
		//handle type of unit separately
		handleSpearman(ennemies,getSpearman(aliveUnits));
		handleCrossbowman(ennemies,getCrossbowman(aliveUnits));
		handleKnight(ennemies,getKnight(aliveUnits));
		handlePriest(ennemies,getPriest(aliveUnits));
		handleInquisitor(ennemies,getInquisitor(aliveUnits));

		//BUILDINGS
		handleBarrack(ennemies,getBarrack(myBuildings));
		handleHeadQuarters(ennemies,getHeadQuarters(myBuildings));
		handleMine(ennemies,getMine(myBuildings));
		handleMill(ennemies,getMill(myBuildings));
		handleUniversity(ennemies,getUniversity(myBuildings));
		handleAcademy(ennemies,getAcademy(myBuildings));
		handleStable(ennemies,getStable(myBuildings));
	}


	
	public void updateAction(Vector<Character> ennemies, Vector<Building> barrack){
		switch(action){
		case Strategy.NO_ACTION:
			this.action = this.strategy.getNextAction();
			break;
		case Strategy.GET_FOOD:
			if(this.currentObjective!=null && this.currentObjective.getTeam()==this.getTeam()){
				this.action = this.strategy.getNextAction();
			}
			break;
		case Strategy.GET_GOLD:
			if(this.currentObjective!=null && this.currentObjective.getTeam()==this.getTeam()){
				this.action = this.strategy.getNextAction();
			}
			break;
		case Strategy.GET_BARRACK:
			if(this.currentObjective!=null && this.currentObjective.getTeam()==this.getTeam()){
				this.action = this.strategy.getNextAction();
			}
			break;
		case Strategy.CONQUER_ENNEMY_BARRACK:
			if(this.currentObjective!=null && this.currentObjective.getTeam()==this.getTeam()){
				this.action = this.strategy.getNextAction();
			}
			break;
		case Strategy.CONQUER_ENNEMY_MILL:
			if(this.currentObjective!=null && this.currentObjective.getTeam()==this.getTeam()){
				this.action = this.strategy.getNextAction();
			}
			break;
		case Strategy.CONQUER_ENNEMY_MINE:
			if(this.currentObjective!=null && this.currentObjective.getTeam()==this.getTeam()){
				this.action = this.strategy.getNextAction();
			}
			break;
		case Strategy.CONQUER_ENNEMY_HQ:
			if(this.currentObjective!=null && this.currentObjective.getTeam()==this.getTeam()){
				this.action = this.strategy.getNextAction();
			}
			break;
		case Strategy.MAKE_SPEARMAN:
				this.productionBarrack = SPEARMAN;
				this.action = this.strategy.getNextAction();
			break;
		case Strategy.MAKE_CROSSBOWMAN:
				this.productionBarrack = CROSSBOWMAN;
				this.action = this.strategy.getNextAction();
			break;
		default:
			break;

		
		}
		
	}
	private void handleStable(Vector<Character> ennemies, Vector<Building> stable2) {
		// TODO Auto-generated method stub
		
	}


	private void handleAcademy(Vector<Character> ennemies, Vector<Building> academy2) {
		// TODO Auto-generated method stub
		
	}


	private void handleUniversity(Vector<Character> ennemies, Vector<Building> university2) {
		// TODO Auto-generated method stub
		
	}


	private void handleMill(Vector<Character> ennemies, Vector<Building> mill2) {
		// TODO Auto-generated method stub
		
	}


	private void handleMine(Vector<Character> ennemies, Vector<Building> mine2) {
		// TODO Auto-generated method stub
		
	}


	private void handleHeadQuarters(Vector<Character> ennemies, Vector<Building> headQuarters) {
		// TODO Auto-generated method stub
		
	}


	private void handleBarrack(Vector<Character> ennemies, Vector<Building> barrack) {
		if(barrack.size()>0){
			if(productionBarrack==SPEARMAN){
				for(Building b : barrack){
					if(getFood()>= UnitsList.Spearman.foodPrice && getGold()>= UnitsList.Spearman.goldPrice && getSpecial()>= UnitsList.Spearman.specialPrice ){
						((BuildingBarrack) b).product(BuildingBarrack.SPEARMAN);
					}
				}
			}
			else if(productionBarrack==CROSSBOWMAN){
				for(Building b : barrack){
					if(getFood()>= UnitsList.Crossbowman.foodPrice && getGold()>= UnitsList.Crossbowman.goldPrice && getSpecial()>= UnitsList.Crossbowman.specialPrice ){
						((BuildingBarrack) b).product(BuildingBarrack.CROSSBOWMAN);
					}
				}
			}
		}
		
	}

	private void handleBuilding(Vector<Character> ennemies) {
		if(this.action==Strategy.MAKE_SPEARMAN){
			
		}
	}
	public void handleSpearman(Vector<Character> ennemies,Vector<Character> units){
		for(Character charac : units){
			Character c = IAUtils.nearestUnit(ennemies, charac);
			if(c!=null){
				charac.setTarget(c);
			}
			else{
				charac.setTarget(currentObjective);
			}
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
				}
			}
		}
	}
	public void handleCrossbowman(Vector<Character> ennemies,Vector<Character> units){
	
		for(Character charac : units){
			Character c = IAUtils.nearestUnit(ennemies, charac);
			if(c!=null){
				charac.setTarget(c);
			}
			else{
				charac.setTarget(currentObjective);
			}
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
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

					charac.setTarget(new Checkpoint(charac.getX()+10f*dirX,charac.getY()+10f*dirY));
				}
			}

		}
	}


	public void handleKnight(Vector<Character> ennemies,Vector<Character> units){
		
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
				}
			}
		}
	}
	public void handlePriest(Vector<Character> ennemies,Vector<Character> units){
		
		for(Character charac : units){
			Character targetConversion = IAUtils.nearestUnit(ennemies, charac);
			SpellConversion sp = (SpellConversion) charac.spells.get(1);
			if(this.getGameTeam().special>=sp.faithCost && targetConversion!=null){
				sp.launch(targetConversion, charac);
			}
		}
	}

	public void handleInquisitor(Vector<Character> ennemies,Vector<Character> units){
	
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
				}
			}
			if(charac.state<charac.chargeTime){
				Character c1 = IAUtils.nearestUnit(ennemies, charac);
				if(c1==null){
					continue;
				}
				if(Utils.distance(c1, charac)==-1f || Utils.distance(c1, charac)>charac.range){
					continue;
				}
				//TODO find a better direction
				ennemies.remove(c1);
				Character c2 = IAUtils.nearestUnit(ennemies, charac);
				if(c2!=null){
					float norm = Utils.distance(c1, c2);
					float dirX = c1.getY()-c2.getY();
					float dirY = c2.getX()-c1.getX();
					dirX /=norm;
					dirY /= norm;

					//Check if this is 

					charac.setTarget(new Checkpoint(charac.getX()+10f*dirX,charac.getY()+10f*dirY));
				}
			}
		}
	}
	public void handleArchange(Vector<Character> ennemies,Vector<Character> units){
		
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
		}
	}



	public void makeUnitGroups(){
		Vector<Character> alive = this.getMyAliveUnits();

		//Split in 5 groups
		for(Character c : alive){
			Vector<Character> toAdd = new Vector<Character>();
			toAdd.add(c);
			if(c instanceof UnitSpearman){

				this.addInUnitGroup(toAdd,SPEARMAN);
			}
			else if(c instanceof UnitCrossbowman){
				this.addInUnitGroup(toAdd,CROSSBOWMAN);
			}
			else if(c instanceof UnitKnight){
				this.addInUnitGroup(toAdd,KNIGHT);
			}
			else if(c instanceof UnitInquisitor){
				this.addInUnitGroup(toAdd,INQUISITOR);
			}
			else if(c instanceof UnitPriest){
				this.addInUnitGroup(toAdd,PRIEST);
			}
			else if(c instanceof UnitArchange){
				this.addInUnitGroup(toAdd,ARCHANGE);
			}
		}
	}

}
