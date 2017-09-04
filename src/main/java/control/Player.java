package control;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import control.KeyMapper.KeyEnum;
import data.Attributs;
import display.Camera;
import menu.Lobby;
import menuutils.Menu_Player;
import model.Colors;
import model.Options;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import utils.Utils;

public class Player {
	public static Rectangle rectangleSelection;
	public static Float recX;
	public static Float recY;
	public static int player;
	public static int team;
	private static int idConnexion;
	public static Vector<Integer> inRectangle = new Vector<Integer>();
	public static Vector<Integer> selection= new Vector<Integer>();
	public static Color color;


	public static void init(int idConnexion){
		
		Player.idConnexion = idConnexion;
		//		System.out.println("Player line 33 : init player");
		boolean toCreate = true;
		if(Lobby.isInit()){			
			synchronized (Lobby.players) {
				for(Menu_Player mp : Lobby.players){
					if(mp.id == idConnexion){
						toCreate = false;
						break;
					}
				}
				if(toCreate){
					Lobby.players.add(new Menu_Player(idConnexion, 1, Options.nickname));
					setTeam(1);
					color = Color.blue;
				}
			}
		}
	}
	public static void setTeam(int team){
		Player.team = team;
		switch(team){
		case 0:
			color = Colors.team0;
			break;
		case 1:
			color = Colors.team1;
			break;
		case 2:
			color = Colors.team2;
			break;
		}
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
			if (Player.rectangleSelection == null && !im.isOnMiniMap && !im.isDown(KeyEnum.ToutSelection)) {
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
		for(Character o : plateau.getCharacters()){
			if(o.getTeam().id == Player.team && rectangleIntersect(o.selectionBox.getMinX(), o.selectionBox.getMinY(),o.selectionBox.getMaxX(), o.selectionBox.getMaxY() , o.getAttribut(Attributs.sizeX))){
				inRectangle.add(o.id);
			}
		}
		if(inRectangle.size()==0){
			for(Building o : plateau.getBuildings()){
				if(o.getTeam().id == Player.team && rectangleIntersect(o.selectionBox.getMinX(), o.selectionBox.getMinY(),o.selectionBox.getMaxX(), o.selectionBox.getMaxY() , o.getAttribut(Attributs.sizeX))){
					inRectangle.add(o.id);
				}
			}
		}
		

	}

	public static boolean rectangleIntersect(float xMin, float yMin, float xMax, float yMax, float size){
		if(Player.rectangleSelection == null){
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
		Vector<Integer> selectForEmptyClick = new Vector<Integer>();
		if(rectangleSelection == null){
			select.addAll(selection);
		}
		selectForEmptyClick.addAll(selection);
		Vector<Integer> toRemove = new Vector<Integer>();
		for(Integer o : selection){
			Objet ob = plateau.getById(o);
			if(ob==null || !ob.isAlive() || ob.team.id!=im.team){
				toRemove.add(o);
			}
		}
		selection.removeAll(toRemove);
		select.removeAll(toRemove);
		
		// As long as the button is pressed, the selection is updated
		Player.updateRectangle(im, plateau);
		
		// If we click on nothing, just keep the current selection
		if(rectangleSelection!=null && inRectangle.size()==0 && select.size()>0 && im.isDown(KeyEnum.LeftClick)){
			
			im.selection = new Vector<Integer>();
			for(Integer o : selectForEmptyClick){
				im.selection.add(o);
			}
			selection = selectForEmptyClick;
			inRectangle.clear();
			return;
		}
		// Put the content of inRectangle in selection
		if(inRectangle.size()>0 && rectangleSelection!=null){
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
		if(im.isPressed(KeyEnum.LeftClick) && im.isDown(KeyEnum.ToutSelection)){
			Player.updateSelectionCTRL(im, plateau);
		}
		
//		 Handling groups of units
		KeyEnum[] tab = new KeyEnum[]{KeyEnum.Spearman,KeyEnum.Crossbowman,KeyEnum.Knight,KeyEnum.Inquisitor,KeyEnum.Priest,KeyEnum.AllUnits,KeyEnum.Headquarters,KeyEnum.Barracks,KeyEnum.Stable};
		KeyEnum  pressed = null;
		for(KeyEnum key : tab){
			if(im.isPressed(key)){
				pressed=key;
				break;
			}
		}
		if(pressed!=null){
			Player.selection = new Vector<Integer>();
			for(Character o : plateau.getCharacters()){
				if(o.getTeam().id == im.team && pressed.getUnitsList().contains(o.name)){
					Player.selection.add(o.id);
				}
			}
			for(Building o : plateau.getBuildings()){
				if(o.getTeam().id == im.team && pressed.getBuildingsList().contains(o.name)){
					Player.selection.add(o.id);
				}
			}
		}
		// Handling hotkeys for gestion of selection
		if (im.isPressed(KeyEnum.Tab)) {
			if (selection.size() > 0) {
				Utils.switchTriName(selection, plateau);
			}
		}
		im.selection = new Vector<Integer>();
		for(Integer o : selection){
			im.selection.add(o);
		}
	}

	public static Vector<Objet> getInCamObjets(Plateau plateau) {
		Vector<Objet> res = new Vector<Objet>();

		for(Objet o : plateau.getObjets().values()){
			if(Camera.visibleByCamera(o.x, o.y, o.getAttribut(Attributs.sight))){
				res.add(o);
			}
		}
		return res;
	}

	public static void updateSelectionCTRL(InputObject im, Plateau plateau) {
		if (rectangleSelection == null) {
			recX=(float) im.x;
			recY= (float) im.y;
			rectangleSelection= new Rectangle(recX, recX, 0.1f, 0.1f);
			Player.selection.clear();
			// handling the selection
			Character c = null;
			float distmin = 15f, disttemp;
			for (Character o : plateau.getCharacters()) {
				disttemp = Utils.distance(o, recX, recY);
				if (disttemp<distmin && o.getTeam().id == im.team) {
					// add character to team selection
					c = o;
					rectangleSelection = null;
					distmin = disttemp;
				}
			}
			Vector<Objet> visibles = getInCamObjets(plateau);
			if (c != null) {
				for (Character o : plateau.getCharacters()) {
					if (o.getTeam().id == im.team && o.name == c.name && visibles.contains(o) && !Player.selection.contains(o.id)) {
						// add character to team selection
						Player.selection.addElement(o.id);
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
