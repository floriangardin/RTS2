package pathfinding;

import java.util.Vector;

import plateau.Character;

public class Case implements java.io.Serializable {
	
	public boolean ok;
	
	private IdTerrain idTerrain;
	
	public int id;
	
	public float x;
	public float y;
	
	public int i, j;
	
	public float sizeX;
	public float sizeY;

	public Vector<Character> characters = new Vector<Character>();
	public Vector<Character> surroundingChars = new Vector<Character>();
	
	public Case(boolean ok, int id, MapGrid map){
		this.ok = ok;
		this.id = id;
		this.idTerrain = IdTerrain.GRASS;
	}
	
	public void setIdTerrain(IdTerrain idTerrain){
		this.ok = (!this.idTerrain.ok || this.ok) & idTerrain.ok;
		this.idTerrain = idTerrain;
	}

	public IdTerrain getIdTerrain(){
		return this.idTerrain;
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
		WATER(false),
		GRASS(true),
		SAND(true);
		
		public final boolean ok;
		private IdTerrain(boolean ok){
			this.ok = ok;
		}
	}
	
	
}
