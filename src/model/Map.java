package model;

import java.util.Vector;

import IA.IAUnit;
import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingHeadQuarters;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingUniversity;
import nature.Water;
import pathfinding.MapGrid;
import units.UnitsList;
import units.Character;

public class Map {

	public Map(){

	}
	
	public static Vector<String> maps(){
		Vector<String> maps = new Vector<String>();
		maps.add("lan");
		maps.add("empty");
		maps.add("duel");
		maps.add("testTech");
		maps.add("duelLarge");
		maps.add("micro");
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
		updateMap(Map.maps().get(id),game);
	}
	
	public static void updateMap(String name, Game game){
		switch(name){
		case "lan": createMapLan(game);break;
		case "empty": createMapEmpty(game);break;
		case "duel": createMapDuel(game);break;
		case "testTech": createMapTestTech(game);break;
		case "duelLarge": createMapDuelLarge(game);break;
		case "micro": createMapMicro(game);break;
		}
	}

	

	public static void createMapLan(Game game){
		game.plateau.setMaxXMaxY(2000f, 3000f);
		Data data1 = game.plateau.players.get(1).data;
		Data data2 = game.plateau.players.get(2).data;
		data1.create(UnitsList.Spearman, game.plateau.maxX/2+3f, 300f);
		BuildingHeadQuarters team1h = new BuildingHeadQuarters(game.plateau,game,game.plateau.maxX/2,100f,1);
		
		new BuildingMill(game.plateau,game,150f,100f);
		new BuildingMine(game.plateau,game,game.plateau.maxX-200f,100f);
		new BuildingBarrack(game.plateau,game,game.plateau.maxX/2,1*game.plateau.maxY/5);
		new BuildingStable(game.plateau,game,game.plateau.maxX/4-20f,2*game.plateau.maxY/6+20f);
		new BuildingAcademy(game.plateau,game,3*game.plateau.maxX/4+20f,2*game.plateau.maxY/6+20f);
		
		// Water

			
		// UbiSoft C'est Cool 
			new Water(175f,3.0f*game.plateau.maxY/6,350f,800f,game.plateau);
			new Water(game.plateau.maxX-175f,3.0f*game.plateau.maxY/6,350f,800f,game.plateau);
			
		// Player 2 side
		BuildingHeadQuarters team2h = new BuildingHeadQuarters(game.plateau,game,game.plateau.maxX/2,game.plateau.maxY-200f,2);
		new BuildingMill(game.plateau,game,150f,game.plateau.maxY-200f);
		new BuildingMine(game.plateau,game,game.plateau.maxX-200f,game.plateau.maxY-200f);
		new BuildingBarrack(game.plateau,game,game.plateau.maxX/2,4*game.plateau.maxY/5);
		
		data2.create(UnitsList.Spearman, game.plateau.maxX/2+3f,  game.plateau.maxY-350f);
		
		
		// CENTER
		
		// Stables and academy 
		new BuildingStable(game.plateau,game,game.plateau.maxX/4-20f, 4*game.plateau.maxY/6);
		new BuildingAcademy(game.plateau,game,3f*game.plateau.maxX/4+20f, 4*game.plateau.maxY/6);
		
		// Barrack in the middle
		new BuildingMill(game.plateau,game,game.plateau.maxX/7,game.plateau.maxY/2);
		new BuildingMine(game.plateau,game,6f*game.plateau.maxX/7,game.plateau.maxY/2);
		new BuildingUniversity(game.plateau,game,game.plateau.maxX/2,game.plateau.maxY/2);
		
	}
		
	public static void createMapDuel(Game game){
		game.plateau.setMaxXMaxY(2000f, 3000f);
		Data data1 = game.plateau.players.get(1).data;
		Data data2 = game.plateau.players.get(2).data;
		data1.create(UnitsList.Spearman, game.plateau.maxX-300f, game.plateau.maxY/2+3f);
		BuildingHeadQuarters team1h = new BuildingHeadQuarters(game.plateau,game,200f,game.plateau.maxY/2,1);
		
		
		
		new BuildingMill(game.plateau,game,200f,1*game.plateau.maxY/4+55f);
		new BuildingMine(game.plateau,game,200f,3*game.plateau.maxY/4-55f);
		new BuildingBarrack(game.plateau,game,1*game.plateau.maxX/5+100f,game.plateau.maxY/2);
		new BuildingStable(game.plateau,game,2*game.plateau.maxX/6+40f,game.plateau.maxY/4-55f);
		new BuildingAcademy(game.plateau,game,2*game.plateau.maxX/6+40f,3*game.plateau.maxY/4+55f);
		
		// Water
		
		// UbiSoft C'est Cool 
			new Water(3.0f*game.plateau.maxX/6,175f,800f,350f,game.plateau);
			new Water(3.0f*game.plateau.maxX/6,game.plateau.maxY-175f,800f,350f,game.plateau);
			
		// Player 2 side
		BuildingHeadQuarters team2h = new BuildingHeadQuarters(game.plateau,game,game.plateau.maxX-200f,game.plateau.maxY/2,2);
		new BuildingMill(game.plateau,game,game.plateau.maxX-200f,1*game.plateau.maxY/4+55f);
		new BuildingMine(game.plateau,game,game.plateau.maxX-200f,3*game.plateau.maxY/4-55f);
		new BuildingBarrack(game.plateau,game,4*game.plateau.maxX/5-100f,game.plateau.maxY/2);

		data2.create(UnitsList.Spearman,  game.plateau.maxX-350f, game.plateau.maxY/2+3f);
		
		
		// CENTER
		
		// Stables and academy 
		new BuildingStable(game.plateau,game, 4*game.plateau.maxX/6,game.plateau.maxY/4-55f);
		new BuildingAcademy(game.plateau,game, 4*game.plateau.maxX/6,3f*game.plateau.maxY/4+55f);
		
		// Barrack in the middle
		new BuildingMill(game.plateau,game,game.plateau.maxX/2,game.plateau.maxY/7);
		new BuildingMine(game.plateau,game,game.plateau.maxX/2,6f*game.plateau.maxY/7);
		new BuildingUniversity(game.plateau,game,game.plateau.maxX/2,game.plateau.maxY/2);
		
	}

