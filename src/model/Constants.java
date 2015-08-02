package model;

public class Constants {

	public final float ACC;
	public final float FROT;
	public final int FRAMERATE;
	public Constants(int framerate){
		this.ACC = 25f;
		this.FROT = 1f;
		this.FRAMERATE = framerate;
	}
}
