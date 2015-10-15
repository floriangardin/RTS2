package model;


import java.util.HashMap;
import java.util.Vector;

import multiplaying.InputModel;
import multiplaying.OutputModel;
import multiplaying.OutputModel.OutputBuilding;
import multiplaying.OutputModel.OutputBullet;
import multiplaying.OutputModel.OutputChar;
import multiplaying.OutputModel.OutputSpell;

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
import buildings.Building;
import buildings.BuildingAction;
import buildings.BuildingProduction;
import buildings.BuildingTech;
import bullets.Arrow;
import bullets.Bullet;
import bullets.Fireball;
import display.BottomBar;
import display.Message;

public class Plateau {

	public Game g;
	public int nTeams;
	public float maxX ;
	public float maxY ;
	// Camera 
	public float Xcam;
	public float Ycam;
	// fog of war
	public Image fog;
	public Graphics gf;

	//
	public String currentString ;


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

	public Vector<NaturalObjet> naturalObjets ;
	public Vector<NaturalObjet> toAddNaturalObjets;
	public Vector<NaturalObjet> toRemoveNaturalObjets;

	public Vector<Vector<ActionObjet>> selection;
	public Vector<Vector<ActionObjet>> toAddSelection;
	public Vector<Vector<ActionObjet>> toRemoveSelection ;

	public Vector<SpellEffect> spells;
	public Vector<SpellEffect> toAddSpells;
	public Vector<SpellEffect> toRemoveSpells;

	public Rectangle rectangleSelection;
	public float recX ;
	public float recY ;
	public Vector<ActionObjet> inRectangle ;

	public Vector<Boolean> isCastingSpell;
	public Vector<Boolean> hasCastSpell;
	public Vector<Integer> castingSpell;

	public Vector<Vector<Message>> messages;

	public MapGrid mapGrid;

	public Plateau(float maxX,float maxY,Game g){
		int nTeams = 2;
		this.g = g;
		this.mapGrid = new MapGrid(0f,maxX,0f,maxY);
		//GENERAL
		this.nTeams = nTeams;
		this.maxX= maxX;
		this.maxY = maxY;
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
		//ENEMYGENERATOR
		this.buildings = new Vector<Building>();
		this.toAddBuildings = new Vector<Building>();
		this.toRemoveBuildings = new Vector<Building>();
		//SELECTION
		this.selection = new Vector<Vector<ActionObjet>>();
		this.toAddSelection = new Vector<Vector<ActionObjet>>();
		this.toRemoveSelection = new Vector<Vector<ActionObjet>>();
		this.inRectangle = new Vector<ActionObjet>();
		//MESSAGES
		this.messages = new Vector<Vector<Message>>();

		//CASTING SPELLS
		this.isCastingSpell = new Vector<Boolean>();
		this.hasCastSpell = new Vector<Boolean>();
		this.castingSpell = new Vector<Integer>();
		for(int i =0; i<=nTeams;i++){
			this.selection.addElement(new Vector<ActionObjet>());
			this.toAddSelection.addElement(new Vector<ActionObjet>());
			this.toRemoveSelection.addElement(new Vector<ActionObjet>());
			this.isCastingSpell.addElement(false);
			this.hasCastSpell.addElement(false);
			this.castingSpell.addElement(-1);
			this.messages.addElement(new Vector<Message>());
		}
		try {
			//			System.out.println(this.g.resX+" "+this.g.resY);
			this.fog = new Image((int)this.g.resX,(int)this.g.resY);
			this.gf = fog.getGraphics();
		} catch (SlickException | RuntimeException e) {
		}

		//UPDATING GAME
		this.g.players = new Vector<Player>();
		this.g.players.add(new Player(this,0,0));
		this.g.players.add(new Player(this,1,0));
		this.g.players.add(new Player(this,2,0));

	}

