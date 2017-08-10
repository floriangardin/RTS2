package mapeditor;

import java.util.HashMap;
import java.util.HashSet;

import nature.Tree;
import pathfinding.Case;
import pathfinding.Case.IdTerrain;
import plateau.Building;
import plateau.Character;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.Map;
import utils.ObjetsList;

public class Actions {
	
	public static class ActionCreateObjet extends Action{
		
		ObjetsList ol;
		int x, y, team;
		Objet o;
		
		public ActionCreateObjet(Plateau plateau, ObjetsList ol, int team, int x, int y){
			super(ActionType.CreateObjet, plateau);
			this.ol = ol;
			this.team = team;
			this.x = x;
			this.y = y;
		}

		@Override
		public void undo() {
			if(o!=null){
				o.lifePoints = -1;
				plateau.clean();
			}
		}

		@Override
		public void redo() {
			switch(ol.type){
			case "NatureObject":
				o = new Tree(x,y,Integer.parseInt(ol.name().substring(5)),plateau);
				break;
			case "Character":
				o = new Character(x, y, ol, plateau.teams.get(team), plateau);
				break;
			default:
				break;
			}
			plateau.clean();
		}
	}
	
	public static class ActionDeleteObjet extends Action{
		
		Objet o;
		float lifepoints;
		float x, y;
		
		public ActionDeleteObjet(Plateau plateau, Objet o){
			super(ActionType.CreateObjet, plateau);
			this.o = o;
			this.x = o.x;
			this.y = o.y;
			this.lifepoints = o.lifePoints;
		}

		@Override
		public void undo() {
			o.lifePoints = lifepoints;
			o.x = x;
			o.y = y;
			switch(o.name.type){
			case "Character":
				plateau.addCharacterObjets((Character)o);
				break;
			case "NatureObjet":
				plateau.addNaturalObjets((NaturalObjet)o);
				break;
			case "Building":
				plateau.addBuilding((Building)o);
				break;
			}
			plateau.clean();
		}

		@Override
		public void redo() {
			o.lifePoints = -1f;
			plateau.clean();
		}
	}
	
	public static class ActionPaintTerrain extends Action{
		
		HashMap<Integer, IdTerrain> idCases;
		IdTerrain idTerrain;

		public ActionPaintTerrain(Plateau plateau, HashSet<Case> highlightedCases, IdTerrain idTerrain) {
			super(ActionType.PaintTerrain, plateau);
			this.idTerrain = idTerrain;
			this.idCases = new HashMap<Integer, IdTerrain>();
			for(Case c : highlightedCases){
				if(c!=null && c.getIdTerrain()!=idTerrain){
					idCases.put(c.id, c.getIdTerrain());
				}
			}
		}

		@Override
		public void undo() {
			for(Integer i : idCases.keySet()){
				plateau.mapGrid.getCase(i).setIdTerrain(idCases.get(i));
			}
		}

		@Override
		public void redo() {
			for(Integer i : idCases.keySet()){
				plateau.mapGrid.getCase(i).setIdTerrain(idTerrain);
			}
		}
		
	}

}
