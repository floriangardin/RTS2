package ressources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import org.newdawn.slick.geom.Point;

import data.Data;
import main.Main;
import nature.Tree;
import plateau.Building;
import plateau.Character;
import plateau.EndCondition;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public strictfp class Map {

	public static float stepGrid = 100f*Main.ratioSpace;

	public static float sizeX;
	public static float sizeY;



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


	public static Plateau createPlateau(String nameMap, String dossier){
		String fichier = "ressources/"+dossier+"/"+nameMap+".rtsmap";
		return createPlateau(fichier);
	}
	public static Plateau createPlateau(String fichier){
		try{
			//lecture du fichier texte	
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			Vector<String> buildings = new Vector<String>();
			Vector<String> units = new Vector<String>();
			Vector<String> naturalObjects = new Vector<String>();
			Vector<String> headquarters = new Vector<String>();
			Vector<String> mapGround = new Vector<String>();
			Vector<String> endConditions = new Vector<String>();
			Vector<String> currentVector = null;

			String ligne;
			String[] tab;
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
					case "mapGround": currentVector = mapGround; break;
					case "endconditions" : currentVector = endConditions; break;
					default:
					} continue;
				default: 
				}
				if(ligne.length()>0){
					if(param){
						tab = ligne.split(" ");
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
			Plateau plateau = new Plateau((int)(sizeX*stepGrid), (int)(sizeY*stepGrid));
			Data data1 = plateau.getTeams().get(1).data;
			Data data2 = plateau.getTeams().get(2).data;
			Objet b;
			
			// Headquarters
			for(int i=0;i<headquarters.size(); i++){
				// format:
				// team_x_y
				tab = headquarters.get(i).split(" ");
				b = new Building(ObjetsList.Headquarters,(int)(float)(Float.parseFloat(tab[1])),(int)(float)(Float.parseFloat(tab[2])),plateau.getTeams().get((int)Float.parseFloat(tab[0])), plateau);
				for(int j = 3; j<tab.length; j++){
					b.addLabel(tab[j]);
				}
			}
			// Buildings
			for(int i=0; i<buildings.size(); i++){
				//format
				// typeBuilding_team_x_y
				tab = buildings.get(i).split(" ");
				//System.out.println(tab[2]);
				b = new Building(ObjetsList.valueOf(tab[0]),(int)(float)(Float.parseFloat(tab[2])),(int)(float)(Float.parseFloat(tab[3])),plateau.getTeams().get((int)Float.parseFloat(tab[1])),plateau);
				for(int j = 4; j<tab.length; j++){
					b.addLabel(tab[j]);
				}
			}
			// Units
			for(int i=0; i<units.size(); i++){
				tab = units.get(i).split(" ");
				Data data;
				if(tab[1].equals("1")){
					data = data1;
				} else if(tab[1].equals("2")){
					data = data2;
				} else {
					throw new Exception();
				}
				b = new Character(Float.parseFloat(tab[2]), Float.parseFloat(tab[3]), ObjetsList.valueOf(tab[0]), plateau.getTeams().get(Integer.parseInt(tab[1])),plateau);
				for(int j = 4; j<tab.length; j++){
					b.addLabel(tab[j]);
				}
			}
			// Vegetation
			for(int i=0; i<naturalObjects.size(); i++){
				tab = naturalObjects.get(i).split(" ");
				switch(tab[0]){
				case "Tree": 
					b = new Tree(Float.parseFloat(tab[2]),Float.parseFloat(tab[3]),(int)Float.parseFloat(tab[1]), plateau);
					for(int j = 4; j<tab.length; j++){
						b.addLabel(tab[j]);
					}
					break;
				}
			}
			// Map ground
			for(int i=0; i<mapGround.size(); i++){
				for(int j=0; j<mapGround.get(i).length(); j++){
					plateau.getMapGrid().grid.get(i).get(j).setIdTerrain(mapGround.get(i).charAt(j));
				}
			}
			plateau.update();
			// End Conditions
			for(int i=0 ; i<endConditions.size(); i++){
				tab = endConditions.get(i).split(" ");
				int team = Integer.parseInt(tab[0]);
				plateau.addEndConditions(team, EndCondition.getEndConditionFromString(endConditions.get(i), plateau));
			}
			return plateau;
		} catch (Exception e){
			System.out.print("erreur");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void savePlateau(String fichier, Plateau plateau){
		try{
			//lecture du fichier texte	
			OutputStream ips=new FileOutputStream(fichier); 
			OutputStreamWriter ipsr=new OutputStreamWriter(ips);
			BufferedWriter br=new BufferedWriter(ipsr);
			br.write("###########################\n");
			br.write("& sizeX "+(int)(plateau.getMaxX()/Map.stepGrid)+"\n");
			br.write("& sizeY "+(int)(plateau.getMaxY()/Map.stepGrid)+"\n");
			br.write("###########################\n");
			br.write("= headquarters\n");
			for(Team team : plateau.getTeams()){
				if(plateau.getHQ(team)!=null){
					br.write(""+team.id+" "+(plateau.getHQ(team)).i+" "+(plateau.getHQ(team)).j+" "+String.join(" ",plateau.getHQ(team).getLabels())+"\n");
				}
			}
			br.write("###########################\n");
			br.write("= buildings\n");
			for(Building b : plateau.getBuildings()){
				if(b.getName()!=ObjetsList.Headquarters){
					br.write(""+b.getName()+" "+b.team.id+" "+b.i+" "+b.j+" "+String.join(" ",b.getLabels())+"\n");
				}
			}
			br.write("###########################\n");
			br.write("= units\n");
			for(Character b : plateau.getCharacters()){
				br.write(""+b.getName()+" "+b.team.id+" "+b.getX()+" "+b.getY()+" "+String.join(" ",b.getLabels())+"\n");
			}
			br.write("###########################\n");
			br.write("= naturalObjects\n");
			for(NaturalObjet b : plateau.getNaturalObjets()){
				br.write("Tree "+Integer.parseInt(b.getName().name().substring(4))+" "+b.getX()+" "+b.getY()+" "+String.join(" ",b.getLabels())+"\n");
			}
			br.write("###########################\n");
			br.write("= mapGround\n");
			for(int i=0; i<plateau.getMapGrid().grid.size(); i++){
				for(int j=0; j<plateau.getMapGrid().grid.get(0).size(); j++){
					br.write(plateau.getMapGrid().grid.get(i).get(j).getIdTerrain().id);
				}
				br.write("\n");
			}
			br.write("###########################\n");
			br.write("= endconditions\n");
			for(int i=1; i<plateau.getTeams().size(); i++){
				for(EndCondition ec :plateau.getTeams().get(i).getEndConditions()) {
					br.write(ec.toString()+"/n");
				}
			}
			br.close();
			ipsr.close();
			ips.close();
		} catch (Exception e){
			System.out.print("erreur");
			e.printStackTrace();
		}
	}

	
}
