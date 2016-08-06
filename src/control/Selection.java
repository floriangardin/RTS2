package control;

import java.util.Vector;

import model.Game;
import model.Objet;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import units.Character;
import utils.Utils;
import buildings.Building;
import control.KeyMapper.KeyEnum;

public class Selection {
	public Rectangle rectangleSelection;
	public Float recX;
	public Float recY;

	public int player;
	public Vector<Objet> inRectangle; // Je sais pas ce que c'est
	public Vector<Objet> selection;
	
	public Selection(int player){
		this.rectangleSelection = null;
		this.inRectangle = new Vector<Objet>();
		this.selection = new Vector<Objet>();
		this.player = player;
		
	}
	
	public void updateRectangle(InputObject im, int player) {
		
		if(im.isOnMiniMap && this.rectangleSelection==null)
			return;
		if (this.rectangleSelection == null || im.isPressed(KeyEnum.ToutSelection)) {
			recX=(float) im.x;
			recY= (float) im.y;
			rectangleSelection= new Rectangle(recX, recX, 0.1f, 0.1f);
		}
		rectangleSelection.setBounds((float) Math.min(recX, im.x),
				(float) Math.min(recY, im.y), (float) Math.abs(im.x - recX) + 0.1f,
				(float) Math.abs(im.y - recY) + 0.1f);
	}
	
	
	
	public void updateSelection() {
		if (rectangleSelection != null) {
			for (Objet a : this.inRectangle) {
				this.selection.remove(a);
			}
			this.inRectangle.clear();
			for (Character o : Game.g.plateau.characters) {
				if ((o.selectionBox.intersects(rectangleSelection) || o.selectionBox.contains(rectangleSelection)
						|| rectangleSelection.contains(o.selectionBox)) && o.getTeam() == Game.g.players.get(player).getTeam()) {
					this.selection.add(o);
					this.inRectangle.addElement(o);
					if (Math.max(rectangleSelection.getWidth(), rectangleSelection.getHeight()) < 2f) {
						break;
					}

				}
			}
			if (this.selection.size() == 0) {
				for (Building o : Game.g.plateau.buildings) {
					if (o.selectionBox.intersects(rectangleSelection) && o.getTeam() == Game.g.players.get(player).getTeam()) {
						this.selection.add(o);
						this.inRectangle.addElement(o);
					}
				}
				
			} else {
				Vector<Character> chars = new Vector<Character>();
				for(Objet o : this.selection){
					if(o instanceof Character){
						chars.add((Character) o);
					}
				}

				Utils.triId(chars);
				this.selection.clear();
				System.out.println(chars.size());
				for(Character c : chars)
					this.selection.add(c);
			}
			Game.g.players.get(player).groupSelection = -1;
		}
	}
	
	
	
