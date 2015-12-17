package battleIA;

import java.util.HashMap;
import java.util.Vector;

import battleIA.IAStateOfGame.BuildingIA;
import battleIA.IAStateOfGame.UnitIA;
import buildings.Building;
import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingHeadQuarters;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingTower;
import buildings.BuildingUniversity;
import buildings.BuildingsList;
import model.Checkpoint;
import model.Objet;
import model.Plateau;
import model.Utils;
import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;

public final class IAfunctions {

	private int currentTeam;
	private Plateau p;
	public IAStateOfGame plateau;

	public IAfunctions(Plateau p, int currentTeam){
		this.p = p;
		this.currentTeam = currentTeam;
	}

	// field to be used
	public Vector<Vector<UnitIA>> unitsGroups;
	public Vector<Vector<BuildingIA>> buildingGroups;

	public Vector<UnitIA> aliveUnits;
	public Vector<BuildingIA> myBuildings;
	//Define the mode for this round 

	public Vector<BuildingIA> neutralBuilding;
	public Vector<BuildingIA> buildingToConquer;
	public Vector<UnitIA> ennemiesInSight;

	public Vector<Mission> missions;
	public Vector<Mission> pastMissions;
	public Vector<Mission> pausedMissions;
	
	public void update(){
		//Main method which is called every iteration on the plateau and common to all 
		//Define the mode for this round 
		aliveUnits = getMyAliveUnits();
		myBuildings = getMyBuildings();
		neutralBuilding = getNeutralBuildings();
		buildingToConquer = getEnnemyBuildings();
		ennemiesInSight =  getEnnemyUnitsInSight();
		this.updateSelection();
		//Update the units groups and building groups ( To remove death ...)
		this.updateGroups();
	}
	

	public void setAttackOrder(int idAlliedPlayer, int idTarget) throws IAException{
		/**
		 * function that set the order to the UnitIA with id 'idAlliedPlayer'
		 * to attack the UnitIA with id 'idTarget'
		 * 
		 * The UnitIA will pursue and attack its target as long as 
		 * it remains in sight and alive
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player 
		 * or idTarget not an ennemy
		 */
		Character ally = null, enemy = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
			if(c.id == idTarget){
				if(c.getTeam() == currentTeam){
					throw new IAException(currentTeam, "Vous ne pouvez pas cibler une de vos unités pour l'attaque");
				} else {
					enemy = c;
				}
			}
		}
		if(ally==null || enemy==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else {
			ally.setTarget(enemy);
			// TODO : set mode ? 
			//			ally.mode = 0;
		}
	}

