package ressources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.newdawn.slick.geom.Point;

import buildings.BonusDamage;
import buildings.BonusLifePoints;
import buildings.BonusSpeed;
import buildings.BuildingAcademy;
import buildings.BuildingBarrack;
import buildings.BuildingHeadquarters;
import buildings.BuildingMill;
import buildings.BuildingMine;
import buildings.BuildingStable;
import buildings.BuildingTower;
import buildings.BuildingUniversity;
import main.Main;
import model.Data;
import model.Game;
import model.Plateau;
import nature.Tree;
import pathfinding.MapGrid;
import units.UnitsList;

public class Map {

	public static float stepGrid = 100f*Main.ratioSpace;

	public static float sizeX;
	public static float sizeY;

	public Map(){

	}

	public static Vector<String> maps(){
		Vector<String> maps = new Vector<String>();
		File repertoire = new File("ressources/maps/");
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

	public static void initializePlateau(Game game, float maxX, float maxY){
		game.plateau = new Plateau(maxX,maxY,game);
		game.plateau.mapGrid = new MapGrid(0f, game.plateau.maxX,0f, game.plateau.maxY);
		
	}

	public static void loadMap(String name, Game game){
		String fichier = "ressources/maps/"+name+".rtsmap";
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
			Point Zcam = new Point(0,0), Scam = new Point(0,0), Qcam = new Point(0,0), Dcam = new Point(0,0);
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
						case "sizeX" : sizeX = (int)Float.parseFloat(tab[2]); break;
						case "sizeY" : sizeY = (int)Float.parseFloat(tab[2]); break;
						case "Zcam" : Zcam = new Point(Float.parseFloat(tab[2])*stepGrid,Float.parseFloat(tab[3])*stepGrid); break;
						case "Scam" : Scam = new Point(Float.parseFloat(tab[2])*stepGrid,Float.parseFloat(tab[3])*stepGrid); break;
						case "Qcam" : Qcam = new Point(Float.parseFloat(tab[2])*stepGrid,Float.parseFloat(tab[3])*stepGrid); break;
						case "Dcam" : Dcam = new Point(Float.parseFloat(tab[2])*stepGrid,Float.parseFloat(tab[3])*stepGrid); break;
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
				new BuildingHeadquarters(Float.parseFloat(tab[1]),Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[0]));
			}
			// Buildings
			for(int i=0; i<buildings.size(); i++){
				//format
				// typeBuilding_team_x_y
				String[] tab = buildings.get(i).split(" ");
				switch(tab[0]){
				// usual buildings
				case "Mill" : new BuildingMill(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1])); break;
				case "Mine" : new BuildingMine(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1])); break;
				case "Barrack" : new BuildingBarrack(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1])); break;
				case "Stable" : new BuildingStable(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1])); break;
				case "Academy" : new BuildingAcademy(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1])); break;
				case "University" : new BuildingUniversity(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1])); break;
				case "Tower" : new BuildingTower(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1])); break;
				// bonus
				case "BonusLifePoints" : new BonusLifePoints(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]));break;
				case "BonusDamage" : new BonusDamage(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]));break;
				case "BonusSpeed" : new BonusSpeed(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]));break;
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
				case "Tree": new Tree(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1]));break;
				}
			}

		} catch (Exception e){
			System.out.print("erreur");
			e.printStackTrace();
		}
	}

	
}
