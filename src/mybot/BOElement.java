package mybot;

import java.util.Vector;

import bot.IA;
import utils.ObjetsList;

public class BOElement {

	Vector<ObjetsList> requirement  = new Vector<ObjetsList>();
	ObjetsList elementToAchieve;
	
	
	public BOElement(Vector<ObjetsList> requirement){
		this.requirement = requirement;
	}
	
	public void action(){
		if(elementToAchieve==null){
			getEasiestToAchieve();
		}
		
		
	}
	public void getEasiestToAchieve(){
		if(requirement.size()>0){
			elementToAchieve = requirement.get(0);
		}
	}
	
	
	

}
