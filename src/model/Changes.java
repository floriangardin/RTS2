package model;

import java.util.Vector;

public class Changes {

	//OBJET
	public boolean x = true;
	public boolean y= true;
	public boolean sight= true;
	public boolean lifePoints= true;
	public boolean team= true;

	
	//ACTION OBJET
	public boolean maxLifePoints= true;
	public boolean orientation= true;
	
	//CHARACTER
	public boolean weapon=true;
	public boolean chargeTime = true;
	public boolean state = true;
	public boolean animation = true;
	public boolean isImmolating = true;
	public boolean remainingTime = true;
	public Vector<Boolean> spellState=new Vector<Boolean>();
	
	
	//BUILDING
	public boolean sizeX=true;
	public boolean sizeY=true;
	public boolean potentialTeam=true;
	public boolean constructionPoints=true;
	public boolean rallyPoint= true;
	
	//BUILDING TECH AND PROD
	public boolean queue;
	public boolean idTech;
	public boolean charge;
	public boolean prod;
	public boolean isFinished;
	
	//BUILDING PROD
	public boolean ownerId= true;
	public boolean ownerV=true;
	
	//BULLET
	
	public Changes(){
		for(boolean b :  spellState){
			b=true;
		}
	}
	



}
