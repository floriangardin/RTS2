package model;

import java.util.Vector;

public class Player {
	
	Vector<Character> selection;
	Vector<Vector<Character>> groups;
	int team;
	
	
	public Player(int team) {
		this.selection = new Vector<Character>();
		this.groups = new Vector<Vector<Character>>();
		for(int i=0; i<10; i++)
			this.groups.add(new Vector<Character>());
		this.team = team;
	}
	
	

}
