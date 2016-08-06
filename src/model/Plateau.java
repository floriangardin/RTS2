package model;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import buildings.Bonus;
import buildings.Building;
import buildings.BuildingAction;
import buildings.BuildingBarrack;
import buildings.BuildingHeadquarters;
import buildings.BuildingProduction;
import buildings.BuildingStable;
import buildings.BuildingTower;
import bullets.Bullet;
import bullets.CollisionBullet;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import control.Selection;
import display.BottomBar;
import main.Main;
import pathfinding.Case;
import pathfinding.MapGrid;
import ressources.Map;
import spells.Spell;
import spells.SpellEffect;
import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitSpearman;

public class Plateau {

	public float maxX;
	public float maxY;

	// Camera
	public int Xcam;
	public int Ycam;
	public boolean slidingCam = false;
	public Point objectiveCam = new Point(0,0);

	// debug
	public boolean collisionSwitch;

	// ADD ALL OBJETS
	public Vector<Character> characters;
	public Vector<Character> toAddCharacters;
	public Vector<Character> toRemoveCharacters;

	public Vector<Bullet> bullets;
	public Vector<Bullet> toAddBullets;
	public Vector<Bullet> toRemoveBullets;

	public Vector<Building> buildings;
	public Vector<Building> toAddBuildings;
	public Vector<Building> toRemoveBuildings;

	public Vector<Checkpoint> checkpoints;
	public Vector<Checkpoint> markersBuilding; 
	public Vector<Bonus> bonus;

	public Vector<NaturalObjet> naturalObjets;
	public Vector<NaturalObjet> toAddNaturalObjets;
	public Vector<NaturalObjet> toRemoveNaturalObjets;


	public Vector<SpellEffect> spells;
	public Vector<SpellEffect> toAddSpells;
	public Vector<SpellEffect> toRemoveSpells;


	


	public MapGrid mapGrid;


	public Plateau(float maxX, float maxY, Game g) {

		this.mapGrid = new MapGrid(0f, maxX, 0f, maxY);
		// GENERAL
		this.maxX = maxX;
		this.maxY = maxY;

		initializePlateau(g);



	}

	public void initializePlateau(Game g) {
		// CHARACTERS
		this.characters = new Vector<Character>();

		this.toAddCharacters = new Vector<Character>();
		this.toRemoveCharacters = new Vector<Character>();
		// WEAPONS
		this.bullets = new Vector<Bullet>();
		this.toAddBullets = new Vector<Bullet>();
		this.toRemoveBullets = new Vector<Bullet>();
		// NATURALOBJETS
		this.naturalObjets = new Vector<NaturalObjet>();
		this.toAddNaturalObjets = new Vector<NaturalObjet>();
		this.toRemoveNaturalObjets = new Vector<NaturalObjet>();
		// SPELLS
		this.spells = new Vector<SpellEffect>();
		this.toAddSpells = new Vector<SpellEffect>();
		this.toRemoveSpells = new Vector<SpellEffect>();

		// BONUS
		this.bonus = new Vector<Bonus>();

		// ENEMYGENERATOR
		this.buildings = new Vector<Building>();
		this.toAddBuildings = new Vector<Building>();
		this.toRemoveBuildings = new Vector<Building>();
		//temporary Checkpoints ( markers )
		this.checkpoints = new Vector<Checkpoint>();
		this.markersBuilding = new Vector<Checkpoint>();


		



		Game.g.idChar = 0;
		Game.g.idBullet = 0;
		for(GameTeam t : g.teams){
			t.pop = 0;
		}
	}

	public void setMaxXMaxY(float MaxX, float MaxY) {
		this.maxX = MaxX;
		this.maxY = MaxY;
		this.mapGrid = new MapGrid(0f, maxX, 0f, maxY);
		for (Player p : Game.g.players) {
			p.bottomBar.minimap.updateRatio();
		}
	}

	public void addCharacterObjets(Character o) {
		toAddCharacters.addElement(o);
	}

	public void removeCharacter(Character o) {
		toRemoveCharacters.addElement(o);

		for (Selection s : Game.g.inputsHandler.selection) {
			if (s.selection.contains(o)) {
				s.selection.remove(o);
			}
		}

	}

	public void addBulletObjets(Bullet o) {
		toAddBullets.addElement(o);
	}

	private void removeBullet(Bullet o) {
		toRemoveBullets.addElement(o);
	}

	public void addNaturalObjets(NaturalObjet o) {
		this.mapGrid.insertNewRec(o.x, o.y, Map.stepGrid,Map.stepGrid);
		toAddNaturalObjets.addElement(o);
	}

