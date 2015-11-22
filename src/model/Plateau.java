package model;


import java.net.InetAddress;
import java.util.HashMap;
import java.util.Vector;

import multiplaying.InputObject;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import pathfinding.Case;
import pathfinding.MapGrid;
import spells.Spell;
import spells.SpellEffect;
import units.Character;
import IA.IAMicroFlo;
import buildings.Building;
import buildings.BuildingAction;
import buildings.BuildingProduction;
import bullets.Bullet;
import bullets.CollisionBullet;
import display.BottomBar;
import display.Message;

public class Plateau {

	public Game g;
	public float maxX ;
	public float maxY ;

	// Number of teams
	public int nTeams;
	// Number of players
	public int nPlayers;

	// Camera 
	public int Xcam;
	public int Ycam;

	// fog of war
	public Image fog;
	public Graphics gf;

	// about the output of the string
	public String currentString ;



	// teams and players
	public Vector<Player> players = new Vector<Player>();
	public Player currentPlayer;
	public Vector<GameTeam> teams = new Vector<GameTeam>();

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

	public Vector<Bonus> bonus;
	
	public Vector<NaturalObjet> naturalObjets ;
	public Vector<NaturalObjet> toAddNaturalObjets;
	public Vector<NaturalObjet> toRemoveNaturalObjets;

	public Vector<Vector<ActionObjet>> selection;
	public Vector<Vector<ActionObjet>> toAddSelection;
	public Vector<Vector<ActionObjet>> toRemoveSelection ;

	public Vector<SpellEffect> spells;
	public Vector<SpellEffect> toAddSpells;
	public Vector<SpellEffect> toRemoveSpells;

	public Vector<Rectangle> rectangleSelection;
	public Vector<Float> recX ;
	public Vector<Float> recY ;
	public Vector<ActionObjet> inRectangle ;

	public Vector<Boolean> isCastingSpell;
	public Vector<Boolean> hasCastSpell;
	public Vector<Integer> castingSpell;

	public Vector<Vector<Message>> messages;

	public MapGrid mapGrid;


	public Plateau(float maxX,float maxY,Game g){

		this.g = g;
		this.mapGrid = new MapGrid(0f,maxX,0f,maxY);
		//GENERAL
		this.nTeams = 2;
		this.maxX= maxX;
		this.maxY = maxY;

		//ABOUT PLAYERS

		//UPDATING GAME
		this.teams.addElement(new GameTeam(players,this,0,0));
		this.teams.addElement(new GameTeam(players,this,1,0));
		this.teams.addElement(new GameTeam(players,this,2,0));
		this.players = new Vector<Player>();
		this.players.add(new Player(this,0,"Nature",teams.get(0),2,2));
		this.players.add(new Player(this,1,this.g.options.nickname,teams.get(1),(int) this.g.resX, (int) this.g.resY));
		this.players.add(new IAMicroFlo(this,2,"IA random",teams.get(2),2,2));
		this.currentPlayer = players.get(1);
		this.nPlayers = players.size();

		//CHARACTERS
		this.characters = new Vector<Character>();

		this.toAddCharacters = new Vector<Character>();
		this.toRemoveCharacters = new Vector<Character>();
		//WEAPONS
		this.bullets = new Vector<Bullet>();
		this.toAddBullets = new Vector<Bullet>();
		this.toRemoveBullets= new Vector<Bullet>();
		//NATURALOBJETS
		this.naturalObjets = new Vector<NaturalObjet>();
		this.toAddNaturalObjets = new Vector<NaturalObjet>();
		this.toRemoveNaturalObjets= new Vector<NaturalObjet>();
		//SPELLS
		this.spells = new Vector<SpellEffect>();
		this.toAddSpells = new Vector<SpellEffect>();
		this.toRemoveSpells= new Vector<SpellEffect>();
		
		//BONUS 
		this.bonus = new Vector<Bonus>();
		
		//ENEMYGENERATOR
		this.buildings = new Vector<Building>();
		this.toAddBuildings = new Vector<Building>();
		this.toRemoveBuildings = new Vector<Building>();
		//SELECTION
		this.selection = new Vector<Vector<ActionObjet>>();
		this.toAddSelection = new Vector<Vector<ActionObjet>>();
		this.toRemoveSelection = new Vector<Vector<ActionObjet>>();
		this.rectangleSelection = new Vector<Rectangle>();
		this.recX = new Vector<Float>();
		this.recY = new Vector<Float>();
		this.inRectangle = new Vector<ActionObjet>();
		//MESSAGES
		this.messages = new Vector<Vector<Message>>();

		//CASTING SPELLS
		this.isCastingSpell = new Vector<Boolean>();
		this.hasCastSpell = new Vector<Boolean>();
		this.castingSpell = new Vector<Integer>();

		for(int i =0; i<nPlayers;i++){
			this.selection.addElement(new Vector<ActionObjet>());
			this.toAddSelection.addElement(new Vector<ActionObjet>());
			this.toRemoveSelection.addElement(new Vector<ActionObjet>());
			this.isCastingSpell.addElement(false);
			this.hasCastSpell.addElement(false);
			this.castingSpell.addElement(-1);
			this.messages.addElement(new Vector<Message>());
			this.rectangleSelection.addElement(null);
			this.recX.addElement(0f);
			this.recY.addElement(0f);
		}
		try {
			//			System.out.println(this.g.resX+" "+this.g.resY);
			this.fog = new Image((int)this.g.resX,(int)this.g.resY);
			this.gf = fog.getGraphics();
		} catch (SlickException | RuntimeException e) {
		}


	}

