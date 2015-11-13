package model;

import java.util.Vector;
import units.Character;
public class Cemetery {
	Vector<Character> characters= new Vector<Character>();
	public static int roundToKeep = 200;
	Game game;
	
	public Cemetery(Game game){
		this.game = game;
	}

	public boolean add(Character e) {
		this.characters.addElement(e);
		return false;
	}

	public boolean addAll(Vector<Character> c) {
		this.characters.addAll(c);
		return false;
	}

	public void clear() {
		this.characters.clear();
		
	}

	public boolean contains(Object o) {
		return this.characters.contains(o);
	}
	
	public void update(){
		int i = 0;
		Vector<Character>  toRemove = new Vector<Character>();
		while(i<this.characters.size()){
			this.characters.get(i).deadSince++;
			if(this.characters.get(i).deadSince>Cemetery.roundToKeep){
				toRemove.add(this.characters.get(i));
			}
			i++;
		}
		this.characters.removeAll(toRemove);
	}

	
	
}
