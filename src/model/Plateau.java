package model;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import bonus.Bonus;
import bullets.Bullet;
import bullets.CollisionBullet;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import control.Selection;
import data.Attributs;
import display.BottomBar;
import events.Events;
import javafx.scene.input.InputMethodHighlight;
import madness.Act;
import madness.ActCard;
import madness.ActRule;
import main.Main;
import pathfinding.MapGrid;
import ressources.Map;
import spells.Etats;
import spells.Spell;
import spells.SpellEffect;
import utils.ObjetsList;
import utils.Utils;



public class Plateau implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4212262274818996077L;



	public float maxX;
	public float maxY;

	// si le plateau est une photo, le round auquel synchroniser
	public int roundToSynchro;

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

	public HashMap<Integer,Objet> objets;

	// About Acts
	public Vector<Act> acts;
	public int currentAct = -1;
	private float currentActTime = 0f;


	// Quick accessors 

	//TODO : Objet pool


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

		// All objects
		objets = new HashMap<Integer,Objet>();
		Game.g.id = 0;

		// Acts
		this.acts = new Vector<Act>();



	}

	public void setMaxXMaxY(float MaxX, float MaxY) {
		this.maxX = MaxX;
		this.maxY = MaxY;
		this.mapGrid = new MapGrid(0f, maxX, 0f, maxY);
		Game.g.bottomBar = new BottomBar();
	}

	public void addCharacterObjets(Character o) {
		toAddCharacters.addElement(o);
	}

	private void removeCharacter(Character o) {
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
		this.mapGrid.insertNewRec(o.x, o.y, o.getAttribut(Attributs.sizeX), o.getAttribut(Attributs.sizeY));
		toAddBuildings.addElement(o);
	}

	private void removeBuilding(Building o) {
		toRemoveBuildings.addElement(o);
	}

	public void addSpell(SpellEffect o) {
		toAddSpells.addElement(o);
	}

	private void removeSpell(SpellEffect o) {
		toRemoveSpells.addElement(o);
	}


	// General methods
	public boolean isImmolating(Character c){
		return c.etats.contains(Etats.Immolated);
	}
	public void clean() {
		// Clean the buffers and handle die
		// Remove and add considering alive
		for (Character o : characters) {
			if (!o.isAlive()) {
				if(o.getAttribut(Attributs.autoImmolation)==1f && !isImmolating(o) ){
					o.lifePoints = 10f;
					o.launchSpell(o,ObjetsList.Immolation);
					continue;
				}
				this.removeCharacter(o);

				Game.g.triggerEvent(Events.Death, o);
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
			//			System.out.println("characters : " + characters.size());
			//			System.out.println("bullets : " + bullets.size());
			//			System.out.println("naturalObjects : " + naturalObjets.size());
			//			System.out.println("buildings : " + buildings.size());
			//			System.out.println("spells : " + spells.size());
			//			System.out.println("checkpoints : " + checkpoints.size());
			//			System.out.println("markers building : " + markersBuilding.size());

			// DEBUG SIZE 
			System.out.println("Plateau 285 : Size checkpoints : "+checkpoints.size());
			System.out.println("Plateau 285 : Size characters : "+characters.size());
			System.out.println("Plateau 285 : Size buildings : "+buildings.size());
			System.out.println("Plateau 285 : Size bullets : "+bullets.size());
			System.out.println("Plateau 285 : Size total : "+(checkpoints.size()+bullets.size() + buildings.size()+characters.size()) );

			System.out.println("PLateau 299 : size hashmap : "+this.objets.size());



		}

		Vector<Checkpoint> toremove = new Vector<Checkpoint>();
		for (Checkpoint o : checkpoints) {
			if (!o.isAlive()) {
				toremove.add(o);
				objets.remove(o.id);
			}
		}
		checkpoints.removeAll(toremove);

		// Update selection and groups


		// Remove objets from lists and streams
		for (Character o : toRemoveCharacters) {
			characters.remove(o);
			objets.remove(o.id);
			o.destroy();

		}
		for (Character o : toAddCharacters) {
			objets.put(o.id,o);

			characters.addElement(o);
		}
		for (SpellEffect o : toAddSpells) {
			objets.put(o.id,o);
			spells.addElement(o);
		}
		for (SpellEffect o : toRemoveSpells) {
			objets.remove(o.id);
			spells.remove(o);
		}
		for (Bullet o : toRemoveBullets) {
			objets.remove(o.id);
			bullets.remove(o);
		}
		for (Bullet o : toAddBullets) {
			objets.put(o.id,o);
			bullets.addElement(o);
		}
		for (NaturalObjet o : toRemoveNaturalObjets) {
			naturalObjets.remove(o);
		}
		for (NaturalObjet o : toAddNaturalObjets) {

			naturalObjets.addElement(o);
		}
		for (Building o : toRemoveBuildings) {
			objets.remove(o.id);
			buildings.remove(o);
		}
		for (Building o : toAddBuildings) {
			objets.put(o.id,o);
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
			// Handle collision between Objets and action objects
			if (o.idCase != -1) {
				for (Character i : Game.g.plateau.mapGrid.getCase(o.idCase).surroundingChars) {
					// We suppose o and i have circle collision box
					if (i != o && Utils.distance(i, o) < (i.getAttribut(Attributs.size) + o.getAttribut(Attributs.size))) {
						i.collision(o);
						o.collision(i);
					}
				}
			}
			Circle range = new Circle(o.x, o.y, o.getAttribut(Attributs.range));
			// Between bonus and characters
			for (Bonus b : this.bonus) {
				if (Utils.distance(b, o) < b.hitBoxSize) {
					b.collision(o);
				}
				if (Utils.distance(b, o) < (b.getAttribut(Attributs.size) + range.radius)) {
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
				if (i instanceof CollisionBullet && Utils.distance(i, o) < (i.size + o.getAttribut(Attributs.size))) {
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
						if(Utils.distance(o,c.getCenterX(),c.getCenterY())<(30f+o.getAttribut(Attributs.size))){
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
		for (Objet a : this.spells) {
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
	public void updateTarget(float x, float y, int team, int mode, Vector<Objet> vo) {
		// called when right click on the mouse
		Objet target = this.findTarget(x, y,team);
		if (target == null) {
			target = new Checkpoint(x, y,true);
		}
		int i = 0;

		for (Objet c : vo) {

			if(c instanceof Building && mode==Character.DESTROY_BUILDING){
				((Building) c).giveUpProcess = true;
				continue;
			}
			if (c instanceof Character) {
				Character o = (Character) c;
				o.setTarget(null);
				o.stop();
				o.secondaryTargets.clear();
				o.mode = mode;
				if (o.getGroup() != null && o.getGroup().size() > 1) {
					for (Character c1 : o.getGroup())
						if (c1 != o)
							c1.removeFromGroup(o.id);
				}
				// Then we create its new group
				o.setGroup(new Vector<Character>());


				if (i == 0 && Math.random() > 0.3) {
					if (c.getTeam() == Game.g.currentPlayer.id && target instanceof Character
							&& (c.getTeam() != target.getTeam() || c.getAttribut(Attributs.damage)<0)) {
						Game.g.triggerEvent(Events.MoveAttack, o);
					} else if (c.getTeam() == Game.g.currentPlayer.id) {
						Game.g.triggerEvent(Events.MoveTarget, o);
					}
				}

				i++;


				Vector<Integer> waypoints = null;
				for (Objet c1 : Game.g.inputsHandler.getSelection(team).selection) {

					if (c1 == c)
						continue;
					if (c1 instanceof Character) {
						o.addInGroup(c1.id);
						// System.out.println("Plateau line 507: " +
						// (waypoints!=null) +" "+(c.c==c1.c)+"
						// "+(((Character)c1).waypoints.size()>0));
						if (((Character) c1).waypoints != null && c1.idCase == c.idCase && c1.getTarget() != null
								&& c1.getTarget().idCase == target.idCase) {
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
			target = new Checkpoint(x, y);
		}

		for (Objet c : Game.g.inputsHandler.getSelection(team).selection) {

			if (c instanceof Character) {
				Character o = (Character) c;
				// first we deal with o's elder group
				if (o.getGroup() != null && o.getGroup().size() > 1) {
					for (Character c1 : o.getGroup())
						if (c1 != o)
							c1.removeFromGroup(o.id);
				}
				// Then we create its new group
				o.setGroup(new Vector<Character>());

				for (Objet c1 : Game.g.inputsHandler.getSelection(team).selection)

					if (c1 instanceof Character)
						o.addInGroup(c1.id);
				o.secondaryTargets.add(target.id);
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

	public Vector<Character> getEnnemiesInSight(Building caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : characters) {
			if (o.getTeam() != caller.potentialTeam && Utils.distance(o, caller) < caller.getAttribut(Attributs.sight)) {
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
			if (o != caller && o.getTeam() == caller.getTeam() && o.lifePoints < o.getAttribut(Attributs.maxLifepoints)
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
		// 1 - Handling inputs
		for (InputObject im : ims) {
			int player = im.idplayer;

			//handle victory
			if(im.isPressed(KeyEnum.AbandonnerPartie)){
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
			this.handleActionOnInterface(im, player);

			// Handling the right click
			this.handleRightClick(im, player);

			this.handleMinimap(im, player);



		}
		Game.g.inputsHandler.updateSelection(ims);

		// 2 - Handling acts and objectives
		this.currentActTime-=1f/Main.framerate;
		//		if(this.currentAct<3f){
		//			Game.g.musicPlaying.fade(1, 0f, true);
		//			Game.g.sounds.get("trompette").play(1f, Game.g.options.soundVolume);
		//		}
		if(this.currentActTime<=0 || this.currentAct==this.acts.size()-1){
			// changement d'acte
			if(this.currentAct>=0){
				// on gère le choix des cartes
				for(GameTeam team : Game.g.teams){
					if(team.id==0){
						continue;
					}
					Vector<ActCard> v = new Vector<ActCard>();
					v.addAll(team.civ.getChoices(this.currentAct+1, false, false));
					if(team.civ.objectiveMadness.isCompleted(this.currentAct)){
						v.addAll(team.civ.getChoices(this.currentAct+1, true, false));
					}
					if(team.civ.objectiveWisdom.isCompleted(this.currentAct)){
						v.addAll(team.civ.getChoices(this.currentAct+1, false, true));
					}
					team.currentChoices.add(v);
				}
			}
			this.currentAct += 1;
			this.currentActTime = this.getCurrentAct().getTime();
		}
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

		// 4- handling victory
		for(GameTeam team : Game.g.teams){
			if(team.id==0){
				continue;
			}
			if(team.hq.constructionPoints<=0){
				Game.g.endGame = true;
				if(team.id!=Game.g.currentPlayer.getTeam()){
					Game.g.victory = true;
				} else{
					Game.g.victory = false;
				}
			}
		}
	}



	private void handleRightClick(InputObject im, int player) {
		Vector<Objet> selection = im.getSelection();
		if (im.isPressed(KeyEnum.RightClick)) {
			// RALLY POINT

			if (selection.size() > 0
					&& selection.get(0) instanceof Building) {
				Objet target = findTarget(im.x, im.y,player);
				if(target instanceof Building || target instanceof Character){
					((Building) selection.get(0)).setRallyPoint(target.x, target.y);;
				}
				if(target==null){
					((Building) selection.get(0)).setRallyPoint(im.x, im.y);
				}
			} else if (im.isPressed(KeyEnum.AjouterSelection)) {

				updateSecondaryTarget(im.x, im.y, player);
			} else {
				updateTarget(im.x, im.y, player, Character.MOVE,Game.g.inputsHandler.getSelection(player).selection);
			}
		}
		if (im.isPressed(KeyEnum.StopperMouvement)) {
			// STOP SELECTION

			for (Objet c : selection) {

				if (c instanceof Character) {
					((Character) c).stop();
					((Character) c).mode = Character.NORMAL;
				}
			}
		}
		if (im.isPressed(KeyEnum.DeplacementOffensif)) {
			updateTarget(im.x, im.y, player, Character.AGGRESSIVE,Game.g.inputsHandler.getSelection(player).selection);
		}
		if (im.isPressed(KeyEnum.TenirPosition)) {
			// Hold position
			updateTarget(im.x, im.y, player, Character.HOLD_POSITION,Game.g.inputsHandler.getSelection(player).selection);
		}
		if (im.isPressed(KeyEnum.GlobalRallyPoint)) {
			//Update rally point
			for(Building b : buildings){
				if(b.getTeam()==Game.g.players.get(player).getTeam() && b instanceof Building){
					((Building) b).setRallyPoint(im.x, im.y);
				}
			}
		}
	}

	private void handleActionOnInterface(InputObject im, int player) {
		// Action bar
		Selection selection = Game.g.inputsHandler.getSelection(player);
		boolean imo = false;
		if (im.isPressed(KeyEnum.Immolation) || im.isPressed(KeyEnum.Prod0) || im.isPressed(KeyEnum.Prod1) || im.isPressed(KeyEnum.Prod2) || im.isPressed(KeyEnum.Prod3) ||  im.isPressed(KeyEnum.Tech0) || im.isPressed(KeyEnum.Tech1) || im.isPressed(KeyEnum.Tech2) || im.isPressed(KeyEnum.Tech3) || im.isPressed(KeyEnum.Escape)) {

			if (selection.selection.size() > 0 && selection.selection.get(0) instanceof Building) {

				for(int i=0; i<4; i++){
					if (im.isPressed(KeyEnum.valueOf("Prod"+i))){

						((Building) selection.selection.get(0)).product(i);
					}
					else if(im.isPressed(KeyEnum.valueOf("Tech"+i))){

						((Building) selection.selection.get(0)).productTech(i);
					}
				}
				if (im.isPressed(KeyEnum.Escape))
					((Building) selection.selection.get(0)).removeProd();
			} else
				if (selection.selection.size() > 0 && selection.selection.get(0) instanceof Character) {
					int number = -1;
					for(int i=0; i<4; i++){
						if (im.isPressed(KeyEnum.valueOf("Prod"+i)))
							number = i;
					}
					if (im.isPressed(KeyEnum.Immolation)){

						number = 0;
						imo = true;
					}

					Character c = ((Character) selection.selection.get(0));
					if (-1 != number && number < c.getSpells().size()
							&& c.canLaunch(number)) {
						Spell s = c.getSpell(number);
						if (s.getAttribut(Attributs.needToClick)==0) {
							if(s.name!=ObjetsList.Immolation || imo){
								s.launch(new Checkpoint(im.x,im.y), c);
								c.spellsState.set(number, 0f);

							}
							// switching selection
							int compteur = 0;
							while(selection.selection.size()>compteur && selection.selection.get(compteur).name==c.name){
								compteur++;
							}
							selection.selection.insertElementAt(c, compteur);
							selection.selection.remove(0);
						}
					}
				}
		}
		if(im.spell!=null){
			Spell s = Game.g.getPlayerById(im.idplayer).getGameTeam().data.getSpell(im.spell);
			Character c = ((Character) selection.selection.get(0));
			if(im.idObjetMouse!=-1){
				s.launch(Game.g.plateau.getById(im.idObjetMouse), c);
			} else {
				s.launch(new Checkpoint(im.x,im.y), c);				
			}
			for(int i=0; i<c.getSpells().size(); i++){
				if(c.getSpells().get(i).name==im.spell){
					c.spellsState.set(i, 0f);
				}
			}
			// switching selection
			int compteur = 0;
			while(selection.selection.size()>compteur && selection.selection.get(compteur).name==c.name){
				compteur++;
			}
			selection.selection.insertElementAt(c, compteur);
			selection.selection.remove(0);
		}
		// Top Bar
		if(Game.g.bottomBar.iconChoice!=null && !im.isOnMiniMap){
			for(int i=0; i<Game.g.bottomBar.iconChoice.size(); i++){
				if(im.isPressed(KeyEnum.valueOf("ActCard"+i))){
					chooseActCard(i);
					break;
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
			if(Game.g.slidingCam==true){
				int deltaX = (int) (Game.g.objectiveCam.getX()-Game.g.Xcam);
				int deltaY = (int) (Game.g.objectiveCam.getY()-Game.g.Ycam);
				Game.g.Xcam += deltaX/5;
				Game.g.Ycam += deltaY/5;
				if(Math.abs(deltaX)<2)
					Game.g.slidingCam = false;
			}
			//			boolean isOnMiniMap = im.xMouse>(1-im.player.bottomBar.ratioMinimapX)*g.resX && im.yMouse>(g.resY-im.player.bottomBar.ratioMinimapX*g.resX);
			// Move camera according to inputs :
			if ((im.isDown(KeyEnum.Up)  || !im.isOnMiniMap && im.y < Game.g.Ycam + 5) && Game.g.Ycam > -Game.g.resY / 2) {
				Game.g.Ycam -= (int) (80 * 30 / Main.framerate);
				Game.g.slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Down) || (!im.isOnMiniMap && im.y > Game.g.Ycam + Game.g.resY - 5))
					&& Game.g.Ycam < this.maxY - Game.g.resY / 2) {
				Game.g.Ycam += (int) (80 * 30 / Main.framerate);
				Game.g.slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Left) ||(!im.isOnMiniMap && im.x < Game.g.Xcam + 5)) && Game.g.Xcam > -Game.g.resX / 2) {
				Game.g.Xcam -= (int) (80 * 30 / Main.framerate);
				Game.g.slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Right) ||(!im.isOnMiniMap && im.x > Game.g.Xcam + Game.g.resX - 5))
					&& Game.g.Xcam < this.maxX - Game.g.resX / 2) {
				Game.g.Xcam += (int) (80 * 30 / Main.framerate);
				Game.g.slidingCam = false;
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
		BottomBar bb = Game.g.bottomBar;
		float relativeXMouse = (im.x - Game.g.Xcam);
		float relativeYMouse = (im.y - Game.g.Ycam);
		bb.update(relativeXMouse, relativeYMouse);

	}

	private void handleMinimap(InputObject im, int player) {
		if (im.isDown(KeyEnum.LeftClick) && player == Game.g.currentPlayer.id && im.isOnMiniMap) {
			// Put camera where the click happened
			Game.g.objectiveCam = new Point((int) (im.x - Game.g.resX / 2f),(int) (im.y - Game.g.resY / 2f));
			Game.g.slidingCam = true;
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

	public boolean isVisibleByTeam(int team, Objet objet) {
		if (objet.getGameTeam() != null && objet.getTeam() == team)
			return true;
		float r = 1f;
		if(objet.collisionBox!=null){
			r = objet.collisionBox.getBoundingCircleRadius();
		} 
		for (Character c : this.characters)
			if (c.getTeam() == team && Utils.distance(c, objet) < c.getAttribut(Attributs.sight) + r)
				return true;
		for (Building b : this.buildings)
			if (b.getTeam() == team && Utils.distance(b, objet) < b.getAttribut(Attributs.sight) + r)
				return true;
		return false;
	}

	public boolean isVisibleByCamera(Objet objet) {
		float sight = objet.getVisibleSize();
		return objet.x + sight > Game.g.Xcam && objet.x - sight < Game.g.Xcam + Game.g.resX && objet.y + sight > Game.g.Ycam
				&& objet.y - sight < Game.g.Ycam + Game.g.resY;

	}

	private void updateVisibility() {
		for (Character c : this.characters) {
			c.visibleByCurrentTeam = this.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c);
			c.visibleByCamera = this.isVisibleByCamera(c);
		}
		for (Building b : this.buildings) {
			b.visibleByCurrentTeam = this.isVisibleByTeam(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (Bullet b : this.bullets) {
			b.visibleByCurrentTeam = this.isVisibleByTeam(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (SpellEffect b : this.spells) {
			b.visibleByCurrentTeam = this.isVisibleByTeam(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
		for (NaturalObjet n : this.naturalObjets) {
			n.visibleByCurrentTeam = this.isVisibleByTeam(Game.g.currentPlayer.getTeam(), n);
			n.visibleByCamera = this.isVisibleByCamera(n);
		}
		for (Bonus b : this.bonus) {
			b.visibleByCurrentTeam = this.isVisibleByTeam(Game.g.currentPlayer.getTeam(), b);
			b.visibleByCamera = this.isVisibleByCamera(b);
		}
	}

	public Objet getById(int id){

		if(this.objets.containsKey(id)){
			return this.objets.get(id);
		}
		return null;
	}


	// getter, setter and act handler

	public Act getCurrentAct(){
		if(this.acts.size()>=0 && this.currentAct>=0 && this.currentAct<this.acts.size()){
			return this.acts.get(this.currentAct);
		} else {
			return null;
		}
	}

	public float getCurrentActTime(){
		return this.currentActTime;
	}

	public void chooseActCard(int i){
		// sélection de la carte

		Game.g.currentPlayer.getGameTeam().currentChoices.get(0).get(i).applyEffect();
		Game.g.bottomBar.addCardChoice(Game.g.currentPlayer.getGameTeam().currentChoices.get(0).get(i));
		Game.g.currentPlayer.getGameTeam().choices.addElement(Game.g.currentPlayer.getGameTeam().currentChoices.get(0).get(i));
		Game.g.currentPlayer.getGameTeam().currentChoices.remove(0);
		Game.g.bottomBar.iconChoice=null;
	}


	public boolean isRuleActive(ActRule rule) {
		return this.getCurrentAct()!=null && this.getCurrentAct().isRuleActive(rule);
	}

}
