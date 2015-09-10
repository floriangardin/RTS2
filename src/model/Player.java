package model;

import java.util.Vector;

public class Player {
	
	Vector<ActionObjet> selection;
	Vector<Vector<ActionObjet>> groups;
	Plateau p;
	int team;
	int groupSelection;
	int ennemiesKilled;
	Data data;
	int food;
	int gold;
	int pop;
	BottomBar bottomBar;
	TopBar topBar;
	public Player(Plateau p ,int team) {
		this.p = p;
		this.selection = new Vector<ActionObjet>();
		this.groups = new Vector<Vector<ActionObjet>>();
		for(int i=0; i<10; i++)
			this.groups.add(new Vector<ActionObjet>());
		this.team = team;
		food = 0;
		gold = 0;
		pop = 0;
		groupSelection = -1;
		ennemiesKilled = 0 ;
		this.data = new Data(this.p,this,this.p.constants.FRAMERATE);
	}
	
	

}
