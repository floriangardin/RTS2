package model;

public class Constants {

	public final float ACC;
	public final float FROT;
	public final int FRAMERATE;
	public Constants(int framerate){
		this.ACC = 40f;
		this.FROT = 1f;
		this.FRAMERATE = framerate;
	}
	
	
	//// UNIT STAT
	
	// Spearman
	public float smVelocity = 80f;
	public float smLifePoints = 80f;
	public float smArmor = 4f;
	public float smDamage = 5f;
	public float smChargeTime = 5f; 
	
	// Bowman
	public float bmVelocity = 90f;
	public float bmLifePoints = 60f;
	public float bmArmor = 2f; 
	public float bmDamage = 7f;
	public float bmRange = 100f;
	public float bmChargeTime = 5f;

	// Wizard
	public float wzVelocity = 60f;
	public float wzLifePoints = 60f;
	public float wzArmor = 3f; 
	public float wzChargeTime = 15f;
	public float wzRange = 100f;
	public float wzDamage = 15f;

	// Knight
	public float ktVelocity = 110f;
	public float ktLifePoints = 110f;
	public float ktArmor = 5f; 
	public float ktDamage = 8f;
	public float ktChargeTime = 7f;
	
	// Priest
	public float prVelocity = 110f;
	public float prLifePoints = 60f;
	public float prArmor = 1f; 
	public float prDamage = -1f;
	public float prChargeTime = 0.2f;
	
	
	//// Attack Bonuses
		
	public float bonusSpearHorse = 2f;
	public float bonusSwordBow = 3f;
	public float bonusBowFoot = 2f;
	public float bonusWandBow = 2f;
}