	public void setMaxXMaxY(float MaxX, float MaxY){
		this.maxX = MaxX;
		this.maxY = MaxY;
		this.mapGrid = new MapGrid(0f,maxX,0f,maxY);
		for(Player p: players){
			p.bottomBar.minimap.update(g);
		}
	}

	public GameTeam getTeamById(int team) {
		for(GameTeam t : this.teams)
			if(t.id == team)
				return t;
		return null;
	}



	public void addPlayer(String name, InetAddress address,int resX,int resY){
		this.players.addElement(new Player(this,players.size(),name,teams.get(1),resX,resY));
		this.players.lastElement().address = address;
		nPlayers+=1;

		// adding components in plateau
		this.selection.addElement(new Vector<ActionObjet>());
		this.toAddSelection.addElement(new Vector<ActionObjet>());
		this.toRemoveSelection.addElement(new Vector<ActionObjet>());
		this.isCastingSpell.addElement(false);
		this.hasCastSpell.addElement(false);
		this.castingSpell.addElement(-1);
		this.messages.addElement(new Vector<Message>());
		this.rectangleSelection.addElement(null);
		this.recX.addElement(0f);
		this.recY.addElement(0f);

	}

	public void removePlayer(int indice){
		if(indice==0 || indice>players.size())
			return;
		players.remove(indice);
		nPlayers -= 1;

		// deleting component from plateau
		this.selection.remove(indice);
		this.toAddSelection.remove(indice);
		this.toRemoveSelection.remove(indice);
		this.isCastingSpell.remove(indice);
		this.hasCastSpell.remove(indice);
		this.castingSpell.remove(indice);
		this.messages.remove(indice);
		this.rectangleSelection.remove(indice);
		this.recX.remove(indice);
		this.recY.remove(indice);
	}
	// functions that handle buffers

	public void clearPlayer(){
		while(players.size()>1){
			removePlayer(players.size()-1);
		}
	}

	public void addCharacterObjets(Character o){
		toAddCharacters.addElement(o);
	}
	public void removeCharacter(Character o){
		toRemoveCharacters.addElement(o);
		if(this.selection.contains(o)){
			this.selection.remove(o);
		}
		for(Player p : this.players){
			if(p.selection.contains(o)){
				p.selection.remove(o);
			}
		}

	}
	public void addBulletObjets(Bullet o){
		toAddBullets.addElement(o);
	}
	private void removeBullet(Bullet o){
		toRemoveBullets.addElement(o);
	}
	public void addNaturalObjets(NaturalObjet o){
		this.mapGrid.insertNewRec(o.x, o.y, o.sizeX, o.sizeY);
		toAddNaturalObjets.addElement(o);
	}
	private void removeNaturalObjets(NaturalObjet o){
		toRemoveNaturalObjets.addElement(o);
	}
	public void addBuilding(Building o){
		this.mapGrid.insertNewRec(o.x, o.y, o.sizeX, o.sizeY);
		toAddBuildings.addElement(o);
	}
	public void removeBuilding(Building o){
		toRemoveBuildings.addElement(o);
	}
	public void addSpell(SpellEffect o){
		toAddSpells.addElement(o);
	}
	public void removeSpell(SpellEffect o){
		toRemoveSpells.addElement(o);
	}
	public void addSelection(ActionObjet o,int team){
		toAddSelection.get(team).addElement(o);
	}
	public void removeSelection(ActionObjet o, int team){
		toRemoveSelection.get(team).addElement(o);
	}


	// General methods

