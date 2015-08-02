package model;
//TODO

import java.util.Vector;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Plateau {

	protected int nTeams;
	protected float maxX ;
	protected float maxY ;

	// ADD ALL OBJETS 
	protected Vector<Character> characters;
	protected Vector<Character> toAddCharacters;
	protected Vector<Character> toRemoveCharacters;

	protected Vector<Weapon> weapons;
	protected Vector<Weapon> toAddWeapons;
	protected Vector<Weapon> toRemoveWeapons;

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

	public Plateau(Constants constants,float maxX,float maxY,int nTeams){
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
		this.weapons = new Vector<Weapon>();
		this.toAddWeapons = new Vector<Weapon>();
		this.toRemoveWeapons = new Vector<Weapon>();
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

	}

	public void addCharacterObjets(Character o){
		toAddCharacters.addElement(o);
	}

	private void removeCharacter(Character o){
		toRemoveCharacters.addElement(o);
	}
	public void addWeaponObjets(Weapon o){
		toAddWeapons.addElement(o);
	}

	private void removeWeapon(Weapon o){
		toRemoveWeapons.addElement(o);
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

	public void remove_selection(Character o, int team){
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
			}
		}
		for(Weapon o : weapons){
			if(!o.isAlive()){
				this.removeWeapon(o);
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
		}
		for(Character o: toAddCharacters){
			characters.addElement(o);
		}
		for(Weapon o: toRemoveWeapons){
			weapons.remove(o);
		}
		for(Weapon o: toAddWeapons){
			weapons.addElement(o);
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
		toRemoveWeapons.clear();
		toRemoveBullets.clear();
		toRemoveNaturalObjets.clear();
		toAddCharacters.clear();
		toAddWeapons.clear();
		toAddBullets.clear();
		toAddNaturalObjets.clear();
	}

	public float getMaxX(){
		return maxX;
	}

	public float getMaxY(){
		return maxY;
	}


	public void collision(){

		for(Character o : characters){
			// Handle collision between actionObjets and action objets
			for(Character i:characters){
				if(i.collisionBox.intersects(o.collisionBox) && i!=o){

					i.collision(o);
					o.collision(i);
				}
			}
			// between Characters and Natural objets
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
			for(Weapon i:weapons){
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
	public void update(){
		// Handle collision and cleaning of buffers
		this.collision();
		this.clean();
	}
	//TODO gné
	private void selection(Rectangle select, int team) {
		for(Character o: characters){
			if(o.collisionBox.intersects(select) && o.team==team ){
				//add character to team selection
				this.addSelection(o, team);
			}
		}
	}


	public Vector<Objet> getEnnemiesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o.team!=0 && o.team!=caller.team && o.collisionBox.intersects(caller.sightBox)){
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



	public void updateTarget(float x, float y, int team){
		System.out.println("updateTarget");
		//TODO fill method behavior
		Point point= new Point(x,y);
		Objet target =new Checkpoint(x,y);
		boolean newTarget = false;
		for(Character o:this.selection.get(team)){
			for(Character i:this.characters){
				if(o.collisionBox.contains(point)){
					o.target = i;
					newTarget = true;
					break;
				}
			}
			if(!newTarget){
				for(NaturalObjet i: naturalObjets){
					if(o.collisionBox.contains(point)){
						o.target = i;
						newTarget = true;
						break;
					}
				}
			}
			if(!newTarget){
				o.target = target;
			}
		}

	}
	public void action(){

		for(Character o: this.characters){
			//TODO : Leader handling leader stuff
			o.action();
		}
		for(Weapon o: this.weapons){
			//TODO : Leader handling leader stuff
			o.action();
		}
		for(Bullet o: bullets){
			//TODO : Leader handling leader stuff
			o.action();
		}

	}
}



