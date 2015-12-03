package IA;

import java.util.Vector;
import units.Character;

public class Group {

	private Vector<Character> group;
	private int number_wished ;
	
	
	public Group(Vector<Character> group){
		this.group = new Vector<Character>();
		this.group = group ;
		number_wished = group.size();
	}
	
	public Group(int number_wished){
		this.group = new Vector<Character>();
		this.number_wished = number_wished;
	}
	
	public void addAll(Vector<Character> toAdd){
		this.group.addAll(toAdd);
	}
	public void add(Character c ){
		this.group.addElement(c);
	}
	public void remove(Character c){
		this.group.remove(c);
	}
	public void removeAll(Vector<Character> toAdd){
		this.group.removeAll(toAdd);
	}
	
	public void clear(){
		this.group.clear();
	}
	
	
	
}
