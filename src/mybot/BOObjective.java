package mybot;
import java.util.Vector;

import bot.IA;
import utils.ObjetsList;
public class BOObjective {

	IAFlo ia;
	
	ObjetsList finalObjective ;
	ObjetsList currentObjective;
	 
	Vector<Vector<BOObjective>> childObjectives= new Vector<Vector<BOObjective>>();
	BOObjective subObjective ;
	
	BOObjective(IAFlo ia,ObjetsList finalObjective){
		this.ia = ia;
		this.finalObjective = finalObjective;
		getOrdersToGet(this);
	}
	/*
	 * Return the ordered necessary things to do in order to get o
	 * If we have everything we need it returns an empty vector
	 */
	public static void getOrdersToGet(BOObjective obj){
		// Get necessary building tech etc ... and check that we have it
		// Create child leaf to be completed first recursively ...
		
		// First check if we have the requirements
		if(!obj.ia.hasRequirements(obj.finalObjective)){
			// We find the requirements
			Vector<Vector<ObjetsList>> requirements = factorizeRequirements(obj.ia.getUnsatisfiedRequirements(obj.finalObjective));
			// On imagine pour le moment que requirements est déjà factorisé au maximum
			// Donc pour le moment vu que c'est pas factorisé ce n'est pas optimal
			// PRINT REQUIREMENTS
			for(Vector<ObjetsList> r : requirements){
				for(ObjetsList re : r){
					System.out.print(re+" ");
				}
				System.out.println();
			}
			// Construit les noeuds fils 
			
			for(Vector<ObjetsList> ol : requirements){
				Vector<BOObjective> toAdd = new Vector<BOObjective>();
				for(ObjetsList o : ol){
					toAdd.add(new BOObjective(obj.ia,o));
				}
			}
			
		}else{
			obj.currentObjective = obj.finalObjective;
		}
		
		
	}
	
	public boolean isTerminal(){
		return this.finalObjective == this.currentObjective;
	}
	public ObjetsList getObjective(){
		
		// Find a terminal node and do it!
		if(isTerminal()){
			return finalObjective;
		}else{
			BOObjective obj = findTerminalChild();
			if(obj!=null){
				return obj.finalObjective;
			}else{
				return getAChild().finalObjective;
			}
			
		}
		
		
	}
	
	public void update(){
		
	}
	
	public BOObjective findTerminalChild(){
		
		return null;
	}
	
	public BOObjective getAChild(){
		for(Vector<BOObjective> bo : childObjectives){
			for(BOObjective b : bo){
				return b;
			}
		}
		return null;
	}
	
	public static Vector<Vector<ObjetsList>> factorizeRequirements(Vector<Vector<ObjetsList>> o){
		// Pour chaque requirements présentant une intersection on prend le plus petit des deux
		// TODO : Body of method !
		return o;
	}
	public void finishedChild(ObjetsList o) {
		Vector<Vector<BOObjective>> toDestroy = new Vector<Vector<BOObjective>>();
		for(Vector<BOObjective> b : childObjectives){
			boolean toKeep = true;
			for(BOObjective b1 : b){
				if(b1.finalObjective==o){
					toKeep = false;
				}
			}
			if(!toKeep){
				toDestroy.add(b);
			}
		}
		childObjectives.removeAll(toDestroy);
	}
	
	
}
