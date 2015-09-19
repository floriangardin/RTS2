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

	

	public void createMapTest(Plateau plateau,Vector<Player> players){
		plateau.maxX = 200f;
		plateau.maxY = 300f;
		Data data1 = players.get(1).data;
		Data data2 = players.get(2).data;
		data1.create(UnitsList.Spearman, plateau.maxX/2+3f, 100f);
		data2.create(UnitsList.Spearman, plateau.maxX/2+3f,  plateau.maxY-100f);
		new BuildingHeadQuarters(plateau,plateau.g,200f,200f,1);
		new BuildingHeadQuarters(plateau,plateau.g,200f,200f,2);
		
	}
	
	
	public void createMapPhillipe(Plateau plateau,Vector<Player> players){
		plateau.maxY = 2000f;
		plateau.maxX = 3000f;
		Data data1 = players.get(1).data;
		Data data2 = players.get(2).data;
		data1.create(UnitsList.Spearman, plateau.maxX-300f, plateau.maxY/2+3f);
		BuildingHeadQuarters team1h = new BuildingHeadQuarters(plateau,plateau.g,200f,plateau.maxY/2,1);
		
		
		
		new BuildingMill(plateau,plateau.g,200f,1*plateau.maxY/4+55f);
		new BuildingMine(plateau,plateau.g,200f,3*plateau.maxY/4-55f);
		new BuildingBarrack(plateau,plateau.g,1*plateau.maxX/5+100f,plateau.maxY/2);
		new BuildingStable(plateau,plateau.g,2*plateau.maxX/6+40f,plateau.maxY/4-55f);
		new BuildingAcademy(plateau,plateau.g,2*plateau.maxX/6+40f,3*plateau.maxY/4+55f);
		
		// Water

			
		// ENCULE KEVIN 
			new Water(3.0f*plateau.maxX/6,175f,800f,350f,plateau);
			new Water(3.0f*plateau.maxX/6,plateau.maxY-175f,800f,350f,plateau);
			
		// Player 2 side
		BuildingHeadQuarters team2h = new BuildingHeadQuarters(plateau,plateau.g,plateau.maxX-200f,plateau.maxY/2,2);
		new BuildingMill(plateau,plateau.g,plateau.maxX-200f,1*plateau.maxY/4+55f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,3*plateau.maxY/4-55f);
		new BuildingBarrack(plateau,plateau.g,4*plateau.maxX/5-100f,plateau.maxY/2);
		
		data2.create(UnitsList.Spearman,  plateau.maxX-350f, plateau.maxY/2+3f);
		
		
		// CENTER
		
		// Stables and academy 
		new BuildingStable(plateau,plateau.g, 4*plateau.maxX/6,plateau.maxY/4-55f);
		new BuildingAcademy(plateau,plateau.g, 4*plateau.maxX/6,3f*plateau.maxY/4+55f);
		
		// Barrack in the middle
		new BuildingMill(plateau,plateau.g,plateau.maxX/2,plateau.maxY/7);
		new BuildingMine(plateau,plateau.g,plateau.maxX/2,6f*plateau.maxY/7);
		new BuildingUniversity(plateau,plateau.g,plateau.maxX/2,plateau.maxY/2);
		
	}

	
	public void createMapPhillipeMacro(Plateau plateau,Vector<Player> players){
		plateau.maxY = 2500f;
		plateau.maxX = 5000f;
		float X = plateau.maxX;
		float Y = plateau.maxY;
		Data data1 = players.get(1).data;
		Data data2 = players.get(2).data;
		//HQ
		new BuildingHeadQuarters(plateau,plateau.g,3*X/18,Y/2,1);
		new BuildingHeadQuarters(plateau,plateau.g,15*X/18,Y/2,2);
		new BuildingMill(plateau,plateau.g,X/18,3*Y/5);
		new BuildingMill(plateau,plateau.g,17*X/18,3*Y/5);
		
		new BuildingMill(plateau,plateau.g,X/18,2*Y/5);
		new BuildingMill(plateau,plateau.g,17*X/18,2*Y/5);
		
		new BuildingMill(plateau,plateau.g,7*X/18,3*Y/5);
		new BuildingMill(plateau,plateau.g,11*X/18,2*Y/5);
		
		new BuildingMine(plateau,plateau.g,7*X/18,2*Y/5);
		new BuildingMine(plateau,plateau.g,11*X/18,3*Y/5);
		
		new BuildingMine(plateau,plateau.g,X/18,4*Y/5);
		new BuildingMine(plateau,plateau.g,17*X/18,4*Y/5);
		
		new BuildingMine(plateau,plateau.g,X/18,1*Y/5);
		new BuildingMine(plateau,plateau.g,17*X/18,1*Y/5);
		
		new BuildingBarrack(plateau,plateau.g,3*X/18,1*Y/5);
		new BuildingBarrack(plateau,plateau.g,15*X/18,1*Y/5);
		
		new BuildingBarrack(plateau,plateau.g,3*X/18,4*Y/5);
		new BuildingBarrack(plateau,plateau.g,15*X/18,4*Y/5);
		
		new BuildingUniversity(plateau,plateau.g,X/9,10*Y/11);
		new BuildingUniversity(plateau,plateau.g,8*X/9,Y/11);
		
		new BuildingStable(plateau,plateau.g,X/2,Y/6);
		new BuildingStable(plateau,plateau.g,X/2,5*Y/6);
		
		new BuildingAcademy(plateau,plateau.g,X/2,Y/2);

		data1.player.create(UnitsList.Spearman, X/9 + 1f, Y/2);
		data2.player.create(UnitsList.Spearman, 8*X/9 - 1f, Y/2);

		// Water

			
		// ENCULE KEVIN 
		new Water(5f*X/18,2f*Y/3,X/9,2f*Y/3,plateau);
		new Water(13f*X/18,1f*Y/3,X/9,2f*Y/3,plateau);
		
		
		
		
			
		// Player 2 side
		

		

		
	}

}
