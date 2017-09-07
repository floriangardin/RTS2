package mapeditor;

import java.util.HashSet;
import java.util.Vector;

import data.Attributs;
import pathfinding.Case;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.Map;
import utils.ObjetsList;
import utils.Utils;

public strictfp class ActionHelper {

	public static int getNumberOfNewSheet(){
		Vector<Integer> v = new Vector<Integer>();
		for(SheetPanel s : MainEditor.getSheets()){
			if(s.getName().equals("New Sheet")){
				v.add(0);
			} else if(s.getName().startsWith("New Sheet (") &&
					s.getName().endsWith(")")){
				try{
					v.addElement(Integer.parseInt(s.getName().substring(11, s.getName().length()-1)));
				}catch(NumberFormatException e) {}
			}
		}
		int i=0;
		while(v.contains(i))
			i++;
		return i;
	}

	public static boolean checkCharacterEmplacement(Plateau p, ObjetsList ol, float x, float y){
		Case c = p.mapGrid.getCase(x, y);
		float min = 60f, d;
		if(c==null || !c.ok){
			return false;
		}
		for(Objet ch : p.mapGrid.getSurroundingChars(c)){
			d = Utils.distance(ch.getX(), ch.getY(), x, y);
			if(d<min){
				return false;
			}
		}
		for(Objet ch : p.mapGrid.getSurroundingNaturalObjet(c)){
			d = Utils.distance(ch.getX(), ch.getY(), x, y);
			if(d<min){
				return false;
			}
		}
		return true;
	}

	public static boolean checkBuildingEmplacement(Plateau p, ObjetsList ol, float x, float y, HashSet<Case> highlightedCases, int idBuilding){
		float sizeX = p.teams.get(0).data.getAttribut(ol, Attributs.sizeX);
		float sizeY = p.teams.get(0).data.getAttribut(ol, Attributs.sizeY);
		if(highlightedCases!=null){
			highlightedCases.clear();
		}
		Case c = p.mapGrid.getCase(x-sizeX/2+Map.stepGrid/2, y-sizeY/2+Map.stepGrid/2);
		if(c==null){
			return false;
		}
		boolean actionOK = true;
		for(int i = c.i;i<c.i + (int)(sizeX/Map.stepGrid);i++){
			for(int j = c.j; j<c.j + (int)(sizeY/Map.stepGrid); j++){
				if(i>=0 && i<p.mapGrid.grid.size() && j>=0 && j<p.mapGrid.grid.get(0).size()){
					if(highlightedCases!=null){
						highlightedCases.add(p.mapGrid.grid.get(i).get(j));
					}
					actionOK = actionOK 
							&& (p.mapGrid.grid.get(i).get(j).ok 
									|| (p.mapGrid.grid.get(i).get(j).building!=null &&  p.mapGrid.grid.get(i).get(j).building.getId() == idBuilding))
							&& p.mapGrid.grid.get(i).get(j).characters.size()==0 
							&& p.mapGrid.grid.get(i).get(j).naturesObjet.size()==0;
				} else {
					actionOK = false;
				}
			}
		}
		return actionOK;
	}
	
	public static boolean checkNatureEmplacement(Plateau p, ObjetsList ol, float x, float y){
		Case c = p.mapGrid.getCase(x, y);
		if(c==null || (!c.ok && c.naturesObjet.size()==0) || c.characters.size()>0){
			return false;
		}
		float min = 60f, d;
		for(NaturalObjet ch : p.mapGrid.getSurroundingNaturalObjet(c)){
			d = Utils.distance(ch.getX(), ch.getY(), x, y);
			if(d<min){
				return false;
			}
		}
		return true;
	}
}