	private void removeNaturalObjets(NaturalObjet o) {
		toRemoveNaturalObjets.addElement(o);
	}

	public void addBuilding(Building o) {
		this.mapGrid.insertNewRec(o.x, o.y, o.sizeX, o.sizeY);
		toAddBuildings.addElement(o);
	}

	public void removeBuilding(Building o) {
		toRemoveBuildings.addElement(o);
	}

	public void addSpell(SpellEffect o) {
		toAddSpells.addElement(o);
	}

	public void removeSpell(SpellEffect o) {
		toRemoveSpells.addElement(o);
	}



	// General methods

	public void clean() {
		// Clean the buffers and handle die
		// Remove and add considering alive
		for (Character o : characters) {
			if (!o.isAlive()) {
				this.removeCharacter(o);
				o.getGameTeam().pop--;
				Sound s = Game.g.sounds.getRandomSoundUnit(o.name,"death");
				if(s!=null)
					s.play(1f, Game.g.options.soundVolume);
			}
		}
		for (Bullet o : bullets) {
			if (!o.isAlive()) {
				this.removeBullet(o);
			}
		}
		for (NaturalObjet o : naturalObjets) {
			if (!o.isAlive()) {
				this.removeNaturalObjets(o);
			}
		}
		for (Building o : buildings) {
			if (!o.isAlive()) {
				this.removeBuilding(o);
			}
		}
		for (SpellEffect o : spells) {
			if (!o.isAlive()) {
				this.removeSpell(o);
			}
		}
		if(Game.debugMemory){
			System.out.println("\n nouveau tour ");
			System.out.println("characters : " + characters.size());
			System.out.println("bullets : " + bullets.size());
			System.out.println("naturalObjects : " + naturalObjets.size());
			System.out.println("buildings : " + buildings.size());
			System.out.println("spells : " + spells.size());
			System.out.println("checkpoints : " + checkpoints.size());
			System.out.println("markers building : " + markersBuilding.size());

		}

		Vector<Checkpoint> toremove = new Vector<Checkpoint>();
		for (Checkpoint o : checkpoints) {
			if (!o.isAlive()) {
				toremove.add(o);
			}
		}
		checkpoints.removeAll(toremove);
		// Update selection and groups
		Vector<ActionObjet> toDelete = new Vector<ActionObjet>();

		// Remove objets from lists
		for (Character o : toRemoveCharacters) {
			characters.remove(o);
			o.destroy();

		}
		for (Character o : toAddCharacters) {
			characters.addElement(o);
		}
		for (SpellEffect o : toAddSpells) {
			spells.addElement(o);
		}
		for (SpellEffect o : toRemoveSpells) {
			spells.remove(o);
		}
		for (Bullet o : toRemoveBullets) {
			bullets.remove(o);
		}
		for (Bullet o : toAddBullets) {
			bullets.addElement(o);
		}
		for (NaturalObjet o : toRemoveNaturalObjets) {
			naturalObjets.remove(o);
		}
		for (NaturalObjet o : toAddNaturalObjets) {
			naturalObjets.addElement(o);
		}
		for (Building o : toRemoveBuildings) {
			buildings.remove(o);
		}
		for (Building o : toAddBuildings) {
			buildings.addElement(o);
		}


		toRemoveCharacters.clear();
		toRemoveBullets.clear();
		toRemoveNaturalObjets.clear();
		toRemoveSpells.clear();
		toRemoveBuildings.clear();
		toAddCharacters.clear();
		toAddSpells.clear();
		toAddBullets.clear();
		toAddNaturalObjets.clear();
		toAddBuildings.clear();
	}

