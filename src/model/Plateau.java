package model;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import buildings.Bonus;
import buildings.Building;
import buildings.BuildingAcademy;
import buildings.BuildingAction;
import buildings.BuildingBarrack;
import buildings.BuildingHeadQuarters;
import buildings.BuildingProduction;
import buildings.BuildingStable;
import buildings.BuildingTower;
import bullets.Bullet;
import bullets.CollisionBullet;
import display.BottomBar;
import main.Main;
import multiplaying.InputObject;
import pathfinding.Case;
import pathfinding.MapGrid;
import spells.Spell;
import spells.SpellEffect;
import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;

public class Plateau {

	public Game g;
	public float maxX;
	public float maxY;

	// Camera
	public int Xcam;
	public int Ycam;
	// preset cameras
	public Point Zcam;
	public Point Scam;
	public Point Qcam;
	public Point Dcam;
	public boolean slidingCam = false;
	public Point objectiveCam = new Point(0,0);

	// fog of war
	public static Image fog;
	public static Graphics gf;

	// about the output of the string
	public String currentString;

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
	public Vector<Bonus> bonus;

	public Vector<NaturalObjet> naturalObjets;
	public Vector<NaturalObjet> toAddNaturalObjets;
	public Vector<NaturalObjet> toRemoveNaturalObjets;

	public Vector<Vector<ActionObjet>> selection;
	public Vector<Vector<ActionObjet>> toAddSelection;
	public Vector<Vector<ActionObjet>> toRemoveSelection;

	public Vector<SpellEffect> spells;
	public Vector<SpellEffect> toAddSpells;
	public Vector<SpellEffect> toRemoveSpells;

	public Vector<Rectangle> rectangleSelection;
	public Vector<Float> recX;
	public Vector<Float> recY;
	public Vector<Vector<ActionObjet>> inRectangle;


	public MapGrid mapGrid;

	//Cosmetic
	public Cosmetic cosmetic;

	public Plateau(float maxX, float maxY, Game g) {

		this.g = g;
		this.mapGrid = new MapGrid(0f, maxX, 0f, maxY);
		// GENERAL
		this.maxX = maxX;
		this.maxY = maxY;

		initializePlateau(g);

		

	}

	public void initializePlateau(Game g) {
		//COSMETIC
		this.cosmetic = new Cosmetic();
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
		// SELECTION
		this.selection = new Vector<Vector<ActionObjet>>();
		this.toAddSelection = new Vector<Vector<ActionObjet>>();
		this.toRemoveSelection = new Vector<Vector<ActionObjet>>();
		this.rectangleSelection = new Vector<Rectangle>();
		this.recX = new Vector<Float>();
		this.recY = new Vector<Float>();
		this.inRectangle = new Vector<Vector<ActionObjet>>();

		for(int i =0; i<g.nPlayers;i++){
			this.selection.addElement(new Vector<ActionObjet>());
			this.toAddSelection.addElement(new Vector<ActionObjet>());
			this.toRemoveSelection.addElement(new Vector<ActionObjet>());
			this.rectangleSelection.addElement(null);
			this.inRectangle.addElement(new Vector<ActionObjet>());
			this.recX.addElement(0f);
			this.recY.addElement(0f);
		}

		this.g.idChar = 0;
		this.g.idBullet = 0;
		for(GameTeam t : g.teams){
			t.pop = 0;
		}
	}

	public void setMaxXMaxY(float MaxX, float MaxY) {
		this.maxX = MaxX;
		this.maxY = MaxY;
		this.mapGrid = new MapGrid(0f, maxX, 0f, maxY);
		for (Player p : g.players) {
			p.bottomBar.minimap.update(g);
		}
	}

	public void addCharacterObjets(Character o) {
		toAddCharacters.addElement(o);
	}

