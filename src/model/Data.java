package model;

import spells.SpellBlessedArea;
import spells.SpellConversion;
import spells.SpellFirewall;
import spells.SpellImmolation;
import spells.SpellInstantDeath;
import spells.SpellInstantHealth;
import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;
import units.UnitsList;

public class Data {


	GameTeam gameteam;
	Plateau p;

	public final float ACC;
	public final float FROT;
	public final int FRAMERATE;


	//// UNITS

	public UnitSpearman spearman;
	public UnitKnight knight;
	public UnitPriest priest;
	public UnitInquisitor inquisitor;
	public UnitCrossbowman crossbowman;
	public UnitArchange archange;
	
	public float damageFactor = 1f;
	public float healthFactor = 1.5f;
	
	//// BUILDINGS STATS
	//headQuarters
	public int headQuartersLifePoints = 30;
	public float headQuartersSight = 500f;
	public float headQuartersSizeX = 300f;
	public float headQuartersSizeY = 200f;
	// Mill
	public int millChargeTime = 5;
	public int millLifePoints = 75;
	public float millSight = 300f;
	public float millSizeX = 300f;
	public float millSizeY = 200f;

	// Mine
	public int mineChargeTime = 5;
	public int mineLifePoints = 75;
	public float mineSight = 300f;
	public float mineSizeX = 300f;
	public float mineSizeY = 200f;

	// Barrack
	public int barrackLifePoints = 100;
	public float barrackSight = 500f;
	public float barrackSizeX = 400f;
	public float barrackSizeY = 250f;

	//Stable
	public int stableLifePoints = 200;
	public float stableSight = 300f;
	public float stableSizeX = 400f;
	public float stableSizeY = 220f;

	// Academy 
	public int academyLifePoints = 200;
	public float academySight = 300f;
	public float academySizeX = 300f;
	public float academySizeY = 200f;

	// University 
	public int universityLifePoints = 200;
	public float universitySight = 300f;
	public float universitySizeX = 350f;
	public float universitySizeY = 250f;


	//// Spells

	public SpellFirewall firewall;
	public SpellBlessedArea blessedArea;
	public SpellImmolation immolation;
	public SpellConversion conversion;
	public SpellInstantHealth instantHealth;
	public SpellInstantDeath instantDeath ;

	//// Special

	public float gainedFaithByImmolation = 1f;


	//// Attack Bonuses

	public float bonusSpearHorse = 3f;
	public float bonusSwordBow = 2f;
	public float bonusBowFoot = 3f;
	public float bonusWandBow = 2f;

	// Ressources
	public int bonusFood = 0;
	public int bonusGold = 0;
	public Data(Plateau plateau, GameTeam gameteam,int framerate){
		this.p = plateau;
		this.gameteam = gameteam;
		this.ACC = 40f;
		this.FROT = 1f;
		this.FRAMERATE = framerate;
		// Init spells
		this.firewall = new SpellFirewall(p,gameteam);
		this.blessedArea = new SpellBlessedArea(p,gameteam);
		this.immolation = new SpellImmolation(p,gameteam);
		this.conversion = new SpellConversion(p,gameteam);
		this.instantDeath = new SpellInstantDeath(p,gameteam);
		this.instantHealth = new SpellInstantHealth(p,gameteam);
		// Init unit 
		this.spearman = new UnitSpearman(p,gameteam,this);
		this.crossbowman = new UnitCrossbowman(p,gameteam,this);
		this.knight = new UnitKnight(p,gameteam,this);
		this.priest = new  UnitPriest(p,gameteam,this);
		this.inquisitor = new UnitInquisitor(p,gameteam,this);
		this.archange = new UnitArchange(p,gameteam,this);
		//this.test = new UnitTest(p,player,this);
	}

	public Character create(UnitsList which,float x, float y){
		Character c;
		switch(which){
		case Spearman:
			c =  new UnitSpearman(this.spearman,x ,y,-1);	
			break;
		case Knight:
			c = new UnitKnight(this.knight,x ,y,-1);
			break;
		case Priest:
			c =  new UnitPriest(this.priest,x ,y,-1);
			break;	
		case Crossbowman:
			c =  new UnitCrossbowman(this.crossbowman,x ,y,-1);
			break;	
		case Inquisitor:
			c =  new UnitInquisitor(this.inquisitor,x ,y,-1);
			break;
		case Archange:
			c = new UnitArchange(this.archange,x,y,-1);
			break;
		default:
			c = null;
		}
		if(c!=null)
			c.setTeam(this.gameteam.id);
		return c;

	}


}
