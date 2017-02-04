package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import bot.IAUnit;
import data.Attributs;
import model.Player;
import utils.ObjetsList;

public class IAFlo extends IA{
	
	
	
	int round = 0;
	
	boolean hasTakenTower;
	
	
	public IAFlo(Player p) {
		super(p);
		
	}

	
	@Override
	public void update() {
		
		// Update pour mes characters
		for(IAAllyObject unit : getMyCharacters()){
			// Si c'est une unité
			IAUnit target =  unit.getTarget();
			if(target.isNotUnit()){
				// Si j'ai pas de caserne et que personne n'en target une
				if(this.getPop()>=this.getMaxPop()){
					unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Mine));
				}
				else if(!this.has(ObjetsList.Barracks) && !isAlreadyTargeted(ObjetsList.Barracks)){
					unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Barracks));
				}else if(!this.has(ObjetsList.Mill) && !isAlreadyTargeted(ObjetsList.Mill)){
					unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Mill));
				}else{
					// Occupy a tower
					IAUnit nearestTower = unit.getNearestNeutralorEnnemy(ObjetsList.Tower);
					if(!hasTakenTower && unit.getName()==ObjetsList.Spearman && nearestTower!=null && nearestTower.getGameTeam()!=0 && !this.isAlreadyTargeted(ObjetsList.Tower)){
						unit.rightClick(nearestTower);
					}
					// Else Attack Head Quarters with all spearmans
					else if(unit.getName()==ObjetsList.Spearman){
						unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Headquarters));
					}
					// With archer attack at sight
					else{
						IAUnit enemy = unit.getNearestEnemyCharacter();
						if(enemy!=null){
							unit.rightClick(enemy);
						}
					}
				}
			}
			if(!target.isNotUnit()){
				
				// Si batiment pris alors arreter et ne rien faire ...
				if(target.getGameTeam()==this.getPlayer().id){
					unit.stop();
				}
				if(target.getName()==ObjetsList.Tower && target.getGameTeam()==0){
					hasTakenTower = true;
					unit.stop();
				}
			}
		}
		
		// update pour mes batiments
		for(IAAllyObject unit : getMyBuildings()){
			//Produire produire et produire
			if(unit.getQueue().size()==0){
				unit.getAttributList(Attributs.productions);
				Vector<ObjetsList> productionList = unit.getProductionList();
				ObjetsList o = findWhatToProduce(productionList);
				if(this.getAttribut(o,Attributs.foodCost)<this.getFood() && this.getAttribut(o, Attributs.popTaken)<= this.getMaxPop() - this.getPop() ){
					unit.produce(o);
				}
			}
		}
		
		
		// Réagir à une situation d'urgence (exemple ennemi trop proche)
		
		// Pour mes buildings, prévenir du danger
		
		// Pour mes characters attaquer s'ils se font target
		
		
		round++;	
		
	}
	private ObjetsList findWhatToProduce(Vector<ObjetsList> productionList){
		//TODO
		// Get representation of all and take equal
		int countLancier = this.getUnits(ObjetsList.Spearman).size();
		int countArcher = this.getUnits(ObjetsList.Crossbowman).size();
		int countWizard = this.getUnits(ObjetsList.Inquisitor).size();
		if(countLancier<3){
			return ObjetsList.Spearman;
		}
		if(countWizard<=countArcher){
			return ObjetsList.Inquisitor;
		}
		if(countArcher<countLancier){
			return ObjetsList.Crossbowman;
		}
			
		return ObjetsList.Spearman;
	}
	private Vector<IAAllyObject> getMyBuildings(){
		Vector<IAAllyObject> result = new Vector<IAAllyObject>();
		for(IAAllyObject u : getUnits()){
			if(ObjetsList.getBuildings().contains(u.getName())){
				result.add(u);
			}
		}
		return result;
	}
	private Vector<IAAllyObject> getMyCharacters(){
		Vector<IAAllyObject> result = new Vector<IAAllyObject>();
		for(IAAllyObject u : getUnits()){
			if(ObjetsList.getUnits().contains(u.getName())){
				result.add(u);
			}
		}
		return result;
	}
	private Vector<IAAllyObject> getUnits(ObjetsList o){
		Vector<IAAllyObject> result = new Vector<IAAllyObject>();
		for(IAAllyObject u: getUnits()){
			if(u.getName()==o){
				result.add(u);
			}
		}
		return result;
	}
	
	private Vector<IAUnit> getEnemyOrNeutralUnits(ObjetsList o){
		Vector<IAUnit> result = new Vector<IAUnit>();
		for(IAUnit u: getNatureAndEnemies()){
			if(u.getName()==o){
				result.add(u);
			}
		}
		return result;
	}
	private boolean isAlreadyTargeted(ObjetsList o){
//		for(IAUnit b : this.getEnemyOrNeutralUnits(o)){
//			for(IAAllyObject u : getUnits()){
//				if(u.getTarget().equals(o)){
//					return true;
//				}
//			}
//		}
//		
//		return false;
		for(IAAllyObject u : getUnits()){
			if(!u.getTarget().isNull() && u.getTarget().getName()==o){
				return true;
			}
		}
		return false;
	}
	private boolean isAlreadyTargeted(IAUnit unit){
		for(IAAllyObject u : getUnits()){
			IAUnit target = u.getTarget();
			if(target.equals(unit)){
				return true;
			}
		}
		return false;
	}
	
	

	
	private Vector<IAUnit> getAll(){
		Vector<IAUnit> result = new Vector<IAUnit>();
		result.addAll(convert(getUnits()));
		result.addAll(this.getEnemies());
		return result;
	}

	private void findTarget(Vector<IAUnit> units) {
		
	}
	
	
	public Vector<IAUnit> convert(Vector<IAAllyObject> units){
		Vector<IAUnit> result = new Vector<IAUnit>();
		for(IAUnit unit : units){
			result.add(unit);
		}
		return result;
	}
	
	
	
}
