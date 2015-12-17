package battleIA;

import java.util.HashMap;
import java.util.Vector;

import battleIA.IAStateOfGame.BuildingIA;
import battleIA.IAStateOfGame.ObjetIA;
import battleIA.IAStateOfGame.UnitIA;
import buildings.Building;
import buildings.BuildingHeadQuarters;
import buildings.BuildingProduction;
import buildings.BuildingsList;
import model.Checkpoint;
import model.Plateau;
import units.Character;
import units.UnitsList;

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


	public void update(){
		//Main method which is called every iteration on the plateau and common to all 
		//Define the mode for this round 
		aliveUnits = getMyAliveUnits();
		myBuildings = getMyBuildings();
		neutralBuilding = getNeutralBuildings();
		buildingToConquer = getEnnemyBuildings();
		ennemiesInSight =  getEnnemyUnitsInSight();
		this.updateSelection();
	}

	/**
	 * function that set the order to the UnitIA with id 'idAlliedPlayer'
	 * to attack the UnitIA with id 'idTarget'.
	 * 
	 * The UnitIA will pursue and attack its target as long as it remains in sight and alive.
	 * 
	 * @throws IAException if idAlliedPlayer is not an allied player or idTarget not an enemy.
	 * 
	 * @param idAlliedPlayer id of the unit to assign the order
	 * @param idTarget id of the targeted unit
	 */
	public void setAttackOrder(int idAlliedPlayer, int idTarget) throws IAException{

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

	/**
	 * function that orders the UnitIA with id 'idAlliedPlayer'
	 * to hold Position and attack any enemy unit in range
	 * 
	 * @throws IAException if idAlliedPlayer is not an allied player
	 * 
	 * @param idAlliedPlayer id of the unit to assign the order
	 */
	public void setHoldPositionOrder(int idAlliedPlayer) throws IAException{
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

	/**
	 * function that orders the UnitIA with id 'idAlliedPlayer'
	 * to go to the specified location
	 * 
	 * @throws IAException if idAlliedPlayer is not an allied player or location out of the map
	 * 
	 * @param idAlliedPlayer id of the unit to assign the order
	 * @param xTogo x coordinate of the location to go
	 * @param yTogo y coordinate of the location to go
	 */
	public void setMoveOrder(int idAlliedPlayer, float xToGo, float yToGo) throws IAException{
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

	/**
	 * function that orders the UnitIA with id 'idAlliedPlayer'
	 * to move in an aggressive fashion to the selected location. 
	 * The UnitIA will attack every enemy unit in sight until 
	 * it arrives to the location
	 * 
	 * @throws IAException if idAlliedPlayer is not an allied player or location out of the map
	 * 
	 * @param idAlliedPlayer id of the unit to assign the order
	 * @param xTogo x coordinate of the location to go
	 * @param yTogo y coordinate of the location to go
	 */
	public void setAggressiveOrder(int idAlliedPlayer, float xToGo, float yToGo) throws IAException{
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

	/**
	 * function that orders the UnitIA with id 'idAlliedPlayer'
	 * to take the building with id 'idBuildingToTake'
	 * 
	 * the unit won't attack any other unit till the building is taken
	 * 
	 * if the building is already yours the unit will stand and defend it, 
	 * but won't attack either
	 * 
	 * @throws IAException if idAlliedPlayer is not an allied player or if idBuildingToTake is not an existing building
	 * 
	 * @param idAlliedPlayer id of the unit to assign the order
	 * @param idBuildingToTake id of the building to take
	 */
	public void setTakeBuildingOrder(int idAlliedPlayer, int idBuildingToTake) throws IAException{
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

	/**
	 * function that orders the UnitIA with id 'idAlliedPlayer'
	 * to take the enemy headquarters
	 * 
	 * the unit won't attack any other unit till the building is taken
	 * 
	 * @throws IAException if idAlliedPlayer is not an allied player
	 * 
	 * @param idAlliedPlayer id of the unit to assign the order
	 */
	public void setTakeEnemyHeadQuarterOrder(int idAlliedPlayer)  throws IAException{
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

	/**
	 * function that orders the UnitIA with id 'idAlliedPlayer'
	 * to defend your own headquarters
	 * 
	 * the unit won't attack any other unit until its order changes
	 * 
	 * @throws IAException if idAlliedPlayer is not an allied player
	 * 
	 * @param idAlliedPlayer id of the unit to assign the order
	 */
	public void setDefendHeadQuarterOrder(int idAlliedPlayer)  throws IAException{
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

	private ObjetIA getNearestObjet(Vector<ObjetIA> objets, ObjetIA caller){
		float ref_dist = 10000000000f;
		ObjetIA closest = null;
		for(ObjetIA o : objets){
			float dist = (o.x-caller.x)*(o.x-caller.x)+(o.y-caller.y)*(o.y-caller.y);
			if(dist < ref_dist){
				ref_dist = dist;
				closest = o;
			}
		}
		return closest;
	}

	//about buildings
	
	private BuildingIA getNearestBuildingByTypeAndTeam(BuildingsList type, int team, boolean ally, ObjetIA caller){
		Vector<ObjetIA> result = new Vector<ObjetIA>();
		for(BuildingIA b : plateau.buildings){
			if(b.type == type && ((ally && b.team==team) || (!ally && b.team!=team && b.team!=0))){
				result.add(b);
			}
		}
		return (BuildingIA) getNearestObjet(result, caller);
	}
	

	public BuildingIA getNearestNeutralMill(ObjetIA caller){
		return getNearestBuildingByTypeAndTeam(BuildingsList.Mill, 0, true, caller);
	}
	public BuildingIA getNearestNeutralMine(ObjetIA caller){
		return getNearestBuildingByTypeAndTeam(BuildingsList.Mine, 0, true, caller);
	}
	public BuildingIA getNearestNeutralBarrack(ObjetIA caller){
		return getNearestBuildingByTypeAndTeam(BuildingsList.Barrack, 0, true, caller);
	}
	public BuildingIA getNearestEnemyMill(ObjetIA caller){
		return getNearestBuildingByTypeAndTeam(BuildingsList.Mill, 0, false, caller);
	}
	public BuildingIA getNearestEnemyMine(ObjetIA caller){
		return getNearestBuildingByTypeAndTeam(BuildingsList.Mine, 0, false, caller);
	}
	public BuildingIA getNearestEnemyBarrack(ObjetIA caller){
		return getNearestBuildingByTypeAndTeam(BuildingsList.Barrack, 0, false, caller);
	}

	public BuildingIA getEnemyHQ(){
		for(BuildingIA b: this.plateau.buildings){
			if(b.type == BuildingsList.Headquarters && b.team != this.currentTeam){
				return b;
			}
		}
		return null;
	}

	private Vector<BuildingIA> getAllBuildingByTypeAndTeam(BuildingsList type, int team, boolean ally){
		Vector<BuildingIA> result = new Vector<BuildingIA>();
		for(BuildingIA b : this.plateau.buildings){
			if(b.type == type && ((ally && b.team==team) || (!ally && b.team!=team && b.team!=0))){
				result.add(b);
			}
		}
		return result;
	}

	public Vector<BuildingIA> getAllyMills(){
		return getAllBuildingByTypeAndTeam(BuildingsList.Mill, currentTeam, true);
	}
	public Vector<BuildingIA> getAllyMines(){
		return getAllBuildingByTypeAndTeam(BuildingsList.Mine, currentTeam, true);
	}
	public Vector<BuildingIA> getAllyBarracks(){
		return getAllBuildingByTypeAndTeam(BuildingsList.Barrack, currentTeam, true);
	}
	public Vector<BuildingIA> getAllyStables(){
		return getAllBuildingByTypeAndTeam(BuildingsList.Stable, currentTeam, true);
	}
	public Vector<BuildingIA> getAllyAcademys(){
		return getAllBuildingByTypeAndTeam(BuildingsList.Academy, currentTeam, true);
	}
	public Vector<BuildingIA> getAllyHeadQuarters(){
		return getAllBuildingByTypeAndTeam(BuildingsList.Headquarters, currentTeam, true);
	}
	public Vector<BuildingIA> getAllyUniversitys(){
		return getAllBuildingByTypeAndTeam(BuildingsList.University, currentTeam, true);
	}
	public Vector<BuildingIA> getAllyTowers(){
		return getAllBuildingByTypeAndTeam(BuildingsList.Tower, currentTeam, true);
	}

	public void addToBuildingAProduction(BuildingIA building, int production) throws IAException{
		for(Building b : this.p.buildings){
			if(b.id == building.id){
				if(b.getTeam()!=this.currentTeam){
					throw new IAException(currentTeam, "Impossible de donner des ordres à un bâtiment ennemi");
				}
				((BuildingProduction) b).product(production);
			}
		}
	}

	// about Units
	private Vector<UnitIA> getUnitsByTypeAndTeam(UnitsList type, int team, boolean ally){
		Vector<UnitIA> result = new Vector<UnitIA>();
		for(UnitIA c : plateau.units){
			if(c.type == type && ((ally && c.team == team) || (!ally && c.team != team && c.team !=0))){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<UnitIA> getAllySpearmen(){
		return getUnitsByTypeAndTeam(UnitsList.Spearman, this.currentTeam, true);
	}
	public Vector<UnitIA> getCrossbowman(){
		return getUnitsByTypeAndTeam(UnitsList.Crossbowman, this.currentTeam, true);
	}
	public Vector<UnitIA> getKnight(){
		return getUnitsByTypeAndTeam(UnitsList.Knight, this.currentTeam, true);
	}
	public Vector<UnitIA> getPriest(){
		return getUnitsByTypeAndTeam(UnitsList.Priest, this.currentTeam, true);
	}
	public Vector<UnitIA> getInquisitor(){
		return getUnitsByTypeAndTeam(UnitsList.Inquisitor, this.currentTeam, true);
	}
	public Vector<UnitIA> getArchange(){
		return getUnitsByTypeAndTeam(UnitsList.Archange, this.currentTeam, true);
	}

	// ressources
	public int getFood(){
		return this.p.g.teams.get(currentTeam).food;
	}

	public int getGold(){
		return this.p.g.teams.get(currentTeam).gold;
	}

	public int getSpecial(){
		return this.p.g.teams.get(currentTeam).special;
	}


//	public HashMap<Integer,Integer> initHashMap(){
//		HashMap<Integer,Integer> r = new HashMap<Integer,Integer>();
//		r.put(UnitIA.SPEARMAN, 0);
//		r.put(UnitIA.CROSSBOWMAN, 0);
//		r.put(UnitIA.KNIGHT, 0);
//		r.put(UnitIA.INQUISITOR, 0);
//		r.put(UnitIA.ARCHANGE, 0);
//		return r;
//	}
//	public HashMap<Integer,Integer> initHashMap(int s,int c, int k , int i , int a){
//		HashMap<Integer,Integer> r = new HashMap<Integer,Integer>();
//		r.put(UnitIA.SPEARMAN, s);
//		r.put(UnitIA.CROSSBOWMAN, c);
//		r.put(UnitIA.KNIGHT, k);
//		r.put(UnitIA.INQUISITOR, i);
//		r.put(UnitIA.ARCHANGE, a);
//		return r;
//	}




}
