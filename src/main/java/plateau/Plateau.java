package plateau;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import bonus.Bonus;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import data.Attributs;
import events.EventHandler;
import events.EventNames;
import pathfinding.Case;
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
	public EndCondition getEndCondition() {
		return endCondition;
	}

	public void setEndCondition(EndCondition endCondition) {
		this.endCondition = endCondition;
	}
	private Vector<Objet> toAddObjet;
	private Vector<Objet> toRemoveObjet;
	public MapGrid mapGrid;
	private HashMap<Integer,Objet> objets;
	// players
	public Vector<Team> teams;
	// round
	public int round = 0;
	// Hold ids of objects
	public int id = 0;
	private EndCondition endCondition;
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
		// OBKETS
		this.toAddObjet = new Vector<Objet>();
		this.toRemoveObjet = new Vector<Objet>();
		// End condition 
		this.endCondition = new NormalEndCondition();
		// All objects
		setObjets(new HashMap<Integer,Objet>());
		id = 0;

	}
	
	public void print(){
//		System.out.println("Size checkpoints : "+checkpoints.size());
//		System.out.println("Size characters : "+characters.size());
//		System.out.println("Size buildings : "+buildings.size());
//		System.out.println("Size bullets : "+bullets.size());
//		System.out.println("Size total : "+(checkpoints.size()+bullets.size() + buildings.size()+characters.size()) );
//		System.out.println("Size hashmap : "+this.getObjets().size());
//		System.out.println("\n\n== Characters");
//		for(Character c : this.characters){
//			System.out.println(c.id+" "+c.x+" "+c.y);
//		}
	}

	public void addCharacterObjets(Character o) {
		Case c = mapGrid.getCase(o.x, o.y);
		if(c!=null){
			o.idCase = c.id;
			mapGrid.getCase(o.idCase).characters.add(o);
		} else {
			o.idCase = -1;
		}
		toAddObjet.addElement(o);
		
	}

	public void removeCharacter(Character o) {
		Case c = mapGrid.getCase(o.idCase);
		if(c!=null && c.characters.contains(o)){
			c.characters.remove(o);
		}
		toRemoveObjet.addElement(o);
		this.toRemoveObjet.addElement(o);
	}

	public void addBulletObjets(Bullet o) {
		toAddObjet.addElement(o);
	}

	public void removeBullet(Bullet o) {
		toRemoveObjet.addElement(o);
	}

	public void addNaturalObjets(NaturalObjet o) {
		this.mapGrid.addNaturalObject(o);
		toAddObjet.addElement(o);
	}

	public void removeNaturalObjets(NaturalObjet o) {
		this.mapGrid.removeNaturalObject(o);
		toRemoveObjet.addElement(o);
	}

	public void addBuilding(Building o) {
		this.mapGrid.addBuilding(o);
		toAddObjet.addElement(o);
	}

	public void removeBuilding(Building o) {
		this.mapGrid.removeBuilding(o);
		toRemoveObjet.addElement(o);
	}

	public void addSpell(SpellEffect o) {
		toAddObjet.addElement(o);
	}

	public void removeSpell(SpellEffect o) {
		toRemoveObjet.addElement(o);
	}


	public boolean isImmolating(Character c){
		return c.etats.contains(Etats.Immolated);
	}

	// General methods

	public void clean() {

		for(Objet obj : this.getValues()){
			if (!obj.isAlive()) {
				if(obj instanceof Character){
					Character o = (Character) obj;
					if(o.getAttribut(Attributs.autoImmolation)==1f && !isImmolating(o) ){
						o.lifePoints = 10f;
						o.launchSpell(o, ObjetsList.Immolation, this);
						continue;
					}
					EventHandler.addEvent(EventNames.Death, o, this);
				}
				this.toRemoveObjet.add(obj);
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
			System.out.println("PLateau 299 : size hashmap : "+this.getObjets().size());
		}

		// Update selection and groups
		// Remove objets from lists and streams
		for (Objet o : toRemoveObjet) {
			objets.remove(o.id);
		}
		for (Objet o : toAddObjet) {
			objets.put(o.id,o);
		}
		toRemoveObjet.clear();
		toAddObjet.clear();

	}
	
	public List<Character> getCharacters(){
		return this.objets.values().stream()
				.filter(x-> x instanceof Character)
				.map(x-> (Character) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
	}
	public List<Building> getBuildings(){
		return  this.objets.values().stream()
				.filter(x-> x instanceof Building)
				.map(x-> (Building) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
	}
	
	public List<Bullet> getBullets() {
		return this.objets.values().stream()
				.filter(x-> x instanceof Bullet)
				.map(x-> (Bullet) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
	}


	private List<SpellEffect> getSpells() {
		return this.objets.values().stream()
				.filter(x-> x instanceof SpellEffect)
				.map(x-> (SpellEffect) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
	}

	public List<Checkpoint> getCheckpoints() {
		return this.objets.values().stream()
				.filter(x-> x instanceof Checkpoint)
				.map(x-> (Checkpoint) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
	}



	public List<MarkerBuilding> getMarkersBuilding() {
		return this.objets.values().stream()
				.filter(x-> x instanceof MarkerBuilding)
				.map(x-> (MarkerBuilding) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
	}



	public List<Bonus> getBonus() {
		return this.objets.values().stream()
				.filter(x-> x instanceof Bonus)
				.map(x-> (Bonus) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
	}



	public List<NaturalObjet> getNaturalObjets() {
		return this.objets.values().stream()
				.filter(x-> x instanceof NaturalObjet)
				.map(x-> (NaturalObjet) x)
				.sorted((x,y) -> x.id - y.id)
				.collect(Collectors.toList());
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
//						o.collision(i, this);
					}
				}
			}
			Circle range = new Circle(o.x, o.y, o.getAttribut(Attributs.range));
			// Between bonus and characters
			for (Bonus b : getBonus()) {
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
			for (Bullet i : getBullets()) {
				if (i instanceof Arrow && Utils.distance(i, o) < (i.size + o.getAttribut(Attributs.size))) {
					i.collision(o, this);
				}
			}

			// Between characters and buildings
			Circle c;
			for (Building e : getBuildings()) {
				if (e.collisionBox.intersects(range)) {
					e.collisionWeapon(o, this);
				}
				if (e.collisionBox.intersects(o.collisionBox)) {
					int collisionCorner = 0;
					for(int i=0; i<e.corners.size(); i++){
						c = e.corners.get(i);
						if(Utils.distance(o,c.getCenterX(),c.getCenterY())<(30f+o.getAttribut(Attributs.size))){
							collisionCorner = i+1;
							break;
						}
					}
					o.collision(e, collisionCorner, this);
				}
			}
			// Between spells and characters
			for (SpellEffect s : getSpells()) {
				if (s.collisionBox != null) {
					if (s.collisionBox.intersects(o.collisionBox)) {
						s.collision(o, this);
					}
				}
			}
			// Between characters and forbidden terrain (water)
			Case ca = mapGrid.idcases.get(o.idCase);
			if(!ca.getIdTerrain().ok){
				o.collisionRect(new Rectangle(ca.x, ca.y, ca.sizeX, ca.sizeY), this);
			}
		}
		// Between bullets and natural objets
		for (Bullet b : getBullets()) {
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
		
		for(Objet o : this.getValues()){
			o.action(this);
		}
		
	}
	
	public Stream<Objet> get(){
		return this.objets.values().stream();
	}
	
	private List<Objet> getValues() {
		return this.getObjets()
				.values()
				.stream()
				.sorted((x,y)-> x.id - y.id)
				.collect(Collectors.toList());
	
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
			if (o.getTeam() != caller.getTeam() 
					&& Utils.distance(o, caller) <= (o.getAttribut(Attributs.size)+caller.getAttribut(Attributs.sight))) {
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
			if (o != caller
					&& Utils.distance(o, caller) <= (o.getAttribut(Attributs.size)+caller.getAttribut(Attributs.sight))) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Character> getWoundedAlliesInSight(Character caller) {
		Vector<Character> ennemies_in_sight = new Vector<Character>();
		for (Character o : getCharacters()) {
			if (o != caller && o.getTeam() == caller.getTeam() && o.lifePoints < o.getAttribut(Attributs.maxLifepoints)
					&& Utils.distance(o, caller) <= (o.getAttribut(Attributs.size)+caller.getAttribut(Attributs.sight))) {
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Objet findTarget(float x, float y,int team) {
		Point point = new Point(x, y);
		Objet target = null;

		// looking for the object on the target
		for (Character i :getCharacters()) {
			// looking amongst other characters
			if (i.selectionBox.contains(point) && i.getTeam().id!=team) {
				target = i;
				break;
			}
		}
		if (target == null) {
			for (Character i : getCharacters()) {
				// looking amongst other characters
				if (i.selectionBox.contains(point) && i.getTeam().id==team) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (Building i : getBuildings()) {
				// looking amongst natural object
				if (i.collisionBox.contains(point)) {
					target = i;
					break;
				}
			}
		}
		if (target == null) {
			for (Bonus i : getBonus()) {
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
				if (Math.sqrt((i.x-x)*(i.x-x)+(i.y-y)*(i.y-y))<i.collisionBox.getBoundingCircleRadius()) {
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
		this.clean();
		
		// 1 - Handling inputs
		for (InputObject im : ims) {
			im.sanitizeInput(this);
			//handle victory
			if(im.isPressed(KeyEnum.AbandonnerPartie)){
				Team team = this.getTeamById(im.team);
				team.hasGaveUp = true;
				return;
			}
			// Handling the right click
			this.handleRightClick(im);
			this.handleActionOnInterface(im);
		}
		// 2 - For everyone
		// Sort by id
		this.collision();
		
		// 
		this.action();
		// 4- handling victory
		for(Team team : teams){
			if(team.id==0){
				continue;
			}
			if(this.endCondition!=null && this.endCondition.hasLost(this, team)){
				this.teamLooser = team.id;
			}
		}
		
//		if(round>10){
//			Case c;
//			for(int i=0; i<mapGrid.grid.get(0).size(); i++){
//				for(int j=0; j<mapGrid.grid.size(); j++){
//					c = mapGrid.grid.get(j).get(i);
//					if(c.building!=null){
//						System.out.print("X");
//					} else if(c.naturesObjet.size()>0){
//						System.out.print("V");
//					} else if(c.characters.size()>0){
//						System.out.print(c.characters.size());
//					} else {
//						System.out.print(" ");
//					}
//				}
//				System.out.println();
//			}
//			System.out.println();
//			System.out.println(naturalObjets.size());
//			System.out.println(characters.size());
//			System.out.println(buildings.size());
//			while(true){
//				
//			}
//		}
		this.clean();
		
	}

	private Team getTeamById(int team) {
		// TODO Auto-generated method stub
		return teams.stream().filter(x-> x.id==team).findFirst().orElse(null);
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
			boolean hasLaunched = false;
			if(im.idObjetMouse!=-1){
				hasLaunched = s.launch(getById(im.idObjetMouse), c, this);
			} else {
				hasLaunched = s.launch(new Checkpoint(im.x,im.y, this), c, this);				
			}
			for(int i=0; i<c.getSpells().size(); i++){
				if(c.getSpells().get(i).name==im.spell && hasLaunched){
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
		return (Building)this.getValues().stream()
			.filter(x -> x.team.id==team.id)
			.filter(x -> x.name==ObjetsList.Headquarters)
			.findFirst()
			.orElse(null);
	}

	public Objet getById(int id){

		if(this.getObjets().containsKey(id)){
			return this.getObjets().get(id);
		}
		return null;
	}
	
	public String toString(){
		String s = "";
		Vector<Objet> concatenation = new Vector<Objet>();
		concatenation.addAll(getCharacters());
		concatenation.addAll(getBuildings());
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
		this.getObjets().forEach((key,value) -> res.put(key, value.toJson()));
		finalResult.put("teams", toPut );
		finalResult.put("plateau", res);
		// Pour chaque objet json
		return finalResult;
	}

	public HashMap<Integer,Objet> getObjets() {
		return objets;
	}

	public void setObjets(HashMap<Integer,Objet> objets) {
		this.objets = objets;
	}



}
