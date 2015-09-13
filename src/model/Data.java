package model;

import spells.*;
import units.Character;
import units.UnitArchange;
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
	public UnitArchange archange;
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
	public float barrackSight = 300f;
	public float barrackSizeX = 285f;
	public float barrackSizeY = 285f*2f/3f;
	
	//Stable
	public int stableLifePoints = 200;
	public float stableSight = 300f;
	public float stableSizeX = 291f;
	public float stableSizeY = 291f*2f/3f;
	
	// Academy 
	public int academyLifePoints = 200;
	public float academySight = 300f;
	public float academySizeX = 291f;
	public float academySizeY = 291f*2f/3f;
	
	
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
	public Data(Plateau plateau, Player player,int framerate){
		this.p = plateau;
		this.player = player;
		this.ACC = 40f;
		this.FROT = 1f;
		this.FRAMERATE = framerate;
		// Init spells
		this.firewall = new SpellFirewall(p,player);
		this.blessedArea = new SpellBlessedArea(p,player);
		this.immolation = new SpellImmolation(p,player);
		this.conversion = new SpellConversion(p,player);
		this.instantDeath = new SpellInstantDeath(p,player);
		this.instantHealth = new SpellInstantHealth(p,player);
		// Init unit 
		this.spearman = new UnitSpearman(p,player,this);
		this.crossbowman = new UnitCrossbowman(p,player,this);
		this.knight = new UnitKnight(p,player,this);
		this.priest = new  UnitPriest(p,player,this);
		this.inquisitor = new UnitInquisitor(p,player,this);
		this.archange = new UnitArchange(p,player,this);
	}
	
	public Character create(UnitsList which,float x, float y){
		Character c;
		switch(which){
		case Spearman:
			c =  new UnitSpearman(this.spearman,x ,y);	
			c.player = this.player;
			break;
		case Knight:
			c = new UnitKnight(this.knight,x ,y);	
			c.player = this.player;
			break;
		case Priest:
			c =  new UnitPriest(this.priest,x ,y);
			c.player = this.player;
			break;	
		case Crossbowman:
			c =  new UnitCrossbowman(this.crossbowman,x ,y);
			c.player = this.player;
			break;	
		case Inquisitor:
			c =  new UnitInquisitor(this.inquisitor,x ,y);
			c.player = this.player;
			break;
		case Archange:
			c = new UnitArchange(this.archange,x,y);
			c.player = this.player;
			break;
		default:
			c = null;
		}
		return c;
		
	}
	

}
