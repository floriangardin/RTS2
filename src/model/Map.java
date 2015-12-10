package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import buildings.BonusDamage;
import buildings.BonusLifePoints;
import buildings.BonusSpeed;
import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingHeadQuarters;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingTower;
import buildings.BuildingUniversity;
import nature.Tree;
import pathfinding.MapGrid;
import units.UnitsList;

public class Map {

	public static float stepGrid = 100f;

	public static float sizeX;
	public static float sizeY;

	public Map(){

	}

	public static Vector<String> maps(){
		Vector<String> maps = new Vector<String>();
		File repertoire = new File("ressources/");
		File[] files=repertoire.listFiles();
		for(int i=0; i<files.length; i++){
			if(files[i].getName().endsWith(".rtsmap")){
				maps.add(files[i].getName().substring(0, files[i].getName().length()-7));
			}
		}
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
		changeMap(Map.maps().get(id),game);
	}

	public static void changeMap(String name, Game game){
		game.plateau.initializePlateau(game);
		updateMap(name, game);
	}

	public static void updateMap(String name, Game game){
		loadMap(name,game);
	}


//	public static void createMapDuelLarge(Game game){
//				game.plateau.setMaxXMaxY(5000f, 2500f);
//				game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
//				float X = game.plateau.maxX;
//				float Y = game.plateau.maxY;
//				Data data1 = game.teams.get(1).data;
//				Data data2 = game.teams.get(2).data;
//				//HQ
//				new BuildingHeadQuarters(game.plateau,game,3*X/18,Y/2,1);
//				new BuildingHeadQuarters(game.plateau,game,15*X/18,Y/2,2);
//				new BuildingMill(game.plateau,game,X/18,3*Y/5);
//				new BuildingMill(game.plateau,game,17*X/18,3*Y/5);
//		
//				new BuildingMill(game.plateau,game,X/18,2*Y/5);
//				new BuildingMill(game.plateau,game,17*X/18,2*Y/5);
//		
//				new BuildingMill(game.plateau,game,7*X/18,3*Y/5);
//				new BuildingMill(game.plateau,game,11*X/18,2*Y/5);
//		
//				new BuildingMine(game.plateau,game,7*X/18,2*Y/5);
//				new BuildingMine(game.plateau,game,11*X/18,3*Y/5);
//		
//				new BuildingMine(game.plateau,game,X/18,4*Y/5);
//				new BuildingMine(game.plateau,game,17*X/18,4*Y/5);
//		
//				new BuildingMine(game.plateau,game,X/18,1*Y/5);
//				new BuildingMine(game.plateau,game,17*X/18,1*Y/5);
//		
//				new BuildingBarrack(game.plateau,game,3*X/18,1*Y/5);
//		
//				new BuildingBarrack(game.plateau,game,15*X/18,1*Y/5);
//		
//				new BuildingBarrack(game.plateau,game,3*X/18,4*Y/5);
//				new BuildingBarrack(game.plateau,game,15*X/18,4*Y/5);
//		
//				new BuildingUniversity(game.plateau,game,X/9,10*Y/11);
//				new BuildingUniversity(game.plateau,game,8*X/9,Y/11);
//		
//				new BuildingStable(game.plateau,game,X/2,Y/6);
//				new BuildingStable(game.plateau,game,X/2,5*Y/6);
//		
//				new BuildingAcademy(game.plateau,game,X/2,Y/2);
//		
//				//for(int c =0; c<50; c++)
//				data1.create(UnitsList.Spearman, X/9, Y/2);
//		
//				//data1.player.create(UnitsList.Spearman, X/9 + 2f, Y/2);
//				data2.create(UnitsList.Spearman, 8*X/9 - 1f, Y/2);
//		
//				// Water
//		
//		
//				new Water(5f*X/18,2f*Y/3,X/9,2f*Y/3,game.plateau);
//				new Water(13f*X/18,1f*Y/3,X/9,2f*Y/3,game.plateau);
//		
//				// Player 2 side
//	}

	
	public static void initializePlateau(Game game, float maxX, float maxY){
		game.plateau = new Plateau(maxX,maxY,game);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
	}

