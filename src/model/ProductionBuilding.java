package model;

import java.util.Vector;

public abstract class ProductionBuilding extends Building {

	
	Vector<UnitsList> productionList;
	Vector<Integer> queue ;
	Vector<Float> productionTime;
	float charge;
	boolean isProducing;
	
	public void product(int unit){
		
	}
	
}