	public void clean(){
		// Clean the buffers and handle die
		// Remove and add considering alive 
		for(Character o : characters){
			if(!o.isAlive()){
				this.removeCharacter(o);
				this.g.sounds.death.play(0.8f+1f*((float)Math.random()),this.g.options.soundVolume);
			}
		}
		for(Bullet o : bullets){
			if(!o.isAlive()){
				this.removeBullet(o);
			}
		}
		for(NaturalObjet o : naturalObjets){
			if(!o.isAlive()){
				this.removeNaturalObjets(o);
			}
		}
		for(Building o : buildings){
			if(!o.isAlive()){
				this.removeBuilding(o);
			}
		}
		for(SpellEffect o : spells){
			if(!o.isAlive()){
				this.removeSpell(o);
			}
		}

		// Update selection and groups
		Vector<ActionObjet> toDelete = new Vector<ActionObjet>();
		for(int i=0;i<nPlayers;i++){
			for(ActionObjet c: selection.get(i)){
				if(!c.isAlive()){
					this.removeSelection(c, i);
				}
			}
			for(ActionObjet o: toRemoveSelection.get(i)){
				selection.get(i).remove(o);
			}
			for(ActionObjet o: toAddSelection.get(i)){
				selection.get(i).addElement(o);
			}
			if(toAddSelection.get(i).size()>0){
				Utils.triName(selection.get(i));
			}
			for(int k=0; k<10; k++){
				toDelete.clear();
				for(ActionObjet c: this.players.get(i).groups.get(k)){
					if(!c.isAlive())
						toDelete.add(c);
				}
				for(ActionObjet c: toDelete){
					this.players.get(i).groups.get(k).remove(c);
				}
			}
		}
		// Remove objets from lists
		for(Character o: toRemoveCharacters){
			characters.remove(o);
			o.destroy();

		}
		for(Character o: toAddCharacters){
			characters.addElement(o);
		}
		for(SpellEffect o: toAddSpells){
			spells.addElement(o);
		}
		for(SpellEffect o: toRemoveSpells){
			spells.remove(o);
		}
		for(Bullet o: toRemoveBullets){
			bullets.remove(o);
		}
		for(Bullet o: toAddBullets){
			bullets.addElement(o);
		}
		for(NaturalObjet o: toRemoveNaturalObjets){
			naturalObjets.remove(o);
		}
		for(NaturalObjet o: toAddNaturalObjets){
			naturalObjets.addElement(o);
		}
		for(Building o: toRemoveBuildings){
			buildings.remove(o);
		}
		for(Building o: toAddBuildings){
			buildings.addElement(o);
		}

		// Clear the vector :
		for(int i = 0;i<nPlayers;i++){
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

	public void collision(){
		this.mapGrid.updateSurroundingChars();
		for(Character o : characters){
			// Handle collision between actionObjets and action objects
			if(o.c!=null){
				for(Character i:o.c.surroundingChars){
					// We suppose o and i have circle collision box
					if(i!=o && Utils.distance(i, o)<(i.size+o.size)){
						i.collision(o);
						o.collision(i);
					}
				}
			}
			
			//Between bonus and characters 
			for(Bonus b : this.bonus){
				if(Utils.distance(b, o)<b.size){
					b.collision(o);
				}
				
			}
			// between Characters and Natural objects
			for(NaturalObjet i: naturalObjets){
				if(i.collisionBox.intersects(o.collisionBox)){
					o.collision(i);
				}
			}
			// Between Characters and bullets
			for(Bullet i: bullets){
				if(i instanceof CollisionBullet && Utils.distance(i, o)<(i.size+o.size)){
					i.collision(o);
				}
			}
			// Between characters and buildings
			Circle range = new Circle(o.x,o.y,o.range);
			for(Building e:buildings){
				if(e.collisionBox.intersects(range)){
					e.collisionWeapon(o);
				}
				if(e.collisionBox.intersects(o.collisionBox)){
					o.collision(e);
				}
			}
			//Between spells and characters
			for(SpellEffect s:this.spells){
				if(s.collisionBox!=null){
					if(s.collisionBox.intersects(o.collisionBox)){
						s.collision(o);		
					}
				}
			}
		}
		// Between bullets and natural objets
		for(Bullet b : bullets){
			for(NaturalObjet n: naturalObjets){
				if(b.collisionBox.intersects(n.collisionBox))
					b.collision(n);
			}
			for(Building c: buildings){
				if(b.collisionBox.intersects(c.collisionBox))
					b.collision(c);
			}
		}
		
		

	}

	public void action(){
		for(Character o: this.characters){
			o.action();
		}
		for(Bullet o: bullets){
			o.action();
		}
		for(Building e: this.buildings){
			e.action();
		}
		for(ActionObjet a : this.spells){
			a.action();
		}
		for(Bonus a : this.bonus){
			a.action();
		}
	}


	//handling the input
	public void updateTarget(float x, float y, int team){
		//called when right click on the mouse
		Objet target = this.findTarget(x, y);
		if(target==null){
			target = new Checkpoint(this,x,y);
		}
		for(ActionObjet c:this.selection.get(team)){
			if(c instanceof Character){
				Character o = (Character) c;
				//first we deal with o's elder group
				if(o.group!=null && o.group.size()>1){
					for(Character c1 : o.group)
						if(c1!=o)
							c1.group.remove(o);
				}
				// Then we create its new group
				o.group = new Vector<Character>();
				Vector<Case> waypoints = null;
				for(ActionObjet c1: this.selection.get(team)){
					if(c1==c)
						continue;
					if(c1 instanceof Character){
						o.group.add((Character)c1);
						//						System.out.println("Plateau line 507: " + (waypoints!=null) +" "+(c.c==c1.c)+" "+(((Character)c1).waypoints.size()>0));
						if(((Character)c1).waypoints!=null && c1.c == c.c && c1.getTarget()!=null && c1.getTarget().c==target.c){
							//							System.out.println("Plateau line 508 : copie d'une chemin");
							waypoints = ((Character) c1).waypoints;
						}
					}
				}
				o.setTarget(target, waypoints);
				o.secondaryTargets.clear();
			}
		}
	}
	public void updateSecondaryTarget(float x, float y, int team){
		//called when right click on the mouse
		Objet target = this.findTarget(x, y);
		if(target==null){
			target = new Checkpoint(this,x,y);
		}
		for(ActionObjet c:this.selection.get(team)){
			if(c instanceof Character){
				Character o = (Character) c;
				//first we deal with o's elder group
				if(o.group!=null && o.group.size()>1){
					for(Character c1 : o.group)
						if(c1!=o)
							c1.group.remove(o);
				}
				// Then we create its new group
				o.group = new Vector<Character>();
				for(ActionObjet c1: this.selection.get(team))
					if(c1 instanceof Character)
						o.group.add((Character)c1);
				o.secondaryTargets.add(target);
			}
		}
	}

	//calling method to the environment
	public Vector<Objet> getEnnemiesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o.getTeam()!=caller.getTeam() && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	public Vector<Objet> getAlliesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o!=caller && o.getTeam()==caller.getTeam() && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	public Vector<Objet> getWoundedAlliesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o!=caller && o.getTeam()==caller.getTeam() && o.lifePoints<o.maxLifePoints && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}	
	public Objet findTarget(float x, float y){
		Point point= new Point(x,y);
		Objet target = null;
		//looking for the object on the target
		for(Character i:this.characters){
			// looking amongst other characters
			if(i.selectionBox.contains(point)){
				target = i;
				break;
			}
		}
		if(target==null){
			for(Building i: this.buildings){
				// looking amongst natural object
				if(i.collisionBox.contains(point)){
					target = i;
					break;
				}
			}
		}	
		if(target==null){
			for(NaturalObjet i: naturalObjets){
				// looking amongst natural object
				if(i.collisionBox.contains(point)){
					target = i;
					break;
				}
			}
		}	
		return target;
	}

	//TODO : Here we handle inputs from each players
	public void update(Vector<InputObject> ims){
		Utils.triId(this.characters);

		collisionSwitch = !collisionSwitch;
		// 1 - Handling inputs 
		for(InputObject im : ims){
			// pour tous les inputs pass�s en argument on fait le traitement
			int player = im.player.id;
			// si on est client on ne g�re que son input

			// on g�re la s�lection des sorts (type firewall/ blessed area)
			this.handleSpellCasting(im, player);
			// on g�re c�t� serveur l'action bar et le click droit
			if(im.player==this.currentPlayer){
				this.handleMouseHover(im,player);
			}
			// Handling action bar
			this.handleActionBar(im,player);
			// Handling the right click
			this.handleRightClick(im,player);
			// handling only the current player
			//Handle minimap
			if(!this.isCastingSpell.get(player) && !this.hasCastSpell.get(player)){
				this.handleMinimap(im, player);
				this.handleSelection(im, player,players.get(player).getTeam());
			}

			//			if(player == this.currentPlayer.id){
			//				// ong�re le d�placement de la cam�ra et la s�lection
			//				if(!this.isCastingSpell.get(player) && !this.hasCastSpell.get(player)){
			//					this.handleView(im, player);
			//				}
			//			}
			// enfin on g�re le lancement des sorts
			//this.handleSpellsOnField(im, player, !g.inMultiplayer || g.host);
			this.handleSpellsOnField(im, player, true);

		} 
		if(Game.debugTimeSteps)
			System.out.println(" - plateau: fin input : " + (System.currentTimeMillis() - g.timeSteps));



	}

	private void handleMouseHover(InputObject im, int player) {
		for(Character c : this.characters){
			if(c.selectionBox.contains(im.xMouse, im.yMouse)){
				c.mouseOver = true;
			}
			else{
				c.mouseOver = false;
			}
		}
		for(Bonus c : this.bonus){
			if(c.selectionBox.contains(im.xMouse, im.yMouse)){
				c.mouseOver = true;
			}
			else{
				c.mouseOver = false;
			}
		}

	}

	public void updateIAOrders(){


		//Pour toute les IA : 
		for(Player p : this.players){
			if(p instanceof IAPlayer){
				updateIAOrders((IAPlayer) p);
			}
		}
	}

	public void updateIAOrders(IAPlayer p){
		//TODO : Update IA orders for a specific player
		p.commonUpdate();
		p.update();
	}

	public void updatePlateauState(){
		// 2 - For everyone
		//Sort by id



		this.collision();
		if(Game.debugTimeSteps)
			System.out.println(" - plateau: fin collision : " + (System.currentTimeMillis() - g.timeSteps));
		this.clean();
		if(Game.debugTimeSteps)
			System.out.println(" - plateau: fin clean : " + (System.currentTimeMillis() - g.timeSteps));			
		this.action();
		if(Game.debugTimeSteps)
			System.out.println(" - plateau: fin action : " + (System.currentTimeMillis() - g.timeSteps));



		// 3 - handling visibility
		this.updateVisibility();

		if(Game.debugTimeSteps)
			System.out.println(" - plateau: fin visibility : " + (System.currentTimeMillis() - g.timeSteps));

		// 4 - Update of the messages
		Vector<Message> toDelete = new Vector<Message>();
		toDelete.clear();
		for(Message m: this.messages.get(currentPlayer.id)){
			m.remainingTime-=1f;
			if(m.remainingTime<=0f)
				toDelete.add(m);
		}
		for(Message m:toDelete)
			this.messages.get(currentPlayer.id).remove(m);
		if(g.debugTimeSteps)
			System.out.println(" - plateau: fin message : " + (System.currentTimeMillis() - g.timeSteps));
	}

	private void updateSelection(InputObject im) {
		this.selection.get(im.player.id).clear();

		for(Integer i : im.selection){
			for(Character c: this.characters)
				if(c.id == i){
					this.selection.get(im.player.id).add(c);
				}
			for(Building c: this.buildings)
				if(c.id == i)
					this.selection.get(im.player.id).add(c);
		}

	}

	private void handleRightClick(InputObject im, int player) {
		if(im.pressedRightClick){
			//RALLY POINT
			if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingProduction){
				((BuildingProduction) this.selection.get(player).get(0)).rallyPoint = new Checkpoint(im.xMouse,im.yMouse);
			} else if(isCastingSpell.get(player)){
				isCastingSpell.set(player,false);
				castingSpell.set(player,-1);
			} else if(im.isPressedMAJ ){
				updateSecondaryTarget(im.xMouse,im.yMouse,player);
			} else {
				updateTarget(im.xMouse,im.yMouse,player);
			}
		}
		if(im.isPressedF){
			//STOP SELECTION
			for(ActionObjet c : this.selection.get(player)){
				if(c instanceof Character){
					((Character) c).stop();
				}
			}
		}
	}

