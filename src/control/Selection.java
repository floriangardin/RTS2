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

public class Selection {
	public Rectangle rectangleSelection;
	public boolean waitForPressedBeforeUpdate=false;
	public Float recX;
	public Float recY;

	public int player;
	public Vector<Objet> inRectangle; // Je sais pas ce que c'est
	public Vector<Objet> selection;
	public Plateau plateau;

	public Selection(int player, Plateau plateau){
		this.rectangleSelection = null;
		this.inRectangle = new Vector<Objet>();
		this.selection = new Vector<Objet>();
		this.player = player;
		this.plateau = plateau;
	}

	public void updateRectangle(InputObject im) {
		if(waitForPressedBeforeUpdate && !im.isPressed(KeyEnum.LeftClick)){
			return;
		}else{
			waitForPressedBeforeUpdate = false;
		}
		if(im.isOnMiniMap && this.rectangleSelection==null)
			return;
		if(im.isDown(KeyEnum.LeftClick)){
			if (this.rectangleSelection == null) {
				this.selection.clear() ;// A appeler quand le rectangle est crée
				recX=(float) im.x;
				recY= (float) im.y;
				rectangleSelection= new Rectangle(recX, recX, 0.1f, 0.1f);
			} else {
				rectangleSelection.setBounds((float) Math.min(recX, im.x),
						(float) Math.min(recY, im.y), (float) Math.abs(im.x - recX) + 0.1f,
						(float) Math.abs(im.y - recY) + 0.1f);
			}
		} else {
			this.rectangleSelection = null;
		}
		
		// Update in rectangle
		inRectangle.clear();
		for(Character o : plateau.characters){
			if(rectangleIntersect(o.selectionBox.getMinX(), o.selectionBox.getMinY(),o.selectionBox.getMaxX(), o.selectionBox.getMaxY() ,o.getAttribut(Attributs.sizeX))){
				inRectangle.add(o);
			}
		}
		if(inRectangle.size()==0){
			for(Building o : plateau.buildings){
				if(rectangleIntersect(o.selectionBox.getMinX(), o.selectionBox.getMinY(),o.selectionBox.getMaxX(), o.selectionBox.getMaxY() ,o.getAttribut(Attributs.sizeX))){
					inRectangle.add(o);
				}
			}
		}

	}

