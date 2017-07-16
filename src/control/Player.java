package control;

import java.util.Vector;

import org.newdawn.slick.geom.Rectangle;

import control.KeyMapper.KeyEnum;
import data.Attributs;
import display.Camera;
import display.Interface;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;

public class Player {
	public static Rectangle rectangleSelection;
	public static Float recX;
	public static Float recY;
	public static int player;
	public static int team;
	private static int idConnexion;
	public static Vector<Integer> inRectangle = new Vector<Integer>();
	public static Vector<Integer> selection= new Vector<Integer>();


	public static void init(int idConnexion){
		Player.idConnexion = idConnexion;
	}
	public static void setTeam(int team){
		Player.team = team;
	}
	public static int getID(){
		return Player.idConnexion;
	}
	public static void updateRectangle(InputObject im, Plateau plateau) {
//		if(waitForPressedBeforeUpdate && !im.isPressed(KeyEnum.LeftClick)){
//			return;
//		}else{
//			waitForPressedBeforeUpdate = false;
//		}
		if(im.isOnMiniMap && rectangleSelection != null){			
			return;
		}
		if(im.isDown(KeyEnum.LeftClick)){
			if (Player.rectangleSelection == null && !im.isOnMiniMap) {
				Player.selection.clear() ;// A appeler quand le rectangle est crée
				recX=(float) im.x;
				recY= (float) im.y;
				rectangleSelection= new Rectangle(recX, recX, 0.1f, 0.1f);
			} else if(Player.rectangleSelection != null){
				rectangleSelection.setBounds((float) Math.min(recX, im.x),
						(float) Math.min(recY, im.y), (float) Math.abs(im.x - recX) + 0.1f,
						(float) Math.abs(im.y - recY) + 0.1f);
			}
		} else {
			Player.rectangleSelection = null;
		}

		// Update in rectangle
		inRectangle.clear();
		for(Character o : plateau.characters){
			if(rectangleIntersect(o.selectionBox.getMinX(), o.selectionBox.getMinY(),o.selectionBox.getMaxX(), o.selectionBox.getMaxY() ,o.getAttribut(Attributs.sizeX))){
				inRectangle.add(o.id);
			}
		}
		if(inRectangle.size()==0){
			for(Building o : plateau.buildings){
				if(rectangleIntersect(o.selectionBox.getMinX(), o.selectionBox.getMinY(),o.selectionBox.getMaxX(), o.selectionBox.getMaxY() ,o.getAttribut(Attributs.sizeX))){
					inRectangle.add(o.id);
				}
			}
		}

	}

	public static boolean rectangleIntersect(float xMin, float yMin, float xMax, float yMax, float size){
		if(Player.rectangleSelection== null){
			return false;
		}
		double interX = Math.min(xMax, Player.rectangleSelection.getMaxX()) - Math.max(xMin, Player.rectangleSelection.getMinX());
		double interY = Math.min(yMax, Player.rectangleSelection.getMaxY()) - Math.max(yMin, Player.rectangleSelection.getMinY());
		return interX>0 && interY>0;
	}


