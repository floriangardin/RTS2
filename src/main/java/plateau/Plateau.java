package plateau;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.glass.ui.CommonDialogs.Type;

import bonus.Bonus;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import data.Attributs;
import events.EventHandler;
import events.EventNames;
import pathfinding.MapGrid;
import spells.Etats;
import spells.Spell;
import spells.SpellEffect;
import system.Debug;
import utils.ObjetsList;
import utils.Utils;

public class Plateau implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4212262274818996077L;

	public int teamLooser = 0;

	public int maxX;
	public int maxY;

	// ADD ALL OBJETS
	private Vector<Character> characters;
	private Vector<Character> toAddCharacters;
	private Vector<Character> toRemoveCharacters;

	private Vector<Bullet> bullets;
	private Vector<Bullet> toAddBullets;
	private Vector<Bullet> toRemoveBullets;

	private Vector<Building> buildings;
	private Vector<Building> toAddBuildings;
	private Vector<Building> toRemoveBuildings;

	private Vector<Checkpoint> checkpoints;
	private Vector<Checkpoint> markersBuilding; 
	private Vector<Bonus> bonus;

	private Vector<NaturalObjet> naturalObjets;
	private Vector<NaturalObjet> toAddNaturalObjets;
	private Vector<NaturalObjet> toRemoveNaturalObjets;
	private Vector<SpellEffect> spells;
	private Vector<SpellEffect> toAddSpells;
	private Vector<SpellEffect> toRemoveSpells;
	public MapGrid mapGrid;
	public HashMap<Integer,Objet> objets;
	// players
	public Vector<Team> teams;
	// round
	public int round = 0;
	// Hold ids of objects
	public int id = 0;

	public Plateau(int maxX, int maxY) {
		// GENERAL
		this.mapGrid = new MapGrid(maxX, maxY);
		this.maxX = maxX;
		this.maxY = maxY;
		// TEAMS
		this.teams = new Vector<Team>();
		for(int id=0 ; id<3; id++){
			this.teams.add(new Team(id, this));
		}
		// CHARACTERS
		this.setCharacters(new Vector<Character>());
		this.toAddCharacters = new Vector<Character>();
		this.toRemoveCharacters = new Vector<Character>();
		// WEAPONS
		this.bullets = new Vector<Bullet>();
		this.toAddBullets = new Vector<Bullet>();
		this.toRemoveBullets = new Vector<Bullet>();
		// NATURALOBJETS
		this.setNaturalObjets(new Vector<NaturalObjet>());
		this.toAddNaturalObjets = new Vector<NaturalObjet>();
		this.toRemoveNaturalObjets = new Vector<NaturalObjet>();
		// SPELLS
		this.spells = new Vector<SpellEffect>();
		this.toAddSpells = new Vector<SpellEffect>();
		this.toRemoveSpells = new Vector<SpellEffect>();

		// BONUS
		this.setBonus(new Vector<Bonus>());

		// ENEMYGENERATOR
		this.setBuildings(new Vector<Building>());
		this.toAddBuildings = new Vector<Building>();
		this.toRemoveBuildings = new Vector<Building>();
		//temporary Checkpoints ( markers )
		this.setCheckpoints(new Vector<Checkpoint>());
		this.markersBuilding = new Vector<Checkpoint>();

		// All objects
		objets = new HashMap<Integer,Objet>();
		id = 0;

	}
	
	public void print(){
		System.out.println("Size checkpoints : "+getCheckpoints().size());
		System.out.println("Size characters : "+getCharacters().size());
		System.out.println("Size buildings : "+getBuildings().size());
		System.out.println("Size bullets : "+bullets.size());
		System.out.println("Size total : "+(getCheckpoints().size()+bullets.size() + getBuildings().size()+getCharacters().size()) );
		System.out.println("Size hashmap : "+this.objets.size());
		System.out.println("\n\n== Characters");
		for(Character c : this.getCharacters()){
			System.out.println(c.id+" "+c.x+" "+c.y);
		}
	}

	public void addCharacterObjets(Character o) {
		((Character)o).setXY(o.x,o.y, this);
		toAddCharacters.addElement(o);
	}

	private void removeCharacter(Character o) {
		((Character)o).setXY(-1,-1, this);
		System.out.println("line 132 characters : removed "+this.id);
		toRemoveCharacters.addElement(o);
	}

	public void addBulletObjets(Bullet o) {
		toAddBullets.addElement(o);
	}

	private void removeBullet(Bullet o) {
		toRemoveBullets.addElement(o);
	}

	public void addNaturalObjets(NaturalObjet o) {
		this.mapGrid.addNaturalObject(o);
		toAddNaturalObjets.addElement(o);
	}

	private void removeNaturalObjets(NaturalObjet o) {
		this.mapGrid.removeNaturalObject(o);
		toRemoveNaturalObjets.addElement(o);
	}

	public void addBuilding(Building o) {
		this.mapGrid.addBuilding(o);
		toAddBuildings.addElement(o);
	}

	private void removeBuilding(Building o) {
		this.mapGrid.removeBuilding(o);
		toRemoveBuildings.addElement(o);
	}

	public void addSpell(SpellEffect o) {
		toAddSpells.addElement(o);
	}

	private void removeSpell(SpellEffect o) {
		toRemoveSpells.addElement(o);
	}


	public boolean isImmolating(Character c){
		return c.etats.contains(Etats.Immolated);
	}

	// General methods

	public void clean() {
		// Clean the buffers and handle die
		// Remove and add considering alive
		for (Character o : getCharacters()) {
			if (!o.isAlive()) {
				if(o.getAttribut(Attributs.autoImmolation)==1f && !isImmolating(o) ){
					o.lifePoints = 10f;
					o.launchSpell(o,ObjetsList.Immolation, this);
					continue;
				}
				this.removeCharacter(o);
				//TODO:001
				EventHandler.addEvent(EventNames.Death, o, this);
			}
		}

		for (Bullet o : bullets) {
			if (!o.isAlive()) {
				this.removeBullet(o);
			}
		}
		for (NaturalObjet o : getNaturalObjets()) {
			if (!o.isAlive()) {
				this.removeNaturalObjets(o);
			}
		}
		for (Building o : getBuildings()) {
			if (!o.isAlive()) {
				this.removeBuilding(o);
			}
		}
		for (SpellEffect o : spells) {
			if (!o.isAlive()) {
				this.removeSpell(o);
			}
		}
		if(Debug.debugMemory){
			System.out.println("\n nouveau tour ");
			//			System.out.println("characters : " + characters.size());
			//			System.out.println("bullets : " + bullets.size());
			//			System.out.println("naturalObjects : " + naturalObjets.size());
			//			System.out.println("buildings : " + buildings.size());
			//			System.out.println("spells : " + spells.size());
			//			System.out.println("checkpoints : " + checkpoints.size());
			//			System.out.println("markers building : " + markersBuilding.size());

			// DEBUG SIZE 
			System.out.println("Plateau 285 : Size checkpoints : "+getCheckpoints().size());
			System.out.println("Plateau 285 : Size characters : "+getCharacters().size());
			System.out.println("Plateau 285 : Size buildings : "+getBuildings().size());
			System.out.println("Plateau 285 : Size bullets : "+bullets.size());
			System.out.println("Plateau 285 : Size total : "+(getCheckpoints().size()+bullets.size() + getBuildings().size()+getCharacters().size()) );

			System.out.println("PLateau 299 : size hashmap : "+this.objets.size());



		}

		Vector<Checkpoint> toremove = new Vector<Checkpoint>();
		for (Checkpoint o : getCheckpoints()) {
			if (!o.isAlive()) {
				toremove.add(o);
				objets.remove(o.id);
			}
		}
		getCheckpoints().removeAll(toremove);

		// Update selection and groups
		// Remove objets from lists and streams
		for (Character o : toRemoveCharacters) {
			getCharacters().remove(o);
			objets.remove(o.id);
			o.destroy();

		}
		for (Character o : toAddCharacters) {
			objets.put(o.id,o);
			getCharacters().addElement(o);
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
			objets.remove(o.id);
			getNaturalObjets().remove(o);
		}
		for (NaturalObjet o : toAddNaturalObjets) {
			objets.put(o.id,o);
			getNaturalObjets().addElement(o);
		}
		for (Building o : toRemoveBuildings) {
			objets.remove(o.id);
			getBuildings().remove(o);
		}
		for (Building o : toAddBuildings) {
			objets.put(o.id,o);
			getBuildings().addElement(o);
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
		
		// Assertion objets equals character ==> Bug du siecle bizarre ++
//		for(Character c : getCharacters()){
//			if(this.getById(c.id)==null){
//				try {
//					throw new Exception();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}

	}

	public void collision() {
		this.mapGrid.updateSurroundingChars();
		for (Character o : getCharacters()) {
			// Handle collision between Objets and action objects
			if (o.idCase != -1) {
				for (Character i : mapGrid.getCase(o.idCase).surroundingChars) {
					// We suppose o and i have circle collision box
					if (i != o && Utils.distance(i, o) < (i.getAttribut(Attributs.size) + o.getAttribut(Attributs.size))) {
						i.collision(o, this);
						o.collision(i, this);
					}
				}
			}
			Circle range = new Circle(o.x, o.y, o.getAttribut(Attributs.range));
			// Between bonus and characters
			for (Bonus b : this.getBonus()) {
				if (Utils.distance(b, o) < b.hitBoxSize) {
					b.collision(o, this);
				}
				if (Utils.distance(b, o) < (b.getAttribut(Attributs.size) + range.radius)) {
					b.collisionWeapon(o, this);
				}
			}
			// between Characters and Natural objects
			for (NaturalObjet i : getNaturalObjets()) {
				if (i.collisionBox.intersects(o.collisionBox)) {
					o.collision(i, this);
				}
			}
			// Between Characters and bullets
			for (Bullet i : bullets) {
				if (i instanceof Arrow && Utils.distance(i, o) < (i.size + o.getAttribut(Attributs.size))) {
					i.collision(o, this);
				}
			}

			// Between characters and buildings

			for (Building e : getBuildings()) {
				if (e.collisionBox.intersects(range)) {
					e.collisionWeapon(o, this);
				}
				if (e.collisionBox.intersects(o.collisionBox)) {
					boolean doCollision = true;
					for(Circle c : e.corners){
						if(Utils.distance(o,c.getCenterX(),c.getCenterY())<(30f+o.getAttribut(Attributs.size))){
							doCollision = false;
						}
					}
					if(doCollision)
						o.collision(e, this);
				}
				else{

				}
			}
			// Between spells and characters
			for (SpellEffect s : this.spells) {
				if (s.collisionBox != null) {
					if (s.collisionBox.intersects(o.collisionBox)) {
						s.collision(o, this);
					}
				}
			}
		}
		// Between bullets and natural objets
		for (Bullet b : bullets) {
			for (NaturalObjet n : getNaturalObjets()) {
				if (b.collisionBox.intersects(n.collisionBox))
					b.collision(n, this);
			}
			for (Building c : getBuildings()) {
				if (b.collisionBox.intersects(c.collisionBox))
					b.collision(c, this);
			}
		}

	}

	public void action() {
		for (Checkpoint a : this.getCheckpoints()) {
			a.action(this);
		}
		for (Checkpoint a : this.markersBuilding) {
			a.action(this);
		}
		for (Character o : this.getCharacters()) {
			o.action(this);
		}
		for (Bullet o : bullets) {
			o.action(this);
		}
		for (Building e : this.getBuildings()) {
			e.action(this);
		}
		for (Objet a : this.spells) {
			a.action(this);
		}
		for (Bonus a : this.getBonus()) {
			a.action(this);
		}

	}

	public Vector<Objet> getById(Vector<Integer> vo){
		Vector<Objet> resultat = new Vector<Objet>();
		for(Integer i : vo ){
			resultat.add(getById(i));
		}
		return resultat;
	}


	// handling the input
	public void updateTarget(InputObject im, int mode, Plateau plateau ) {
		float x = im.x;
		float y = im.y;
		int team = im.team;
		for (Objet c : getById(im.selection)) {
			updateTarget(c,x,y,team,mode,im.selection);
		}
	}

	public void updateTarget(Objet c, float x, float y, int team, int mode, Vector<Integer> selection ) {

		// called when right click on the mouse
		Objet target = this.findTarget(x, y,team);
		if (target == null) {
			
			target = new Checkpoint(x, y,true, this);
		}

//		if(c instanceof Building && mode==Character.DESTROY_BUILDING){
//			((Building) c).giveUpProcess = true;
//			return;
//		}
		if (c instanceof Character) {
			Character o = (Character) c;
			o.setTarget(null, this);
			o.stop(this);
			o.secondaryTargets.clear();
			o.mode = mode;
			if (o.getGroup(this) != null && o.getGroup(this).size() > 1) {
				for (Character c1 : o.getGroup(this))
					if (c1 != o)
						c1.removeFromGroup(o.id);
			}
			// Then we create its new group
			o.setGroup(new Vector<Character>());
			Vector<Integer> waypoints = null;
			Objet c1;
			for (Integer i1 : selection) {
				c1 = getById(i1);
				if (c1 == c)
					continue;
				if (c1 instanceof Character) {
					o.addInGroup(c1.id);
					// System.out.println("Plateau line 507: " +
					// (waypoints!=null) +" "+(c.c==c1.c)+"
					// "+(((Character)c1).waypoints.size()>0));
					if (((Character) c1).waypoints != null && c1.idCase == c.idCase && c1.getTarget(this) != null
							&& c1.getTarget(this).idCase == target.idCase) {
						// System.out.println("Plateau line 508 : copie
						// d'une chemin");
						waypoints = ((Character) c1).waypoints;
					}
				}
			}
			if(target instanceof Building){
				mode = Character.TAKE_BUILDING;
			}

			o.setTarget(target, waypoints, mode, this);
			o.secondaryTargets.clear();
		}
	}

	public void updateSecondaryTarget(InputObject im) {
		float x = im.x;
		float y = im.y;
		int team = im.team;
		// called when right click on the mouse
		Objet target = this.findTarget(x, y,team);
		if (target == null) {
			target = new Checkpoint(x, y, this);
		}
		Objet c;
		Objet o1 = null;
		for (Integer d : im.selection) {
			c = this.getById(d);
			if (c instanceof Character) {
				Character o = (Character) c;
				// first we deal with o's elder group
				if (o.getGroup(this) != null && o.getGroup(this).size() > 1) {
					for (Character c1 : o.getGroup(this))
						if (c1 != o)
							c1.removeFromGroup(o.id);
				}
				// Then we create its new group
				o.setGroup(new Vector<Character>());
				for (Integer i1 : im.selection)
					o1 = getById(i1);
					if(o1 instanceof Character)
						o.addInGroup(o1.id);
				o.secondaryTargets.add(target.id);
			}
		}
	}

	// calling method to the environment
	public Vector<Character> getEnnemiesInSight(Character caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : getCharacters()) {
			if (o.getTeam() != caller.getTeam() && o.collisionBox.intersects(caller.sightBox)) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Character> getEnnemiesInSight(Building caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : getCharacters()) {
			if (o.getTeam().id != caller.potentialTeam && Utils.distance(o, caller) < caller.getAttribut(Attributs.sight)) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Objet> getAlliesInSight(Character caller) {
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for (Character o : getCharacters()) {
			if (o != caller && o.getTeam() == caller.getTeam() && o.collisionBox.intersects(caller.sightBox)) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Character> getWoundedAlliesInSight(Character caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : getCharacters()) {
			if (o != caller && o.getTeam() == caller.getTeam() && o.lifePoints < o.getAttribut(Attributs.maxLifepoints)
					&& o.collisionBox.intersects(caller.sightBox)) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Objet findTarget(float x, float y,int team) {
		Point point = new Point(x, y);
		Objet target = null;

		// looking for the object on the target
		for (Character i : this.getCharacters()) {
			// looking amongst other characters
			if (i.selectionBox.contains(point) && i.getTeam().id!=team) {
				target = i;
				break;
			}
		}
		if (target == null) {
			for (Character i : this.getCharacters()) {
				// looking amongst other characters
				if (i.selectionBox.contains(point) && i.getTeam().id==team) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (Building i : this.getBuildings()) {
				// looking amongst natural object
				if (i.collisionBox.contains(point)) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (Bonus i : this.getBonus()) {
				// looking amongst natural object
				if (i.collisionBox.contains(point)) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (NaturalObjet i : getNaturalObjets()) {
				// looking amongst natural object
				if (Math.sqrt((i.x-x)*(i.x-x)+(i.y-y)*(i.y-y))<4*i.collisionBox.getBoundingCircleRadius()) {
					target = i;
					break;
				}
			}
		}
		return target;
	}
	
	public void update(){
		update(new Vector<InputObject>());
	}
	public void update(Vector<InputObject> ims) {
		
		round ++;
		// 1 - Handling inputs
		
		for (InputObject im : ims) {
			//handle victory
			if(im.isPressed(KeyEnum.AbandonnerPartie)){
				this.teamLooser = im.team;
				return;
			}
			// Handling the right click
			this.handleRightClick(im);
			// Handling action bar TODO : �a n'a rien � faire l�, � d�gager
			this.handleActionOnInterface(im);

		}
		
		// 2 - For everyone
		// Sort by id
		this.collision();
		
		this.clean();
		
		this.action();
		// 4- handling victory
		for(Team team : teams){
			if(team.id==0){
				continue;
			}
			if(((Building)this.getById(team.hq)).constructionPoints<=0){
				this.teamLooser = team.id;
			}
		}
		
	}

	void handleMouseHover(InputObject im) {
		for (Character c : this.getCharacters()) {
			if (c.selectionBox.contains(im.x, im.y)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}
		for (Bonus c : this.getBonus()) {
			if (c.selectionBox.contains(im.x, im.y)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}
		for (Building c : this.getBuildings()) {
			if (c.selectionBox.contains(im.x, im.y)) {
				c.mouseOver = true;
			} else {
				c.mouseOver = false;
			}
		}
	}

	private void handleRightClick(InputObject im) {
		int team = im.team;
		Vector<Objet> selection = im.getSelection(this);
		if (im.isPressed(KeyEnum.RightClick)) {
			// RALLY POINT
			if (selection.size() > 0
					&& selection.get(0) instanceof Building) {
				Objet target = findTarget(im.x, im.y,team);
				if(target instanceof Building || target instanceof Character){
					((Building) selection.get(0)).setRallyPoint(target.x, target.y, this);;
				}
				if(target==null){
					((Building) selection.get(0)).setRallyPoint(im.x, im.y,this);
				}
			} else if (im.isPressed(KeyEnum.AjouterSelection)) {
				updateSecondaryTarget(im);
			} else {
				updateTarget(im, Character.MOVE, this);
			}
		}
		if (im.isPressed(KeyEnum.StopperMouvement)) {
			// STOP SELECTION

			for (Objet c : selection) {

				if (c instanceof Character) {
					((Character) c).stop(this);
					((Character) c).mode = Character.NORMAL;
				}
			}
		}
		if (im.isPressed(KeyEnum.DeplacementOffensif)) {
			updateTarget(im, Character.AGGRESSIVE, this);
		}
		if (im.isPressed(KeyEnum.TenirPosition)) {
			// Hold position
			updateTarget(im, Character.HOLD_POSITION, this);
		}
		if (im.isPressed(KeyEnum.GlobalRallyPoint)) {
			//Update rally point
			for(Building b : getBuildings()){
				if(b.getTeam().id==im.team && b instanceof Building){
					((Building) b).setRallyPoint(im.x, im.y, this);
				}
			}
		}
	}

	private void handleActionOnInterface(InputObject im) {
		// Action bar
		boolean imo = false;
		if (im.isPressed(KeyEnum.Immolation) || im.isPressed(KeyEnum.Prod0) || im.isPressed(KeyEnum.Prod1) || im.isPressed(KeyEnum.Prod2) || im.isPressed(KeyEnum.Prod3) ||  im.isPressed(KeyEnum.Tech0) || im.isPressed(KeyEnum.Tech1) || im.isPressed(KeyEnum.Tech2) || im.isPressed(KeyEnum.Tech3) || im.isPressed(KeyEnum.Escape)) {

			if (im.selection.size() > 0 && this.getById(im.selection.get(0)) instanceof Building) {

				for(int i=0; i<4; i++){
					if (im.isPressed(KeyEnum.valueOf("Prod"+i))){

						((Building) this.getById(im.selection.get(0))).product(i, this);
					}
					else if(im.isPressed(KeyEnum.valueOf("Tech"+i))){

						((Building) this.getById(im.selection.get(0))).productTech(i, this);
					}
				}
				if (im.isPressed(KeyEnum.Escape))
					((Building) this.getById(im.selection.get(0))).removeProd(this);
			} else if (im.selection.size() > 0 && this.getById(im.selection.get(0)) instanceof Character) {
					int number = -1;
					for(int i=0; i<4; i++){
						if (im.isPressed(KeyEnum.valueOf("Prod"+i)))
							number = i;
					}
					if (im.isPressed(KeyEnum.Immolation)){

						number = 0;
						imo = true;
					}

					Character c = ((Character) this.getById(im.selection.get(0)));
					if (-1 != number && number < c.getSpells().size()
							&& c.canLaunch(number)) {
						Spell s = c.getSpell(number);
						if (s.getAttribut(Attributs.needToClick)==0) {
							if(s.name!=ObjetsList.Immolation || imo){
								s.launch(new Checkpoint(im.x,im.y, this), c, this);
								c.spellsState.set(number, 0f);
								
							}
						}
					}
				}
		}
		if(im.spell!=null && im.idSpellLauncher > -1){
			Spell s = teams.get(im.team).data.getSpell(im.spell);
			Character c = ((Character) this.getById(im.idSpellLauncher));
			if(im.idObjetMouse!=-1){
				s.launch(getById(im.idObjetMouse), c, this);
			} else {
				s.launch(new Checkpoint(im.x,im.y, this), c, this);				
			}
			for(int i=0; i<c.getSpells().size(); i++){
				if(c.getSpells().get(i).name==im.spell){
					c.spellsState.set(i, 0f);
				}
			}

		}

	}


	// METHODS ONLY CALLED BY THE CURRENT PLAYER

	// updating rectangle


//	public void handleInterface(InputObject im){
//		// display for the action bar
//		float relativeXMouse = (im.x - Camera.Xcam);
//		float relativeYMouse = (im.y - Camera.Ycam);
//		Game.gameSystem.bottombar.update(relativeXMouse, relativeYMouse);
//
//	}
//
//	private void handleMinimap(InputObject im, int player) {
//		if (im.isDown(KeyEnum.LeftClick) && player == Game.gameSystem.getCurrentPlayer().id && im.isOnMiniMap) {
//			// Put camera where the click happened
//			Camera.objXcam = (int) (im.x - Game.resX / 2f);
//			Camera.objYcam = (int) (im.y - Game.resY / 2f);
//			Camera.slidingCam = true;
//			//			System.out.println(slidingCam);
//		}
//	}
	
	public boolean isVisibleByTeam(int team, Objet objet) {
		if (objet.getTeam() != null && objet.getTeam().id == team)
			return true;
		float r = Math.max(objet.getAttribut(Attributs.size),objet.getAttribut(Attributs.sizeX))/2;
		for (Character c : getCharacters())
			if (c.getTeam().id == team && Utils.distance(c, objet) < c.getAttribut(Attributs.sight) + r)
				return true;
		for (Building b : getBuildings())
			if (b.getTeam().id == team && Utils.distance(b, objet) < b.getAttribut(Attributs.sight) + r)
				return true;
		return false;
	}

	
	public Building getHQ(Team team){
		return (Building)this.getById(team.hq);
	}

	public Objet getById(int id){

		if(this.objets.containsKey(id)){
			return this.objets.get(id);
		}
		return null;
	}
	
	public String toString(){
		String s = "";
		Vector<Objet> concatenation = new Vector<Objet>();
		concatenation.addAll(this.getCharacters());
		concatenation.addAll(this.getBuildings());
		for(Objet o : concatenation){
			s+=o.hash();
		}
		return s;
	}
	public boolean equals(Object o){
		if(!(o instanceof Plateau)){
			return false;
		}
		return this.toString().equals(((Plateau)o).toString());
	}

	public int getRound() {
		
		return round;
	}

	public Vector<Character> getCharacters() {
		return characters;
	}

	private void setCharacters(Vector<Character> characters) {
		this.characters = characters;
	}

	public Vector<NaturalObjet> getNaturalObjets() {
		return naturalObjets;
	}

	public void setNaturalObjets(Vector<NaturalObjet> naturalObjets) {
		this.naturalObjets = naturalObjets;
	}

	public Vector<Bonus> getBonus() {
		return bonus;
	}

	public void setBonus(Vector<Bonus> bonus) {
		this.bonus = bonus;
	}

	public Vector<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(Vector<Building> buildings) {
		this.buildings = buildings;
	}

	public Vector<Checkpoint> getCheckpoints() {
		return checkpoints;
	}

	public void setCheckpoints(Vector<Checkpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}
	
	public Object toJson(){
		
		HashMap<Integer, HashMap<String, Object>> res = new HashMap<Integer, HashMap<String, Object>>();
		HashMap<String, HashMap<?,?>> finalResult = new HashMap<String, HashMap<?,?>>();
		finalResult.put("teams", null);
		// Get teams state
		HashMap<Integer, HashMap<String, Integer>> toPut = new HashMap<Integer, HashMap<String, Integer>>();
		for(Team t : teams){
			HashMap<String, Integer> stats = new HashMap<String, Integer>();
			stats.put("pop", t.getPop(this));
			stats.put("maxPop", t.getMaxPop(this));
			stats.put("food", t.food);
			stats.put("hasLost", (t.id==this.teamLooser) && t.id!=0 ? 1 : 0);
			if((t.id==this.teamLooser) && t.id!=0){
				System.out.println("has lost");
			}
			toPut.put(t.id, stats);
		}
		this.objets.forEach((key,value) -> res.put(key, value.toJson()));
		finalResult.put("teams", toPut );
		finalResult.put("plateau", res);
		// Pour chaque objet json
		return finalResult;
	}


}