	public void setHoldPositionOrder(int idAlliedPlayer) throws IAException{
		/**
		 * function that orders the UnitIA with id 'idAlliedPlayer'
		 * to hold Position and attack any enemy unit in range
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else {
			ally.setTarget(null);
			ally.stop();
			ally.mode = Character.HOLD_POSITION;
		}
	}

	public void setMoveOrder(int idAlliedPlayer, float xToGo, float yToGo) throws IAException{
		/**
		 * function that orders the UnitIA with id 'idAlliedPlayer'
		 * to move to the selected location. The UnitIA won't attack 
		 * until it arrives to the location
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied 
		 * player or location out of the map
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else {
			ally.setTarget(null);
			ally.stop();
			ally.mode = Character.HOLD_POSITION;
		}
	}

	public void setAggressiveOrder(int idAlliedPlayer, float xToGo, float yToGo) throws IAException{
		/**
		 * function that orders the UnitIA with id 'idAlliedPlayer'
		 * to move in an aggressive fashion to the selected location. 
		 * The UnitIA will attack every enemy unit in sight until 
		 * it arrives to the location
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied 
		 * player or location out of the map
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(xToGo<0 || xToGo>=p.maxX || yToGo<0 || yToGo>p.maxY){
			throw new IAException(currentTeam, "Vous ne pouvez pas donner l'ordre d'aller en dehors du plateau");
		} else {
			ally.setTarget(new Checkpoint(p, xToGo,yToGo));
			ally.mode = Character.AGGRESSIVE;
		}
	}

	public void setTakeBuildingOrder(int idAlliedPlayer, int idBuildingToTake) throws IAException{
		/**
		 * function that orders the UnitIA with id 'idAlliedPlayer'
		 * to take the building with id 'idBuildingToTake'
		 * 
		 * the unit won't attack any other unit till the building is taken
		 * 
		 * if the building is already yours the unit will stand and defend it, 
		 * but won't attack either
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 * if idBuildingToTake is not an existing building
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		Building buildingToTake = null;
		for(Building b : this.p.buildings){
			if(b.id == idAlliedPlayer){
				if(b.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					buildingToTake = b;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(buildingToTake==null){
			throw new IAException(currentTeam, "Le batiment ciblé n'existe pas");
		} else {
			ally.setTarget(buildingToTake);
		}
	}

	public void setTakeEnemyHeadQuarterOrder(int idAlliedPlayer)  throws IAException{
		/**
		 * function that orders the UnitIA with id 'idAlliedPlayer'
		 * to take the enemy headquarters
		 * 
		 * the unit won't attack any other unit till the building is taken
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		Building buildingToTake = null;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingHeadQuarters && b.getTeam()!=currentTeam){
				buildingToTake = b;
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(buildingToTake==null){
			IAException e = new IAException(currentTeam, "Tiens Kévin a encore fait de la merde en codant... => IA function ligne 205");
			e.printStackTrace();
			throw e;
		} else {
			ally.setTarget(buildingToTake);
		}
	}

	public void setDefendHeadQuarterOrder(int idAlliedPlayer)  throws IAException{
		/**
		 * function that orders the UnitIA with id 'idAlliedPlayer'
		 * to defend your own headquarters
		 * 
		 * the unit won't attack any other unit until its order changes
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		Building buildingToTake = null;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingHeadQuarters && b.getTeam()==currentTeam){
				buildingToTake = b;
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(buildingToTake==null){
			IAException e = new IAException(currentTeam, "Tiens Kévin a encore fait de la merde en codant... => IA function ligne 205");
			e.printStackTrace();
			throw e;
		} else {
			ally.setTarget(buildingToTake);
		}
	}

	//INTERN METHODS

	//units
	private Vector<UnitIA> getUnitsFromTeam(int team, boolean ally){
		Vector<UnitIA> result = new Vector<UnitIA>();
		for(UnitIA c : this.plateau.units){
			if((ally && c.team==team) || (!ally && c.team!=team))
				result.add(c);
		}
		return result;
	}
	public Vector<UnitIA> getMyAliveUnits(){
		return getUnitsFromTeam(this.currentTeam, true);
	}
	public Vector<UnitIA> getEnnemyUnitsInSight(){
		return getUnitsFromTeam(this.currentTeam, false);
	}
	
	// buildings
	private Vector<BuildingIA> getBuildingsFromTeam(int team, boolean ally){
		Vector<BuildingIA> result = new Vector<BuildingIA>();
		for(BuildingIA b : this.plateau.buildings){
			if((ally && b.team==team) || (!ally && b.team!=team))
				result.add(b);
		}
		return result;
	}
	public Vector<BuildingIA> getMyBuildings(){
		return getBuildingsFromTeam(this.currentTeam, true);
	}
	public Vector<BuildingIA> getNeutralBuildings(){
		return getBuildingsFromTeam(0, true);
	}
	public Vector<BuildingIA> getEnnemyBuildings(){
		return getBuildingsFromTeam(this.currentTeam, false);
	}

	

	

	public void action(){
		Vector<Mission> toRemove = new Vector<Mission>();
		for(Mission m : missions){
			boolean finished = m.action();
			if(finished){
				toRemove.add(m);
				this.pastMissions.addElement(m);
			}
		}
		missions.removeAll(toRemove);
	}

	public void abortAllMission(){
		Vector<Mission> toRemove = new Vector<Mission>();
		for(Mission m : missions){
			toRemove.add(m);
			m.abortMission();
			this.pastMissions.addElement(m);
		}
		missions.removeAllElements();
	}

	public void abortMission(Mission m){
		m.abortMission();
		this.pastMissions.addElement(m);
		this.missions.remove(m);
		this.pastMissions.addElement(m);
	}

	public void pauseMission(Mission m){
		this.pausedMissions.addElement(m);
		m.pauseMission();
		this.missions.remove(m);
	}

	public void resumeMission(Mission m){
		this.pausedMissions.remove(m);
		m.resumeMission();
		this.missions.add(m);
	}

	public void updateGroups(){
		for(int i = 0;i<10;i++){
			makeUnitGroup(getUnitsGroup(i),i);
		}
		for(int i = 0;i<10;i++){
			makeBuildingsGroup(getBuildingsGroup(i),i);
		}

	}

	public void product(){

	}
	public void updateMission(){

	}

	public void updateSelection(){

		//Clean Units groups 
		this.aliveUnits = this.getMyAliveUnits();
		this.myBuildings = this.getMyBuildings();
		int i = 0;
		int j = 0;
		while(i<unitsGroups.size()){
			while(j<unitsGroups.get(i).size()){
				if(!aliveUnits.contains(unitsGroups.get(i).get(j))){
					unitsGroups.get(i).remove(j);

				}
				j++;

			}
			i++;
			j=0;
		}

		//Clean building groups
		i = 0;
		j = 0;
		while(i<buildingGroups.size()){
			while(j<buildingGroups.get(i).size()){
				if(!myBuildings.contains(buildingGroups.get(i).get(j))){
					buildingGroups.get(i).remove(j);
				}
				j++;
			}
			i++;
			j=0;
		}
	}



	//BUILDING GETTER METHODS
	private BuildingIA getNearestBuildingByTypeAndTeam(BuildingsList type, int team, Objet caller){
		Vector<BuildingIA> result = new Vector<BuildingIA>();
		for(BuildingIA b : plateau.buildings){
			if(b.type == type && b.team==team){
				result.add(b);
			}
		}

		return Utils.nearestObject(result, caller);
	}
	
	public BuildingIA getNearestNeutralMill(Vector<BuildingIA> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(BuildingIA b : buildings){
			if(b instanceof BuildingMill && b.getTeam()==0){
				result.add(b);

			}
		}

		return (BuildingIA) Utils.nearestObject(result, caller);

	}
	public BuildingIA getNearestNeutralMine(Vector<BuildingIA> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(BuildingIA b : buildings){
			if(b instanceof BuildingMine && b.getTeam()==0){
				result.add(b);
			}
		}

		return (BuildingIA) Utils.nearestObject(result, caller);

	}
	public BuildingIA getNearestNeutralBarrack(Vector<BuildingIA> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(BuildingIA b : buildings){
			if(b instanceof BuildingBarrack && b.getTeam()==0){
				result.add(b);
			}
		}
		return (BuildingIA) Utils.nearestObject(result, caller);
	}

	public BuildingIA getNearestMillToConquer(Vector<BuildingIA> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(BuildingIA b : buildings){
			if(b instanceof BuildingMill && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}

		return (BuildingIA) Utils.nearestObject(result, caller);

	}
	public BuildingIA getNearestMineToConquer(Vector<BuildingIA> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(BuildingIA b : buildings){
			if(b instanceof BuildingMine && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}

		return (BuildingIA) Utils.nearestObject(result, caller);

	}

	public BuildingIA getNearestHQToConquer(Vector<BuildingIA> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(BuildingIA b : buildings){
			if(b instanceof BuildingHeadQuarters && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}
		return (BuildingIA) Utils.nearestObject(result, caller);

	}
	public BuildingIA getNearestBarrackToConquer(Vector<BuildingIA> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(BuildingIA b : buildings){
			if(b instanceof BuildingBarrack && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}
		return (BuildingIA) Utils.nearestObject(result, caller);
	}

	public Vector<UnitIA> getSpearman(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitSpearman ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getIdleSpearman(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitSpearman && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}
	public Vector<UnitIA> getCrossbowman(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitCrossbowman ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getIdleCrossbowman(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitCrossbowman && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getKnight(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitKnight ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getIdleKnight(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitKnight  && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}


	public Vector<UnitIA> getPriest(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitPriest ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getIdlePriest(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitPriest && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getInquisitor(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitInquisitor ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getIdleInquisitor(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitInquisitor  && c.mission==null  ){
				result.add(c);
			}
		}
		return result;
	}


	public Vector<UnitIA> getArchange(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitArchange ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getIdleArchange(Vector<UnitIA> units){
		Vector<UnitIA> result = new Vector<UnitIA>();

		for(UnitIA c : units){
			if(c instanceof UnitArchange && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}

	public int getFood(){
		return this.getGameTeam().food;
	}

	public int getGold(){
		return this.getGameTeam().gold;
	}

	public int getSpecial(){
		return this.getGameTeam().special;
	}


	public HashMap<Integer,Integer> initHashMap(){
		HashMap<Integer,Integer> r = new HashMap<Integer,Integer>();
		r.put(UnitIA.SPEARMAN, 0);
		r.put(UnitIA.CROSSBOWMAN, 0);
		r.put(UnitIA.KNIGHT, 0);
		r.put(UnitIA.INQUISITOR, 0);
		r.put(UnitIA.ARCHANGE, 0);
		return r;
	}
	public HashMap<Integer,Integer> initHashMap(int s,int c, int k , int i , int a){
		HashMap<Integer,Integer> r = new HashMap<Integer,Integer>();
		r.put(UnitIA.SPEARMAN, s);
		r.put(UnitIA.CROSSBOWMAN, c);
		r.put(UnitIA.KNIGHT, k);
		r.put(UnitIA.INQUISITOR, i);
		r.put(UnitIA.ARCHANGE, a);
		return r;
	}

	public Vector<BuildingIA> getMill(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingMill ){
				result.add(c);
			}
		}
		return result;
	}
	public Vector<BuildingIA> getMine(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingMine ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<BuildingIA> getBarrack(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingBarrack ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<BuildingIA> getStable(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingStable ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<BuildingIA> getAcademy(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingAcademy ){
				result.add(c);
			}
		}
		return result;
	}
	public Vector<BuildingIA> getHeadQuarters(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingHeadQuarters ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<BuildingIA> getUniversity(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingUniversity ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<BuildingIA> getTower(Vector<BuildingIA> units){
		Vector<BuildingIA> result = new Vector<BuildingIA>();

		for(BuildingIA c : units){
			if(c instanceof BuildingTower ){
				result.add(c);
			}
		}
		return result;
	}

	/**
	 *
	 * Clear specified units group
	 *
	 * @param  c  the vector of wanted selection
	 * @param  group  the vector of wanted selection
	 */
	public void clearUnitGroup(int group){
		if(group>=10){
			return;
		}
		for(int i = 0;i<this.unitsGroups.size();i++){
			this.unitsGroups.get(group).clear();
		}
	}

	/**
	 *
	 * Clear specified buildings group
	 *
	 * @param  c  the vector of wanted selection
	 * @param  group  the vector of wanted selection
	 */
	public void clearBuildingsGroup(int group){
		if(group>=10){
			return;
		}
		for(int i = 0;i<this.buildingGroups.size();i++){
			this.buildingGroups.get(group).clear();
		}
	}

	/**
	 *
	 * Make specific unit group with specified unit
	 *
	 * @param  c  the vector of wanted selection
	 * @param  group  the vector of wanted selection
	 */
	public void makeUnitGroup(Vector<UnitIA> c,int group){
		this.clearUnitGroup(group);
		addInUnitGroup(c,group);
	}

	/**
	 *
	 * Make specific building group with specified buildings
	 *
	 * @param  c  the vector of wanted selection
	 * @param  group  the vector of wanted selection
	 */
	public void makeBuildingsGroup(Vector<BuildingIA> c,int group){
		this.clearBuildingsGroup(group);
		addInBuildingGroup(c,group);
	}

	/**
	 *
	 *  Add units in specified group if possible
	 *
	 * @param  c  the vector of wanted selection
	 * @param  group  the vector of wanted selection
	 */
	public void addInUnitGroup(Vector<UnitIA> c,int group){
		if(group>=10){
			return;
		}

		for(UnitIA ch : c){
			if(this.aliveUnits.contains(ch) && !this.unitsGroups.get(group).contains(ch)){
				this.unitsGroups.get(group).add(ch);
			}
		}
	}

	/**
	 *
	 * Add buildings in specified group if possible
	 *
	 * @param  c  the vector of wanted selection
	 * @param  group  the vector of wanted selection
	 */
	public void addInBuildingGroup(Vector<BuildingIA> c,int group){
		if(group>=10){
			return;
		}

		for(BuildingIA b : c){
			if(this.myBuildings.contains(b) && !this.buildingGroups.get(group).contains(b)){
				this.buildingGroups.get(group).add(b);
			}
		}

	}

	public Vector<UnitIA> getUnitsGroup(int i){

		if(i<10){
			return this.unitsGroups.get(i);
		}
		else{
			return new Vector<UnitIA>();
		}
	}

	public Vector<BuildingIA> getBuildingsGroup(int i){
		if(i<10){
			return this.buildingGroups.get(i);
		}
		else{
			return new Vector<BuildingIA>();
		}
	}


}