	public void collision() {
		this.mapGrid.updateSurroundingChars();
		for (Character o : characters) {
			// Handle collision between actionObjets and action objects
			if (o.c != null) {
				for (Character i : o.c.surroundingChars) {
					// We suppose o and i have circle collision box
					if (i != o && Utils.distance(i, o) < (i.size + o.size)) {
						i.collision(o);
						o.collision(i);
					}
				}
			}
			Circle range = new Circle(o.x, o.y, o.range);
			// Between bonus and characters
			for (Bonus b : this.bonus) {
				if (Utils.distance(b, o) < b.hitBoxSize) {
					b.collision(o);
				}
				if (Utils.distance(b, o) < (b.size + range.radius)) {
					b.collisionWeapon(o);
				}
			}
			// between Characters and Natural objects
			for (NaturalObjet i : naturalObjets) {
				if (i.collisionBox.intersects(o.collisionBox)) {
					o.collision(i);
				}
			}
			// Between Characters and bullets
			for (Bullet i : bullets) {
				if (i instanceof CollisionBullet && Utils.distance(i, o) < (i.size + o.size)) {
					i.collision(o);
				}
			}

			// Between characters and buildings

			for (Building e : buildings) {
				if (e.collisionBox.intersects(range)) {
					e.collisionWeapon(o);
				}
				if (e.collisionBox.intersects(o.collisionBox)) {
					boolean doCollision = true;
					for(Circle c : e.corners){
						if(Utils.distance(new Checkpoint(this,c.getCenterX(),c.getCenterY()), o)<(30f+o.size)){
							doCollision = false;
						}
					}
					if(doCollision)
						o.collision(e);
				}
				else{

				}
			}
			// Between spells and characters
			for (SpellEffect s : this.spells) {
				if (s.collisionBox != null) {
					if (s.collisionBox.intersects(o.collisionBox)) {
						s.collision(o);
					}
				}
			}
		}
		// Between bullets and natural objets
		for (Bullet b : bullets) {
			for (NaturalObjet n : naturalObjets) {
				if (b.collisionBox.intersects(n.collisionBox))
					b.collision(n);
			}
			for (Building c : buildings) {
				if (b.collisionBox.intersects(c.collisionBox))
					b.collision(c);
			}
		}

	}

	public void action() {
		for (Checkpoint a : this.checkpoints) {
			a.action();
		}
		for (Checkpoint a : this.markersBuilding) {
			a.action();
		}
		for (Character o : this.characters) {
			o.action();
		}
		for (Bullet o : bullets) {
			o.action();
		}
		for (Building e : this.buildings) {
			e.action();
		}
		for (ActionObjet a : this.spells) {
			a.action();
		}
		for (Bonus a : this.bonus) {
			a.action();
		}
		for (GameTeam gt : Game.g.teams){
			gt.civ.update();
		}
	}

	


	// handling the input
	public void updateTarget(float x, float y, int team, int mode) {
		// called when right click on the mouse
		Objet target = this.findTarget(x, y,team);
		if (target == null) {
			target = new Checkpoint(this, x, y);
		}
		int i = 0;
		for (Objet c : Game.g.inputsHandler.getSelection(team).selection) {
			if(c instanceof Building && mode==Character.DESTROY_BUILDING){
				((Building) c).giveUpProcess = true;
				continue;
			}
			if (c instanceof Character) {
				Character o = (Character) c;
				if (mode == Character.HOLD_POSITION) {
					o.setTarget(null);
					o.stop();
					o.secondaryTargets.clear();
					o.mode = Character.HOLD_POSITION;
					if (o.group != null && o.group.size() > 1) {
						for (Character c1 : o.group)
							if (c1 != o)
								c1.group.remove(o);
					}
					// Then we create its new group
					o.group = new Vector<Character>();
					continue;
				}
				if (i == 0 && Math.random() > 0.3) {
					Sound s = null;
					if (c.getTeam() == Game.g.currentPlayer.id && target instanceof Character
							&& c.getTeam() != target.getTeam()) {
						s = Game.g.sounds.getRandomSoundUnit(c.name, "attack");
					} else if (c.getTeam() == Game.g.currentPlayer.id) {
						s = Game.g.sounds.getRandomSoundUnit(c.name, "target");
					}
					if(s!=null)
						s.play(1f, Game.g.options.soundVolume);
				}
				i++;
				// first we deal with o's elder group
				if (o.group != null && o.group.size() > 1) {
					for (Character c1 : o.group)
						if (c1 != o)
							c1.group.remove(o);
				}
				// Then we create its new group
				o.group = new Vector<Character>();
				Vector<Case> waypoints = null;
				for (Objet c1 : Game.g.inputsHandler.getSelection(team).selection) {
					if (c1 == c)
						continue;
					if (c1 instanceof Character) {
						o.group.add((Character) c1);
						// System.out.println("Plateau line 507: " +
						// (waypoints!=null) +" "+(c.c==c1.c)+"
						// "+(((Character)c1).waypoints.size()>0));
						if (((Character) c1).waypoints != null && c1.c == c.c && c1.getTarget() != null
								&& c1.getTarget().c == target.c) {
							// System.out.println("Plateau line 508 : copie
							// d'une chemin");
							waypoints = ((Character) c1).waypoints;
						}
					}
				}
				if(target instanceof Building){
					mode = Character.TAKE_BUILDING;
				}

				o.setTarget(target, waypoints, mode);
				o.secondaryTargets.clear();
			}
		}
	}

