package model;

import java.util.Vector;

import org.newdawn.slick.Color;

import buildings.BuildingHeadquarters;
import data.Data;
import main.Main;

public class GameTeam {

	public Vector<Player> players;
	public Data data;
	Plateau plateau;
	public int id;
	public Civilisation civ;
	public String civName;
	public String colorName;
	public int food;
	public int gold;
	public int special;
	public int pop;
	public BuildingHeadquarters hq ;
	public int maxPop;
	public Color color;
	
	
	public GameTeam(Vector<Player> players, Plateau plateau, int id, int civ) {
		this.players = players;
		this.id = id;
		this.data = new Data();
		if(id==0){
			color = Colors.team0;
			colorName = "neutral";
		}
		else if(id==1){
			color = Colors.team1;
			colorName = "blue";
		}
		else if(id==2){
			color = Colors.team2;
			colorName = "red";
		}
		this.plateau = plateau;
		
		switch(civ){
		case 0 : civName = "Dualists"; this.civ = new Civilisation("dualists",this);break;
		case 1 : civName = "Zinaids";this.civ = new Civilisation("zinaids",this);break;
		case 2 : civName = "kitano";this.civ = new Civilisation("kitanos",this);break;
		default:
		}
		this.maxPop = 15;
		this.gold = 50;
		this.food = 100;
	}
	
	
}
