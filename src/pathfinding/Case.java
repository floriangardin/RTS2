package pathfinding;

import java.util.HashSet;
import java.util.Vector;

import plateau.Building;
import plateau.Character;
import plateau.NaturalObjet;

public strictfp class Case implements java.io.Serializable {
	
	public boolean ok;
	
	private IdTerrain idTerrain;
	
	public int id;
	
	public float x;
	public float y;
	
	public int i, j;
	
	public float sizeX;
	public float sizeY;

	public HashSet<Character> characters = new HashSet<Character>();
	public Vector<Character> surroundingChars = new Vector<Character>();
	public HashSet<NaturalObjet> naturesObjet = new HashSet<NaturalObjet>(); 
	public Building building;
	
	public Case(boolean ok, int id, MapGrid map){
		this.ok = ok;
		this.id = id;
		this.idTerrain = IdTerrain.GRASS;
	}
	
	public void setIdTerrain(IdTerrain idTerrain){
		this.ok = (!this.idTerrain.ok || this.ok) & idTerrain.ok;
		this.idTerrain = idTerrain;
	}

	public void setIdTerrain(char id){
		for(IdTerrain it : IdTerrain.values()){
			if(it.id == id){
				this.setIdTerrain(it);
				return;
			}
		}
	}

	public IdTerrain getIdTerrain(){
		return this.idTerrain;
	}
	
	public void update(){
		this.ok = this.idTerrain.ok && building == null && naturesObjet.size()==0;
		this.ok = this.idTerrain.ok && building == null;
	}

	public void updateX(float x, float x1){
		this.x = x;
		this.sizeX = x1-x;
	}
	
	public void updateY(float y, float y1){
		this.y = y;
		this.sizeY = y1-y;
	}
	public void update(float x, float y, float x1, float y1){
		this.x = x;
		this.sizeX = x1-x;
		this.y = y;
		this.sizeY = y1-y;
	}
	
	public String toString(){
		String s ="";
		s+= "case "+id+" = x:" + x+" y:"+y+"   sizeX:"+sizeX+" sizeY:"+sizeY;
		return s;
	}
	
	public enum IdTerrain{
		WATER(false, 'w', 0),
		GRASS(true, ' ', 8),
		SAND(true, 's', 1);
		
		public final boolean ok;
		public final char id;
		public final int alt;
		
		private IdTerrain(boolean ok, char id, int alt){
			this.ok = ok;
			this.id = id;
			this.alt = alt;
		}

		public static Vector<IdTerrain> getSortedIT() {
			int minAlt = 0;
			int maxAlt = 0;
			for(IdTerrain it : values()){
				if(it.alt>maxAlt){
					maxAlt = it.alt;
				}
				if(it.alt<minAlt){
					minAlt = it.alt;
				}
			}
			Vector<IdTerrain> vec = new Vector<IdTerrain>();
			for(int alt = minAlt; alt<=maxAlt; alt++){
				for(IdTerrain it : values()){
					if(it.alt==alt){
						vec.add(it);
					}
				}
			}
			return vec;
		}
	}
	
	
}
