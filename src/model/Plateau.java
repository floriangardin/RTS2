package model;
//TODO

import java.util.Vector;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Plateau {
	
	Sounds sounds;
	public Game g;
	protected Sound deathSound;
	protected int nTeams;
	protected float maxX ;
	protected float maxY ;

	// ADD ALL OBJETS 
	protected Vector<Character> characters;
	protected Vector<Character> toAddCharacters;
	protected Vector<Character> toRemoveCharacters;

	protected Vector<ActionObjet> equipments;
	protected Vector<ActionObjet> toAddEquipments;
	protected Vector<ActionObjet> toRemoveEquipments;

	protected Vector<Bullet> bullets;
	protected Vector<Bullet> toAddBullets;
	protected Vector<Bullet> toRemoveBullets;



	protected Vector<NaturalObjet> naturalObjets ;
	protected Vector<NaturalObjet> toAddNaturalObjets;
	protected Vector<NaturalObjet> toRemoveNaturalObjets;
	// Vector of vector of vector of vector * 10^12
	protected Vector<Vector<Character>> selection;
	protected Vector<Vector<Character>> toAddSelection;
	protected Vector<Vector<Character>> toRemoveSelection ;
	protected Constants constants;
	//TODO : make actionsObjets and everything else private 

	public Plateau(Constants constants,float maxX,float maxY,int nTeams, Game g){
		this.sounds = g.sounds;
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
		//SELECTION
		this.selection = new Vector<Vector<Character>>();
		this.toAddSelection = new Vector<Vector<Character>>();
		this.toRemoveSelection = new Vector<Vector<Character>>();
		for(int i =0; i<nTeams;i++){
			this.selection.addElement(new Vector<Character>());
			this.toAddSelection.addElement(new Vector<Character>());
			this.toRemoveSelection.addElement(new Vector<Character>());
		}
		try {
			this.deathSound = new Sound("music/death.ogg");
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
	public void addSelection(Character o,int team){
		toAddSelection.get(team).addElement(o);
	}
	public void removeSelection(Character o, int team){
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
				if(o.team==1)
					this.g.players.get(0).ennemiesKilled+=1;
				
				this.deathSound.play(0.8f+1f*((float)Math.random()),0.2f);
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
		for(Character o : characters){
			if(!o.isAlive()){
				this.removeCharacter(o);
			}
		}
		for(NaturalObjet o : naturalObjets){
			if(!o.isAlive()){
				this.removeNaturalObjets(o);
			}
		}

		// Update selection
		for(int i=0;i<nTeams;i++){
			for(Character c: selection.get(i)){
				if(!c.isAlive()){
					this.removeSelection(c, i);
				}
			}
			for(Character o: toRemoveSelection.get(i)){
				selection.get(i).remove(o);
			}
			for(Character o: toAddSelection.get(i)){
				selection.get(i).addElement(o);
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
		for(ActionObjet o: toRemoveEquipments){
			equipments.remove(o);
		}
		for(ActionObjet o: toAddEquipments){
			equipments.addElement(o);
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

		// Clear the vector :
		for(int i = 0;i<nTeams;i++){
			toAddSelection.get(i).clear();
			toRemoveSelection.get(i).clear();
		}

		toRemoveCharacters.clear();
		toRemoveEquipments.clear();
		toRemoveBullets.clear();
		toRemoveNaturalObjets.clear();
		toAddCharacters.clear();
		toAddEquipments.clear();
		toAddBullets.clear();
		toAddNaturalObjets.clear();
	}

	//getters and setters
	public float getMaxX(){return maxX;}
	public float getMaxY(){return maxY;}

	//general method calling collision method of the regarded objects
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
			// Between characters and weapons
			for(ActionObjet i:equipments){

				if(i.collisionBox.intersects(o.collisionBox)){

					i.collision(o);
				}
			}
		}
		// Between bullets and natural objets
		for(Bullet b : bullets){
			for(NaturalObjet n: naturalObjets){
				b.collision(n);
			}
		}
	}

	public void updateSelection(Rectangle select,int team){
		if(select!=null){
			this.clearSelection(team);
			this.selection(select,team);
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
			target = new Checkpoint(x,y);
		}
		Character leader = null;
		for(Character o:this.selection.get(team)){
			if(leader==null){
				leader=o;
			}
			//first we deal with o's elder group
			//if o was the leader and there were other members in the group
			if(o.isLeader() && o.group.size()>1){
				//we set the group of the new leader
				o.group.get(1).group = o.group;
				//we set the new leader amongst the member of the group
				for(Character o1: o.group){
					o1.leader = o.group.get(1);
				}
				//we remove o from the group

			}
			if(o.leader!=null){
				o.leader.group.remove(o);
			}
			if(leader==o){
				o.group = new Vector<Character>();
			}
			//we set to o its new leader and to its leader's group the new member
			o.leader = leader;
			if(!o.isLeader())
				o.group=null;
			o.leader.group.add(o);
			//eventually we assign the target
			o.setTarget(target);
			o.secondaryTargets.clear();
		}

	}
	
	public void updateSecondaryTarget(float x, float y, int team){
		//called when right click on the mouse
		Objet target = this.findTarget(x, y);
		//TODO : look amongst horses and weapons too
		if(target==null){
			target = new Checkpoint(x,y);
		}
		Character leader = null;
		for(Character o:this.selection.get(team)){
			if(leader==null){
				leader=o;
			}
			//first we deal with o's elder group
			//if o was the leader and there were other members in the group
			if(o.isLeader() && o.group.size()>1){
				//we set the group of the new leader
				o.group.get(1).group = o.group;
				//we set the new leader amongst the member of the group
				for(Character o1: o.group){
					o1.leader = o.group.get(1);
				}
				//we remove o from the group

			}
			if(o.leader!=null){
				o.leader.group.remove(o);
			}
			if(leader==o){
				o.group = new Vector<Character>();
			}
			//we set to o its new leader and to its leader's group the new member
			o.leader = leader;
			if(!o.isLeader())
				o.group=null;
			o.leader.group.add(o);
			//eventually we assign the target
			o.secondaryTargets.add(target);
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
		//handling the selection
		for(Character o: characters){
			if(o.collisionBox.intersects(select) && o.team==team ){
				//add character to team selection
				this.addSelection(o, team);
			}
		}
	}


	//general methods 
	public void action(){
		for(Character o: this.characters){
			//TODO : Leader handling leader stuff

			o.action();
		}
		for(ActionObjet o: this.equipments){
			//TODO : Leader handling leader stuff
			o.action();
		}
		for(Bullet o: bullets){
			//TODO : Leader handling leader stuff
			o.action();
		}
	}
	public void update(){
		// Handle collision and cleaning of buffers
		this.collision();
		this.clean();
	}
}