	public boolean rectangleIntersect(float xMin, float yMin, float xMax, float yMax, float size){
		if(this.rectangleSelection== null){
			return false;
		}
		double interX = Math.min(xMax, this.rectangleSelection.getMaxX()) - Math.max(xMin, this.rectangleSelection.getMinX());
		double interY = Math.min(yMax, this.rectangleSelection.getMaxY()) - Math.max(yMin, this.rectangleSelection.getMinY());
		return interX>0 && interY>0;
	}

	
	public void handleSelection(InputObject im, Interface bottombar) {
		// This method put selection in im ...
		// Remove death and not team from selection
		Vector<Objet> select = new Vector<Objet>();
		if(bottombar.spellCurrent != null){
			select.addAll(selection);
		}
		Vector<Objet> toRemove = new Vector<Objet>();
		for(Objet o : selection){
			if(!o.isAlive() || o.team.id!=im.team){
				toRemove.add(o);
			}
		}
		selection.removeAll(toRemove);
		// As long as the button is pressed, the selection is updated
		this.updateRectangle(im);
		// Put the content of inRectangle in selection
		if(inRectangle.size()>0 || rectangleSelection!=null){
			this.selection.clear();
			
			}
			if(select.size()>0){
				this.selection = select;
				waitForPressedBeforeUpdate = true;
			}else{
				for(Objet o : inRectangle){
					if(o.team.id==im.team){
						this.selection.add(o);
					}
			}
		}
		im.selection = new Vector<Integer>();
		for(Objet o : selection){
			im.selection.add(o.id);
		}
		
		
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
//			this.selection = new Vector<Objet>();
//			for(Character o : plateau.characters){
//				if(o.getTeam().id == im.team && pressed.getUnitsList().contains(o.name)){
//					this.selection.add(o);
//				}
//			}
//			for(Building o : plateau.buildings){
//				if(o.getTeam().id == im.team && pressed.getBuildingsList().contains(o.name)){
//					this.selection.add(o);
//				}
//			}
//		}

		// Selection des batiments
		

		// Cleaning the rectangle and buffer if mouse is released
		//		boolean isOnMiniMap = im.xMouse>(1-im.player.bottomBar.ratioMinimapX)*g.resX && im.yMouse>(g.resY-im.player.bottomBar.ratioMinimapX*g.resX);
//
//		
//		if (this.rectangleSelection != null) {
//			if (this.selection.size() > 0
//					&& this.selection.get(0) instanceof Character) {
//				Character c = (Character) this.selection.get(0);
//				
//
//			}
//			if (player == im.idplayer && this.selection.size() > 0
//					&& this.selection.get(0) instanceof Building) {
//				Building c = (Building) this.selection.get(0);
//
//			}

		
//		}

		
		// Handling hotkeys for gestion of selection
//		if (im.isPressed(KeyEnum.Tab)) {
//			if (this.selection.size() > 0) {
//				Utils.switchTriName(this.selection);
//			}
//
//		}
		


		// we update the selection according to the rectangle wherever is the
		// mouse
//		if(!im.isOnMiniMap){
//			if (im.isPressed(KeyEnum.LeftClick) && !im.isPressed(KeyEnum.Tab)) {
//				this.selection.clear();
//		
//			}
//		}
//		if (!im.isPressed(KeyEnum.ToutSelection)) {
//			this.updateSelection(im);
//
//		} else {
//			this.updateSelectionCTRL(im);
//		}

		

		
	}
//	public void updateSelection(InputObject im) {
//		if (rectangleSelection != null) {
//			for (Objet a : this.inRectangle) {
//				this.selection.remove(a);
//			}
//			this.inRectangle.clear();
//			for (Character o : plateau.characters) {
//				if ((o.selectionBox.intersects(rectangleSelection) || o.selectionBox.contains(rectangleSelection)
//						|| rectangleSelection.contains(o.selectionBox)) && o.getTeam().id == im.team) {
//					this.selection.add(o);
//					this.inRectangle.addElement(o);
//					if (Math.max(rectangleSelection.getWidth(), rectangleSelection.getHeight()) < 2f) {
//						break;
//					}
//
//				}
//			}
//			if (this.selection.size() == 0) {
//				for (Building o : plateau.buildings) {
//					if (o.selectionBox.intersects(rectangleSelection) && o.getTeam().id == im.team) {
//						this.selection.add(o);
//						this.inRectangle.addElement(o);
//					}
//				}
//
//			} else {
//				Vector<Character> chars = new Vector<Character>();
//				for(Objet o : this.selection){
//					if(o instanceof Character){
//						chars.add((Character) o);
//					}
//				}
//
//				Utils.triId(chars);
//				this.selection.clear();
//
//				for(Character c : chars)
//					this.selection.add(c);
//			}
//			
//		}
//	}


	public void updateSelectionCTRL(InputObject im, Camera camera) {
		if (rectangleSelection != null) {
			this.selection.clear();;
			// handling the selection
			for (Character o : plateau.characters) {
				if ((o.selectionBox.intersects(rectangleSelection) || o.selectionBox.contains(rectangleSelection)) && o.getTeam().id == im.team) {
					// add character to team selection
					this.selection.add(o);
				}
			}

			if (this.selection.size() == 0) {

				for (Building o : plateau.buildings) {
					if (o.selectionBox.intersects(rectangleSelection) && o.getTeam().id ==im.team) {
						// add character to team selection
						this.selection.addElement(o);
					}
				}
			}
			Vector<Objet> visibles = plateau.getInCamObjets(camera);
			if (this.selection.size() == 1) {
				Objet ao = this.selection.get(0);
				if (ao instanceof Character) {
					for (Character o : plateau.characters) {
						if (o.getTeam().id == im.team && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							this.selection.addElement(o);
						}
					}
				} else if (ao instanceof Building) {
					for (Building o : plateau.buildings) {
						if (o.getTeam().id == im.team && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							this.selection.addElement(o);
						}
					}
				}
			}
			
		}
	}

}
