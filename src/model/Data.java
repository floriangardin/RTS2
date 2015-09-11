package model;

import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;
import units.UnitsList;

public class Data {

	
	Player player;
	Plateau p;
	
	public final float ACC;
	public final float FROT;
	public final int FRAMERATE;

	
	//// UNIT STAT
	
	public UnitSpearman spearman;
	public UnitKnight knight;
	public UnitPriest priest;
	public UnitInquisitor inquisitor;
	public UnitCrossbowman crossbowman;
	
	//// BUILDINGS STATS
	//headQuarters
	public int headQuartersLifePoints = 200;
	public float headQuartersSight = 300f;
	public float headQuartersSizeX = 291f;
	public float headQuartersSizeY = 291f*2f/3f;
	// Mill
	public int millChargeTime = 5;
	public int millLifePoints = 100;
	public float millSight = 300f;
	public float millSizeX = 294f;
	public float millSizeY = 294f*2f/3f;
	
	// Mine
	public int mineChargeTime = 5;
	public int mineLifePoints = 100;
	public float mineSight = 300f;
	public float mineSizeX = 291f;
	public float mineSizeY = 291f*2f/3f;
	
	// Barrack
	public int barrackLifePoints = 200;
	public float spearmanProdTime = 40f;
	public float bowmanProdTime = 60f;
	public float barrackSight = 300f;
	public float barrackSizeX = 285f;
	public float barrackSizeY = 285f*2f/3f;
	
	//Stable
	public int stableLifePoints = 200;
	public float knightProdTime = 40f;
	public float priestProdTime = 60f;
	public float stableSight = 300f;
	public float stableSizeX = 291f;
	public float stableSizeY = 291f*2f/3f;
	
	// Academy 
	public int academyLifePoints = 200;
	public float wizardProdTime = 40f;
	public float academySight = 300f;
	public float academySizeX = 291f;
	public float academySizeY = 291f*2f/3f;
	
	
	//// Attack Bonuses
		
	public float bonusSpearHorse = 2f;
	public float bonusSwordBow = 3f;
	public float bonusBowFoot = 2f;
	public float bonusWandBow = 2f;
	
	// Ressources
	public int bonusFood = 0;
	public int bonusGold = 0;
	public Data(Plateau plateau, Player player,int framerate){
		this.p = plateau;
		this.player = player;
		this.ACC = 40f;
		this.FROT = 1f;
		this.FRAMERATE = framerate;
		// Init unit 
		this.spearman = new UnitSpearman(p,player);
		this.crossbowman = new UnitCrossbowman(p,player);
		this.knight = new UnitKnight(p,player);
		this.priest = new  UnitPriest(p,player);
		this.inquisitor = new UnitInquisitor(p,player);
		
	}
	
	public Character create(UnitsList which,float x, float y){
		
		switch(which){
		case Spearman:
			return  new UnitSpearman(this.spearman,x ,y);	
		case Knight:
			return new UnitKnight(this.knight,x ,y);	
		case Priest:
			return  new UnitPriest(this.priest,x ,y);	
		case Crossbowman:
			return  new UnitCrossbowman(this.crossbowman,x ,y);	
		case Inquisitor:
			return  new UnitInquisitor(this.inquisitor,x ,y);	

		default:
			return null;
		}

		
	}

}
