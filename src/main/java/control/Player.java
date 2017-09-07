package control;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import control.KeyMapper.KeyEnum;
import data.Attributs;
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

public strictfp class Player {
	public static Rectangle rectangleSelection;
	public static Float recX;
	public static Float recY;
	public static int player;
	public static int team;
	private static int idConnexion;
	public static Vector<Integer> inRectangle = new Vector<Integer>();
	public static Vector<Integer> selection= new Vector<Integer>();
	public static Color color;
	public static int mouseOver=-1;

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
		inRectangle.clear();
		selection.clear();
		mouseOver = -1;
		rectangleSelection = null;
		recX = 0f;
		recY = 0f;
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
	
	static void handleMouseOver(InputObject im, Plateau plateau) {
		mouseOver = -1;
		Vector<Objet> mouseOvers = new Vector<Objet>();
		for (Character c : plateau.getCharacters()) {
			if (c.getSelectionBox().contains(im.x, im.y)) {
				mouseOvers.add(c);
			} 
		}
		for (Building c : plateau.getBuildings()) {
			if (c.getSelectionBox().contains(im.x, im.y)) {
				mouseOvers.add(c);
			} 
		}
		// Find nearest to selection Box for mouseOver
		
		Objet o = Utils.nearestObjectToSelectionBox(mouseOvers, im.x, im.y);
		if(o!=null){
			setMouseOver(im, o.getId());
		}
				
	}
	
	
	public static void setMouseOver(InputObject im, int id){
		im.idObjetMouse = id;
		mouseOver = id;
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
				rectangleSelection.setBounds((float) StrictMath.min(recX, im.x),
						(float) StrictMath.min(recY, im.y), (float) StrictMath.abs(im.x - recX) + 0.1f,
						(float) StrictMath.abs(im.y - recY) + 0.1f);
			}
		} else {
			Player.rectangleSelection = null;
		}
		
		// Update in rectangle
		inRectangle.clear();
		for(Character o : plateau.getCharacters()){
			if(o.getTeam().id == Player.team && rectangleIntersect(o.getSelectionBox().getMinX(), o.getSelectionBox().getMinY(),o.getSelectionBox().getMaxX(), o.getSelectionBox().getMaxY() , o.getAttribut(Attributs.sizeX))){
				inRectangle.add(o.getId());
			}
		}
		if(inRectangle.size()==0){
			for(Building o : plateau.getBuildings()){
				if(o.getTeam().id == Player.team && rectangleIntersect(o.getSelectionBox().getMinX(), o.getSelectionBox().getMinY(),o.getSelectionBox().getMaxX(), o.getSelectionBox().getMaxY() , o.getAttribut(Attributs.sizeX))){
					inRectangle.add(o.getId());
				}
			}
		}
		

	}

	public static boolean rectangleIntersect(float xMin, float yMin, float xMax, float yMax, float size){
		if(Player.rectangleSelection == null){
			return false;
		}
		double interX = StrictMath.min(xMax, Player.rectangleSelection.getMaxX()) - StrictMath.max(xMin, Player.rectangleSelection.getMinX());
		double interY = StrictMath.min(yMax, Player.rectangleSelection.getMaxY()) - StrictMath.max(yMin, Player.rectangleSelection.getMinY());
		return interX>0 && interY>0;
	}


	public static void handleSelection(InputObject im, Plateau plateau) {
		// This method put selection in im ...
		// Remove death and not team from selection
//		Set<ObjetsList> oldSelection = Player.selection.stream()
//				.map(x-> plateau.getById(x))
//				.filter(x -> x!=null)
//				.map(x -> x.name)
//				.distinct()
//				.collect(Collectors.toSet());
		
		handleMouseOver(im, plateau);
		Vector<Integer> select = new Vector<Integer>();
		Vector<Integer> selectForEmptyClick = new Vector<Integer>();
		if(rectangleSelection == null){
			select.addAll(selection);
		}
		selectForEmptyClick.addAll(selection);
		Vector<Integer> toRemove = new Vector<Integer>();
		// On enleve de la sélection les unités mortes ou converties 
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
			// Heuristique si rectangle trop petit alors on selectionne le plus proche (vis à vis du centre de la selection box)
			if(rectangleSelection!=null && rectangleSelection.getWidth()+rectangleSelection.getHeight() < 10){
				Objet toSelect = getNearestToSelectionBox(inRectangle, plateau, rectangleSelection.getX(), rectangleSelection.getY());
				if(toSelect!=null){					
					Player.selection.add(toSelect.getId());
				}
			}else{				
				for(Integer o : inRectangle){
					if(plateau.getById(o).team.id==im.team){
						Player.selection.add(o);
					}
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
				if(o.getTeam().id == im.team && pressed.getUnitsList().contains(o.getName())){
					Player.selection.add(o.getId());
				}
			}
			for(Building o : plateau.getBuildings()){
				if(o.getTeam().id == im.team && pressed.getBuildingsList().contains(o.getName())){
					Player.selection.add(o.getId());
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
			if (c != null) {
				for (Character o : plateau.getCharacters()) {
					if (o.getTeam().id == im.team && o.getName() == c.getName() && !Player.selection.contains(o.getId())) {
						// add character to team selection
						Player.selection.addElement(o.getId());
					}
				}
			}
		}
	}
	
	public static Objet getNearest(Vector<Integer> units, Plateau plateau, float x, float y){
		Vector<Objet> res = new Vector<Objet>();
		for(Integer i: units){
			Objet o = plateau.getById(i);
			if(o!=null){				
				res.add(o);
			}
		}
		if(res.size()==0){
			return null;
		}
		return Utils.nearestObject(res, x, y);
	}
	
	public static Objet getNearestToSelectionBox(Vector<Integer> units, Plateau plateau, float x, float y){
		Vector<Objet> res = new Vector<Objet>();
		for(Integer i: units){
			Objet o = plateau.getById(i);
			if(o!=null){				
				res.add(o);
			}
		}
		if(res.size()==0){
			return null;
		}
		return Utils.nearestObjectToSelectionBox(res, x, y);
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