	public void handleSelection(InputObject im) {
		// Handling groups of units
			KeyEnum[] tab = new KeyEnum[]{KeyEnum.Spearmen,KeyEnum.Bowmen,KeyEnum.Knights,KeyEnum.Inquisitors,KeyEnum.AllUnits,KeyEnum.HeadQuarters,KeyEnum.Barracks,KeyEnum.Stable};
			KeyEnum  pressed= null;
			for(KeyEnum key : tab){
				if(im.isPressed(key)){
					pressed=key;
					break;
				}
			}
			
			if(pressed!=null){
				this.selection =  new Vector<Objet>();
				for(Character o : Game.g.plateau.characters){
					if(o.getGameTeam().id == Game.g.players.get(player).getTeam() && pressed.getClassFrom().isAssignableFrom(o.getClass())){
						this.selection.add(o);
					}
				}
				for(Building o : Game.g.plateau.buildings){
					if(o.getGameTeam().id == Game.g.players.get(player).getTeam() && pressed.getClassFrom().isAssignableFrom(o.getClass())){
						this.selection.add(o);
					}
				}
			}
		


		
		// Selection des batiments
		
		
		// Cleaning the rectangle and buffer if mouse is released
		//		boolean isOnMiniMap = im.xMouse>(1-im.player.bottomBar.ratioMinimapX)*g.resX && im.yMouse>(g.resY-im.player.bottomBar.ratioMinimapX*g.resX);

		if (!im.isDown(KeyEnum.LeftClick)) {
			if (this.rectangleSelection != null) {
				// Play selection sound
				Sound s = null;
				if (player == Game.g.currentPlayer.id && this.selection.size() > 0
						&& this.selection.get(0) instanceof Character) {
					Character c = (Character) this.selection.get(0);
					if (Math.random() > 0) {
						s = Game.g.sounds.getRandomSoundUnit(c.name, "selection");
					}

				}
				if (player == Game.g.currentPlayer.id && this.selection.size() > 0
						&& this.selection.get(0) instanceof Building) {
					Building c = (Building) this.selection.get(0);
					s = Game.g.sounds.get("selection"+c.name);
				}
				if(s!=null)
					s.play(1f, Game.g.options.soundVolume);
				Game.g.players.get(player).groupSelection = -1;
			}
			
			this.rectangleSelection= null;
			this.inRectangle.clear();
		}
		// Handling hotkeys for gestion of selection
		if (im.isPressed(KeyEnum.Tab)) {
			if (this.selection.size() > 0) {
				Utils.switchTriName(this.selection);
			}
			
		}
		if (im.isPressed(KeyEnum.PouvoirSpecial)) {
			boolean hasLaunched= false;
			for(Character c : Game.g.plateau.characters){
				if(c.selectionBox.contains(im.x, im.y)){
					Game.g.players.get(player).getGameTeam().civ.launchSpell(c);
					hasLaunched = true;
					break;
				}
			}
			if(!hasLaunched){
				for(Building c : Game.g.plateau.buildings){
					if(c.selectionBox.contains(im.x, im.y)){
						Game.g.players.get(player).getGameTeam().civ.launchSpell(c);
						hasLaunched = true;
						break;
					}
				}
			}
		}
		// update the rectangle
		if (im.isDown(KeyEnum.LeftClick)) {
			// As long as the button is pressed, the selection is updated
			this.updateRectangle(im, player);
			
		}
		// we update the selection according to the rectangle wherever is the
		// mouse
		if(!im.isOnMiniMap){
			if (im.isPressed(KeyEnum.LeftClick) && !im.isPressed(KeyEnum.Tab)) {
				this.selection.clear();
				Game.g.players.get(player).groupSelection = -1;
			}
		}
		if (!im.isPressed(KeyEnum.ToutSelection)) {
			this.updateSelection();
		
		} else {
			this.updateSelectionCTRL();
		}
		// Update the selections of the players
		Game.g.players.get(player).selection.clear();
		for (Objet c : this.selection)
			Game.g.players.get(player).selection.addElement( c);
		
	}
	
	
	public void updateSelectionCTRL() {
		if (rectangleSelection != null) {
			this.selection.clear();;
			// handling the selection
			for (Character o : Game.g.plateau.characters) {
				if ((o.selectionBox.intersects(rectangleSelection) || o.selectionBox.contains(rectangleSelection)) && o.getTeam() == Game.g.players.get(player).getTeam()) {
					// add character to team selection
					this.selection.add(o);
				}
			}

			if (this.selection.size() == 0) {

				for (Building o : Game.g.plateau.buildings) {
					if (o.selectionBox.intersects(rectangleSelection) && o.getTeam() ==Game.g.players.get(player).getTeam()) {
						// add character to team selection
						this.selection.addElement(o);
					}
				}
			}
			Vector<Objet> visibles = Game.g.plateau.getInCamObjets(player);
			if (this.selection.size() == 1) {
				Objet ao = this.selection.get(0);
				if (ao instanceof Character) {
					for (Character o : Game.g.plateau.characters) {
						if (o.getTeam() == Game.g.players.get(player).getTeam() && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							this.selection.addElement(o);
						}
					}
				} else if (ao instanceof Building) {
					for (Building o : Game.g.plateau.buildings) {
						if (o.getTeam() == Game.g.players.get(player).getTeam() && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							this.selection.addElement(o);
						}
					}
				}
			}
			Game.g.players.get(Game.g.players.get(player).getTeam()).groupSelection = -1;
		}
	}

}
