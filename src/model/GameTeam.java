package model;

import java.util.Vector;

import buildings.BuildingHeadQuarters;
import main.Main;

public class GameTeam {

	public Vector<Player> players;
	public Data data;
	public Plateau plateau;
	public int id;
	public int civ;
	public String civName;
	public int food;
	public int gold;
	public int special;
	public int pop;
	public BuildingHeadQuarters hq ;
	
	public GameTeam(Vector<Player> players, Plateau plateau, int id, int civ) {
		this.players = players;
		this.data = new Data(plateau,this,Main.framerate);
		this.plateau = plateau;
		this.id = id;
		this.civ = civ;
		switch(civ){
		case 0 : civName = "Dualists";break;
		case 1 : civName = "Zinaids";break;
		case 2 : civName = "Japs";break;
		default:
		}
	}
	
	
}
