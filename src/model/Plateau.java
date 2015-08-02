package model;
//TODO

import java.util.Vector;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Plateau {

	protected int nTeams;
	protected float maxX ;
	protected float maxY ;

	
	protected Vector<ActionObjet> actionsObjets;
	protected Vector<NaturalObjet> naturalObjets ;
	protected Vector<ActionObjet> toAddActionsObjets;
	protected Vector<ActionObjet> toRemoveActionsObjets;
	protected Vector<NaturalObjet> toAddNaturalObjets;
	protected Vector<NaturalObjet> toRemoveNaturalObjets;
	// Vector of vector of vector of vector * 10^12
	protected Vector<Vector<Character>> selection;
	protected Vector<Vector<Character>> toAddSelection;
	protected Vector<Vector<Character>> toRemoveSelection ;
	protected Constants constants;
	//TODO : make actionsObjets and everything else private 
	
	public Plateau(Constants constants,float maxX,float maxY,int nTeams){
		this.constants = constants;
		this.nTeams = nTeams;
		this.actionsObjets = new Vector<ActionObjet>();
		this.toAddActionsObjets = new Vector<ActionObjet>();
		this.toRemoveActionsObjets= new Vector<ActionObjet>();
		this.toAddNaturalObjets = new Vector<NaturalObjet>();
		this.toRemoveNaturalObjets= new Vector<NaturalObjet>();
		this.selection = new Vector<Vector<Character>>();
		this.toAddSelection = new Vector<Vector<Character>>();
		this.toRemoveSelection = new Vector<Vector<Character>>();
		this.naturalObjets = new Vector<NaturalObjet>();
		for(int i =0; i<nTeams;i++){
			this.selection.addElement(new Vector<Character>());
			this.toAddSelection.addElement(new Vector<Character>());
			this.toRemoveSelection.addElement(new Vector<Character>());
		}
		
		this.maxX= maxX;
		this.maxY = maxY;
	}

	public void addActionsObjets(ActionObjet o){
		toAddActionsObjets.addElement(o);
	}

	private void removeActionsObjets(ActionObjet o){
		toRemoveActionsObjets.addElement(o);
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
		for(ActionObjet o : actionsObjets){
			if(!o.isAlive()){
				this.removeActionsObjets(o);
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
		for(ActionObjet o: toRemoveActionsObjets){
			actionsObjets.remove(o);
		}
		for(ActionObjet o: toAddActionsObjets){
			actionsObjets.addElement(o);
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
		toRemoveActionsObjets.clear();
		toAddActionsObjets.clear();

	}

	public float getMaxX(){
		return maxX;
	}

	public float getMaxY(){
		return maxY;
	}


	public void collision(){
		
		for(ActionObjet o : actionsObjets){
			// Handle collision between actionObjets and action objets
			for(ActionObjet i:actionsObjets){
				if(i.collisionBox.intersects(o.collisionBox) && i!=o){
					i.collision(o);
					o.collision(i);
				}
			}
			// between actionObjets and 
			for(NaturalObjet i:naturalObjets){
				if(i.collisionBox.intersects(o.collisionBox)){
					o.collision(i);
				}
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
		for(ActionObjet o: this.actionsObjets){
			if(o instanceof Character && o.collisionBox.intersects(select) && o.team==team ){
				//add character to team selection
				this.addSelection((Character)o, team);
			}
		}
	}

	public boolean isSelected(Objet o){
		return this.selection.contains(o);
	}

	public Vector<Objet> getEnnemiesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(ActionObjet o : this.actionsObjets){
			if(o.team!=0 && o.team!=caller.team && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Objet> getRessourcesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(ActionObjet o : this.actionsObjets){
			if(o.team==0 && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}

	public Vector<Objet> getAllInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Objet o : this.actionsObjets){
			if(o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	
	public void updateTarget(float x, float y){
		//TODO fill method behavior
		
	}
	public void action(float x, float y,boolean new_objective){
		// Action should be called for all the players
		// Method is called whenever there is a right click
		// Get every object in the selection point :
		// Return every units in the sight range
		Point point= new Point(x,y);
		Objet target =new Checkpoint(x,y);
		// If new objective update the target list
		if(new_objective){
			for(Objet o:this.actionsObjets){
				if(o.collisionBox.contains(point)){
					target = o;
					break;
				}
			}

		}
		// Apply actions for all the  objects, different behaviour considering selection or not
		for(ActionObjet o: this.actionsObjets){
			//TODO : Leader handling leader stuff
			System.out.println(o);
			o.action();
		}

	}


}



