package model;
//TODO

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import technologies.Technologie;
import units.*;
import buildings.Building;
import buildings.BuildingProduction;
import buildings.BuildingTech;
import buildings.BuildingHeadQuarters;
import bullets.Arrow;
import bullets.Bullet;
import bullets.Fireball;
import display.BottomBar;
import display.Message;
import multiplaying.*;
import multiplaying.OutputModel.OutputBuilding;
import multiplaying.OutputModel.OutputBullet;
import multiplaying.OutputModel.OutputChar;
import multiplaying.OutputModel.OutputSpell;
import spells.Firewall;
import spells.Spell;
import spells.SpellEffect;
import spells.SpellFirewall;
import units.Character;
import weapon.Bow;
import weapon.Weapon;

public class Plateau {

	public float soundVolume;
	public Sounds sounds;
	public Images images;
	public Game g;
	public Sound deathSound;
	public int nTeams;
	public int maxX ;
	public int maxY ;
	// Camera 
	public int Xcam;
	public int Ycam;
	// fog of war
	public Image fog;
	public Graphics gf;


	// ADD ALL OBJETS 
	public Vector<Character> characters;
	public Vector<Character> toAddCharacters;
	public Vector<Character> toRemoveCharacters;

	public Vector<ActionObjet> equipments;
	public Vector<ActionObjet> toAddEquipments;
	public Vector<ActionObjet> toRemoveEquipments;

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

	public Vector<Rectangle> rectangleSelection;
	public Vector<Float> recX ;
	public Vector<Float> recY ;
	public Vector<Vector<ActionObjet>> inRectangle ;

	public Vector<Boolean> isCastingSpell;
	public Vector<Integer> castingSpell;

	public Vector<Vector<Message>> messages;

	public Constants constants;
	//TODO : make actionsObjets and everything else private 