	public static void createMapEmpty(Game game){
		game.plateau.setMaxXMaxY(800f, 600f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.plateau.players.get(1).data;
		Data data2 = game.plateau.players.get(2).data;

		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,1);
		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,2);
		
		//data1.player.create(UnitsList.Spearman, 2*X/9 + (float)Math.random(), Y/2+(float)Math.random());
		for(int i = 0;i<1;i++){
			data1.create(UnitsList.Crossbowman, 2*X/9+i - 1f, Y/2);
		}
			

		//data1.player.create(UnitsList.Spearman, X/9 + 2f, Y/2);
		data1.gameteam.gold = 1000;
		data1.gameteam.food = 1000;
		
		for(int i = 0;i<1;i++){
			data2.create(UnitsList.Crossbowman, 7*X/9+i - 1f, Y/2);
		}
		
		
		// Player 2 side

	}

	public static void createMapTestTech(Game game){
		game.plateau.setMaxXMaxY(1000f, 1000f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.plateau.players.get(1).data;
		Data data2 = game.plateau.players.get(2).data;

		new BuildingHeadQuarters(game.plateau,game,0f,0f,1);
		new BuildingHeadQuarters(game.plateau,game,X-200f,0f,2);
		//new BuildingBarrack(game.plateau,game,0f,Y-200f);
		//new BuildingBarrack(game.plateau,game,X-200f,Y-200f);
		
		//data1.player.create(UnitsList.Spearman, 2*X/9 + (float)Math.random(), Y/2+(float)Math.random());
		for(int i = 0;i<1;i++){
			data1.create(UnitsList.Inquisitor, 2*X/9+i - 1f, Y/2);
			//data1.player.create(UnitsList.Spearman, 2*X/9+i - 1f, Y/2+1f);
		}
		//data1.player.create(UnitsList.Spearman, X/9 + 2f, Y/2);
		data1.gameteam.gold = 1000;
		data1.gameteam.food = 1000;
		
		data2.gameteam.gold = 1000;
		data2.gameteam.food = 1000;
		
		for(int i = 0;i<1;i++){
			data2.create(UnitsList.Inquisitor, 7*X/9+i - 1f, Y/2);
			//data2.player.create(UnitsList.Spearman, 7*X/9+i - 1f, Y/2+1f);
		}
		
		// Player 2 side

	}
	
	public static void createMapDuelLarge(Game game){
		game.plateau.setMaxXMaxY(5000f, 2500f);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.plateau.players.get(1).data;
		Data data2 = game.plateau.players.get(2).data;
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
		
		BuildingBarrack bar = new BuildingBarrack(game.plateau,game,3*X/18,1*Y/5);

		new BuildingBarrack(game.plateau,game,15*X/18,1*Y/5);
		
		new BuildingBarrack(game.plateau,game,3*X/18,4*Y/5);
		new BuildingBarrack(game.plateau,game,15*X/18,4*Y/5);
		
		new BuildingUniversity(game.plateau,game,X/9,10*Y/11);
		new BuildingUniversity(game.plateau,game,8*X/9,Y/11);
		
		new BuildingStable(game.plateau,game,X/2,Y/6);
		new BuildingStable(game.plateau,game,X/2,5*Y/6);
		
		new BuildingAcademy(game.plateau,game,X/2,Y/2);

		for(int c =0; c<20; c++)
			data1.create(UnitsList.Spearman, X/9 + (float)Math.random(), Y/2+(float)Math.random());

		//data1.player.create(UnitsList.Spearman, X/9 + 2f, Y/2);
		data1.gameteam.gold = 1000;
		data1.gameteam.food = 1000;
		data2.create(UnitsList.Spearman, 8*X/9 - 1f, Y/2);

		// Water

			
		// UbiSoft C'est Cool 
		new Water(5f*X/18,2f*Y/3,X/9,2f*Y/3,game.plateau);
		new Water(13f*X/18,1f*Y/3,X/9,2f*Y/3,game.plateau);
					
		// Player 2 side
	}

	public static void createMapMicro(Game game){
		game.plateau.setMaxXMaxY(400f, 500f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.plateau.players.get(1).data;
		Data data2 = game.plateau.players.get(2).data;

		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,1);
		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,2);

		data1.create(UnitsList.Crossbowman, X/9 - 1f, Y/2);
		data1.create(UnitsList.Spearman, X/9 - 2f, Y/2);
		data1.create(UnitsList.Knight, X/9 - 3f, Y/2);
		data1.create(UnitsList.Priest, X/9 - 4f, Y/2);
		data1.create(UnitsList.Inquisitor, X/9 - 5f, Y/2);
		
		data2.create(UnitsList.Crossbowman, 8*X/9 - 1f, Y/2);
		data2.create(UnitsList.Spearman, 8*X/9 - 2f, Y/2);
		data2.create(UnitsList.Knight, 8*X/9 - 3f, Y/2);
		data2.create(UnitsList.Priest, 8*X/9 - 4f, Y/2);
		data2.create(UnitsList.Inquisitor, 8*X/9 - 5f, Y/2);

	}
	
	public static void initializePlateau(Game game, float maxX, float maxY){
		game.plateau = new Plateau(maxX,maxY,game);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
	}
	
}