	public static void handleSelection(InputObject im, Plateau plateau) {
		// This method put selection in im ...
		// Remove death and not team from selection
		
		Vector<Integer> select = new Vector<Integer>();
		if(rectangleSelection == null){
			select.addAll(selection);
		}
		Vector<Integer> toRemove = new Vector<Integer>();
		for(Integer o : selection){
			Objet ob = plateau.getById(o);
			if(ob==null || !ob.isAlive() || ob.team.id!=im.team){
				toRemove.add(o);
			}
		}
		selection.removeAll(toRemove);
		// As long as the button is pressed, the selection is updated
		Player.updateRectangle(im, plateau);
		// Put the content of inRectangle in selection
		if(inRectangle.size()>0 || rectangleSelection!=null){
			Player.selection.clear();
		}
		if(select.size()>0){
			Player.selection = select;
		}else{
			for(Integer o : inRectangle){
				if(plateau.getById(o).team.id==im.team){
					Player.selection.add(o);
				}
			}
		}
		im.selection = new Vector<Integer>();
		for(Integer o : selection){
			im.selection.add(o);
		}

		//System.out.println("Vanneau4"+ im.selection.size());
		// Ajout du clique droit


		// Handling groups of units
		//		KeyEnum[] tab = new KeyEnum[]{KeyEnum.Spearman,KeyEnum.Crossbowman,KeyEnum.Knight,KeyEnum.Inquisitor,KeyEnum.AllUnits,KeyEnum.Headquarters,KeyEnum.Barracks,KeyEnum.Stable};
		//		KeyEnum  pressed = null;
		//		for(KeyEnum key : tab){
		//			if(im.isPressed(key)){
		//				pressed=key;
		//				break;
		//			}
		//		}
		//
		//		if(pressed!=null){
		//			Selection.selection = new Vector<Objet>();
		//			for(Character o : plateau.characters){
		//				if(o.getTeam().id == im.team && pressed.getUnitsList().contains(o.name)){
		//					Selection.selection.add(o);
		//				}
		//			}
		//			for(Building o : plateau.buildings){
		//				if(o.getTeam().id == im.team && pressed.getBuildingsList().contains(o.name)){
		//					Selection.selection.add(o);
		//				}
		//			}
		//		}

		// Selection des batiments


		// Cleaning the rectangle and buffer if mouse is released
		//		boolean isOnMiniMap = im.xMouse>(1-im.player.bottomBar.ratioMinimapX)*g.resX && im.yMouse>(g.resY-im.player.bottomBar.ratioMinimapX*g.resX);
		//
		//		
		//		if (Selection.rectangleSelection != null) {
		//			if (Selection.selection.size() > 0
		//					&& Selection.selection.get(0) instanceof Character) {
		//				Character c = (Character) Selection.selection.get(0);
		//				
		//
		//			}
		//			if (player == im.idplayer && Selection.selection.size() > 0
		//					&& Selection.selection.get(0) instanceof Building) {
		//				Building c = (Building) Selection.selection.get(0);
		//
		//			}


		//		}


		// Handling hotkeys for gestion of selection
		//		if (im.isPressed(KeyEnum.Tab)) {
		//			if (Selection.selection.size() > 0) {
		//				Utils.switchTriName(Selection.selection);
		//			}
		//
		//		}



		// we update the selection according to the rectangle wherever is the
		// mouse
		//		if(!im.isOnMiniMap){
		//			if (im.isPressed(KeyEnum.LeftClick) && !im.isPressed(KeyEnum.Tab)) {
		//				Selection.selection.clear();
		//		
		//			}
		//		}
		//		if (!im.isPressed(KeyEnum.ToutSelection)) {
		//			Selection.updateSelection(im);
		//
		//		} else {
		//			Selection.updateSelectionCTRL(im);
		//		}




	}
	//	public void updateSelection(InputObject im) {
	//		if (rectangleSelection != null) {
	//			for (Objet a : Selection.inRectangle) {
	//				Selection.selection.remove(a);
	//			}
	//			Selection.inRectangle.clear();
	//			for (Character o : plateau.characters) {
	//				if ((o.selectionBox.intersects(rectangleSelection) || o.selectionBox.contains(rectangleSelection)
	//						|| rectangleSelection.contains(o.selectionBox)) && o.getTeam().id == im.team) {
	//					Selection.selection.add(o);
	//					Selection.inRectangle.addElement(o);
	//					if (Math.max(rectangleSelection.getWidth(), rectangleSelection.getHeight()) < 2f) {
	//						break;
	//					}
	//
	//				}
	//			}
	//			if (Selection.selection.size() == 0) {
	//				for (Building o : plateau.buildings) {
	//					if (o.selectionBox.intersects(rectangleSelection) && o.getTeam().id == im.team) {
	//						Selection.selection.add(o);
	//						Selection.inRectangle.addElement(o);
	//					}
	//				}
	//
	//			} else {
	//				Vector<Character> chars = new Vector<Character>();
	//				for(Objet o : Selection.selection){
	//					if(o instanceof Character){
	//						chars.add((Character) o);
	//					}
	//				}
	//
	//				Utils.triId(chars);
	//				Selection.selection.clear();
	//
	//				for(Character c : chars)
	//					Selection.selection.add(c);
	//			}
	//			
	//		}
	//	}

	public static Vector<Objet> getInCamObjets(Camera camera, Plateau plateau) {
		Vector<Objet> res = new Vector<Objet>();

		for(Objet o : plateau.objets.values()){
			if(camera.visibleByCamera(o.x, o.y, o.getAttribut(Attributs.sight))){
				res.add(o);
			}
		}
		return res;
	}

	public static void updateSelectionCTRL(InputObject im, Camera camera, Plateau plateau) {
		if (rectangleSelection != null) {
			Player.selection.clear();;
			// handling the selection
			for (Character o : plateau.characters) {
				if ((o.selectionBox.intersects(rectangleSelection) || o.selectionBox.contains(rectangleSelection)) && o.getTeam().id == im.team) {
					// add character to team selection
					Player.selection.add(o.id);
				}
			}

			if (Player.selection.size() == 0) {

				for (Building o : plateau.buildings) {
					if (o.selectionBox.intersects(rectangleSelection) && o.getTeam().id ==im.team) {
						// add character to team selection
						Player.selection.addElement(o.id);
					}
				}
			}
			Vector<Objet> visibles = getInCamObjets(camera, plateau);
			if (Player.selection.size() == 1) {
				Objet ao = plateau.getById(Player.selection.get(0));
				if (ao instanceof Character) {
					for (Character o : plateau.characters) {
						if (o.getTeam().id == im.team && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							Player.selection.addElement(o.id);
						}
					}
				} else if (ao instanceof Building) {
					for (Building o : plateau.buildings) {
						if (o.getTeam().id == im.team && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							Player.selection.addElement(o.id);
						}
					}
				}
			}

		}
	}

	public static int getTeamId() {
		// TODO Auto-generated method stub
		return Player.team;
	}
	public static Team getTeam(Plateau plateau){
		for(Team team : plateau.teams){
			if(team.id == Player.team){
				return team;
			}
		}
		return null;
	}

	public static boolean hasRectangleSelection() {
		// TODO Auto-generated method stub
		return rectangleSelection!=null;
	}

}
