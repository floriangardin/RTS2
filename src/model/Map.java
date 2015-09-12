package model;

import java.util.Vector;

import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingUniversity;
import buildings.BuildingHeadQuarters;
import nature.Tree;
import nature.Water;
import units.Character;
import units.UnitsList;
import weapon.Bible;
import weapon.Bow;
import weapon.Sword;
import weapon.Wand;

public class Map {

	public Map(){

	}

	

	public void createMapLan(Plateau plateau,Vector<Player> players){
		plateau.maxX = 2000f;
		plateau.maxY = 3000f;
		Data data1 = players.get(1).data;
		Data data2 = players.get(2).data;
		data1.create(UnitsList.Spearman, plateau.maxX/2+3f, 300f);
		BuildingHeadQuarters team1h = new BuildingHeadQuarters(plateau,plateau.g,plateau.maxX/2,100f,1);
		
		new BuildingMill(plateau,plateau.g,150f,100f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,100f);
		new BuildingBarrack(plateau,plateau.g,plateau.maxX/2,1*plateau.maxY/5);
		new BuildingStable(plateau,plateau.g,plateau.maxX/4-20f,2*plateau.maxY/6+20f);
		new BuildingAcademy(plateau,plateau.g,3*plateau.maxX/4+20f,2*plateau.maxY/6+20f);
		
		// Water

			
		// ENCULE KEVIN 
			new Water(175f,3.0f*plateau.maxY/6,350f,800f,plateau);
			new Water(plateau.maxX-175f,3.0f*plateau.maxY/6,350f,800f,plateau);
			
		// Player 2 side
		BuildingHeadQuarters team2h = new BuildingHeadQuarters(plateau,plateau.g,plateau.maxX/2,plateau.maxY-200f,2);
		new BuildingMill(plateau,plateau.g,150f,plateau.maxY-200f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,plateau.maxY-200f);
		new BuildingBarrack(plateau,plateau.g,plateau.maxX/2,4*plateau.maxY/5);
		
		data2.create(UnitsList.Spearman, plateau.maxX/2+3f,  plateau.maxY-350f);
		
		
		// CENTER
		
		// Stables and academy 
		new BuildingStable(plateau,plateau.g,plateau.maxX/4-20f, 4*plateau.maxY/6);
		new BuildingAcademy(plateau,plateau.g,3f*plateau.maxX/4+20f, 4*plateau.maxY/6);
		
		// Barrack in the middle
		new BuildingMill(plateau,plateau.g,plateau.maxX/7,plateau.maxY/2);
		new BuildingMine(plateau,plateau.g,6f*plateau.maxX/7,plateau.maxY/2);
		new BuildingUniversity(plateau,plateau.g,plateau.maxX/2,plateau.maxY/2);
		
		
		
		
	}


}
