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
	
	// Bowman
	public float bmVelocity = 90f;
	public float bmLifePoints = 60f;
	public float bmArmor = 2f; 

	// Wizard
	public float wzVelocity = 60f;
	public float wzLifePoints = 60f;
	public float wzArmor = 3f; 

	// Knight
	public float ktVelocity = 110f;
	public float ktLifePoints = 110f;
	public float ktArmor = 5f; 
	
	// Priest
	public float prVelocity = 110f;
	public float prLifePoints = 60f;
	public float prArmor = 1f; 
	
	
	//// Attack Bonuses
		
	public float bonusSpearHorse = 1.5f;
	public float bonusSwordBow = 1.3f;
	public float bonusBowFoot = 4.3f;
	public float bonusWandBow = 1.5f;
}
