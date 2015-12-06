package model;

import java.util.Vector;

import org.newdawn.slick.Image;

import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingHeadQuarters;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingTower;
import buildings.BuildingUniversity;
import nature.Tree;
import nature.Water;
import pathfinding.MapGrid;
import units.UnitsList;

public class Map {

	
	public Map(){

	}
	
	public static Vector<String> maps(){
		Vector<String> maps = new Vector<String>();
		maps.add("small duel");
		maps.add("large duel");
		maps.add("microgestion");
		maps.add("duel very small");
		maps.add("The Island");
		return maps;
	}
	
	public static void createMap(int id, Game game){
		Vector<String> maps = Map.maps();
		Map.createMap(maps.get(id), game);
	}
	
	public static void createMap(String name, Game game){
		initializePlateau(game, 2000f, 3000f);
		updateMap(name, game);
	}
	public static void updateMap(int id, Game game){
		createMap(Map.maps().get(id),game);
	}
	
	public static void updateMap(String name, Game game){
		switch(name){
		case "small duel": createMapDuelSmall(game);break;
		case "large duel": createMapDuelLarge(game);break;
		case "microgestion": createMapMicro(game);break;
		case "duel very small": createMapDuelVerySmall(game);break;
		case "The Island": createMapTheIsland(game);break;
		}
	}

	
	private static void createMapTheIsland(Game game) {
		game.plateau.setMaxXMaxY(1400f, 1200f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.plateau.teams.get(1).data;
		Data data2 = game.plateau.teams.get(2).data;

		new BuildingHeadQuarters(game.plateau,game,0,Y/2,1);
		new BuildingHeadQuarters(game.plateau,game,X,Y/2,2);

		data1.create(UnitsList.Crossbowman, 2*X/9, Y/2-1f);
		data1.create(UnitsList.Crossbowman, 2*X/9, Y/2-2f);
		data1.create(UnitsList.Spearman, 2*X/9, Y/2);
		data1.create(UnitsList.Spearman, 2*X/9, Y/2+1f);
		data1.create(UnitsList.Spearman, X/9, Y/2+2f);
		data1.create(UnitsList.Inquisitor, X/9, Y/2+3f);
		data1.create(UnitsList.Knight, X/9, Y/2+4f);
//		
		data2.create(UnitsList.Crossbowman, 7*X/9, Y/2-1f);
		data2.create(UnitsList.Crossbowman, 7*X/9, Y/2-2f);
		data2.create(UnitsList.Spearman, 7*X/9, Y/2);
		data2.create(UnitsList.Spearman, 7*X/9, Y/2+1f);
		data2.create(UnitsList.Spearman, 7*X/9, Y/2+2f);
		data2.create(UnitsList.Inquisitor, 7*X/9, Y/2+3f);
		data2.create(UnitsList.Knight, 7*X/9, Y/2+4f);
		
		//Bonus at center 
		new BonusLifePoints(game.plateau, X/4, Y/2);
		new BonusLifePoints(game.plateau, 3*X/4, Y/2);
		new BonusDamage(game.plateau, X/2, Y/9);
		new BonusSpeed(game.plateau, X/2, 8*Y/9);
		//new BuildingTower(game.plateau,game,X/2,Y/2);
		//Tree
		//new Tree(X/3,Y/3,game.plateau,1);
	}

	public static void createMapDuelSmall(Game game){
		game.plateau.setMaxXMaxY(3000f, 3000f);
		Data data1 = game.plateau.teams.get(1).data;
		Data data2 = game.plateau.teams.get(2).data;
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		int team1 = 1;
		int team2 = 2;
		// Team 1 side
		BuildingHeadQuarters team1h = new BuildingHeadQuarters(game.plateau,game,X/2,200f,team1);
		data1.create(UnitsList.Spearman, X/2+3f-40f,team1h.y+team1h.sizeY+20f);
		data1.create(UnitsList.Spearman, X/2+3f+40f, team1h.y+team1h.sizeY+20f);
		new BuildingMill(game.plateau,game,3*X/10,Y/15);
		new BuildingMine(game.plateau,game,7*X/10,Y/15);
		new BuildingMill(game.plateau,game,1*X/10,Y/15);
		new BuildingMine(game.plateau,game,9*X/10,Y/15);
		new BuildingBarrack(game.plateau,game,X/2,1f*Y/5);
			
		// Team 2 side
		BuildingHeadQuarters team2h = new BuildingHeadQuarters(game.plateau,game,X/2,Y-200f,team2);
		data2.create(UnitsList.Spearman, X/2+3f-40f,  team2h.y-team2h.sizeY-20f);
		data2.create(UnitsList.Spearman, X/2+3f+40f,  team2h.y-team2h.sizeY-20f);
		new BuildingMill(game.plateau,game,3*X/10,14*Y/15);
		new BuildingMine(game.plateau,game,7*X/10,14*Y/15);
		
		new BuildingMill(game.plateau,game,X/10,14*Y/15);
		new BuildingMine(game.plateau,game,9*X/10,14*Y/15);
		
		new BuildingBarrack(game.plateau,game,X/2,4f*Y/5);
		
		// CENTER
		new BonusLifePoints(game.plateau, X/2, 2*Y/6);
		new BonusLifePoints(game.plateau, X/2, 4*Y/6);
		
		// Stables and academy 
		new BuildingStable(game.plateau,game,X/4,2*Y/6);
		new BuildingAcademy(game.plateau,game,3*X/4,2*Y/6);
		new BuildingStable(game.plateau,game,X/4, 4*Y/6);
		new BuildingAcademy(game.plateau,game,3f*X/4, 4*Y/6);
		// Barrack in the middle
		new BuildingMill(game.plateau,game,X/5,Y/2);
		new BuildingMine(game.plateau,game,4f*X/5,Y/2);
		new BuildingUniversity(game.plateau,game,X/2,Y/2);
		
		//TOWERS
		new BuildingTower(game.plateau,game,4*X/10,200f);
		new BuildingTower(game.plateau,game,6*X/10,200f);
		new BuildingTower(game.plateau,game,4*X/10,Y-200f);
		new BuildingTower(game.plateau,game,6*X/10,Y-200f);
		//VEGETATION
		new Tree(2*X/6,Y/6,game.plateau,1);
		new Tree(4*X/6,Y/6,game.plateau,2);
		new Tree(4*X/6,5*Y/6,game.plateau,2);
		new Tree(2*X/6,5*Y/6,game.plateau,1);
	}
	
	
	
	public static void createMapDuelVerySmall(Game game){
		game.plateau.setMaxXMaxY(1000f, 1300f);
		Data data1 = game.plateau.teams.get(1).data;
		Data data2 = game.plateau.teams.get(2).data;
		
		// Team 1 side
		BuildingHeadQuarters team1h = new BuildingHeadQuarters(game.plateau,game,-200f+game.plateau.maxX/2,200f,1);
		
//		for(int i =0;i<21;i++){
//			data1.create(UnitsList.Spearman, game.plateau.maxX/2+3f-40f, 300f+i);
//			data2.create(UnitsList.Spearman, game.plateau.maxX/2+3f-40f,  game.plateau.maxY-350f+i);
//		}
		
		
		game.plateau.getTeamById(1).gold = 10000;
		game.plateau.getTeamById(1).food= 10000;
		game.plateau.getTeamById(2).gold = 10000;
		game.plateau.getTeamById(2).food = 10000;
		
		
		new BuildingBarrack(game.plateau,game,200f+game.plateau.maxX/2,1f*game.plateau.maxY/5).setTeam(1);
		// Team 2 side
		BuildingHeadQuarters team2h = new BuildingHeadQuarters(game.plateau,game,-200f+game.plateau.maxX/2,game.plateau.maxY-200f,2);

		new BuildingBarrack(game.plateau,game,200f+game.plateau.maxX/2,4f*game.plateau.maxY/5).setTeam(2);
			
	}

	
	
	public static void createMapDuelLarge(Game game){
		game.plateau.setMaxXMaxY(5000f, 2500f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.plateau.teams.get(1).data;
		Data data2 = game.plateau.teams.get(2).data;
		//HQ
		new BuildingHeadQuarters(game.plateau,game,3*X/18,Y/2,1);
		new BuildingHeadQuarters(game.plateau,game,15*X/18,Y/2,2);
		new BuildingMill(game.plateau,game,X/18,3*Y/5);
		new BuildingMill(game.plateau,game,17*X/18,3*Y/5);
		
		new BuildingMill(game.plateau,game,X/18,2*Y/5);
		new BuildingMill(game.plateau,game,17*X/18,2*Y/5);
		
		new BuildingMill(game.plateau,game,7*X/18,3*Y/5);
		new BuildingMill(game.plateau,game,11*X/18,2*Y/5);
		
		new BuildingMine(game.plateau,game,7*X/18,2*Y/5);
		new BuildingMine(game.plateau,game,11*X/18,3*Y/5);
		
		new BuildingMine(game.plateau,game,X/18,4*Y/5);
		new BuildingMine(game.plateau,game,17*X/18,4*Y/5);
		
		new BuildingMine(game.plateau,game,X/18,1*Y/5);
		new BuildingMine(game.plateau,game,17*X/18,1*Y/5);
		
		new BuildingBarrack(game.plateau,game,3*X/18,1*Y/5);

		new BuildingBarrack(game.plateau,game,15*X/18,1*Y/5);
		
		new BuildingBarrack(game.plateau,game,3*X/18,4*Y/5);
		new BuildingBarrack(game.plateau,game,15*X/18,4*Y/5);
		
		new BuildingUniversity(game.plateau,game,X/9,10*Y/11);
		new BuildingUniversity(game.plateau,game,8*X/9,Y/11);
		
		new BuildingStable(game.plateau,game,X/2,Y/6);
		new BuildingStable(game.plateau,game,X/2,5*Y/6);
		
		new BuildingAcademy(game.plateau,game,X/2,Y/2);

		//for(int c =0; c<50; c++)
			data1.create(UnitsList.Spearman, X/9, Y/2);

		//data1.player.create(UnitsList.Spearman, X/9 + 2f, Y/2);
		data2.create(UnitsList.Spearman, 8*X/9 - 1f, Y/2);

		// Water

			
		new Water(5f*X/18,2f*Y/3,X/9,2f*Y/3,game.plateau);
		new Water(13f*X/18,1f*Y/3,X/9,2f*Y/3,game.plateau);
					
		// Player 2 side
	}

	public static void createMapMicro(Game game){
		game.plateau.setMaxXMaxY(800f, 600f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.plateau.teams.get(1).data;
		Data data2 = game.plateau.teams.get(2).data;

		new BuildingHeadQuarters(game.plateau,game,-data1.headQuartersSizeX/2f-10f,Y/2,1);
		new BuildingHeadQuarters(game.plateau,game,-data2.headQuartersSizeX/2f+10f,data2.headQuartersSizeY+Y/2,2);

		data1.create(UnitsList.Crossbowman, 2*X/9, Y/2-1f);
		data1.create(UnitsList.Spearman, 2*X/9, Y/2-2f);
		data1.create(UnitsList.Knight, 2*X/9, Y/2);
		data1.create(UnitsList.Priest, 2*X/9, Y/2+1f);
		data1.create(UnitsList.Inquisitor, X/9, Y/2+2f);
		
		data2.create(UnitsList.Crossbowman, 7*X/9, Y/2-1f);
		data2.create(UnitsList.Spearman, 7*X/9, Y/2-2f);
		data2.create(UnitsList.Knight, 7*X/9, Y/2);
		data2.create(UnitsList.Priest, 7*X/9, Y/2+1f);
		data2.create(UnitsList.Inquisitor, 7*X/9, Y/2+2f);

	}
	
	public static void initializePlateau(Game game, float maxX, float maxY){
		game.plateau = new Plateau(maxX,maxY,game);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		
	}
	
}
