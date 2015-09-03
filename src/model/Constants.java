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
	
	// Attack Bonuses
	public float bonusSpearHorse = 1.5f;
	public float bonusSwordBow = 1.3f;
	public float bonusBowFoot = 1.3f;
	public float bonusWandBow = 1.5f;
}
