package data;

import org.newdawn.slick.geom.Point;

import main.Main;
import model.GameTeam;
import model.Plateau;
import ressources.Map;
import spells.SpellBlessedArea;
import spells.SpellConversion;
import spells.SpellDash;
import spells.SpellFirewall;
import spells.SpellFrozen;
import spells.SpellImmolation;
import spells.SpellInstantDeath;
import spells.SpellInstantHealth;
import spells.SpellManualArrow;
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
	public float speedFactor = 1.3f;
	
	//// BUILDINGS STATS
	public boolean explosionWhenImmolate= false;
	//headQuarters
	public int headQuartersLifePoints = 30;
	public float headQuartersSight = 500f;
	public static float headquartersSizeX = 3f*Map.stepGrid;
	public static float headquartersSizeY = 2f*Map.stepGrid;
	// Mill
	public int millChargeTime = 5;
	public int millLifePoints = 50;
	public float millSight = 300f;
	public static float millSizeX = 3f*Map.stepGrid;
	public static float millSizeY = 2f*Map.stepGrid;

	// Mine
	public int mineChargeTime = 5;
	public int mineLifePoints = 50;
	public float mineSight = 300f;
	public static float mineSizeX = 3f*Map.stepGrid;
	public static float mineSizeY = 2f*Map.stepGrid;

	// Barrack
	public int barrackLifePoints = 50;
	public float barrackSight = 500f;
	public static float barrackSizeX = 5f*Map.stepGrid;
	public static float barrackSizeY = 3f*Map.stepGrid;

	//Stable
	public int stableLifePoints = 50;
	public float stableSight = 300f;
	public static float stableSizeX = 4f*Map.stepGrid;
	public static float stableSizeY = 3f*Map.stepGrid;

	// Academy 
	public int academyLifePoints = 50;
	public float academySight = 300f;
	public static float academySizeX = 4f*Map.stepGrid;
	public static float academySizeY = 3f*Map.stepGrid;

	// University 
	public int universityLifePoints = 50;
	public float universitySight = 300f;
	public static float universitySizeX = 3f*Map.stepGrid;
	public static float universitySizeY = 3f*Map.stepGrid;
	
	// Tower
	public int towerLifePoints = 50;
	public float towerSight = 400f;
	public float towerChargeTime = 5f;
	public static float towerSizeX = 2f*Map.stepGrid;
	public static float towerSizeY = 2f*Map.stepGrid;

	//Bonus
	public static int bonusLifePoints = 50;
	public static float bonusSight = 100f;
	public static float bonusChargeTime = 100f;
	
	//// Spells

	public SpellFirewall firewall;
	public SpellBlessedArea blessedArea;
	public SpellImmolation immolation;
	public SpellConversion conversion;
	public SpellInstantHealth instantHealth;
	public SpellInstantDeath instantDeath ;
	public SpellManualArrow manualArrow;
	public SpellDash spellDash;
	public SpellFrozen fence;
	//// Special

	public float gainedFaithByImmolation = 1f;


	//// Attack Bonuses

	public float bonusSpearHorse = 2f;
	public float bonusSwordBow = 1.5f;
	public float bonusBowFoot = 2f;
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

		//this.test = new UnitTest(p,player,this);
	}
	public void init(){
		this.firewall = new SpellFirewall(gameteam);
		this.manualArrow = new SpellManualArrow(gameteam);
		this.blessedArea = new SpellBlessedArea(gameteam);
		this.immolation = new SpellImmolation(gameteam);
		this.conversion = new SpellConversion(gameteam);
		this.instantDeath = new SpellInstantDeath(gameteam);
		this.instantHealth = new SpellInstantHealth(gameteam);
		this.spellDash = new SpellDash(gameteam);
		this.fence = new SpellFrozen(gameteam);
		// Init unit 
		this.spearman = new UnitSpearman(gameteam);
		this.crossbowman = new UnitCrossbowman(gameteam);
		this.knight = new UnitKnight(gameteam);
		this.priest = new  UnitPriest(gameteam);
		this.inquisitor = new UnitInquisitor(gameteam);
		this.archange = new UnitArchange(gameteam);
	}
	
	public Data(){
		this.ACC = 40f;
		this.FROT = 1f;
		this.FRAMERATE = Main.framerate;
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

	public static Point getSize(String s){
		switch(s){
		case "academy" : return new Point((int)academySizeX,(int)academySizeY);
		case "tower" : return new Point((int)towerSizeX,(int)towerSizeY);
		case "university" : return new Point((int)universitySizeX,(int)universitySizeY);
		case "barrack" : return new Point((int)barrackSizeX,(int)barrackSizeY);
		case "headquarters" : return new Point((int)headquartersSizeX,(int)headquartersSizeY);
		case "mill" : return new Point((int)millSizeX,(int)millSizeY);
		case "mine" : return new Point((int)mineSizeX,(int)mineSizeY);
		case "stable" : return new Point((int)stableSizeX,(int)stableSizeY);
		default: return new Point(0,0);
		}
	}

}
