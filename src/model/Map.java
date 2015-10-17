package model;

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

	

	public static void createMapLan(Game game){
		initializePlateau(game, 2000f, 3000f);
		Data data1 = game.players.get(1).data;
		Data data2 = game.players.get(2).data;
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
		
	public static void createMapPhillipe(Game game){
		initializePlateau(game, 2000f, 3000f);
		Data data1 = game.players.get(1).data;
		Data data2 = game.players.get(2).data;
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
		initializePlateau(game, 800f, 600f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.players.get(1).data;
		Data data2 = game.players.get(2).data;

		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,1);
		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,2);
		
		data1.player.create(UnitsList.Spearman, 2*X/9 + (float)Math.random(), Y/2+(float)Math.random());
		for(int i = 0;i<10;i++){
			data1.player.create(UnitsList.Crossbowman, 2*X/9+i - 1f, Y/2);
		}
			

		//data1.player.create(UnitsList.Spearman, X/9 + 2f, Y/2);
		data1.player.gold = 1000;
		data1.player.food = 1000;
		data2.player.create(UnitsList.Spearman, 7*X/9 - 1f, Y/2);
		for(int i = 0;i<10;i++){
			data2.player.create(UnitsList.Crossbowman, 7*X/9+i - 1f, Y/2);
		}
		
		
		// Player 2 side

	}

	public static void createMapPhillipeMacro(Game game){
		initializePlateau(game, 5000f, 2500f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.players.get(1).data;
		Data data2 = game.players.get(2).data;
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
		bar.team = 1;
		new BuildingBarrack(game.plateau,game,15*X/18,1*Y/5);
		
		new BuildingBarrack(game.plateau,game,3*X/18,4*Y/5);
		new BuildingBarrack(game.plateau,game,15*X/18,4*Y/5);
		
		new BuildingUniversity(game.plateau,game,X/9,10*Y/11);
		new BuildingUniversity(game.plateau,game,8*X/9,Y/11);
		
		new BuildingStable(game.plateau,game,X/2,Y/6);
		new BuildingStable(game.plateau,game,X/2,5*Y/6);
		
		new BuildingAcademy(game.plateau,game,X/2,Y/2);

		for(int caca =0; caca<20; caca++)
			data1.player.create(UnitsList.Spearman, X/9 + (float)Math.random(), Y/2+(float)Math.random());

		//data1.player.create(UnitsList.Spearman, X/9 + 2f, Y/2);
		data1.player.gold = 1000;
		data1.player.food = 1000;
		data2.player.create(UnitsList.Spearman, 8*X/9 - 1f, Y/2);

		// Water

			
		// UbiSoft C'est Cool 
		new Water(5f*X/18,2f*Y/3,X/9,2f*Y/3,game.plateau);
		new Water(13f*X/18,1f*Y/3,X/9,2f*Y/3,game.plateau);
					
		// Player 2 side
	}

	public static void createMapMicro(Game game){
		initializePlateau(game, 400f, 500f);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		float X = game.plateau.maxX;
		float Y = game.plateau.maxY;
		Data data1 = game.players.get(1).data;
		Data data2 = game.players.get(2).data;

		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,1);
		new BuildingHeadQuarters(game.plateau,game,-30*X,-30*Y,2);

		data1.player.create(UnitsList.Crossbowman, X/9 - 1f, Y/2);
		data1.player.create(UnitsList.Spearman, X/9 - 2f, Y/2);
		data1.player.create(UnitsList.Knight, X/9 - 3f, Y/2);
		data1.player.create(UnitsList.Priest, X/9 - 4f, Y/2);
		data1.player.create(UnitsList.Inquisitor, X/9 - 5f, Y/2);
		
		data2.player.create(UnitsList.Crossbowman, 8*X/9 - 1f, Y/2);
		data2.player.create(UnitsList.Spearman, 8*X/9 - 2f, Y/2);
		data2.player.create(UnitsList.Knight, 8*X/9 - 3f, Y/2);
		data2.player.create(UnitsList.Priest, 8*X/9 - 4f, Y/2);
		data2.player.create(UnitsList.Inquisitor, 8*X/9 - 5f, Y/2);

	}
	
	public static void initializePlateau(Game game, float maxX, float maxY){
		game.plateau = new Plateau(maxX,maxY,game);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
	}
	
}
