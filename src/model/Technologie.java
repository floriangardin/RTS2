package model;

import org.newdawn.slick.Image;

public class Technologie {

	
	
	int ageRequired;
	float prodTime;
	Technologie techRequired;
	String name;
	Image icon;
	
	public Technologie(int ageRequired,float prodTime,String name){
		this.ageRequired = ageRequired ;
		this.prodTime = prodTime;
		this.name = name;
		
	}
}
