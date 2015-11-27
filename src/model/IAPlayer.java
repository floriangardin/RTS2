package model;

import java.util.Vector;

import buildings.Building;
import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingHeadQuarters;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingTower;
import buildings.BuildingUniversity;
import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;

public class IAPlayer extends Player{

	
	private Vector<Vector<Character>> unitsGroups;
	private Vector<Vector<Building>> buildingGroups;

	private Vector<Character> aliveUnits;
	private Vector<Building> myBuildings;


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
		this.updateSelection();
		//Update the units groups and building groups ( To remove death ...)
		this.updateGroups();
	}



	public void updateGroups(){
		for(int i = 0;i<10;i++){
			makeUnitGroup(getUnitsGroup(i),i);
		}
		for(int i = 0;i<10;i++){
			makeBuildingsGroup(getBuildingsGroup(i),i);
		}
		
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
	public Building getNearestNeutralMill(Vector<Building> buildings,Character caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMill && b.getTeam()==0){
				result.add(b);
				
			}
		}
		
		return (Building) Utils.nearestObject(result, caller);
		
	}
	public Building getNearestNeutralMine(Vector<Building> buildings,Character caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMine && b.getTeam()==0){
				result.add(b);
			}
		}
		
		return (Building) Utils.nearestObject(result, caller);
		
	}
	public Building getNearestNeutralBarrack(Vector<Building> buildings,Character caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingBarrack && b.getTeam()==0){
				result.add(b);
			}
		}
		return (Building) Utils.nearestObject(result, caller);
	}
	
	public Building getNearestMillToConquer(Vector<Building> buildings,Character caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMill && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}
		
		return (Building) Utils.nearestObject(result, caller);
		
	}
	public Building getNearestMineToConquer(Vector<Building> buildings,Character caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingMine && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}
		
		return (Building) Utils.nearestObject(result, caller);
		
	}
	
	public Building getNearestHQToConquer(Vector<Building> buildings,Character caller){
		Vector<Objet> result = new Vector<Objet>();
		for(Building b : buildings){
			if(b instanceof BuildingHeadQuarters && b.getTeam()!=caller.getTeam()){
				result.add(b);
			}
		}
		
		return (Building) Utils.nearestObject(result, caller);
		
	}
	public Building getNearestBarrackToConquer(Vector<Building> buildings,Character caller){
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
	public Vector<Character> getCrossbowman(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();
		
		for(Character c : units){
			if(c instanceof UnitCrossbowman ){
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
	public Vector<Character> getPriest(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();
		
		for(Character c : units){
			if(c instanceof UnitPriest ){
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
	
	public Vector<Character> getArchange(Vector<Character> units){
		Vector<Character> result = new Vector<Character>();
		
		for(Character c : units){
			if(c instanceof UnitArchange ){
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
