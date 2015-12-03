package IA;

import java.util.HashMap;
import java.util.Vector;

import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;

public class Requirement {
	

	//public int supply;
	
	
	public int spearman;
	public int crossbowman;
	public int knight;
	public int inquisitor;
	public int priest;

	public int mill;
	public int mine;
	public int barrack;
	public int stables;
	public int academy;
	public int university;



	public Requirement(){

	}

	public Requirement(int spearman){
		this.spearman = spearman;
	}

	public Requirement(int spearman,int crossbowman){
		this.spearman = spearman;
		this.crossbowman = crossbowman;
	}
	
	public Requirement(int spearman,int crossbowman,int knight,int inquisitor,int priest){
		this.spearman = spearman;
		this.crossbowman = crossbowman;
		this.knight = knight;
		this.inquisitor = inquisitor;
		this.priest = priest;
	}
	
	public Requirement(int spearman,int crossbowman,int knight,int inquisitor,int priest,int mill,int mine,int barrack,int stables,int academy,int university){
		this.spearman = spearman;
		this.crossbowman = crossbowman;
		this.knight = knight;
		this.inquisitor = inquisitor;
		this.priest = priest;
		this.mill = mill;
		this.mine = mine;
		this.barrack = barrack;
		this.stables = stables;
		this.academy = academy;
		this.university = university;
	}

	public void setUnitsRequirements(int spearman,int crossbowman,int knight,int inquisitor,int priest){
		this.spearman = spearman;
		this.crossbowman = crossbowman;
		this.knight = knight;
		this.inquisitor = inquisitor;
		this.priest = priest;
	}
	
	public void setBuildingsRequirements(int mill,int mine,int barrack,int stables,int academy,int university){
		this.mill = mill;
		this.mine = mine;
		this.barrack = barrack;
		this.stables = stables;
		this.academy = academy;
		this.university = university;
	}

	public boolean requirementSatisfied(Vector<Character> group){
		int spearman = 0;
		int crossbowman= 0;
		int knight = 0;
		int priest = 0;
		int inquisitor = 0;
		for(Character c : group){
			if(c instanceof UnitSpearman){
				spearman++;
			}
			else if(c instanceof UnitCrossbowman){
				crossbowman++;
			}
			else if(c instanceof UnitKnight){
				knight++;
			}
			else if(c instanceof UnitPriest){
				priest++;
			}
			else if(c instanceof UnitInquisitor){
				inquisitor++;
			}
		}
		return spearman>=this.spearman && this.crossbowman>=this.crossbowman &&
				this.knight>=this.knight && priest>=this.priest && inquisitor>=this.inquisitor;
	}
	
	
	public boolean isRequired(Character c,Vector<Character> group) {
		
		
		return false;
	}
	
	
	
}
