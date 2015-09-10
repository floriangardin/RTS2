package model;

import java.util.Vector;

import display.BottomBar;
import display.TopBar;
import units.UnitsList;

public class Player {
	public int civ;
	public Vector<ActionObjet> selection;
	public Vector<Vector<ActionObjet>> groups;
	public Plateau p;
	public int team;
	public int groupSelection;
	public int ennemiesKilled;
	public Data data;
	public int food;
	public int gold;
	public int special;
	public int pop;
	public BottomBar bottomBar;
	public TopBar topBar;
	public Player(Plateau p ,int team,int civ) {
		this.civ  = civ;
		this.p = p;
		this.selection = new Vector<ActionObjet>();
		this.groups = new Vector<Vector<ActionObjet>>();
		for(int i=0; i<10; i++)
			this.groups.add(new Vector<ActionObjet>());
		this.team = team;
		food = 0;
		gold = 0;
		pop = 0;
		special = 0;
		groupSelection = -1;
		ennemiesKilled = 0 ;
		this.data = new Data(this.p,this,this.p.constants.FRAMERATE);
	}
	
	
	public void create(UnitsList u,float x , float y){
		this.data.create(u,x,y);
	}
}
