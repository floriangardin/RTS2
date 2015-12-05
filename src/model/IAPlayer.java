package model;

import java.util.HashMap;
import java.util.Vector;

import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;
import IA.Mission;
import buildings.Building;
import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingHeadQuarters;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingTower;
import buildings.BuildingUniversity;

public class IAPlayer extends Player{


	private Vector<Vector<Character>> unitsGroups;
	private Vector<Vector<Building>> buildingGroups;

	public Vector<Character> aliveUnits;
	public Vector<Building> myBuildings;
	//Define the mode for this round 

	public Vector<Building> neutralBuilding;
	public Vector<Building> buildingToConquer;
	public Vector<Character> ennemiesInSight;

	public Vector<Mission> missions;
	public Vector<Mission> pastMissions;
	public Vector<Mission> pausedMissions;

	//Minimal class to get an IA running
	//An IA which extends this IA can't have access to plateau, an IA should be then programmed in IA package

	//TODO find a way to isolate data for package ..

	public IAPlayer(Plateau p, int id, String name, GameTeam gameteam,int resX, int resY) {
		super(p, id, name, gameteam, resX, resY);


		unitsGroups = new Vector<Vector<Character>>();
		buildingGroups = new Vector<Vector<Building>>();
		for(int i=0;i<10;i++){
			unitsGroups.add(new Vector<Character>());
			buildingGroups.add(new Vector<Building>());
		}

		aliveUnits = new Vector<Character>();
		myBuildings = new Vector<Building>();
		neutralBuilding = new Vector<Building>();
		buildingToConquer = new Vector<Building>();
		ennemiesInSight = new Vector<Character>();
		this.missions = new Vector<Mission>();
		this.pastMissions = new Vector<Mission>();
		this.pausedMissions = new Vector<Mission>();
	}




	/*
	 * OVERRIDE THIS METHOD IN AN INHERITHED CLASS IN PACKAGE IA IF YOU WANT TO CREATE AN IA
	 */
	public void update(){
		//OVERRIDE THIS METHOD IN AN INHERITHED CLASS IN PACKAGE IA IF YOU WANT TO CREATE AN IA

	}



	//INTERN METHODS

	public Vector<Character> getMyAliveUnits(){
		Vector<Character> result = new Vector<Character>();
		for(Character c : this.p.characters){
			if(c.getTeam()==this.getTeam())
				result.add(c);
		}
		return result;
	}

	public Vector<Building> getMyBuildings(){
		Vector<Building> result = new Vector<Building>();
		for(Building b : this.p.buildings){
			if(b.getTeam()==this.getTeam())
				result.add(b);
		}
		return result;
	}


	public Vector<Building> getNeutralBuildings(){
		Vector<Building> result = new Vector<Building>();
		for(Building b : this.p.buildings){
			if(b.getTeam()==0)
				result.add(b);
		}
		return result;
	}

	public Vector<Building> getEnnemyBuildings(){
		Vector<Building> result = new Vector<Building>();
		for(Building b : this.p.buildings){
			if(b.getTeam()!=this.getTeam() && b.getTeam()!=0)
				result.add(b);
		}
		return result;
	}

	public Vector<Character> getEnnemyUnitsInSight(){
		Vector<Character> result = new Vector<Character>();
		for(Character c : this.p.characters){
			if(c.getTeam()!=this.getTeam() && this.p.isVisibleByPlayer(this.getTeam(), c))
				result.add(c);
		}
		return result;
	}

	public void commonUpdate(){
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
		this.action();


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


	public void setTarget(Character assignee , Objet target,int mode){
		assignee.setTarget(target, null, mode);
	}


	//BUILDING GETTER METHODS
	public Building getNearestNeutralMill(Vector<Building> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMill && b.getTeam()==0){
				result.add(b);

			}
		}

