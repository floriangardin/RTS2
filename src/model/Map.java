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
		data1.create(UnitsList.Crossbowman, plateau.maxX/2-1f, 300f);
		data1.create(UnitsList.Knight, plateau.maxX/2, 300f);
		data1.create(UnitsList.Inquisitor, plateau.maxX/2+1f, 300f);
		data1.create(UnitsList.Priest, plateau.maxX/2+2f, 300f);
		data1.create(UnitsList.Spearman, plateau.maxX/2+3f, 300f);
		
		data2.create(UnitsList.Spearman, plateau.maxX/2+4f, 300f);
		BuildingHeadQuarters team1h = new BuildingHeadQuarters(plateau,plateau.g,plateau.maxX/2,1*plateau.maxY/5,1);
		data1.player.hq = team1h;
		new BuildingMill(plateau,plateau.g,150f,100f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,100f);
		BuildingUniversity bar = new BuildingUniversity(plateau,plateau.g,plateau.maxX/2,100f);
		
		new BuildingStable(plateau,plateau.g,plateau.maxX/4,2*plateau.maxY/5);
		new BuildingAcademy(plateau,plateau.g,3*plateau.maxX/4,2*plateau.maxY/5);
		players.get(1).food = 1000;
		players.get(1).gold = 1000;
		data2.create(UnitsList.Crossbowman, plateau.maxX/2-1f, plateau.maxY-300f);
		data2.create(UnitsList.Knight, plateau.maxX/2,  plateau.maxY-300f);
		data2.create(UnitsList.Inquisitor, plateau.maxX/2+1f,  plateau.maxY-300f);
		data2.create(UnitsList.Priest, plateau.maxX/2+2f,  plateau.maxY-300f);
		data2.create(UnitsList.Spearman, plateau.maxX/2+3f,  plateau.maxY-300f);
		BuildingHeadQuarters team2h = new BuildingHeadQuarters(plateau,plateau.g,plateau.maxX/2,4*plateau.maxY/5,2);
		data2.player.hq = team2h;
		new BuildingMill(plateau,plateau.g,150f,plateau.maxY-200f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,plateau.maxY-200f);
		new BuildingBarrack(plateau,plateau.g,plateau.maxX/2,plateau.maxY-200f);
		
		
		team1h.constructionPoints = team1h.maxLifePoints-1f;
		// Stables and academy 
		new BuildingStable(plateau,plateau.g,plateau.maxX/4, 3*plateau.maxY/5);
		new BuildingAcademy(plateau,plateau.g,3f*plateau.maxX/4, 3*plateau.maxY/5);
		
		// Barrack in the middle
		new BuildingMill(plateau,plateau.g,150f,plateau.maxY/2);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,plateau.maxY/2);
		new BuildingBarrack(plateau,plateau.g,plateau.maxX/2,plateau.maxY/2);
		
		
		team2h.constructionPoints = team2h.maxLifePoints-1f;
		
	}


}
