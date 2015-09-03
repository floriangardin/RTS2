package model;

import java.util.Vector;

public class Player {
	
	Vector<Character> selection;
	Vector<Vector<Character>> groups;
	int team;
	int groupSelection;
	int ennemiesKilled;
	
	int food;
	int gold;
	int pop;
	
	
	public Player(int team) {
		this.selection = new Vector<Character>();
		this.groups = new Vector<Vector<Character>>();
		for(int i=0; i<10; i++)
			this.groups.add(new Vector<Character>());
		this.team = team;
		food = 0;
		gold = 0;
		pop = 0;
		groupSelection = -1;
		ennemiesKilled = 0;
	}
	
	

}
