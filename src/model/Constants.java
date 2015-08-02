package model;

public class Constants {

	public final float ACC;
	public final float FROT;
	public final int FRAMERATE;
	
	public Constants(int framerate){
		this.ACC = 0.01f;
		this.FROT = 4f;
		this.FRAMERATE = framerate;
	}
	
}