	public void removeCharacter(Character o) {
		toRemoveCharacters.addElement(o);
		if (this.selection.contains(o)) {
			this.selection.remove(o);
		}
		for (Player p : this.g.players) {
			if (p.selection.contains(o)) {
				p.selection.remove(o);
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
		this.mapGrid.insertNewRec(o.x, o.y, o.sizeX, o.sizeY);
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

	public void addSelection(ActionObjet o, int team) {
		toAddSelection.get(team).addElement(o);
	}

	public void removeSelection(ActionObjet o, int team) {
		toRemoveSelection.get(team).addElement(o);
	}

	// General methods

	public void clean() {
		// Clean the buffers and handle die
		// Remove and add considering alive
		for (Character o : characters) {
			if (!o.isAlive()) {
				this.removeCharacter(o);
				o.getGameTeam().pop--;
				if (o.soundDeath != null && o.soundDeath.size() > 0) {
					Utils.getRandomSound(o.soundDeath).play(1f, this.g.options.soundVolume);
				}
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
		// Update selection and groups
		Vector<ActionObjet> toDelete = new Vector<ActionObjet>();
		for (int i = 0; i < g.nPlayers; i++) {
			for (ActionObjet c : selection.get(i)) {
				if (!c.isAlive()) {
					this.removeSelection(c, i);
				}
			}
			for (ActionObjet o : toRemoveSelection.get(i)) {
				selection.get(i).remove(o);
			}
			for (ActionObjet o : toAddSelection.get(i)) {
				selection.get(i).addElement(o);
			}
			if (toAddSelection.get(i).size() > 0) {
				Utils.triName(selection.get(i));
			}
			for (int k = 0; k < 10; k++) {
				toDelete.clear();
				for (ActionObjet c : this.g.players.get(i).groups.get(k)) {
					if (!c.isAlive())
						toDelete.add(c);
				}
				for (ActionObjet c : toDelete) {
					this.g.players.get(i).groups.get(k).remove(c);
				}
			}
		}
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

		// Clear the vector :
		for (int i = 0; i < g.nPlayers; i++) {
			toAddSelection.get(i).clear();
			toRemoveSelection.get(i).clear();
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
	}

	//Handling cosmetic for current player in lan game
	public void updateCosmetic(InputObject im){
		//SELECTION RECTANGLE
		if (im.leftClick) {


			if (im.isOnMiniMap && cosmetic.selection==null) {
				return;
			}
			if (this.cosmetic.selection == null || im.isPressedCTRL) {
				cosmetic.recX= (float) im.xMouse;
				cosmetic.recY= (float) im.yMouse;
				cosmetic.selection=  new Rectangle(cosmetic.recX, cosmetic.recX, 0.1f, 0.1f);
			}
			cosmetic.selection.setBounds((float) Math.min(cosmetic.recX, im.xMouse),
					(float) Math.min(cosmetic.recY, im.yMouse), (float) Math.abs(im.xMouse - cosmetic.recX) + 0.1f,
					(float) Math.abs(im.yMouse - cosmetic.recY) + 0.1f);
		}else{
			cosmetic.selection = null;
		}
		//Selection character

		//RIGHT CLICK HERE
		//TODO
	}
	private void updateRectangle(InputObject im, int player) {
		if(im.isOnMiniMap && this.rectangleSelection.get(player)==null)
			return;
		if (this.rectangleSelection.get(player) == null || im.isPressedCTRL) {
			recX.set(player, (float) im.xMouse);
			recY.set(player, (float) im.yMouse);
			rectangleSelection.set(player, new Rectangle(recX.get(player), recX.get(player), 0.1f, 0.1f));
		}
		rectangleSelection.get(player).setBounds((float) Math.min(recX.get(player), im.xMouse),
				(float) Math.min(recY.get(player), im.yMouse), (float) Math.abs(im.xMouse - recX.get(player)) + 0.1f,
				(float) Math.abs(im.yMouse - recY.get(player)) + 0.1f);
	}
	// handling the input
	public void updateTarget(float x, float y, int team, int mode) {
		// called when right click on the mouse
		Objet target = this.findTarget(x, y);
		if (target == null) {
			target = new Checkpoint(this, x, y);
		}
		int i = 0;
		for (ActionObjet c : this.selection.get(team)) {
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
				if (i == 0 && c.soundSetTarget != null && c.soundSetTarget.size() > 0 && Math.random() > 0.3) {
					if (c.getTeam() == this.g.currentPlayer.id && target instanceof Character
							&& c.getTeam() != target.getTeam()) {
						Utils.getRandomSound(c.soundAttack).play(1f, this.g.options.soundVolume);
					} else if (c.getTeam() == this.g.currentPlayer.id) {
						Utils.getRandomSound(c.soundSetTarget).play(1f, this.g.options.soundVolume);
					}
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
				for (ActionObjet c1 : this.selection.get(team)) {
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
		Objet target = this.findTarget(x, y);
		if (target == null) {
			target = new Checkpoint(this, x, y);
		}
		for (ActionObjet c : this.selection.get(team)) {
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
				for (ActionObjet c1 : this.selection.get(team))
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

	public Objet findTarget(float x, float y) {
		Point point = new Point(x, y);
		Objet target = null;
		// looking for the object on the target
		for (Character i : this.characters) {
			// looking amongst other characters
			if (i.selectionBox.contains(point)) {
				target = i;
				break;
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

		for (Player p : g.players) {
			Utils.triIdActionObjet(p.selection);
		}
		collisionSwitch = !collisionSwitch;
		// 1 - Handling inputs
		for (InputObject im : ims) {
			// pour tous les inputs pass�s en argument on fait le traitement
			int player = im.player.id;
			// si on est client on ne g�re que son input

			//handle victory
			if(im.isPressedPause){
				this.g.endGame = true;
				if(player!=this.g.currentPlayer.id){
					this.g.victory = true;
				}
				else{
					this.g.victory = false;
				}
				return;
			}

			// on g�re la s�lection des sorts (type firewall/ blessed area)
			//this.handleSpellCasting(im, player);
			// on g�re c�t� serveur l'action bar et le click droit
			if (im.player == this.g.currentPlayer) {
				this.handleMouseHover(im, player);
			}
			// Handling action bar
			this.handleActionBar(im, player);
			// Handling the right click
			this.handleRightClick(im, player);
			// handling only the current player
			// Handle minimap

			this.handleMinimap(im, player);
			this.handleSelection(im, player, g.players.get(player).getTeam());

			// if(player == this.currentPlayer.id){
			// // ong�re le d�placement de la cam�ra et la s�lection
			// if(!this.isCastingSpell.get(player) &&
			// !this.hasCastSpell.get(player)){
			// this.handleView(im, player);
			// }
			// }
			// enfin on g�re le lancement des sorts
			// this.handleSpellsOnField(im, player, !g.inMultiplayer || g.host);

		}
		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin input : " + (System.currentTimeMillis() - g.timeSteps));

	}

	
	
	

	private void handleMouseHover(InputObject im, int player) {
		for (Character c : this.characters) {
			if (c.selectionBox.contains(im.xMouse, im.yMouse)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}
		for (Bonus c : this.bonus) {
			if (c.selectionBox.contains(im.xMouse, im.yMouse)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}
		for (Building c : this.buildings) {
			if (c.selectionBox.contains(im.xMouse, im.yMouse)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}

	}

	public void updateIAOrders() {

		// Pour toute les IA :
		for (Player p : this.g.players) {
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
			System.out.println(" - plateau: fin collision : " + (System.currentTimeMillis() - g.timeSteps));
		this.clean();
		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin clean : " + (System.currentTimeMillis() - g.timeSteps));
		this.action();
		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin action : " + (System.currentTimeMillis() - g.timeSteps));

		// 3 - handling visibility
		this.updateVisibility();

		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin visibility : " + (System.currentTimeMillis() - g.timeSteps));

		if (Game.debugTimeSteps)
			System.out.println(" - plateau: fin message : " + (System.currentTimeMillis() - g.timeSteps));
	}



	private void handleRightClick(InputObject im, int player) {
		if (im.pressedRightClick) {
			// RALLY POINT
			if (this.selection.get(player).size() > 0
					&& this.selection.get(player).get(0) instanceof BuildingProduction) {
				Objet target = findTarget(im.xMouse, im.yMouse);
				if(target instanceof Building || target instanceof Character){
					((BuildingProduction) this.selection.get(player).get(0)).rallyPoint = target;
				}
				if(target==null){
					((BuildingProduction) this.selection.get(player).get(0)).rallyPoint = new Checkpoint(this,im.xMouse,
							im.yMouse);
				}
			} else if (im.isPressedMAJ) {
			
				updateSecondaryTarget(im.xMouse, im.yMouse, player);
			} else {
				updateTarget(im.xMouse, im.yMouse, player, Character.MOVE);
			}
		}
		if (im.isPressedF) {
			// STOP SELECTION
			for (ActionObjet c : this.selection.get(player)) {
				if (c instanceof Character) {
					((Character) c).stop();
					((Character) c).mode = Character.NORMAL;
				}
			}
		}
		if (im.isPressedA) {

			updateTarget(im.xMouse, im.yMouse, player, Character.AGGRESSIVE);
		}
		if (im.isPressedE) {
			// Attack building

			updateTarget(im.xMouse, im.yMouse, player, Character.TAKE_BUILDING);
		}
		if (im.isPressedH) {
			// Hold position
			updateTarget(im.xMouse, im.yMouse, player, Character.HOLD_POSITION);
		}
		if (im.isPressedSuppr) {
			// Sell building
			updateTarget(im.xMouse, im.yMouse, player, Character.DESTROY_BUILDING);
		}
		if (im.isPressedR) {
			
			//Update rally point
			for(Building b : buildings){
				if(b.getTeam()==this.g.players.get(player).getTeam() && b instanceof BuildingProduction){
					Checkpoint c = new Checkpoint(this,im.xMouse,im.yMouse,true,Colors.team1);
					((BuildingProduction) b).rallyPoint = new Checkpoint(this,im.xMouse,im.yMouse);
				}
			}
		}
	}

	private void handleActionBar(InputObject im, int player) {
		boolean imo = false;
		if (im.isPressedImmolation || im.isPressedProd0 || im.isPressedProd1 || im.isPressedProd2 || im.isPressedProd3 || im.isPressedESC) {
			if (this.selection.get(player).size() > 0 && this.selection.get(player).get(0) instanceof BuildingAction) {
				if (im.isPressedProd0)
					((BuildingAction) this.selection.get(player).get(0)).product(0);
				if (im.isPressedProd1)
					((BuildingAction) this.selection.get(player).get(0)).product(1);
				if (im.isPressedProd2)
					((BuildingAction) this.selection.get(player).get(0)).product(2);
				if (im.isPressedProd3)
					((BuildingAction) this.selection.get(player).get(0)).product(3);
				if (im.isPressedESC)
					((BuildingAction) this.selection.get(player).get(0)).removeProd();
			} else
				if (this.selection.get(player).size() > 0 && this.selection.get(player).get(0) instanceof Character) {
					int number = -1;
					if (im.isPressedProd0)
						number = 0;
					if (im.isPressedProd1)
						number = 1;
					if (im.isPressedProd2)
						number = 2;
					if (im.isPressedProd3)
						number = 3;
					if (im.isPressedImmolation){
						number = 0;
						imo = true;
					}

					Character c = ((Character) this.selection.get(player).get(0));
					if (-1 != number && number < c.spells.size()
							&& c.spellsState.get(number) >= c.spells.get(number).chargeTime) {
						if (!c.spells.get(number).needToClick) {
							Spell s = c.spells.get(number);
							if(s.name.equals("Immolation")){
								if(imo){
									s.launch(new Checkpoint(this,im.xMouse,im.yMouse), c);
								}
							}else{
								s.launch(new Checkpoint(this,im.xMouse,im.yMouse), c);
								c.spellsState.set(number, 0f);
							}
							// switching selection
							int compteur = 0;
							while(this.selection.get(player).size()>compteur && this.selection.get(player).get(compteur).getClass()==c.getClass()){
								compteur++;
							}
							this.selection.get(player).insertElementAt(c, compteur);
							this.selection.get(player).remove(0);
						}
					}
				}
		}

	}
//					number = 1;
//				if (im.isPressedProd2)
//					number = 2;
//				if (im.isPressedProd3)
//					number = 3;
//				if (im.isPressedImmolation)
//					number = 4;
//
//				this.handleSpellsOnField(im, number, player, true);
//			}
//		}
//	}

	// METHODS ONLY CALLED BY THE CURRENT PLAYER

	// updating rectangle
	public void handleView(InputObject im, int player) {
		// Handle the display (camera movement & minimap)

		// camera movement
		if (player == this.g.currentPlayer.id && this.rectangleSelection.get(player) == null && (!im.leftClick || im.isOnMiniMap)) {
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
			if ((im.isPressedUP || ((im.isPressedZ && !im.isPressedMAJ) ||!im.isOnMiniMap && im.yMouse < Ycam + 5)) && Ycam > -g.resY / 2) {
				Ycam -= (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			if ((im.isPressedDOWN || (im.isPressedS && !im.isPressedMAJ) || (!im.isOnMiniMap && im.yMouse > Ycam + g.resY - 5))
					&& Ycam < this.maxY - g.resY / 2) {
				Ycam += (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			if ((im.isPressedLEFT || (im.isPressedQ && !im.isPressedMAJ) ||(!im.isOnMiniMap && im.xMouse < Xcam + 5)) && Xcam > -g.resX / 2) {
				Xcam -= (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			if ((im.isPressedRIGHT || (im.isPressedD && !im.isPressedMAJ) ||(!im.isOnMiniMap && im.xMouse > Xcam + g.resX - 5))
					&& Xcam < this.maxX - g.resX / 2) {
				Xcam += (int) (80 * 30 / Main.framerate);
				this.slidingCam = false;
			}
			// Displaying the selected group
			for (int to = 0; to < 10; to++) {
				if (im.isPressedNumPad[to]) {
					if (this.g.players.get(player).groupSelection == to
							&& this.g.players.get(player).groups.get(to).size() > 0) {
						float xmoy = this.g.players.get(player).groups.get(to).get(0).getX();
						float ymoy = this.g.players.get(player).groups.get(to).get(0).getY();
						this.Xcam = (int) Math.min(maxX - g.resX / 2f, Math.max(-g.resX / 2f, xmoy - g.resX / 2f));
						this.Ycam = (int) Math.min(maxY - g.resY / 2f, Math.max(-g.resY / 2f, ymoy - g.resY / 2f));
						this.slidingCam = false;
					}
				}
			}
			// Displaying selected area
			if(im.isPressedZ && im.isPressedMAJ){
				this.slidingCam = true;
				this.objectiveCam = new Point(this.Zcam.getX()-this.g.resX/2,this.Zcam.getY()-this.g.resY/2);
			}
			if(im.isPressedQ && im.isPressedMAJ){
				this.slidingCam = true;
				this.objectiveCam = new Point(this.Qcam.getX()-this.g.resX/2,this.Qcam.getY()-this.g.resY/2);
			}
			if(im.isPressedS && im.isPressedMAJ){
				this.slidingCam = true;
				this.objectiveCam = new Point(this.Scam.getX()-this.g.resX/2,this.Scam.getY()-this.g.resY/2);
			}
			if(im.isPressedD && im.isPressedMAJ){
				this.slidingCam = true;
				this.objectiveCam = new Point(this.Dcam.getX()-this.g.resX/2,this.Dcam.getY()-this.g.resY/2);
			}
			//			for (int to = 0; to < 10; to++) {
			//				if (im.isPressedNumPad[to]) {
			//					if (this.g.players.get(player).groupSelection == to
			//							&& this.g.players.get(player).groups.get(to).size() > 0) {
			//						float xmoy = this.g.players.get(player).groups.get(to).get(0).getX();
			//						float ymoy = this.g.players.get(player).groups.get(to).get(0).getY();
			//						this.Xcam = (int) Math.min(maxX - g.resX / 2f, Math.max(-g.resX / 2f, xmoy - g.resX / 2f));
			//						this.Ycam = (int) Math.min(maxY - g.resY / 2f, Math.max(-g.resY / 2f, ymoy - g.resY / 2f));
			//					}
			//				}
			//			}
			// Selection des batiments
			Vector<Building> visible = new Vector<Building>();
			for(Building b : this.buildings){
				if(b.getTeam()==this.g.players.get(player).getTeam()){
					visible.addElement(b);
				}
			}
			if(im.isPressedF1 || im.isPressedF2 || im.isPressedF3 || im.isPressedF4){
				if(im.isPressedF1){
					//Barrack
					for(Objet o : visible){
						if(o instanceof BuildingBarrack &&(this.selection.get(player).size()==0 || this.selection.get(player).get(0)!=o)){
							Vector<ActionObjet> sel = new Vector<ActionObjet>();
							sel.addElement((ActionObjet)o);
							this.selection.set(player,sel);
							break;
						}else if(o instanceof BuildingBarrack && this.selection.get(player).size()!=0 && this.selection.get(player).get(0)==o){
							this.buildings.remove((BuildingBarrack)o);
							this.buildings.addElement((BuildingBarrack)o);
						}
					}
				}
				else if(im.isPressedF2){
					// Stable
					for(Objet o : visible){
						if(o instanceof BuildingStable &&(this.selection.get(player).size()==0 || this.selection.get(player).get(0)!=o)){
							Vector<ActionObjet> sel = new Vector<ActionObjet>();
							sel.addElement((ActionObjet)o);
							this.selection.set(player,sel);
							break;
						}else if(o instanceof BuildingStable && this.selection.get(player).size()!=0 && this.selection.get(player).get(0)==o){
							this.buildings.remove((BuildingStable)o);
							this.buildings.addElement((BuildingStable)o);
						}
					}
				}
				else if(im.isPressedF3){
					// Academy
					//Barrack
					for(Objet o : visible){
						if(o instanceof BuildingAcademy &&(this.selection.get(player).size()==0 || this.selection.get(player).get(0)!=o)){
							Vector<ActionObjet> sel = new Vector<ActionObjet>();
							sel.addElement((ActionObjet)o);
							this.selection.set(player,sel);
							break;
						}else if(o instanceof BuildingAcademy && this.selection.get(player).size()!=0 && this.selection.get(player).get(0)==o){
							this.buildings.remove((BuildingAcademy)o);
							this.buildings.addElement((BuildingAcademy)o);
						}
					}
				}
				else if(im.isPressedF4){
					//Headquarter
					//Barrack
					for(Objet o : visible){
						if(o instanceof BuildingHeadQuarters &&(this.selection.get(player).size()==0 || this.selection.get(player).get(0)!=o)){
							Vector<ActionObjet> sel = new Vector<ActionObjet>();
							sel.addElement((ActionObjet)o);
							this.selection.set(player,sel);
							break;
						}else if(o instanceof BuildingHeadQuarters && this.selection.get(player).size()!=0 && this.selection.get(player).get(0)==o){
							this.buildings.remove((BuildingHeadQuarters)o);
							this.buildings.addElement((BuildingHeadQuarters)o);
						}
					}
				}
			}
			Vector<Character> visible2 = new Vector<Character>();
			for(Character b : this.characters){
				if(b.getTeam()==this.g.players.get(player).getTeam()){
					visible2.addElement(b);
				}
			}
			if(im.isPressedNumPad[0] || im.isPressedNumPad[1] || im.isPressedNumPad[4] || im.isPressedNumPad[2] || im.isPressedNumPad[3]){
				if(im.isPressedNumPad[0]){
					//Lancier
					this.selection.set(player, new Vector<ActionObjet>());
					for(Objet o : visible2){
						if(o instanceof UnitSpearman){
							this.selection.get(player).add((ActionObjet)o);
						}
					}
				}
				else if(im.isPressedNumPad[1]){
					//Lancier
					this.selection.set(player, new Vector<ActionObjet>());
					for(Objet o : visible2){
						if(o instanceof UnitCrossbowman){
							this.selection.get(player).add((ActionObjet)o);
						}
					}
				}
				else if(im.isPressedNumPad[2]){
					//Lancier
					this.selection.set(player, new Vector<ActionObjet>());
					for(Objet o : visible2){

						if(o instanceof UnitKnight){

							this.selection.get(player).add((ActionObjet)o);
						}
					}
				}
				else if(im.isPressedNumPad[3]){
					//Lancier
					this.selection.set(player, new Vector<ActionObjet>());
					for(Objet o : visible2){
						if(o instanceof UnitInquisitor){
							this.selection.get(player).add((ActionObjet)o);
						}
					}
				}
				else if(im.isPressedNumPad[4]){
					//Lancier
					this.selection.set(player, new Vector<ActionObjet>());
					for(Objet o : visible2){
						if(o instanceof Character){
							this.selection.get(player).add((ActionObjet)o);
						}
					}
				}
			}

		}
		// display for the bottom bar
		BottomBar bb = g.currentPlayer.bottomBar;
		float relativeXMouse = (im.xMouse - Xcam);
		float relativeYMouse = (im.yMouse - Ycam);
		if (relativeXMouse > bb.action.x && relativeXMouse < bb.action.x + bb.action.icoSizeX
				&& relativeYMouse > bb.action.y && relativeYMouse < bb.action.y + bb.action.sizeY) {
			int mouseOnItem = (int) ((relativeYMouse - bb.action.y) / (bb.action.sizeY / bb.action.prodIconNb));
			bb.action.toDrawDescription[0] = false;
			bb.action.toDrawDescription[1] = false;
			bb.action.toDrawDescription[2] = false;
			bb.action.toDrawDescription[3] = false;
			if (mouseOnItem >= 0 && mouseOnItem < 4)
				bb.action.toDrawDescription[mouseOnItem] = true;
		} else {
			bb.action.toDrawDescription[0] = false;
			bb.action.toDrawDescription[1] = false;
			bb.action.toDrawDescription[2] = false;
			bb.action.toDrawDescription[3] = false;
		}
	}

	private void handleMinimap(InputObject im, int player) {
		if (im.leftClick && player == this.g.currentPlayer.id && im.isOnMiniMap) {
			// Put camera where the click happened
			this.objectiveCam = new Point((int) (im.xMouse - g.resX / 2f),(int) (im.yMouse - g.resY / 2f));
			slidingCam = true;
//			System.out.println(slidingCam);
		}
	}

	// drawing fog of war method
	public void drawFogOfWar(Graphics g) {
		Vector<Objet> visibleObjet = new Vector<Objet>();
		visibleObjet = this.getInCamObjets(this.g.currentPlayer.getTeam());
		float resX = this.g.resX;
		float resY = this.g.resY;
		gf.setColor(new Color(255, 255, 255));
		gf.fillRect(-this.maxX, -this.maxY, this.maxX + resX, this.maxY + resX);
		gf.setColor(new Color(50, 50, 50));
		float xmin = Math.max(-this.maxX, -this.maxX - Xcam);
		float ymin = Math.max(-this.maxY, -this.maxY - Ycam);
		float xmax = Math.min(resX + this.maxX, 2 * maxX - Xcam);
		float ymax = Math.min(resY + this.maxY, 2 * maxY - Ycam);
		gf.fillRect(xmin, ymin, xmax - xmin, ymax - ymin);
		gf.setColor(Color.white);
		for (Objet o : visibleObjet) {
			gf.fillOval(o.x - Xcam - o.sight, o.y - Ycam - o.sight, o.sight * 2f, o.sight * 2f);
		}
		gf.flush();
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.drawImage(fog, Xcam, Ycam);
		g.setDrawMode(Graphics.MODE_NORMAL);
	}

	// Handling visibility
	private Vector<Objet> getInCamObjets(int team) {
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
		return objet.x + objet.sight > Xcam && objet.x - objet.sight < Xcam + g.resX && objet.y + objet.sight > Ycam
				&& objet.y - objet.sight < Ycam + g.resY;
	}

	private void updateVisibility() {
		for (Character c : this.characters) {
			c.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer.getTeam(), c);
			c.visibleByCamera = this.isVisibleByCamera(c);
		}
		for (Building b : this.buildings) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (Bullet b : this.bullets) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (SpellEffect b : this.spells) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (NaturalObjet n : this.naturalObjets) {
			n.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer.getTeam(), n);
			n.visibleByCamera = this.isVisibleByCamera(n);
		}
		for (Bonus b : this.bonus) {
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
	}


	// handling selection
	public void handleSelection(InputObject im, int player, int team) {
		// Handling groups of units
		//		for (int to = 0; to < 10; to++) {
		//			if (im.isPressedNumPad[to]) {
		//				if (im.isPressedCTRL) {
		//					// Creating a new group made of the selection
		//					this.g.players.get(player).groups.get(to).clear();
		//					for (ActionObjet c : this.selection.get(player))
		//						this.g.players.get(player).groups.get(to).add(c);
		//				} else if (im.isPressedMAJ) {
		//					// Adding the current selection to the group
		//
		//					for (ActionObjet c : this.selection.get(player))
		//						this.g.players.get(player).groups.get(to).add(c);
		//				} else {
		//					this.selection.get(player).clear();
		//					int i = 0;
		//					for (ActionObjet c : this.g.players.get(player).groups.get(to)){
		//						this.selection.get(player).add(c);
		//						if(c.soundSelection!=null && c.soundSelection.size()>0 && i==0){
		//							Utils.getRandomSound(c.soundSelection).play(1f, this.g.options.soundVolume);
		//						}
		//						i++;
		//					}
		//				}
		//				this.g.players.get(player).groupSelection = to;
		//			}
		//		}
		// Cleaning the rectangle and buffer if mouse is released
//		boolean isOnMiniMap = im.xMouse>(1-im.player.bottomBar.ratioMinimapX)*g.resX && im.yMouse>(g.resY-im.player.bottomBar.ratioMinimapX*g.resX);
		
		if (!im.leftClick) {
			if (this.rectangleSelection.get(player) != null) {
				// Play selection sound
				if (player == this.g.currentPlayer.id && this.selection.get(player).size() > 0
						&& this.selection.get(player).get(0) instanceof Character) {
					Character c = (Character) this.selection.get(player).get(0);
					if (c.soundSelection != null && c.soundSelection.size() > 0 && Math.random() > 0.3) {
						Utils.getRandomSound(c.soundSelection).play(1f, this.g.options.soundVolume);
					}

				}
				if (player == this.g.currentPlayer.id && this.selection.get(player).size() > 0
						&& this.selection.get(player).get(0) instanceof Building) {
					Building c = (Building) this.selection.get(player).get(0);
					if (c.soundSelection != null && c.soundSelection.size() > 0) {
						Utils.getRandomSound(c.soundSelection).play(1f, this.g.options.soundVolume);
					}

				}
			}
			this.rectangleSelection.set(player, null);
			this.inRectangle.get(player).clear();
		}
		// Handling hotkeys for gestion of selection
		if (im.isPressedTAB) {
			if (this.selection.get(player).size() > 0) {
				Utils.switchTriName(this.selection.get(player));
				if (this.g.players.get(player).groupSelection != -1)
					Utils.switchTriName(
							this.g.players.get(player).groups.get(this.g.players.get(player).groupSelection));
			}
		}
		// update the rectangle
		if (im.leftClick) {
			// As long as the button is pressed, the selection is updated
			this.updateRectangle(im, player);
		}
		// we update the selection according to the rectangle wherever is the
		// mouse
		if(!im.isOnMiniMap){
			if (im.pressedLeftClick && !im.isPressedMAJ) {
				this.clearSelection(player);
			}
		}
		if (!im.isPressedCTRL) {
			this.updateSelection(rectangleSelection.get(player), player, team);
		} else {
			this.updateSelectionCTRL(rectangleSelection.get(player), player, team);
		}
		// Update the selections of the players
		this.g.players.get(player).selection.clear();
		for (ActionObjet c : this.selection.get(player))
			this.g.players.get(player).selection.addElement(c);

	}


	public void updateSelection(Rectangle select, int player, int team) {
		if (select != null) {
			for (ActionObjet a : this.inRectangle.get(player)) {
				this.selection.get(player).remove(a);
			}
			this.inRectangle.get(player).clear();
			for (Character o : characters) {
				if ((o.selectionBox.intersects(select) || o.selectionBox.contains(select)
						|| select.contains(o.selectionBox)) && o.getTeam() == team) {
					this.selection.get(player).add(o);
					this.inRectangle.get(player).addElement(o);
					if (Math.max(select.getWidth(), select.getHeight()) < 2f) {
						break;
					}

				}
			}
			if (this.selection.get(player).size() == 0) {
				for (Building o : buildings) {
					if (o.selectionBox.intersects(select) && o.getTeam() == team) {
						this.selection.get(player).add(o);
						this.inRectangle.get(player).addElement(o);
					}
				}
			} else {
				Vector<Character> chars = new Vector<Character>();
				for(ActionObjet o : this.selection.get(player)){
					if(o instanceof Character){
						chars.add((Character) o);
					}
				}
					
				Utils.triId(chars);
				this.selection.get(player).clear();
				for(Character c : chars)
					this.selection.get(player).add(c);
			}
			this.g.players.get(player).groupSelection = -1;
		}

	}

	public void updateSelectionCTRL(Rectangle select, int player, int team) {
		if (select != null) {
			this.clearSelection(player);
			// handling the selection
			for (Character o : characters) {
				if ((o.selectionBox.intersects(select) || o.selectionBox.contains(select)) && o.getTeam() == team) {
					// add character to team selection
					this.addSelection(o, player);
				}
			}

			if (this.toAddSelection.get(player).size() == 0) {

				for (Building o : buildings) {
					if (o.selectionBox.intersects(select) && o.getTeam() == team) {
						// add character to team selection
						this.addSelection(o, player);
					}
				}
			}
			Vector<Objet> visibles = this.getInCamObjets(player);
			if (this.toAddSelection.get(player).size() == 1) {
				ActionObjet ao = this.toAddSelection.get(player).get(0);
				if (ao instanceof Character) {
					for (Character o : characters) {
						if (o.getTeam() == team && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							this.addSelection(o, player);
						}
					}
				} else if (ao instanceof Building) {
					for (Building o : buildings) {
						if (o.getTeam() == team && o.name == ao.name && visibles.contains(o)) {
							// add character to team selection
							this.addSelection(o, player);
						}
					}
				}
			}
			this.g.players.get(team).groupSelection = -1;
		}
	}

	public void clearSelection(int player) {
		this.selection.get(player).clear();
	}


	// MULTIPLAYING
	public String toStringArray() {

		int id_charac = 0;
		String s = "";
		// IDS
		s += this.g.round;
		s += "!";
		s += this.g.idChar;
		s += "!";
		// Time to restart the game
		s += this.g.clock.getCurrentTime() + (long) (0.05 * 1e9);
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
		for(int i = 0;i<this.g.players.size();i++){
			if(this.selection.get(i).size()==0){
				s+="-1;";
			}
			else{
				for(ActionObjet o: this.selection.get(i)){
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
			this.g.idChar = Integer.parseInt(u[1]);

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
		for(int i = 0; i<this.g.players.size();i++){
			this.selection.get(i).clear();
			if(u[i].equals("")){
				continue;	
			}
			String[] ids = u[i].split(";");
			for(int j=0; j<ids.length;j++){
				int id = Integer.parseInt(ids[j]);
				if(id!=-1){
					this.selection.get(i).add(getById(id));
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
				cha = Character.createNewCharacter(hs, g);
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
				bul = Bullet.createNewBullet(hs, g);
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
				bul = SpellEffect.createNewSpell(hs, g);
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