	public Plateau(Constants constants,int maxX,int maxY,int nTeams, Game g){
		this.soundVolume = g.soundVolume;
		this.sounds = g.sounds;
		this.images = g.images;
		this.g = g;
		//GENERAL
		this.constants = constants;
		this.nTeams = nTeams;
		this.maxX= maxX;
		this.maxY = maxY;
		//CHARACTERS
		this.characters = new Vector<Character>();
		this.toAddCharacters = new Vector<Character>();
		this.toRemoveCharacters = new Vector<Character>();
		//WEAPONS
		this.equipments = new Vector<ActionObjet>();
		this.toAddEquipments = new Vector<ActionObjet>();
		this.toRemoveEquipments = new Vector<ActionObjet>();
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
		this.rectangleSelection = new Vector<Rectangle>();
		this.recX = new Vector<Float>();
		this.recY = new Vector<Float>();
		this.inRectangle = new Vector<Vector<ActionObjet>>();
		//MESSAGES
		this.messages = new Vector<Vector<Message>>();

		//CASTING SPELLS
		this.isCastingSpell = new Vector<Boolean>();
		this.castingSpell = new Vector<Integer>();
		for(int i =0; i<=nTeams;i++){
			this.recX.addElement(0f);
			this.recY.addElement(0f);
			this.selection.addElement(new Vector<ActionObjet>());
			this.toAddSelection.addElement(new Vector<ActionObjet>());
			this.toRemoveSelection.addElement(new Vector<ActionObjet>());
			this.rectangleSelection.addElement(null);
			this.inRectangle.addElement(new Vector<ActionObjet>());
			this.isCastingSpell.addElement(false);
			this.castingSpell.addElement(-1);
			this.messages.addElement(new Vector<Message>());
		}
		try {
			this.deathSound = new Sound("music/death.ogg");
			this.fog = new Image((int)this.g.resX,(int)this.g.resY);
			this.gf = fog.getGraphics();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// functions that handle buffers
	public void addCharacterObjets(Character o){
		toAddCharacters.addElement(o);
	}
	private void removeCharacter(Character o){
		toRemoveCharacters.addElement(o);
	}
	public void addEquipmentObjets(ActionObjet o){
		toAddEquipments.addElement(o);
	}
	private void removeEquipment(ActionObjet o){
		toRemoveEquipments.addElement(o);
	}
	public void addBulletObjets(Bullet o){
		toAddBullets.addElement(o);
	}
	private void removeBullet(Bullet o){
		toRemoveBullets.addElement(o);
	}
	public void addNaturalObjets(NaturalObjet o){
		toAddNaturalObjets.addElement(o);
	}
	private void removeNaturalObjets(NaturalObjet o){
		toRemoveNaturalObjets.addElement(o);
	}
	public void addBuilding(Building o){
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

	public void clearSelection(int team){
		this.selection.get(team).clear();
	}

	public void clean(){
		// Clean the buffers and handle die
		// Remove and add considering alive 
		for(Character o : characters){
			if(!o.isAlive()){
				this.removeCharacter(o);
				this.deathSound.play(0.8f+1f*((float)Math.random()),this.soundVolume);
			}
		}
		for(ActionObjet o : equipments){
			if(!o.isAlive()){
				this.removeEquipment(o);
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
			if(o.weapon!=null)
				o.weapon.destroy();
		}
		for(Character o: toAddCharacters){
			characters.addElement(o);
		}
		for(ActionObjet o: toRemoveEquipments){
			equipments.remove(o);
		}
		for(ActionObjet o: toAddEquipments){
			equipments.addElement(o);
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
		toRemoveEquipments.clear();
		toRemoveBullets.clear();
		toRemoveNaturalObjets.clear();
		toRemoveSpells.clear();
		toRemoveBuildings.clear();
		toAddCharacters.clear();
		toAddSpells.clear();
		toAddEquipments.clear();
		toAddBullets.clear();
		toAddNaturalObjets.clear();
		toAddBuildings.clear();
	}

	//getters and setters
	public float getMaxX(){return maxX;}
	public float getMaxY(){return maxY;}

	//general method calling collision method of the regarded objects
	public void collision(){
		for(Character o : characters){
			if(o.team!=this.g.currentPlayer)
				continue;
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
			// Between characters and weapons
			for(ActionObjet i:equipments){
				if(i.collisionBox.intersects(o.collisionBox)){
					i.collision(o);
				}
			}
			// Between characters and generator
			for(Building e:buildings){
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
			if(b.team!=g.currentPlayer)
				continue;
			for(NaturalObjet n: naturalObjets){
				if(b.collisionBox.intersects(n.collisionBox))
					b.collision(n);
			}
			for(Building c: buildings){
				if(b.collisionBox.intersects(c.collisionBox))
					b.collision(c);
			}
		}
		for(Building b : buildings){
			for(ActionObjet o : equipments){
				if(o.collisionBox.intersects(b.collisionBox) && o instanceof Weapon){
					Weapon w = (Weapon) o;
					w.collision(b);
					b.collision(w);

				}
			}
		}
	}

	public void updateSelection(Rectangle select,int team){
		if(select!=null){
			//this.clearSelection(team);
			for(ActionObjet a : this.inRectangle.get(team)){
				this.toRemoveSelection.get(team).add(a);
			}
			this.selection(select,team);
			this.g.players.get(team).groupSelection = -1;
		}

	}

	public void updateSelectionCTRL(Rectangle select,int team){
		if(select!=null){
			this.clearSelection(team);
			this.selectionCTRL(select,team);
			this.g.players.get(team).groupSelection = -1;
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
	public Vector<Objet> getRessourcesInSight(Character caller){
		Vector<Objet> ressources_in_sight = new Vector<Objet>();
		for(NaturalObjet o : naturalObjets){
			if(o.collisionBox.intersects(caller.sightBox)){
				ressources_in_sight.add(o);
			}
		}
		return ressources_in_sight;
	}


	//handling the input
	public void updateTarget(float x, float y, int team){
		//called when right click on the mouse
		Objet target = this.findTarget(x, y);
		//TODO : look amongst horses and weapons too
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
				o.setTarget(target);
				o.secondaryTargets.clear();
			}
		}
	}

	public void updateSecondaryTarget(float x, float y, int team){
		//called when right click on the mouse
		Objet target = this.findTarget(x, y);
		//TODO : look amongst horses and weapons too
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

	private void selection(Rectangle select, int team) {
		//return the units in the rectangle select for the team team
		this.inRectangle.get(team).clear();
		for(Character o: characters){
			if(o.collisionBox.intersects(select) && o.team==team){
				this.addSelection(o, team);
				this.inRectangle.get(team).addElement(o);
			}
		}
		if(this.toAddSelection.get(team).size()==0){
			for(Building o: buildings){
				if(o.collisionBox.intersects(select) && o.team==team){
					this.addSelection(o, team);
					this.inRectangle.get(team).addElement(o);
				}
			}
		}
	}
	private void selectionCTRL(Rectangle select, int team) {
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
	}

	//general methods 
	public void action(){
		for(Character o: this.characters){
			if(o.team==this.g.currentPlayer)
				o.action();
		}
		for(ActionObjet o: this.equipments){
			if(o.team==this.g.currentPlayer || !(o instanceof Bow))
				o.action();
		}
		for(Bullet o: bullets){
			if(o.team==this.g.currentPlayer)
				o.action();
		}
		for(Building e: this.buildings){
			if(e.team==this.g.currentPlayer)
				e.action();
		}
		for(ActionObjet a : this.spells){
			if(a.team==this.g.currentPlayer)
				a.action();
		}
	}

	public OutputModel update(InputModel im){
		/* Pipeline of the update:
		 * 1 - If ESC start menu
		 * 2 - Handling inputs (1 loop per player)
		 * 3 - Collision, Action, Cleaning
		 * 4 - Other updates
		 */
		//		if(this.spells.size()>0){
		//			System.out.println(this.spells);
		//		}
		OutputModel om = new OutputModel(0);

		//		// 1 - If ESC start menu
		//		if(!this.g.inMultiplayer && !g.isInMenu && ims.get(0)!=null && ims.get(0).isPressedESC){
		//			this.g.setMenu(g.menuPause);
		//			return om;
		//		}
		// 2 - Handling inputs (1 loop per player)

		int player=this.g.currentPlayer;
		this.updateView(im, player);
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
		if(!im.leftClick){
			// The button is not pressed and wasn't, the selection is non null
			this.rectangleSelection.set(player, null);
			this.inRectangle.get(player).clear();
		}
		// Split click bottom bar and not bottom bar
		//Top Bar
		if((im.yMouse-im.Ycam)<this.g.relativeHeightTopBar*im.resY){


		}

		//Bottom Bar
		else if((im.yMouse-im.Ycam)>this.g.players.get(player).bottomBar.y){
			BottomBar bb = this.g.players.get(player).bottomBar;
			float relativeXMouse = (im.xMouse-im.Xcam);
			float relativeYMouse = (im.yMouse-im.Ycam);
			//Handling production buildings
			if(relativeXMouse>bb.prodX && relativeXMouse<bb.prodX+bb.prodW && relativeYMouse>bb.prodY && relativeYMouse<bb.prodY+bb.prodH){
				if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingProduction){
					if(im.isPressedLeftClick){

						((BuildingProduction) this.selection.get(player).get(0)).product((int)((relativeYMouse-bb.prodY)/(bb.prodH/bb.prodIconNb)));
					}else{

					}

				}
				else if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingTech){
					if(im.isPressedLeftClick){

						((BuildingTech) this.selection.get(player).get(0)).product((int)((relativeYMouse-bb.prodY)/(bb.prodH/bb.prodIconNb)));
					}

				}
				else if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof Character){
					if(im.isPressedLeftClick){
						int number = (int)((relativeYMouse-bb.prodY)/(bb.prodH/bb.prodIconNb));
						Character c = ((Character) this.selection.get(player).get(0));
						if(c.spells.size()>number){
							if(c.spellsState.get(number)>=c.spells.get(number).chargeTime){
								if(c.spells.get(number).needToClick){
									this.isCastingSpell.set(player, true);
									this.castingSpell.set(player, number);
								} else {
									c.spells.get(number).launch(c, c);
								}
							} else {
								this.addMessage(Message.getById(4), player);
							}
						}
					}else{

					}

				}
			}

		}
		// FIELD
		else if( (im.yMouse-im.Ycam)>=this.g.players.get(player).topBar.y && (im.yMouse-im.Ycam)<=this.g.players.get(player).bottomBar.y ){
			//update the rectangle

			if(im.leftClick){

				if(isCastingSpell.get(player)){

				} else {
					// As long as the button is pressed, the selection is updated
					this.updateRectangle(im,player);
				}
			}
			if(im.isPressedLeftClick){
				if(isCastingSpell.get(player)){
					// Handling the spell
					if(this.g.players.get(player).selection.size()>0){
						Character c = (Character)this.g.players.get(player).selection.get(0); 
						Spell spell = c.spells.get(castingSpell.get(player));
						spell.launch(new Checkpoint(im.xMouse,im.yMouse),(Character)this.g.players.get(player).selection.get(0));
						c.spellsState.set(castingSpell.get(player),0f);
					}
					isCastingSpell.set(player,false);
					castingSpell.set(player,-1);
				} else if(im.isPressedMAJ){

				} else {
					this.clearSelection(player);
				}
			}
			// Action for player k
			if(im.isPressedRightClick){
				//RALLY POINT
				if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingProduction){
					((BuildingProduction) this.selection.get(player).get(0)).rallyPoint = new Checkpoint(im.xMouse,im.yMouse);
				} else if(isCastingSpell.get(player)){
					isCastingSpell.set(player,false);
					castingSpell.set(player,-1);
				} else if(im.isPressedMAJ){
					updateSecondaryTarget(im.xMouse,im.yMouse,player);
				} else {				
					updateTarget(im.xMouse,im.yMouse,player);
				}
			}
		}
		// Handling other hotkeys in production bar
		if(im.isPressedW || im.isPressedX || im.isPressedC || im.isPressedV || im.isPressedESC){
			if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingProduction){

				if(im.isPressedW)
					((BuildingProduction) this.selection.get(player).get(0)).product(0);
				if(im.isPressedX)
					((BuildingProduction) this.selection.get(player).get(0)).product(1);
				if(im.isPressedC)
					((BuildingProduction) this.selection.get(player).get(0)).product(2);
				if(im.isPressedV)
					((BuildingProduction) this.selection.get(player).get(0)).product(3);
				if(im.isPressedESC)
					((BuildingProduction) this.selection.get(player).get(0)).removeProd();
			}
			else if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof BuildingTech){

				if(im.isPressedW)
					((BuildingTech) this.selection.get(player).get(0)).product(0);
				if(im.isPressedX)
					((BuildingTech) this.selection.get(player).get(0)).product(1);
				if(im.isPressedC)
					((BuildingTech) this.selection.get(player).get(0)).product(2);
				if(im.isPressedV)
					((BuildingTech) this.selection.get(player).get(0)).product(3);
				if(im.isPressedESC)
					((BuildingTech) this.selection.get(player).get(0)).removeProd();
			}
			else if(this.selection.get(player).size()>0 && this.selection.get(player).get(0) instanceof Character){

				int number = 4;

				if(im.isPressedW)
					number = 0;
				if(im.isPressedX)
					number = 1;
				if(im.isPressedC)
					number = 2;
				if(im.isPressedV)
					number = 3;
				if(im.isPressedESC){
					isCastingSpell.set(player,false);
					castingSpell.set(player,-1);
				}

				Character c = ((Character) this.selection.get(player).get(0));
				if(c.spells.size()>number && c.spellsState.get(number)>=c.spells.get(number).chargeTime){
					if(c.spells.get(number).needToClick){
						isCastingSpell.set(player,true);
						castingSpell.set(player,number);
					} else {
						c.spells.get(number).launch(c, c);
					}
				}
			}
		}
		// Handling hotkeys for gestion of selection
		if(im.isPressedTAB){
			if(this.selection.get(player).size()>0){
				Utils.switchTriName(this.selection.get(player));
				if(this.g.players.get(player).groupSelection!=-1)
					Utils.switchTriName(this.g.players.get(player).groups.get(this.g.players.get(player).groupSelection));
			}
		}

		// we update the selection according to the rectangle wherever is the mouse
		if(!im.isPressedCTRL){
			// The button is not pressed and wasn't, the selection is non null
			this.updateSelection(rectangleSelection.get(player), player);
		} else {
			this.updateSelectionCTRL(rectangleSelection.get(player),player);
		}
		// Update the selections of the players
		this.g.players.get(player).selection.clear();
		for(ActionObjet c: this.selection.get(player))
			this.g.players.get(player).selection.addElement(c);

		// Handling the changes
		
		

		// 3 - Collision, Action, Cleaning
		this.collision();
		this.clean();
		this.action();

		// 4 - Update the visibility
		for(Character c:this.characters)
			c.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, c);
		for(Building b:this.buildings)
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, b);
		for(Bullet b:this.bullets)
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, b);
		for(SpellEffect b:this.spells)
			b.visibleByCurrentPlayer = this.isVisibleByPlayer(this.g.currentPlayer, b);

		// 4 - Update of the music
		if(!g.isInMenu && !this.g.musicStartGame.playing() && !this.g.mainMusic.playing()){
			this.g.mainMusic.loop();
		}

		// 5 - Update of the messages
		Vector<Message> toDelete = new Vector<Message>();
		toDelete.clear();
		for(Message m: this.messages.get(player)){
			m.remainingTime-=1f;
			if(m.remainingTime<=0f)
				toDelete.add(m);
		}
		for(Message m:toDelete)
			this.messages.get(player).remove(m);

		// 6 - creation of the outputmodel
		om.food = this.g.players.get(2).food;
		om.gold = this.g.players.get(2).gold;
		om.special = this.g.players.get(2).special;
		for(Character c: this.characters){
			om.toChangeCharacters.add(new OutputChar(c));
		}
		for(Bullet b: this.bullets){
			if(b instanceof Arrow)
				om.toChangeBullets.add(new OutputBullet(b.id,0,b.team,b.x,b.y,b.vx,b.vy,b.damage));
			else
				om.toChangeBullets.add(new OutputBullet(b.id,1,b.team,b.x,b.y,b.vx,b.vy,b.damage));

		}
		for(Building b: this.buildings){
			om.toChangeBuildings.add(new OutputBuilding(b));
		}
		for(SpellEffect s : this.spells){
			om.toChangeSpells.add(new OutputSpell(s));
		}

		return om;
	}


	public void updateFromOutput(OutputModel om){

		if(om!=null){
			// Characters
			// Changing characters
			Character c=null;
			for(OutputChar occ : om.toChangeCharacters){
				c = null;
				for(Character c2: this.characters)
					if(c2.id==occ.id)
						c = c2;
				if(c!=null){
					c.change(occ);
				} else {
					new Character(occ,this);
				}
			}
			boolean toErase = true;
			for(Character c2: this.characters){
				if(c2.team==this.g.currentPlayer)
					continue;
				toErase = true;
				for(OutputChar occ : om.toChangeCharacters){
					if(occ.id==c2.id)
						toErase = false;
				}
				if(toErase)
					this.toRemoveCharacters.addElement(c2);
			}
			// Bullets
			// Changing bullets
			Bullet b=null;
			for(OutputBullet ocb : om.toChangeBullets){
				//				if(ocb.team==this.g.currentPlayer)
				//					continue;
				b = null;
				for(Bullet b2: this.bullets)
					if(b2.id==ocb.id && b2.team==ocb.team)
						b = b2;
				if(b!=null){
					b.change(ocb);
				}else{
					if(ocb.typeBullet==0){
						new Arrow(ocb,this);
					} else if (ocb.typeBullet == 1){
						new Fireball(ocb,this);				
					}
				}

			}
			toErase = true;
			for(Bullet c2: this.bullets){
				if(c2.team!=this.g.currentPlayer)
					continue;
				toErase = true;
				for(OutputBullet occ : om.toChangeBullets){
					if(occ.id==c2.id && occ.team == c2.team)
						toErase = false;
				}
				if(toErase)
					this.toRemoveBullets.addElement(c2);
			}
			// Buildings
			// Changing buildings
			Building bu=null;
			for(OutputBuilding ocb : om.toChangeBuildings){
				bu = null;
				for(Building b2: this.buildings)
					if(b2.id==ocb.id)
						bu = b2;
				if(bu!=null){
					bu.change(ocb);
				}else{
					new Building(ocb, this);
				}

			}
			toErase = true;
			for(Building c2: this.buildings){
				if(c2.team==this.g.currentPlayer || c2.team==0)
					continue;
				toErase = true;
				for(OutputBuilding occ : om.toChangeBuildings){
					if(occ.id==c2.id)
						toErase = false;
				}
				if(toErase)
					this.toRemoveBuildings.addElement(c2);
			}

			// Spell
			// Changing spells
			SpellEffect sp=null;
			for(OutputSpell ocs : om.toChangeSpells){
				sp = null;
				for(SpellEffect s2: this.spells)
					if(s2.id==ocs.id)
						sp = s2;
				if(sp!=null){

				}else{
					new SpellEffect(this, ocs);
				}
			}
			toErase = true;
			for(SpellEffect c2: this.spells){
				if(c2.team==this.g.currentPlayer)
					continue;
				toErase = true;
				for(OutputSpell occ : om.toChangeSpells){
					if(occ.id==c2.id)
						toErase = false;
				}
				if(toErase)
					this.toRemoveSpells.addElement(c2);
			}

			//			System.out.println("allTechs: " +this.g.players.get(2).hq.allTechs);
			//			System.out.println("techDiscovered: " +this.g.players.get(2).hq.techsDiscovered);

		}
	}

	public void updateView(InputModel im, int player){
		if(!isCastingSpell.get(player) && player==this.g.currentPlayer && this.rectangleSelection.get(player)==null && !im.leftClick){
			// Move camera according to inputs :
			if((im.isPressedUP || im.yMouse<im.Ycam+10)&&im.Ycam>-im.resY/2){
				Ycam -= 20;
			}
			if((im.isPressedDOWN || im.yMouse>im.Ycam+im.resY-10) && im.Ycam<this.maxY-im.resY/2){
				Ycam +=20;
			}
			if((im.isPressedLEFT|| im.xMouse<im.Xcam+10) && im.Xcam>-im.resX/2 ){
				Xcam -=20;
			}
			if((im.isPressedRIGHT || im.xMouse>im.Xcam+im.resX-10)&& im.Xcam<this.maxX-im.resX/2){
				Xcam += 20;
			}
			//Displaying the selected group
			for(int to=0; to<10; to++){
				if(im.isPressedNumPad[to]){
					if(this.g.players.get(player).groupSelection == to && this.g.players.get(player).groups.get(to).size()>0){
						int xmoy=(int)Math.floor(this.g.players.get(player).groups.get(to).get(0).getX());
						int ymoy=(int)Math.floor(this.g.players.get(player).groups.get(to).get(0).getY());
						this.Xcam = Math.min(maxX-im.resX/2, Math.max(-im.resX/2, xmoy-im.resX/2));
						this.Ycam = Math.min(maxY-im.resY/2, Math.max(-im.resY/2, ymoy-im.resY/2));
					}
				}
			}
		}
		float bottomTopBar = this.g.players.get(player).topBar.y+(float)this.g.players.get(player).topBar.sizeY+2f;
		// check if in topbar
		if((im.leftClick||im.rightClick) && (im.yMouse-im.Ycam)<bottomTopBar){
			if(this.rectangleSelection.get(player)!=null){
				rectangleSelection.get(player).setBounds( (float)Math.min(recX.get(player),im.xMouse), (float)this.g.players.get(player).topBar.y
						+(float)this.g.players.get(player).topBar.sizeY+im.Ycam+2f,
						(float)Math.abs(im.xMouse-recX.get(player))+0.1f, (float)Math.abs(this.g.players.get(player).topBar.y+(float)this.g.players.get(player).topBar.sizeY+2f+im.Ycam-recY.get(player))+0.1f);
			}
		} else 
			// check if in bottombar	
			if((im.leftClick||im.rightClick) && (im.yMouse-im.Ycam)>this.g.players.get(player).bottomBar.y){

				BottomBar b = this.g.players.get(player).bottomBar;
				//If click on minimap
				if(im.leftClick && player==this.g.currentPlayer && (im.xMouse-im.Xcam)>b.startX && (im.xMouse-im.Xcam)<
						b.startX+b.w && this.rectangleSelection.get(player)==null){
					// Put camera where the click happened
					Xcam = (int)Math.floor((im.xMouse-im.Xcam-b.startX)/b.rw)-im.resX/2;
					Ycam = (int)Math.floor((im.yMouse-im.Ycam-b.startY)/b.rh)-im.resY/2;

				}
				if(im.rightClick && player==this.g.currentPlayer && (im.xMouse-im.Xcam)>b.startX && (im.xMouse-im.Xcam)<
						b.startX+b.w && this.rectangleSelection.get(player)==null){
					// Handle right click
					if(im.isPressedMAJ){
						updateSecondaryTarget((int)Math.floor((im.xMouse-im.Xcam-b.startX)/b.rw),(int)Math.floor((im.yMouse-im.Ycam-b.startY)/b.rh),player);
					} else {				
						updateTarget((int)Math.floor((im.xMouse-im.Xcam-b.startX)/b.rw),(int)Math.floor((im.yMouse-im.Ycam-b.startY)/b.rh),player);
					}

				}
				if(this.rectangleSelection.get(player)!=null){
					rectangleSelection.get(player).setBounds( (float)Math.min(recX.get(player),im.xMouse), (float)Math.min(recY.get(player), im.yMouse),
							(float)Math.abs(im.xMouse-recX.get(player))+0.1f, (float)Math.abs(this.g.players.get(player).bottomBar.y+im.Ycam-2f-recY.get(player))+0.1f);
				}
			}

	}

	private void updateRectangle(InputModel im, int player) {
		if(rectangleSelection.get(player)==null || im.isPressedCTRL){
			recX.set(player, (float)im.xMouse);
			recY.set(player, (float)im.yMouse);
			rectangleSelection.set(player, new Rectangle(recX.get(player),recY.get(player),0.1f,0.1f));
		}
		rectangleSelection.get(player).setBounds( (float)Math.min(recX.get(player),im.xMouse), (float)Math.min(recY.get(player), im.yMouse),
				(float)Math.abs(im.xMouse-recX.get(player))+0.1f, (float)Math.abs(im.yMouse-recY.get(player))+0.1f);
	}

	// drawing method
	public void drawFogOfWar(Graphics g){
		Vector<Objet> visibleObjet = new Vector<Objet>();
		visibleObjet = this.getInCamObjets(this.g.currentPlayer);
		float resX = this.g.resX;
		float resY = this.g.resY;
		this.gf.setColor(new Color(50,50,50));
		gf.fillRect(0, 0, resX, resY);
		gf.setColor(Color.white);
		for(Objet o:visibleObjet){
			gf.fillOval(o.x-Xcam-o.sight,o.y-Ycam-o.sight,o.sight*2f,o.sight*2f);
		}
		gf.flush();
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.drawImage(fog,Xcam,Ycam);		
		g.setDrawMode(Graphics.MODE_NORMAL);
	}

	private Vector<Objet> getInCamObjets(int team) {
		Vector<Objet> obj = new Vector<Objet>();
		for(Character c: this.characters)
			if(c.team==team&&(c.x+c.sight>Xcam&&c.x-c.sight<Xcam+this.g.resX&&c.y+c.sight>Ycam&&c.y-c.sight<Ycam+this.g.resY))
				obj.add(c);
		for(Building c: this.buildings)
			if(c.team==team&&(c.x+c.sight>Xcam||c.x-c.sight<Xcam+this.g.resX||c.y+c.sight>Ycam||c.y-c.sight<Ycam+this.g.resY)){
				obj.add(c);

			}

		return obj;
	}

	public boolean isVisibleByPlayer(int player, Objet objet){
		if(objet.x+objet.sight<Xcam||objet.x-objet.sight>Xcam+this.g.resX||objet.y+objet.sight<Ycam||objet.y-objet.sight>Ycam+this.g.resY)
			return false;
		if(objet.team==player && !(objet instanceof Bullet))
			return true;
		for(Character c: this.characters)
			if(c.team==player && Utils.distance(c, objet)<c.sight)
				return true;
		for(Building b: this.buildings)
			if(b.team==player && Utils.distance(b,  objet)<b.sight)
				return true;
		return false;
	}

	public boolean isVisibleByPlayerMinimap(int player, Objet objet){
		if(objet.team==player)
			return true;
		for(Character c: this.characters)
			if(c.team==player && Utils.distance(c, objet)<c.sight)
				return true;
		for(Building b: this.buildings)
			if(b.team==player && Utils.distance(b,  objet)<b.sight)
				return true;
		return false;
	}

	public void addMessage(Message m, int team){
		if(this.messages.get(team).size()>19)
			return;
		this.messages.get(team).add(0, m);
	}

}