	public static void loadMap(String name, Game game){
		String fichier = "ressources/"+name+".rtsmap";
		try{
			//lecture du fichier texte	
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			Vector<String> buildings = new Vector<String>();
			Vector<String> units = new Vector<String>();
			Vector<String> naturalObjects = new Vector<String>();
			Vector<String> headquarters = new Vector<String>();
			Vector<String> currentVector = null;
			String ligne;
			int sizeX = 0, sizeY = 0;
			boolean param;
			while ((ligne=br.readLine())!=null){
				param = false;
				if(ligne.length()<=0){
					continue;
				}
				switch(ligne.charAt(0)){
				case '#': continue; 
				case '&': param = true; break;
				case '=': 
					switch(ligne.substring(2)){
					case "buildings": currentVector = buildings; break;
					case "units": currentVector = units; break;
					case "naturalObjects": currentVector = naturalObjects; break;
					case "headquarters": currentVector = headquarters; break;
					default:
					} continue;
				default: 
				}
				if(ligne.length()>0){
					if(param){
						String[] tab = ligne.split(" ");
						switch(tab[1]){
						case "sizeX" : sizeX = Integer.parseInt(tab[2]); break;
						case "sizeY" : sizeY = Integer.parseInt(tab[2]); break;
						default:
						}
					} else if(currentVector!=null){
						currentVector.add(ligne);
					} else {
						throw new Exception();
					}
				}
			}
			br.close(); 
			// Création de la map
			game.plateau.setMaxXMaxY(sizeX*stepGrid, sizeY*stepGrid);
			Data data1 = game.teams.get(1).data;
			Data data2 = game.teams.get(2).data;
			// Headquarters
			for(int i=0;i<headquarters.size(); i++){
				// format:
				// team_x_y
				String[] tab = headquarters.get(i).split(" ");
				BuildingHeadQuarters team1h = new BuildingHeadQuarters(game.plateau,game,Integer.parseInt(tab[1]),Integer.parseInt(tab[2]),Integer.parseInt(tab[0]));
			}
			// Buildings
			for(int i=0; i<buildings.size(); i++){
				//format
				// typeBuilding_team_x_y
				String[] tab = buildings.get(i).split(" ");
				switch(tab[0]){
				// usual buildings
				case "Mill" : new BuildingMill(game.plateau,game,Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),Integer.parseInt(tab[1])); break;
				case "Mine" : new BuildingMine(game.plateau,game,Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),Integer.parseInt(tab[1])); break;
				case "Barrack" : new BuildingBarrack(game.plateau,game,Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),Integer.parseInt(tab[1])); break;
				case "Stable" : new BuildingStable(game.plateau,game,Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),Integer.parseInt(tab[1])); break;
				case "Academy" : new BuildingAcademy(game.plateau,game,Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),Integer.parseInt(tab[1])); break;
				case "University" : new BuildingUniversity(game.plateau,game,Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),Integer.parseInt(tab[1])); break;
				case "Tower" : new BuildingTower(game.plateau,game,Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),Integer.parseInt(tab[1])); break;
				// bonus
				case "BonusLifePoints" : new BonusLifePoints(game.plateau, Integer.parseInt(tab[1]),Integer.parseInt(tab[2]));
				case "BonusDamage" : new BonusDamage(game.plateau, Integer.parseInt(tab[1]),Integer.parseInt(tab[2]));
				case "BonusSpeed" : new BonusSpeed(game.plateau, Integer.parseInt(tab[1]),Integer.parseInt(tab[2]));
				default : 
				}
			}
			// Units
			for(int i=0; i<units.size(); i++){
				String[] tab = units.get(i).split(" ");
				Data data;
				if(tab[1].equals("1")){
					data = data1;
				} else if(tab[1].equals("2")){
					data = data2;
				} else {
					throw new Exception();
				}
				data.create(UnitsList.switchName(tab[0]), Float.parseFloat(tab[2])*Map.stepGrid, Float.parseFloat(tab[3])*Map.stepGrid);
			}
			// Vegetation
			for(int i=0; i<naturalObjects.size(); i++){
				String[] tab = naturalObjects.get(i).split(" ");
				switch(tab[0]){
				case "Tree": new Tree(Integer.parseInt(tab[2]),Integer.parseInt(tab[3]),game.plateau,Integer.parseInt(tab[1]));break;
				}
			}

		} catch (Exception e){
			System.out.print("erreur");
			e.printStackTrace();
		}
	}

}