		return (Building) Utils.nearestObject(result, caller);

	}
	public Building getNearestNeutralMine(Vector<Building> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMine && b.getTeam()==0){
				result.add(b);
			}
		}

		return (Building) Utils.nearestObject(result, caller);

	}
	public Building getNearestNeutralBarrack(Vector<Building> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingBarrack && b.getTeam()==0){
				result.add(b);
			}
		}
		return (Building) Utils.nearestObject(result, caller);
	}

	public Building getNearestMillToConquer(Vector<Building> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMill && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}

		return (Building) Utils.nearestObject(result, caller);

	}
	public Building getNearestMineToConquer(Vector<Building> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMine && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}

		return (Building) Utils.nearestObject(result, caller);

	}

	public Building getNearestHQToConquer(Vector<Building> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingHeadQuarters && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}
		return (Building) Utils.nearestObject(result, caller);

	}
	public Building getNearestBarrackToConquer(Vector<Building> buildings,Objet caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingBarrack && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}
		return (Building) Utils.nearestObject(result, caller);
	}

	public Vector<Character> getSpearman(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitSpearman ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getIdleSpearman(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitSpearman && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}
	public Vector<Character> getCrossbowman(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitCrossbowman ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getIdleCrossbowman(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitCrossbowman && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getKnight(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitKnight ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getIdleKnight(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitKnight  && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}


	public Vector<Character> getPriest(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitPriest ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getIdlePriest(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitPriest && c.mission==null ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getInquisitor(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitInquisitor ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getIdleInquisitor(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitInquisitor  && c.mission==null  ){
				result.add(c);
			}
		}
		return result;
	}


	public Vector<Character> getArchange(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
			if(c instanceof UnitArchange ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Character> getIdleArchange(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();

		for(Character c : units){
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
		r.put(Character.SPEARMAN, 0);
		r.put(Character.CROSSBOWMAN, 0);
		r.put(Character.KNIGHT, 0);
		r.put(Character.INQUISITOR, 0);
		r.put(Character.ARCHANGE, 0);
		return r;
	}
	public HashMap<Integer,Integer> initHashMap(int s,int c, int k , int i , int a){
		HashMap<Integer,Integer> r = new HashMap<Integer,Integer>();
		r.put(Character.SPEARMAN, s);
		r.put(Character.CROSSBOWMAN, c);
		r.put(Character.KNIGHT, k);
		r.put(Character.INQUISITOR, i);
		r.put(Character.ARCHANGE, a);
		return r;
	}

	public Vector<Building> getMill(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
			if(c instanceof BuildingMill ){
				result.add(c);
			}
		}
		return result;
	}
	public Vector<Building> getMine(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
			if(c instanceof BuildingMine ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Building> getBarrack(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
			if(c instanceof BuildingBarrack ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Building> getStable(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
			if(c instanceof BuildingStable ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Building> getAcademy(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
			if(c instanceof BuildingAcademy ){
				result.add(c);
			}
		}
		return result;
	}
	public Vector<Building> getHeadQuarters(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
			if(c instanceof BuildingHeadQuarters ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Building> getUniversity(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
			if(c instanceof BuildingUniversity ){
				result.add(c);
			}
		}
		return result;
	}

	public Vector<Building> getTower(Vector<Building> units){
		Vector<Building> result = new Vector<Building>();

		for(Building c : units){
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
	public void makeUnitGroup(Vector<Character> c,int group){
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
	public void makeBuildingsGroup(Vector<Building> c,int group){
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
	public void addInUnitGroup(Vector<Character> c,int group){
		if(group>=10){
			return;
		}

		for(Character ch : c){
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
	public void addInBuildingGroup(Vector<Building> c,int group){
		if(group>=10){
			return;
		}

		for(Building b : c){
			if(this.myBuildings.contains(b) && !this.buildingGroups.get(group).contains(b)){
				this.buildingGroups.get(group).add(b);
			}
		}

	}

	public Vector<Character> getUnitsGroup(int i){

		if(i<10){
			return this.unitsGroups.get(i);
		}
		else{
			return new Vector<Character>();
		}
	}

	public Vector<Building> getBuildingsGroup(int i){
		if(i<10){
			return this.buildingGroups.get(i);
		}
		else{
			return new Vector<Building>();
		}
	}

}
