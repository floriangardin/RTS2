
import java.util.Vector;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Point;


public class Plateau{
	private Vector<Objet> selection; 
	private Vector<Objet> elements ;
	private Vector<Objet> to_add;
	private Vector<Objet> to_remove;
	private Vector<Objet> to_add_selection;
	private Vector<Objet> to_remove_selection ;
	private float maxX;
	private float maxY;
	// We let the game have a reference on the plateau 

	public Plateau(Vector<Objet> objets,Vector<Objet> objets_selection,float maxX,float maxY){
		this.elements = objets;
		this.to_add = new Vector<Objet>();
		this.to_remove= new Vector<Objet>();
		this.selection = new Vector<Objet>();
		this.to_add_selection = new Vector<Objet>();
		this.to_remove_selection = new Vector<Objet>();
		this.selection = objets_selection;
		this.maxX= maxX;
		this.maxY = maxY;
	}
	
	public void add(Objet o){
		to_add.addElement(o);
	}

	private void remove(Objet o){
		to_remove.addElement(o);
	}

	public void add_selection(Objet o){
		to_add_selection.addElement(o);
	}

	public void remove_selection(Objet o){
		to_remove_selection.addElement(o);
	}

	public void clear_selection(){
		this.selection.clear();
	}
	
	public void clean(){
		for(Objet o : elements){
			if(!o.isAlive()||o.getX()>this.maxX || o.getX()<0 || o.getY()> this.maxY || o.getY()<0 ){
				this.remove(o);
			}
		}
		for(Objet o: to_remove_selection){
			selection.remove(o);
		}
		for(Objet o: to_add_selection){
			selection.addElement(o);
		}
		for(Objet o: to_remove){
			elements.remove(o);
		}
		for(Objet o: to_add){
			elements.addElement(o);
		}


		// Clear the vector : 
		to_remove.clear();
		to_add.clear();
		to_add_selection.clear();
		to_remove_selection.clear();
	}

	public float getMaxX(){
		return maxX;
	}

	public float getMaxY(){
		return maxY;
	}


	public void collision(){
		for(Objet o : elements){
			for(Objet i:elements){
				if(i.getBox().intersects(o.getBox()) && i!=o){
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
	private void selection(Rectangle select) {
		for(Objet o: this.elements){
			if(o.getBox().intersects(select)){
				this.add_selection(o);
			}
		}
	}
	
	public boolean isSelected(Objet o){
		return this.selection.contains(o);
	}

	public Vector<Objet> getEnnemiesInSight(Objet caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Objet o : this.elements){
			if(o.getCamps()!=0 && o.getCamps()!=caller.getCamps() && o.getBox().intersects(caller.getSightRange())){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	
	public Vector<Objet> getRessourcesInSight(Objet caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Objet o : this.elements){
			if(o.getCamps()==0 && o.getBox().intersects(caller.getSightRange())){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	
	public Vector<Objet> getAllInSight(Objet caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Objet o : this.elements){
			if(o.getBox().intersects(caller.getSightRange())){
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
		Vector<Objet> target = new Vector<Objet>() ;
		// If new objective update the target list
		if(new_objective){
			for(Objet o:this.elements){
				if(o.getBox().contains(point)){
					target.add(o);
				}
			}
			// For all objects in selection do action
			if(target.size()==0){
				// We add the target as a void element belonging to nature 
				target.addElement(new Marqueur(this,x,y,10f,0f,0f,0,100));
			}
		}
		// Apply actions for all the  objects, different behaviour considering selection or not
		for(Objet o: this.elements){
			if(this.selection.contains(o)){
				o.action(target);
			}
			else{
				o.action(new Vector<Objet>());
			}
		}

	}
}