	public void updateSecondaryTarget(float x, float y, int team) {
		// called when right click on the mouse
		Objet target = this.findTarget(x, y,team);
		if (target == null) {
			target = new Checkpoint(this, x, y);
		}
		for (Objet c : Game.g.inputsHandler.getSelection(team).selection) {
			if (c instanceof Character) {
				Character o = (Character) c;
				// first we deal with o's elder group
				if (o.group != null && o.group.size() > 1) {
					for (Character c1 : o.group)
						if (c1 != o)
							c1.group.remove(o);
				}
				// Then we create its new group
				o.group = new Vector<Character>();
				for (Objet c1 : Game.g.inputsHandler.getSelection(team).selection)
					if (c1 instanceof Character)
						o.group.add((Character) c1);
				o.secondaryTargets.add(target);
			}
		}
	}

	// calling method to the environment
	public Vector<Character> getEnnemiesInSight(Character caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : characters) {
			if (o.getTeam() != caller.getTeam() && o.collisionBox.intersects(caller.sightBox)) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Character> getEnnemiesInSight(BuildingTower caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : characters) {
			if (o.getTeam() != caller.potentialTeam && Utils.distance(o, caller) < caller.sight) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Objet> getAlliesInSight(Character caller) {
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for (Character o : characters) {
			if (o != caller && o.getTeam() == caller.getTeam() && o.collisionBox.intersects(caller.sightBox)) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Character> getWoundedAlliesInSight(Character caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : characters) {
			if (o != caller && o.getTeam() == caller.getTeam() && o.lifePoints < o.maxLifePoints
					&& o.collisionBox.intersects(caller.sightBox)) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Objet findTarget(float x, float y,int player) {
		Point point = new Point(x, y);
		Objet target = null;
		// looking for the object on the target
		for (Character i : this.characters) {
			// looking amongst other characters
			if (i.selectionBox.contains(point) && i.getTeam()!=Game.g.getPlayerById(player).getTeam()) {
				target = i;
				break;
			}
		}
		if (target == null) {
			for (Character i : this.characters) {
				// looking amongst other characters
				if (i.selectionBox.contains(point) && i.getTeam()==Game.g.getPlayerById(player).getTeam()) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (Building i : this.buildings) {
				// looking amongst natural object
				if (i.collisionBox.contains(point)) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (Bonus i : this.bonus) {
				// looking amongst natural object
				if (i.collisionBox.contains(point)) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (NaturalObjet i : naturalObjets) {
				// looking amongst natural object
				if (Math.sqrt((i.x-x)*(i.x-x)+(i.y-y)*(i.y-y))<4*i.collisionBox.getBoundingCircleRadius()) {
					target = i;
					break;
				}
			}
		}
		return target;
	}




	public void update(Vector<InputObject> ims) {
		Utils.triId(this.characters);

		for (Player p : Game.g.players) {
			Utils.triIdActionObjet(p.selection);
		}
		collisionSwitch = !collisionSwitch;
		// 1 - Handling inputs
		for (InputObject im : ims) {
			int player = im.idplayer;

			//handle victory
			if(im.isPressed(KeyEnum.Abandon)){
				Game.g.endGame = true;
				if(player!=Game.g.currentPlayer.id){
					Game.g.victory = true;
				}
				else{
					Game.g.victory = false;
				}
				return;
			}

			this.handleInterface(im);
			// Handling action bar
			this.handleActionBar(im, player);
			// Handling the right click
			this.handleRightClick(im, player);

			this.handleMinimap(im, player);
			


		}
		Game.g.inputsHandler.updateSelection(ims);
		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin input : " + (System.currentTimeMillis() - Game.g.timeSteps));

	}





	void handleMouseHover(InputObject im) {
		for (Character c : this.characters) {
			if (c.selectionBox.contains(im.x, im.y)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}
		for (Bonus c : this.bonus) {
			if (c.selectionBox.contains(im.x, im.y)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}
		for (Building c : this.buildings) {
			if (c.selectionBox.contains(im.x, im.y)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}

	}

	public void updateIAOrders() {

		// Pour toute les IA :
		for (Player p : Game.g.players) {
			if (p instanceof IAPlayer) {
				updateIAOrders((IAPlayer) p);
			}
		}
	}

	public void updateIAOrders(IAPlayer p) {
		// TODO : Update IA orders for a specific player
		p.commonUpdate();
		p.update();
	}

	public void updatePlateauState() {
		// 2 - For everyone
		// Sort by id

		this.collision();
		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin collision : " + (System.currentTimeMillis() - Game.g.timeSteps));
		this.clean();
		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin clean : " + (System.currentTimeMillis() - Game.g.timeSteps));
		this.action();
		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin action : " + (System.currentTimeMillis() - Game.g.timeSteps));

		// 3 - handling visibility
		this.updateVisibility();

		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin visibility : " + (System.currentTimeMillis() - Game.g.timeSteps));

		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin message : " + (System.currentTimeMillis() - Game.g.timeSteps));
	}



	private void handleRightClick(InputObject im, int player) {
		Selection selection = Game.g.inputsHandler.getSelection(player);
		if (im.isPressed(KeyEnum.RightClick)) {
			// RALLY POINT
			
			if (selection.selection.size() > 0
					&& selection.selection.get(0) instanceof BuildingProduction) {
				Objet target = findTarget(im.x, im.y,player);
				if(target instanceof Building || target instanceof Character){
					((BuildingProduction) selection.selection.get(0)).rallyPoint = target;
				}
				if(target==null){
					((BuildingProduction) selection.selection.get(0)).rallyPoint = new Checkpoint(this,im.x,
							im.y);
				}
			} else if (im.isPressed(KeyEnum.AjouterSelection)) {

				updateSecondaryTarget(im.x, im.y, player);
			} else {
				updateTarget(im.x, im.y, player, Character.MOVE);
			}
		}
		if (im.isPressed(KeyEnum.StopperMouvement)) {
			// STOP SELECTION
			for (Objet c : selection.selection) {
				if (c instanceof Character) {
					((Character) c).stop();
					((Character) c).mode = Character.NORMAL;
				}
			}
		}
		if (im.isPressed(KeyEnum.DeplacementOffensif)) {
			updateTarget(im.x, im.y, player, Character.AGGRESSIVE);
		}
		if (im.isPressed(KeyEnum.TenirPosition)) {
			// Hold position
			updateTarget(im.x, im.y, player, Character.HOLD_POSITION);
		}
		if (im.isPressed(KeyEnum.GlobalRallyPoint)) {
			//Update rally point
			for(Building b : buildings){
				if(b.getTeam()==Game.g.players.get(player).getTeam() && b instanceof BuildingProduction){
					Checkpoint c = new Checkpoint(this,im.x,im.y,true,Colors.team1);
					((BuildingProduction) b).rallyPoint = new Checkpoint(this,im.x,im.y);
				}
			}
		}
	}

	private void handleActionBar(InputObject im, int player) {
		Selection selection = Game.g.inputsHandler.getSelection(player);
		boolean imo = false;
		if (im.isPressed(KeyEnum.Immolation) || im.isPressed(KeyEnum.Prod0) || im.isPressed(KeyEnum.Prod1) || im.isPressed(KeyEnum.Prod2) || im.isPressed(KeyEnum.Prod3) || im.isPressed(KeyEnum.Escape)) {
			if (selection.selection.size() > 0 && selection.selection.get(0) instanceof BuildingAction) {
				if (im.isPressed(KeyEnum.Prod0))
					((BuildingAction) selection.selection.get(0)).product(0);
				if (im.isPressed(KeyEnum.Prod1))
					((BuildingAction) selection.selection.get(0)).product(1);
				if (im.isPressed(KeyEnum.Prod2))
					((BuildingAction) selection.selection.get(0)).product(2);
				if (im.isPressed(KeyEnum.Prod3))
					((BuildingAction) selection.selection.get(0)).product(3);
				if (im.isPressed(KeyEnum.Escape))
					((BuildingAction) selection.selection.get(0)).removeProd();
			} else
				if (selection.selection.size() > 0 && selection.selection.get(0) instanceof Character) {
					int number = -1;
					if (im.isPressed(KeyEnum.Prod0))
						number = 0;
					if (im.isPressed(KeyEnum.Prod1))
						number = 1;
					if (im.isPressed(KeyEnum.Prod2))
						number = 2;
					if (im.isPressed(KeyEnum.Prod3))
						number = 3;
					if (im.isPressed(KeyEnum.Immolation)){
						number = 0;
						imo = true;
					}

					Character c = ((Character) selection.selection.get(0));
					if (-1 != number && number < c.spells.size()
							&& c.spellsState.get(number) >= c.spells.get(number).chargeTime) {
						if (!c.spells.get(number).needToClick) {
							Spell s = c.spells.get(number);
							if(s.name.equals("Immolation")){
								if(imo){
									s.launch(new Checkpoint(this,im.x,im.y), c);
								}
							}else{
								s.launch(new Checkpoint(this,im.x,im.y), c);
								c.spellsState.set(number, 0f);
							}
							// switching selection
							int compteur = 0;
							while(selection.selection.size()>compteur && selection.selection.get(compteur).getClass()==c.getClass()){
								compteur++;
							}
							selection.selection.insertElementAt(c, compteur);
							selection.selection.remove(0);
						}
					}
				}
		}

	}


	// METHODS ONLY CALLED BY THE CURRENT PLAYER

	// updating rectangle
	public void handleView(InputObject im, int player) {
		// Handle the display (camera movement & minimap)

		// camera movement
		if (player == Game.g.currentPlayer.id && Game.g.inputsHandler.getSelection(player).rectangleSelection == null && (!im.isDown(KeyEnum.LeftClick) || im.isOnMiniMap)) {
			// Handling sliding
			if(this.slidingCam==true){
				int deltaX = (int) (this.objectiveCam.getX()-this.Xcam);
				int deltaY = (int) (this.objectiveCam.getY()-this.Ycam);
				this.Xcam += deltaX/5;
				this.Ycam += deltaY/5;
				if(Math.abs(deltaX)<2)
					this.slidingCam = false;
			}
			//			boolean isOnMiniMap = im.xMouse>(1-im.player.bottomBar.ratioMinimapX)*g.resX && im.yMouse>(g.resY-im.player.bottomBar.ratioMinimapX*g.resX);
			// Move camera according to inputs :
			if ((im.isDown(KeyEnum.Up)  || !im.isOnMiniMap && im.y < Ycam + 5) && Ycam > -Game.g.resY / 2) {
				Ycam -= (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Down) || (!im.isOnMiniMap && im.y > Ycam + Game.g.resY - 5))
					&& Ycam < this.maxY - Game.g.resY / 2) {
				Ycam += (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Left) ||(!im.isOnMiniMap && im.x < Xcam + 5)) && Xcam > -Game.g.resX / 2) {
				Xcam -= (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Right) ||(!im.isOnMiniMap && im.x > Xcam + Game.g.resX - 5))
					&& Xcam < this.maxX - Game.g.resX / 2) {
				Xcam += (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			
			// TODO : Centrer la selection sur un groupe d'unité
//			for (int to = 0; to < 10; to++) {
//				if (im.isPressed(KeyEnum.valueOf("Key"+to)) && Game.g.players.get(player).groupSelection == to
//						&& this.selection.get(player).size() > 0) {
//					float xmoy = this.selection.get(player).get(0).getX();
//					float ymoy = this.selection.get(player).get(0).getY();
//					this.Xcam = (int) Math.min(maxX - g.resX / 2f, Math.max(-g.resX / 2f, xmoy - g.resX / 2f));
//					this.Ycam = (int) Math.min(maxY - g.resY / 2f, Math.max(-g.resY / 2f, ymoy - g.resY / 2f));
//				}
//			}
		}


	}

	public void handleInterface(InputObject im){
		// display for the action bar
		BottomBar bb = Game.g.currentPlayer.bottomBar;
		float relativeXMouse = (im.x - Xcam);
		float relativeYMouse = (im.y - Ycam);
		if (relativeXMouse > bb.action.x && relativeXMouse < bb.action.x + bb.action.icoSizeX
				&& relativeYMouse > bb.action.y && relativeYMouse < bb.action.y + bb.action.sizeY) {
			int mouseOnItem = (int) ((relativeYMouse - bb.action.y) / (bb.action.sizeY / bb.action.prodIconNb));
			bb.action.toDrawDescription[0] = false;
			bb.action.toDrawDescription[1] = false;
			bb.action.toDrawDescription[2] = false;
			bb.action.toDrawDescription[3] = false;
			if (mouseOnItem >= 0 && mouseOnItem < 4)
				bb.action.toDrawDescription[mouseOnItem] = true;
			System.out.println("youhou " + im.isDown(KeyEnum.LeftClick));
		} else {
			bb.action.toDrawDescription[0] = false;
			bb.action.toDrawDescription[1] = false;
			bb.action.toDrawDescription[2] = false;
			bb.action.toDrawDescription[3] = false;
		}
	}

	private void handleMinimap(InputObject im, int player) {
		if (im.isDown(KeyEnum.LeftClick) && player == Game.g.currentPlayer.id && im.isOnMiniMap) {
			// Put camera where the click happened
			this.objectiveCam = new Point((int) (im.x - Game.g.resX / 2f),(int) (im.y - Game.g.resY / 2f));
			slidingCam = true;
			//			System.out.println(slidingCam);
		}
	}


	// Handling visibility
	public Vector<Objet> getInCamObjets(int team) {
		// return all objects from a team in the camera view
		Vector<Objet> obj = new Vector<Objet>();
		for (Character c : this.characters)
			if (c.getTeam() == team && c.visibleByCamera)
				obj.add(c);
		for (Building c : this.buildings)
			if (c.getTeam() == team && c.visibleByCamera)
				obj.add(c);
		return obj;
	}

	public boolean isVisibleByPlayer(int team, Objet objet) {
		if (objet.getGameTeam() != null && objet.getTeam() == team)
			return true;
		float r = objet.collisionBox.getBoundingCircleRadius();
		for (Character c : this.characters)
			if (c.getTeam() == team && Utils.distance(c, objet) < c.sight + r)
				return true;
		for (Building b : this.buildings)
			if (b.getTeam() == team && Utils.distance(b, objet) < b.sight + r)
				return true;
		return false;
	}

	public boolean isVisibleByCamera(Objet objet) {
		return objet.x + objet.sight > Xcam && objet.x - objet.sight < Xcam + Game.g.resX && objet.y + objet.sight > Ycam
				&& objet.y - objet.sight < Ycam + Game.g.resY;
	}

	private void updateVisibility() {
		for (Character c : this.characters) {
			c.visibleByCurrentPlayer = this.isVisibleByPlayer(Game.g.currentPlayer.getTeam(), c);
			c.visibleByCamera = this.isVisibleByCamera(c);
		}
		for (Building b : this.buildings) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (Bullet b : this.bullets) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (SpellEffect b : this.spells) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (NaturalObjet n : this.naturalObjets) {
			n.visibleByCurrentPlayer = this.isVisibleByPlayer(Game.g.currentPlayer.getTeam(), n);
			n.visibleByCamera = this.isVisibleByCamera(n);
		}
		for (Bonus b : this.bonus) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
	}





	

	// MULTIPLAYING
	public String toStringArray() {
		
		int id_charac = 0;
		String s = "";
		// IDS
		s += Game.g.round;
		s += "!";
		s += Game.g.idChar;
		s += "!";
		// Time to restart the game
		s += Game.g.clock.getCurrentTime() + (long) (0.05 * 1e9);
		s += "!";
		// We want to send the content of plateau

		// CHARACTERS
		while (id_charac < this.characters.size()) {
			s += this.characters.get(id_charac).toString();
			s += "|";
			id_charac++;
		}

		s += "!";
		int id_building = 0;
		while (id_building < this.buildings.size()) {
			s += this.buildings.get(id_building).toString();
			s += "|";
			id_building++;
		}

		s += "!";

		//SELECTION
		for(int i = 0;i<Game.g.players.size();i++){
			Selection selection = Game.g.inputsHandler.getSelection(Game.g.players.get(i).id);
			if(selection.selection.size()==0){
				s+="-1;";
			}
			else{
				for(Objet o: selection.selection){
					s+=o.id+";";
				}
			}
			s+="|";
		}
		s+="!";
		return s;
	}

	public void parse(String s) {

		// APPLY ACTION ON ALL CONCERNED OBJECTS
		// GET ARRAY OF CHARACTERS,BUILDING,BULLET
		// System.out.println(s);
		if (s != null && s != "") {
			String[] u = s.split("!");
			// Take care of id sent
			parseCharacter(u[3]);
			parseBuilding(u[4]);
			parseSelection(u[5]);

			//PARSE SELECTION
			Game.g.idChar = Integer.parseInt(u[1]);

		}

		// Update groups

		Vector<Character> group = new Vector<Character>();
		for (Character c : this.characters) {
			c.group.clear();
			if (c.target instanceof Checkpoint) {
				group = new Vector<Character>();
				group.add(c);
				for (Character d : this.characters) {
					if (d.target instanceof Checkpoint && c.id != d.id && c.getTeam() == d.getTeam()) {
						if (d.target.x == c.target.x && d.target.y == c.target.y) {
							group.addElement(d);
						}
					}
				}
				c.group.addAll(group);
			}
		}
	}

	public void parseBuilding(String s) {
		String[] u = s.split("\\|");
		// Loop over each Building
		Building bul = null;
		int finish = u.length;
		// For all buildings in received message
		//		System.out.println(s);
		for (int i = 0; i < finish; i++) {
			HashMap<String, String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			// Find corresponding Building in plateau
			bul = this.getBuildingById(idTest);
			//Parse this building
			bul.parse(hs);
		}
	}

	public void parseSelection(String s){
		//		System.out.println(s);
		String[] u = s.split("\\|");
		//Loop over each player
		for(int i = 0; i<Game.g.players.size();i++){
			Selection selection = Game.g.inputsHandler.getSelection(Game.g.players.get(i).id);
			selection.selection.clear();
			if(u[i].equals("")){
				continue;	
			}
			String[] ids = u[i].split(";");
			for(int j=0; j<ids.length;j++){
				int id = Integer.parseInt(ids[j]);
				if(id!=-1){
					selection.selection.add(getById(id));
				}
			}
		}
	}
	public ActionObjet getById(int id){
		ActionObjet o = getCharacterById(id);
		if(o!=null){
			return o;
		}
		else{
			return getBuildingById(id);
		}
	}
	public Character getCharacterById(int id) {
		for (Character cha : this.characters) {
			if (id == cha.id) {
				return cha;
			}
		}
		// for(Character cha : this.population.characters){
		// if(id==cha.id){
		// return cha;
		// }
		// }
		return null;
	}

	public Character getCharacterByIdAndName(int id, String name) {
		for (Character cha : this.characters) {
			if (id == cha.id && name.equals(cha.name)) {
				return cha;
			}
		}
		// for(Character cha : this.population.characters){
		// if(id==cha.id){
		// return cha;
		// }
		// }
		return null;
	}

	public Bullet getBulletById(int id) {

		for (Bullet cha : this.bullets) {
			if (id == cha.id) {
				return cha;
			}
		}
		return null;
	}

	public Building getBuildingById(int id) {
		for (Building cha : this.buildings) {
			if (id == cha.id) {
				return cha;
			}
		}
		return null;
	}

	private SpellEffect getSpellEffectById(int id) {
		for (SpellEffect cha : this.spells) {
			if (id == cha.id) {
				return cha;
			}
		}
		return null;
	}

	public void parseCharacter(String s) {
		// SPLIT SELON |
		//		System.out.println("characters : "+s);
		for (Character c : this.characters) {
			c.setTarget(null, null);
			c.group.clear();
			c.checkpointTarget = null;
			c.secondaryTargets.clear();
			c.leader = null;
			c.moveAhead = false;
			c.waypoints.clear();
		}
		String[] u = s.split("\\|");
		// LOOP OVER EACH CHARACTER
		Character cha = null;
		int finish = u.length;

		// //Clear all characters
		// while(this.characters.size()>0){
		// Character toErase = this.characters.get(0);
		// toErase.lifePoints = -1f;
		// toErase.destroy();
		// this.removeCharacter(toErase);
		// this.characters.remove(toErase);
		// }

		// this.characters.clear();
		Utils.triId(this.characters);
		for (int i = 0; i < finish; i++) {
			// FIND CONCERNED CHARACTER
			HashMap<String, String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			cha = this.getCharacterByIdAndName(idTest, hs.get("name"));
			if (cha == null) {
				cha = Character.createNewCharacter(hs);
				//				System.out.println("Create new character");

			}
			if (cha != null) {
				cha.parse(hs);
				cha.toKeep = true;
			}
		}

		// Erase characters who didn't give any news
		Utils.triId(this.characters);
		for (Character c : this.characters) {
			if (!c.toKeep) {
				//				System.out.println("Destroyed " + c.id);
				c.destroy();
			}
		}
		// Clean plateau
		this.clean();

	}

	public void parseBullet(String s) {
		String[] u = s.split("\\|");
		// Loop over each bullet
		Bullet bul = null;
		int finish = u.length;
		if (!u[u.length - 1].contains("id")) {
			finish--;
		}
		// For all bullets in received message
		for (int i = 0; i < finish; i++) {
			HashMap<String, String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			// Find corresponding bullet in plateau
			bul = this.getBulletById(idTest);
			// Create bullet if not in plateau
			if (bul == null) {
				bul = Bullet.createNewBullet(hs);
			}
			bul.parse(hs);
			bul.toKeep = true;
		}
		// Destroy bullets who didn't give any news
		for (Bullet b : this.bullets) {
			if (!b.toKeep) {
				b.setLifePoints(-1f);
			} else {
				b.toKeep = false;
			}
		}
	}

	@Deprecated
	public void parseSpell(String s) {
		String[] u = s.split("\\|");
		// Loop over each spells
		SpellEffect bul = null;
		int finish = u.length;
		if (!u[u.length - 1].contains("id")) {
			finish--;
		}
		// For all spellEffects in received message
		for (int i = 0; i < finish; i++) {
			HashMap<String, String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			// Find corresponding spellEffect in plateau
			bul = this.getSpellEffectById(idTest);
			// Create spellEffect if not in plateau
			if (bul == null) {
				bul = SpellEffect.createNewSpell(hs);
			}
			bul.parse(hs);
			bul.toKeep = true;
		}
		// Destroy spellEffects who didn't give any news
		for (SpellEffect b : this.spells) {
			if (!b.toKeep) {
				b.setLifePoints(-1f);
			} else {
				b.toKeep = false;
			}
		}
	}

}
