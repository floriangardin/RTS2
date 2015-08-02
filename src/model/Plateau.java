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
	//TODO : make actionsObjets and everything else private 
	
	public Plateau(float maxX,float maxY,int nTeams){
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

	public void add_selection(Character o,int team){
		toAddSelection.get(team).addElement(o);
	}

	public void remove_selection(Character o, int team){
		toRemoveSelection.get(team).addElement(o);
	}

	public void clear_selection(int team){
		this.selection.get(team).clear();
	}

	public void clean(){
		for(ActionObjet o : actionsObjets){
			if(!o.isAlive()){
				this.removeActionsObjets(o);
			}
		}
		for(int i=0;i<nTeams;i++){
			for(Character o: toRemoveSelection.get(i)){
				selection.get(i).remove(o);
			}
			for(Character o: toAddSelection.get(i)){
				selection.get(i).addElement(o);
			}
		}

		for(ActionObjet o: toRemoveActionsObjets){
			actionsObjets.remove(o);
		}
		for(ActionObjet o: toAddActionsObjets){
			actionsObjets.addElement(o);
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
		for(Objet o : actionsObjets){
			for(Objet i:actionsObjets){
				if(i.collisionBox.intersects(o.collisionBox) && i!=o){
					i.collision(o);
					o.collision(i);
				}
			}
		}
	}

	public void update(Rectangle select){
		if(select!=null){
			this.selection(select);
		}
		this.collision();
		this.clean();
	}
	//TODO gné
	private void selection(Rectangle select) {
		for(Objet o: this.actionsObjets){
			if(o.collisionBox.intersects(select)){
				//TODO 

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

	public void action(float x, float y,boolean new_objective){
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
		Objet leader = null;
		Vector<Objet> leader_group = new Vector<Objet>();
		for(ActionObjet o: this.actionsObjets){
			//TODO : Leader handling leader stuff
			o.action(target);
		}

	}


}