	private void handleSpellsOnField(InputObject im, int player, boolean host) {
		if(im.pressedLeftClick && isCastingSpell.get(player)){
			if(host && this.selection.get(player).size()>0){
				Character c = (Character)this.selection.get(player).get(0); 
				Spell spell = c.spells.get(castingSpell.get(player));
				spell.launch(new Checkpoint(im.xMouse,im.yMouse),(Character)this.selection.get(player).get(0));
				c.spellsState.set(castingSpell.get(player),0f);
			}
			isCastingSpell.set(player,false);
			hasCastSpell.set(player, true);
			castingSpell.set(player,-1);
		}

		if(hasCastSpell.get(player) && !im.leftClick)
			hasCastSpell.set(player,false);
	}



	private void handleActionBar(InputObject im, int player) {
		if(im.isPressedProd0 || im.isPressedProd1 || im.isPressedProd2 || im.isPressedProd3 || im.isPressedESC){
			if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingAction){
				if(im.isPressedProd0)
					((BuildingAction) this.selection.get(player).get(0)).product(0);
				if(im.isPressedProd1)
					((BuildingAction) this.selection.get(player).get(0)).product(1);
				if(im.isPressedProd2)
					((BuildingAction) this.selection.get(player).get(0)).product(2);
				if(im.isPressedProd3)
					((BuildingAction) this.selection.get(player).get(0)).product(3);
				if(im.isPressedESC)
					((BuildingAction) this.selection.get(player).get(0)).removeProd();
			}
			else if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof Character){
				int number = -1;
				if(im.isPressedProd0)
					number = 0;
				if(im.isPressedProd1)
					number = 1;
				if(im.isPressedProd2)
					number = 2;
				if(im.isPressedProd3)
					number = 3;
				if(im.isPressedESC){
					isCastingSpell.set(player,false);
					castingSpell.set(player,-1);
				}
				Character c = ((Character) this.selection.get(player).get(0));
				if(-1!=number && number<c.spells.size() && c.spellsState.get(number)>=c.spells.get(number).chargeTime){
					if(!c.spells.get(number).needToClick){
						c.spells.get(number).launch(c, c);
					}
				}
			}
		}

	}

	private void handleSpellCasting(InputObject im, int player){
		if(im.isPressedProd0 || im.isPressedProd1 || im.isPressedProd2 || im.isPressedProd3 || im.isPressedESC){
			if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof Character){
				int number = -1;
				if(im.isPressedProd0)
					number = 0;
				if(im.isPressedProd1)
					number = 1;
				if(im.isPressedProd2)
					number = 2;
				if(im.isPressedProd3)
					number = 3;
				if(im.isPressedESC){
					isCastingSpell.set(player,false);
					castingSpell.set(player,-1);
				}
				Character c = ((Character) this.selection.get(player).get(0));
				c.castSpell(number, player);
			}
		}
	}

	// METHODS ONLY CALLED BY THE CURRENT PLAYER

	// updating rectangle
	public void handleView(InputObject im, int player){
		// Handle the display (camera movement & minimap)

		// camera movement
		if(!isCastingSpell.get(player) && player==this.currentPlayer.id && this.rectangleSelection.get(player)==null && !im.leftClick){
			// Move camera according to inputs :
			if((im.isPressedUP || (!im.isPressedA && im.yMouse<Ycam+5))&&Ycam>-g.resY/2){
				Ycam -= 20;
			}
			if((im.isPressedDOWN || (!im.isPressedA && im.yMouse>Ycam+g.resY-5)) && Ycam<this.maxY-g.resY/2){
				Ycam +=20;
			}
			if((im.isPressedLEFT|| (!im.isPressedA && im.xMouse<Xcam+5)) && Xcam>-g.resX/2 ){
				Xcam -=20;
			}
			if((im.isPressedRIGHT || (!im.isPressedA && im.xMouse>Xcam+g.resX-5))&& Xcam<this.maxX-g.resX/2){
				Xcam += 20;
			}
			//Displaying the selected group
			for(int to=0; to<10; to++){
				if(im.isPressedNumPad[to]){
					if(this.players.get(player).groupSelection == to && this.players.get(player).groups.get(to).size()>0){
						float xmoy=this.players.get(player).groups.get(to).get(0).getX();
						float ymoy=this.players.get(player).groups.get(to).get(0).getY();
						this.Xcam = (int) Math.min(maxX-g.resX/2f, Math.max(-g.resX/2f, xmoy-g.resX/2f));
						this.Ycam = (int) Math.min(maxY-g.resY/2f, Math.max(-g.resY/2f, ymoy-g.resY/2f));
					}
				}
			}
		}
		// display for the bottom bar
		BottomBar bb = currentPlayer.bottomBar;
		float relativeXMouse = (im.xMouse-Xcam);
		float relativeYMouse = (im.yMouse-Ycam);
		if(relativeXMouse>bb.action.x && relativeXMouse<bb.action.x+bb.action.icoSizeX && relativeYMouse>bb.action.y && relativeYMouse<bb.action.y+bb.action.sizeY){
			int mouseOnItem = (int)((relativeYMouse-bb.action.y)/(bb.action.sizeY/bb.action.prodIconNb));
			bb.action.toDrawDescription[0]=false;
			bb.action.toDrawDescription[1]=false;
			bb.action.toDrawDescription[2]=false;
			bb.action.toDrawDescription[3]=false;
			if(mouseOnItem>=0 && mouseOnItem<4)
				bb.action.toDrawDescription[mouseOnItem]=true;
		}else{
			bb.action.toDrawDescription[0]=false;
			bb.action.toDrawDescription[1]=false;
			bb.action.toDrawDescription[2]=false;
			bb.action.toDrawDescription[3]=false;
		}
	}

	public void handleMinimap(InputObject im, int player){
		if(im.isPressedA){
			BottomBar b = this.players.get(player).bottomBar;
			b.minimap.toDraw = true;
			if(im.leftClick && player==this.currentPlayer.id){
				// Put camera where the click happened
				Xcam = (int) ((im.xMouse)-g.resX/2f);
				Ycam = (int) ((im.yMouse)-g.resY/2f);
			}
		}
		else{
			BottomBar b = this.players.get(player).bottomBar;
			b.minimap.toDraw = false;
		}
	}
	// drawing fog of war method
	public void drawFogOfWar(Graphics g){
		Vector<Objet> visibleObjet = new Vector<Objet>();
		visibleObjet = this.getInCamObjets(this.currentPlayer.getTeam());
		float resX = this.g.resX;
		float resY = this.g.resY;
		this.gf.setColor(new Color(255,255,255));
		gf.fillRect(0, 0, resX, resX);
		this.gf.setColor(new Color(50,50,50));
		float xmin = Math.max(0,-Xcam);
		float ymin = Math.max(0,-Ycam);
		float xmax = Math.min(resX,maxX-Xcam);
		float ymax = Math.min(resY,maxY-Ycam);
		gf.fillRect(xmin,ymin,xmax-xmin,ymax-ymin);
		gf.setColor(Color.white);
		for(Objet o:visibleObjet){
			gf.fillOval(o.x-Xcam-o.sight,o.y-Ycam-o.sight,o.sight*2f,o.sight*2f);
		}
		gf.flush();
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.drawImage(fog,Xcam,Ycam);		
		g.setDrawMode(Graphics.MODE_NORMAL);
	}
	// Handling visibility
	private Vector<Objet> getInCamObjets(int team) {
		//return all objects from a team in the camera view 
		Vector<Objet> obj = new Vector<Objet>();
		for(Character c: this.characters)
			if(c.getTeam()==team && c.visibleByCamera)
				obj.add(c);
		for(Building c: this.buildings)
			if(c.getTeam()==team && c.visibleByCamera)
				obj.add(c);
		return obj;
	}
	public boolean isVisibleByPlayer(int team, Objet objet){
		if(objet.getTeam()==team)
			return true;
		float r = objet.collisionBox.getBoundingCircleRadius();
		for(Character c: this.characters)
			if(c.getTeam()==team && Utils.distance(c, objet)<c.sight+r)
				return true;
		for(Building b: this.buildings)
			if(b.getTeam()==team && Utils.distance(b,  objet)<b.sight+r)
				return true;
		return false;
	}
	public boolean isVisibleByCamera(Objet objet){
		return objet.x+objet.sight>Xcam && objet.x-objet.sight<Xcam+g.resX && objet.y+objet.sight>Ycam && objet.y-objet.sight<Ycam+g.resY;
	}
	private void updateVisibility() {
		for(Character c:this.characters){
			c.visibleByCurrentPlayer = this.isVisibleByPlayer(this.currentPlayer.getTeam(), c);
			c.visibleByCamera =this.isVisibleByCamera(c);
		}
		for(Building b:this.buildings){
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.currentPlayer.getTeam(), b);
			b.visibleByCamera =this.isVisibleByCamera(b);
		}
		for(Bullet b:this.bullets){
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.currentPlayer.getTeam(), b);
			b.visibleByCamera =this.isVisibleByCamera(b);
		}
		for(SpellEffect b:this.spells){
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.currentPlayer.getTeam(), b);
			b.visibleByCamera =this.isVisibleByCamera(b);
		}
	}
	// handling selection
	public void handleSelection(InputObject im, int player, int team){
		// Handling groups of units
		for(int to=0; to<10; to++){
			if(im.isPressedNumPad[to]){
				if(isCastingSpell.get(player)){
					isCastingSpell.set(player, false);
					castingSpell.set(player,-1);
				}
				if(im.isPressedCTRL){
					// Creating a new group made of the selection
					this.players.get(player).groups.get(to).clear();
					for(ActionObjet c: this.selection.get(player))
						this.players.get(player).groups.get(to).add(c);
				} else if(im.isPressedMAJ){
					// Adding the current selection to the group
					for(ActionObjet c: this.selection.get(player))
						this.players.get(player).groups.get(to).add(c);
				} else {
					this.selection.get(player).clear();
					for(ActionObjet c: this.players.get(player).groups.get(to))
						this.selection.get(player).add(c);
				}
				this.players.get(player).groupSelection = to;
			}
		}
		// Cleaning the rectangle and buffer if mouse is released
		if(!im.leftClick){
			this.rectangleSelection.set(player, null);
			this.inRectangle.clear();
		}
		// Handling hotkeys for gestion of selection
		if(im.isPressedTAB){
			if(this.selection.get(player).size()>0){
				Utils.switchTriName(this.selection.get(player));
				if(this.players.get(player).groupSelection!=-1)
					Utils.switchTriName(this.players.get(player).groups.get(this.players.get(player).groupSelection));
			}
		}
		//update the rectangle
		if(im.leftClick){
			// As long as the button is pressed, the selection is updated
			this.updateRectangle(im,player);
		}
		// we update the selection according to the rectangle wherever is the mouse
		if(im.pressedLeftClick && !im.isPressedMAJ){
			this.clearSelection(player);
		}
		if(!im.isPressedCTRL){
			this.updateSelection(rectangleSelection.get(player), player, team);
		} else {
			this.updateSelectionCTRL(rectangleSelection.get(player), player, team);
		}

		// Update the selections of the players
		this.players.get(player).selection.clear();
		for(ActionObjet c: this.selection.get(player))
			this.players.get(player).selection.addElement(c);

	}
	private void updateRectangle(InputObject im, int player) {
		if(im.isPressedA){
			return;
		}
		if(this.rectangleSelection.get(player)==null || im.isPressedCTRL){
			recX.set(player, (float)im.xMouse) ;
			recY.set(player, (float)im.yMouse) ;
			rectangleSelection.set(player,new Rectangle(recX.get(player),recX.get(player),0.1f,0.1f)) ;
		}
		rectangleSelection.get(player).setBounds( (float)Math.min(recX.get(player),im.xMouse), (float)Math.min(recY.get(player), im.yMouse),
				(float)Math.abs(im.xMouse-recX.get(player))+0.1f, (float)Math.abs(im.yMouse-recY.get(player))+0.1f);
	}
	public void updateSelection(Rectangle select,int player, int team){
		if(select!=null){
			for(ActionObjet a : this.inRectangle){
				this.selection.get(player).remove(a);
			}
			this.inRectangle.clear();
			for(Character o: characters){
				if((o.selectionBox.intersects(select)|| o.selectionBox.contains(select) || select.contains(o.selectionBox) )&& o.getTeam()==team){
					this.selection.get(player).add(o);
					this.inRectangle.addElement(o);
					if(Math.max(select.getWidth(), select.getHeight())<2f){
						break;
					}

				}
			}
			if(this.selection.get(player).size()==0){
				for(Building o: buildings){
					if(o.selectionBox.intersects(select) && o.getTeam()==team){
						this.selection.get(player).add(o);
						this.inRectangle.addElement(o);
					}
				}
			}
			this.players.get(player).groupSelection = -1;
		}

	}
	public void updateSelectionCTRL(Rectangle select,int player, int team){
		if(select!=null){
			this.clearSelection(player);
			//handling the selection
			for(Character o: characters){
				if((o.selectionBox.intersects(select)|| o.selectionBox.contains(select)) && o.getTeam()==team){
					//add character to team selection
					this.addSelection(o, player);
				}
			}

			if(this.toAddSelection.get(player).size()==0){

				for(Building o: buildings){
					if(o.selectionBox.intersects(select) && o.getTeam()==team){
						//add character to team selection
						this.addSelection(o, player);
					}
				}
			}
			Vector<Objet> visibles = this.getInCamObjets(player);
			if(this.toAddSelection.get(player).size()==1){
				ActionObjet ao = this.toAddSelection.get(player).get(0);
				if(ao instanceof Character){
					for(Character o: characters){
						if(o.getTeam()==team && o.name==ao.name && visibles.contains(o)){
							//add character to team selection
							this.addSelection(o, player);
						}
					}
				} else if(ao instanceof Building){
					for(Building o: buildings){
						if(o.getTeam()==team && o.name==ao.name && visibles.contains(o)){
							//add character to team selection
							this.addSelection(o, player);
						}
					}
				}
			}
			this.players.get(team).groupSelection = -1;
		}
	}
	public void clearSelection(int player){
		this.selection.get(player).clear();
	}

	// handling messages
	public void addMessage(Message m, int player){
		if(this.messages.get(player).size()>19)
			return;
		this.messages.get(player).add(0, m);
	}
	//MULTIPLAYING
	public String toStringArray(){

		
		int id_charac = 0;
		String s = "3P!";
		//IDS
		s+=this.g.round;
		s+="!";
		s+=this.g.idChar;
		s+="!";
		//Time to restart the game
		s+=this.g.clock.getCurrentTime()+1e9;
		s+="!";
		//We want to send the content of plateau

		//CHARACTERS
		while(id_charac<this.characters.size()){
			s+=this.characters.get(id_charac).toString();
			s+="|";
			id_charac++;
		}

		s+="!";
		return s;
	}

	public void parse(String s){

		//APPLY ACTION ON ALL CONCERNED OBJECTS
		//GET ARRAY OF CHARACTERS,BUILDING,BULLET
		//System.out.println(s);
		if(s!=null && s!=""){
			String[] u = s.split("!");
			//Take care of id sent
			parseCharacter(u[4]);
			this.g.idChar = Integer.parseInt(u[2]);
			//Activate restart process
			this.g.timeRestart = Long.parseLong(u[3]);
			this.g.restartProcess = true;
		}

		// Update groups 

		Vector<Character> group  = new Vector<Character>();

		for(Character c : this.characters){
			c.group.clear();
			if(c.target instanceof Checkpoint){
				group  = new Vector<Character>();
				group.add(c);
				for(Character d  : this.characters){
					if(d.target instanceof Checkpoint && c.id!=d.id && c.getTeam()==d.getTeam()){
						if(d.target.x == c.target.x && d.target.y == c.target.y ){
							group.addElement(d);
						}
					}
				}
				c.group.addAll(group);
			}
		}

	}
	@Deprecated
	public String toStringEx(){
		String s = "1!";

		//IDS
		s+=this.g.idPaquetSend;
		s+="!";
		//PLAYERS

		for(Player p: players){
			s+=p;
			s+="|";
		}
		//CHARACTER
		s +="!";
		for(Character c : this.characters){
			s+=c;
			s+="|";
		}
		//BUILDING
		s+="!";
		for(Building b : this.buildings){
			s+=b;
			s+="|";
		}
		//BULLETS
		s+="!";
		for(Bullet b : this.bullets){
			s+=b;
			s+="|";
		}
		s+=" ! ";
		for(SpellEffect b : this.spells){
			s+=b;
			s+="|";
		}
		s+="!";
		return s;
	}

	@Deprecated
	public void parseEx(String s){

		//APPLY ACTION ON ALL CONCERNED OBJECTS
		//GET ARRAY OF PLAYER,CHARACTER,BUILDING,BULLET*
		//System.out.println(s);
		if(s!=null && s!=""){
			String[] u = s.split("!");
			//Take care of id sent
			this.g.idPaquetReceived = Integer.parseInt(u[1]);
			//Take care of player
			this.currentPlayer.parsePlayer(u[2]);
			//double chrono1 = System.nanoTime();
			parseCharacter(u[3]);
			//System.out.println("bullets : "+(System.nanoTime()-chrono1));
			parseBuilding(u[4]);
			//double chrono2 = System.nanoTime();
			parseBullet(u[5]);
			//System.out.println("bullets : "+(System.nanoTime()-chrono2));
			parseSpell(u[6]);
		}

	}

	public void parseBuilding(String s){
		String[] u = s.split("\\|");
		//Loop over each Building
		Building bul=null;
		int finish = u.length;
		if(!u[u.length-1].contains("id")){
			finish--;
		}
		// For all buildings in received message
		for(int i =0;i<finish;i++){
			HashMap<String,String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			// Find corresponding Building in plateau
			bul = this.getBuildingById(idTest);

			bul.parse(hs);

		}


	}


	public Character getCharacterById(int id){
		for(Character cha : this.characters){
			if(id==cha.id){
				return cha;
			}
		}
		//		for(Character cha : this.population.characters){
		//			if(id==cha.id){
		//				return cha;
		//			}
		//		}
		return null;
	}

	public Bullet getBulletById(int id){

		for(Bullet cha : this.bullets){
			if(id==cha.id){
				return cha;
			}
		}
		return null;
	}

	public Building getBuildingById(int id){
		for(Building cha : this.buildings){
			if(id==cha.id){
				return cha;
			}
		}
		return null;
	}

	private SpellEffect getSpellEffectById(int id) {
		for(SpellEffect cha : this.spells){
			if(id==cha.id){
				return cha;
			}
		}
		return null;
	}

	public void parseCharacter(String s){
		//SPLIT SELON |
		
		for(Character c : this.characters){
			c.setTarget(null, null);
			c.group.clear();
		}
		
		
		String[] u = s.split("\\|");
		// LOOP OVER EACH CHARACTER
		Character cha=null;
		int finish = u.length;
		if(!u[u.length-1].contains("id")){
			finish--;
		}

//		//Clear all characters 
//		while(this.characters.size()>0){
//			Character toErase = this.characters.get(0);
//			toErase.lifePoints = -1f;
//			toErase.destroy();
//			this.removeCharacter(toErase);
//			this.characters.remove(toErase);
//		}
		
//		this.characters.clear();

		for(int i =0;i<finish;i++){
			//FIND CONCERNED CHARACTER
			HashMap<String,String> hs = Objet.preParse(u[i]);
			int idTest= Integer.parseInt(hs.get("id"));
			cha = this.getCharacterById(idTest);
			if(cha == null){
				cha = Character.createNewCharacter(hs, g);
				System.out.println("Create new character");
				
			}
			if(cha!=null){
				cha.parse(hs);
				cha.toKeep = true;
			}
		}

		
		//Erase characters who didn't give any news
		
		for(Character c : this.characters){
			if(!c.toKeep){
				System.out.println("Destroyed " + c.id);
				c.destroy();
			}
		}
		//Clean plateau
		this.clean();
		
	}

	public void parseBullet(String s){
		String[] u = s.split("\\|");
		//Loop over each bullet
		Bullet bul=null;
		int finish = u.length;
		if(!u[u.length-1].contains("id")){
			finish--;
		}
		// For all bullets in received message
		for(int i =0;i<finish;i++){
			HashMap<String,String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			// Find corresponding bullet in plateau
			bul = this.getBulletById(idTest);
			//Create bullet if not in plateau
			if(bul==null){
				bul = Bullet.createNewBullet(hs, g);
			}
			bul.parse(hs);
			bul.toKeep = true;	
		}
		//Destroy bullets who didn't give any news
		for(Bullet b : this.bullets){
			if(!b.toKeep){
				b.setLifePoints(-1f);
			}else{
				b.toKeep = false;
			}
		}
	}

	@Deprecated
	public void parseSpell(String s){
		String[] u = s.split("\\|");
		//Loop over each spells
		SpellEffect bul=null;
		int finish = u.length;
		if(!u[u.length-1].contains("id")){
			finish--;
		}
		// For all spellEffects in received message
		for(int i =0;i<finish;i++){
			HashMap<String,String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			// Find corresponding spellEffect in plateau
			bul = this.getSpellEffectById(idTest);
			//Create spellEffect if not in plateau
			if(bul==null){
				bul = SpellEffect.createNewSpell(hs, g);
			}
			bul.parse(hs);
			bul.toKeep = true;	
		}
		//Destroy spellEffects who didn't give any news
		for(SpellEffect b : this.spells){
			if(!b.toKeep){
				b.setLifePoints(-1f);
			}else{
				b.toKeep = false;
			}
		}
	}




}