	// functions that handle buffers
	public void addCharacterObjets(Character o){
		toAddCharacters.addElement(o);
	}
	private void removeCharacter(Character o){
		toRemoveCharacters.addElement(o);
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
		for(int i=0;i<=nTeams;i++){
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
				for(ActionObjet c: this.g.players.get(i).groups.get(k)){
					if(!c.isAlive())
						toDelete.add(c);
				}
				for(ActionObjet c: toDelete){
					this.g.players.get(i).groups.get(k).remove(c);
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
		for(int i = 0;i<=nTeams;i++){
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
		for(Character o : characters){
			// Handle collision between actionObjets and action objects
			for(Character i:characters){
				if(i.collisionBox.intersects(o.collisionBox) && i!=o){
					i.collision(o);
					o.collision(i);
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
				if(i.collisionBox.intersects(o.collisionBox)){
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
			if(o.team!=caller.team && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	public Vector<Objet> getAlliesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o!=caller && o.team==caller.team && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	public Vector<Objet> getWoundedAlliesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o!=caller && o.team==caller.team && o.lifePoints<o.maxLifePoints && o.collisionBox.intersects(caller.sightBox)){
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
			if(i.collisionBox.contains(point)){
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


	public void update(Vector<InputModel> ims){
		// 1 - Handling inputs 
		InputModel im;
		for(int player = 1; player<=nTeams; player++){
			if(!g.host && player!=g.currentPlayer)
				continue;
			im = null;
			for(InputModel inp : ims)
				if(inp.team==player)
					im = inp;
			if(im!=null){
				if(g.host){
					// Handling action bar
					this.handleActionBar(im,player);
					// Handling the right click
					this.handleRightClick(im, player);
					// handling only the current player
				}
				if(player == this.g.currentPlayer && !this.isCastingSpell.get(player) && !this.hasCastSpell.get(player)){
					this.handleView(im, player);
					this.handleSelection(im, player);
				}
				if(g.host){
					// Handling the spell on the field
					this.handleSpellsOnField(im, player);
				}
			}
		}

		// 2 - Only for host - Collision, Action, Cleaning
		if(g.host)
			this.collision();
		this.clean();
		if(g.host)			
			this.action();

		if(g.host){
			this.currentString = this.toString();
		} else {
			if(currentString!=null){
				this.parse(currentString);
				currentString= null;
			}
		}



		// 3 - handling visibility
		this.updateVisibility();

		// 4 - Update of the messages
		//		Vector<Message> toDelete = new Vector<Message>();
		//		toDelete.clear();
		//		for(Message m: this.messages.get(player)){
		//			m.remainingTime-=1f;
		//			if(m.remainingTime<=0f)
		//				toDelete.add(m);
		//		}
		//		for(Message m:toDelete)
		//			this.messages.get(player).remove(m);
	}


	private void handleRightClick(InputModel im, int player) {
		if(im.isPressedRightClick){
			//RALLY POINT
			if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingProduction){
				((BuildingProduction) this.selection.get(player).get(0)).rallyPoint = new Checkpoint(im.xMouse,im.yMouse);
			} else if(isCastingSpell.get(player)){
				isCastingSpell.set(player,false);
				castingSpell.set(player,-1);
			} else if(im.isPressedMAJ ){
				if(!im.isPressedA)
					updateSecondaryTarget(im.xMouse,im.yMouse,player);
			} else {
				if(!im.isPressedA)
					updateTarget(im.xMouse,im.yMouse,player);
			}
		}
	}

	private void handleSpellsOnField(InputModel im, int player) {
		if(im.isPressedLeftClick && isCastingSpell.get(player)){
			if(this.g.players.get(player).selection.size()>0){
				Character c = (Character)this.g.players.get(player).selection.get(0); 
				Spell spell = c.spells.get(castingSpell.get(player));
				spell.launch(new Checkpoint(im.xMouse,im.yMouse),(Character)this.g.players.get(player).selection.get(0));
				c.spellsState.set(castingSpell.get(player),0f);
			}
			isCastingSpell.set(player,false);
			hasCastSpell.set(player, true);
			castingSpell.set(player,-1);
		}
		if(hasCastSpell.get(player) && !im.leftClick)
			hasCastSpell.set(player,false);
	}

	private void handleActionBar(InputModel im, int player) {
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
					if(c.spells.get(number).needToClick){
						isCastingSpell.set(player,true);
						castingSpell.set(player,number);
					} else {
						c.spells.get(number).launch(c, c);
					}
				}
			}
		}

	}

	// METHODS ONLY CALLED BY THE CURRENT PLAYER

	// updating rectangle
	public void handleView(InputModel im, int player){
		// Handle the display (camera movement & minimap)

		// camera movement
		if(!isCastingSpell.get(player) && player==this.g.currentPlayer && this.rectangleSelection==null && !im.leftClick){
			// Move camera according to inputs :
			if((im.isPressedUP || im.yMouse<im.Ycam+5)&&im.Ycam>-im.resY/2){
				Ycam -= 20;
			}
			if((im.isPressedDOWN || im.yMouse>im.Ycam+im.resY-5) && im.Ycam<this.maxY-im.resY/2){
				Ycam +=20;
			}
			if((im.isPressedLEFT|| im.xMouse<im.Xcam+5) && im.Xcam>-im.resX/2 ){
				Xcam -=20;
			}
			if((im.isPressedRIGHT || im.xMouse>im.Xcam+im.resX-5)&& im.Xcam<this.maxX-im.resX/2){
				Xcam += 20;
			}
			//Displaying the selected group
			for(int to=0; to<10; to++){
				if(im.isPressedNumPad[to]){
					if(this.g.players.get(player).groupSelection == to && this.g.players.get(player).groups.get(to).size()>0){
						float xmoy=this.g.players.get(player).groups.get(to).get(0).getX();
						float ymoy=this.g.players.get(player).groups.get(to).get(0).getY();
						this.Xcam = Math.min(maxX-im.resX/2f, Math.max(-im.resX/2f, xmoy-im.resX/2f));
						this.Ycam = Math.min(maxY-im.resY/2f, Math.max(-im.resY/2f, ymoy-im.resY/2f));
					}
				}
			}
		}
		// minimap
		if(im.isPressedA){
			BottomBar b = this.g.players.get(player).bottomBar;
			b.minimap.toDraw = true;
			if(im.leftClick && player==this.g.currentPlayer && (im.xMouse-im.Xcam)>b.minimap.startX && (im.xMouse-im.Xcam)<
					b.minimap.startX+b.minimap.w && this.rectangleSelection==null){
				// Put camera where the click happened
				Xcam = (int)Math.floor((im.xMouse-im.Xcam-b.minimap.startX)/b.minimap.rw)-im.resX/2f;
				Ycam = (int)Math.floor((im.yMouse-im.Ycam-b.minimap.startY)/b.minimap.rh)-im.resY/2f;

			}
			if(im.rightClick && player==this.g.currentPlayer && (im.xMouse-im.Xcam)>b.minimap.startX && (im.xMouse-im.Xcam)<
					b.minimap.startX+b.minimap.w && this.rectangleSelection==null){
				// Handle right click
				if(im.isPressedMAJ){
					updateSecondaryTarget((int)Math.floor((im.xMouse-im.Xcam-b.minimap.startX)/b.minimap.rw),(int)Math.floor((im.yMouse-im.Ycam-b.minimap.startY)/b.minimap.rh),player);
				} else {				
					updateTarget((int)Math.floor((im.xMouse-im.Xcam-b.minimap.startX)/b.minimap.rw),(int)Math.floor((im.yMouse-im.Ycam-b.minimap.startY)/b.minimap.rh),player);
				}

			}
		}
		else{
			BottomBar b = this.g.players.get(player).bottomBar;
			b.minimap.toDraw = false;
		}
		// display for the bottom bar
		BottomBar bb = g.players.get(g.currentPlayer).bottomBar;
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
	// drawing fog of war method
	public void drawFogOfWar(Graphics g){
		Vector<Objet> visibleObjet = new Vector<Objet>();
		visibleObjet = this.getInCamObjets(this.g.currentPlayer);
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
			if(c.team==team && c.visibleByCamera)
				obj.add(c);
		for(Building c: this.buildings)
			if(c.team==team && c.visibleByCamera)
				obj.add(c);
		return obj;
	}
	public boolean isVisibleByPlayer(int player, Objet objet){
		if(objet.team==player)
			return true;
		float r = objet.collisionBox.getBoundingCircleRadius();
		for(Character c: this.characters)
			if(c.team==player && Utils.distance(c, objet)<c.sight+r)
				return true;
		for(Building b: this.buildings)
			if(b.team==player && Utils.distance(b,  objet)<b.sight+r)
				return true;
		return false;
	}
	public boolean isVisibleByCamera(Objet objet){
		return objet.x+objet.sight>Xcam && objet.x-objet.sight<Xcam+g.resX && objet.y+objet.sight>Ycam && objet.y-objet.sight<Ycam+g.resY;
	}
	private void updateVisibility() {
		for(Character c:this.characters){
			c.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, c);
			c.visibleByCamera =this.isVisibleByCamera(c);
		}
		for(Building b:this.buildings){
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, b);
			b.visibleByCamera =this.isVisibleByCamera(b);
		}
		for(Bullet b:this.bullets){
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, b);
			b.visibleByCamera =this.isVisibleByCamera(b);
		}
		for(SpellEffect b:this.spells){
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, b);
			b.visibleByCamera =this.isVisibleByCamera(b);
		}
	}
	// handling selection
	public void handleSelection(InputModel im, int player){
		// Handling groups of units
		for(int to=0; to<10; to++){
			if(im.isPressedNumPad[to]){
				if(isCastingSpell.get(player)){
					isCastingSpell.set(player, false);
					castingSpell.set(player,-1);
				}
				if(im.isPressedCTRL){
					// Creating a new group made of the selection
					this.g.players.get(player).groups.get(to).clear();
					for(ActionObjet c: this.selection.get(player))
						this.g.players.get(player).groups.get(to).add(c);
				} else if(im.isPressedMAJ){
					// Adding the current selection to the group
					for(ActionObjet c: this.selection.get(player))
						this.g.players.get(player).groups.get(to).add(c);
				} else {
					this.selection.get(player).clear();
					for(ActionObjet c: this.g.players.get(player).groups.get(to))
						this.selection.get(player).add(c);
				}
				this.g.players.get(player).groupSelection = to;
			}
		}
		// Cleaning the rectangle and buffer if mouse is released
		if(!im.leftClick){
			this.rectangleSelection = null;
			this.inRectangle.clear();
		}
		// Handling hotkeys for gestion of selection
		if(im.isPressedTAB){
			if(this.selection.get(player).size()>0){
				Utils.switchTriName(this.selection.get(player));
				if(this.g.players.get(player).groupSelection!=-1)
					Utils.switchTriName(this.g.players.get(player).groups.get(this.g.players.get(player).groupSelection));
			}
		}
		//update the rectangle
		if(im.leftClick){
			// As long as the button is pressed, the selection is updated
			this.updateRectangle(im,player);
		}
		// we update the selection according to the rectangle wherever is the mouse
		if(im.isPressedLeftClick && !im.isPressedMAJ){
			this.clearSelection(player);
		}
		if(!im.isPressedCTRL){
			this.updateSelection(rectangleSelection, player);
		} else {
			this.updateSelectionCTRL(rectangleSelection, player);
		}

		// Update the selections of the players
		this.g.players.get(player).selection.clear();
		for(ActionObjet c: this.selection.get(player))
			this.g.players.get(player).selection.addElement(c);

	}
	private void updateRectangle(InputModel im, int player) {
		if(im.isPressedA){
			return;
		}
		if(rectangleSelection==null || im.isPressedCTRL){
			recX = (float)im.xMouse;
			recY = (float)im.yMouse;
			rectangleSelection = new Rectangle(recX,recY,0.1f,0.1f);
		}
		rectangleSelection.setBounds( (float)Math.min(recX,im.xMouse), (float)Math.min(recY, im.yMouse),
				(float)Math.abs(im.xMouse-recX)+0.1f, (float)Math.abs(im.yMouse-recY)+0.1f);
	}
	public void updateSelection(Rectangle select,int team){
		if(select!=null){
			for(ActionObjet a : this.inRectangle){
				this.selection.get(team).remove(a);
			}
			this.inRectangle.clear();
			for(Character o: characters){
				if(o.collisionBox.intersects(select) && o.team==team){
					this.selection.get(team).add(o);
					this.inRectangle.addElement(o);
				}
			}
			if(this.toAddSelection.get(team).size()==0){
				for(Building o: buildings){
					if(o.collisionBox.intersects(select) && o.team==team){
						this.selection.get(team).add(o);
						this.inRectangle.addElement(o);
					}
				}
			}
			this.g.players.get(team).groupSelection = -1;
		}

	}
	public void updateSelectionCTRL(Rectangle select,int team){
		if(select!=null){
			this.clearSelection(team);
			//handling the selection
			for(Character o: characters){
				if(o.collisionBox.intersects(select) && o.team==team){
					//add character to team selection
					this.addSelection(o, team);
				}
			}

			if(this.toAddSelection.get(team).size()==0){

				for(Building o: buildings){
					if(o.collisionBox.intersects(select) && o.team==team){
						//add character to team selection
						this.addSelection(o, team);
					}
				}
			}
			Vector<Objet> visibles = this.getInCamObjets(team);
			if(this.toAddSelection.get(team).size()==1){
				ActionObjet ao = this.toAddSelection.get(team).get(0);
				if(ao instanceof Character){
					for(Character o: characters){
						if(o.team==team && o.name==ao.name && visibles.contains(o)){
							//add character to team selection
							this.addSelection(o, team);
						}
					}
				} else if(ao instanceof Building){
					for(Building o: buildings){
						if(o.team==team && o.name==ao.name && visibles.contains(o)){
							//add character to team selection
							this.addSelection(o, team);
						}
					}
				}
			}
			this.g.players.get(team).groupSelection = -1;
		}
	}
	public void clearSelection(int team){
		this.selection.get(team).clear();
	}

	// handling messages
	public void addMessage(Message m, int team){
		if(this.messages.get(team).size()>19)
			return;
		this.messages.get(team).add(0, m);
	}

	//MULTIPLAYING

	public String toString(){
		//PLAYERS
		String s = "1 separation ";
		s+=this.g.players.get(3-this.g.currentPlayer);
		//CHARACTER
		s += " separation ";
		for(Character c : this.characters){
			s+=c;
			s+="|";
		}
		//BUILDING
		s+=" separation ";
		for(Building b : this.buildings){
			s+=b;
			s+="|";
		}
		//BULLETS
		s+=" separation ";
		for(Bullet b : this.bullets){
			s+=b;
			s+="|";
		}
		return s;
	}

	public void parse(String s){
		//APPLY ACTION ON ALL CONCERNED OBJECTS
		//GET ARRAY OF PLAYER,CHARACTER,BUILDING,BULLET
		if(s!=null && s!=""){
		System.out.println("parse");
		System.out.println(s);
		String[] u = s.split(" separation ");
		System.out.println("u0 et u1");
		System.out.println(u[1]);
		System.out.println(u[2]);
		//Take care of player
		this.g.players.get(g.currentPlayer).parsePlayer(u[0]);
		parseCharacter(u[3]);
		parseBuilding(u[4]);
		parseBullet(u[5]);
		}
	}



	public void parseBuilding(String s){

	}

	public void parseCharacter(String s){
		//SPLIT SELON |
		String[] u = s.split("|");
		// LOOP OVER EACH CHARACTER
		Character cha=null;

		for(int i =0;i<u.length;i++){
			//FIND CONCERNED CHARACTER
			HashMap<String,String> hs = Objet.preParse(u[i]);
			int idTest = Integer.parseInt(hs.get("id"));
			for(Character c : this.characters){
				if(c.id==idTest){
					cha  = c;
					break;
				}
			}

			if(cha!=null){
				cha.parse(hs);
			}
			else{
				//CREATE NEW CHARACTER
				//TODO
				Character.createNewCharacter(hs, g);
			}
		}
	}
	public void parseBullet(String s){

	}



}



