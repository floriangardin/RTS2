package mapeditor;

import java.util.HashMap;
import java.util.HashSet;

import data.Attributs;
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

public strictfp class Actions {
	
	public static strictfp class ActionCreateObjet extends Action{
		
		ObjetsList ol;
		int x, y, team;
		Objet o = null;
		
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
				switch(o.name.type){
				case Character:
					plateau.removeCharacter((Character)o);
					break;
				case NatureObject:
					plateau.removeNaturalObjets((NaturalObjet)o);
					break;
				case Building:
					plateau.removeBuilding((Building) o);
					break;
				default:
					break;
				}
				plateau.clean();
			}
		}

		@Override
		public void redo() {
			if(o==null){
				switch(ol.type){
				case NatureObject:
					o = new Tree(x,y,Integer.parseInt(ol.name().substring(5)),plateau);
					break;
				case Character:
					o = new Character(x, y, ol, plateau.teams.get(team), plateau);
					break;
				case Building:
					o = new Building(ol, x, y, plateau.teams.get(team), plateau);
					break;
				default:
					break;
				}
			} else {
				switch(o.name.type){
				case Character:
					plateau.addCharacterObjets((Character)o);
					break;
				case NatureObject:
					plateau.addNaturalObjets((NaturalObjet)o);
					break;
				case Building:
					plateau.addBuilding((Building)o);
					break;
				default:
					break;
				}
			}
			plateau.clean();
		}
	}
	
	public static strictfp class ActionDeleteObjet extends Action{
		
		Objet o;

		
		public ActionDeleteObjet(Plateau plateau, Objet o){
			super(ActionType.CreateObjet, plateau);
			this.o = o;
		}

		@Override
		public void undo() {
			switch(o.name.type){
			case Character:
				plateau.addCharacterObjets((Character)o);
				break;
			case NatureObject:
				plateau.addNaturalObjets((NaturalObjet)o);
				break;
			case Building:
				plateau.addBuilding((Building)o);
				break;
			default:
				break;
			}
			plateau.clean();
		}

		@Override
		public void redo() {
			switch(o.name.type){
			case Character:
				plateau.removeCharacter((Character)o);
				break;
			case NatureObject:
				plateau.removeNaturalObjets((NaturalObjet)o);
				break;
			case Building:
				plateau.removeBuilding((Building) o);
				break;
			default:
				break;
			}
			plateau.clean();
		}
	}
	
	public static strictfp class ActionPaintTerrain extends Action{
		
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

	public static strictfp class ActionMoveObjet extends Action{
		Objet o;
		float oldX, oldY, newX, newY;

		public ActionMoveObjet(Plateau plateau, Objet objet, float newX, float newY) {
			super(ActionType.MoveObjet, plateau);
			this.o = objet;
			this.oldX = o.x;
			this.oldY = o.y;
			this.newX = newX;
			this.newY = newY;
		}

		@Override
		public void undo() {
			o.setXY(oldX, oldY, plateau);
			plateau.mapGrid.update();
		}

		@Override
		public void redo() {
			o.setXY(newX, newY, plateau);
			plateau.mapGrid.update();
		}
		
	}
}
