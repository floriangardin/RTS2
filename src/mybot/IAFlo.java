package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import bot.IAUnit;
import control.InputObject;
import data.Attributs;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public class IAFlo extends IA{
	
	
	
	int round = 0;
	boolean hasTakenTower;
	private boolean hasSendMessagePhilippe;
	private boolean missionRogerLaventurierDone;
	
	public IAFlo(Team team) {
		super(team);
	}

	
	@Override
	public void update(InputObject im) {
		if(round==30){
			this.sendMessage("Tu es un traître, tu as vendu des informations aux Japonais");
		}
		// Update pour mes characters
		for(IAAllyObject unit : getMyCharacters()){
			// Si c'est une unité
			IAUnit target =  unit.getTarget();
			if(target.isNotUnit()){
				// Si j'ai pas de caserne et que personne n'en target une
				if(this.getPop()>=this.getMaxPop()){
					unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Mine), im);
				}
				else if(!this.has(ObjetsList.Barracks) && !isAlreadyTargeted(ObjetsList.Barracks)){
					unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Barracks), im);
				}else if(!this.has(ObjetsList.Mill) && !isAlreadyTargeted(ObjetsList.Mill)){
					unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Mill), im);
				}else{
					// Occupy a tower
					IAUnit nearestTower = unit.getNearestNeutralorEnnemy(ObjetsList.Tower);
					if(!hasTakenTower && unit.getName()==ObjetsList.Spearman && nearestTower!=null && nearestTower.getGameTeam()!=0 && !this.isAlreadyTargeted(ObjetsList.Tower)){
						unit.rightClick(nearestTower, im);
					}
					// Else Attack Head Quarters with all spearmans
					else if(unit.getName()==ObjetsList.Spearman){
						unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Headquarters), im);
					}
					// With archer attack at sight
					else{
						IAUnit enemy = unit.getNearestEnemyCharacter();
						if(enemy!=null){
							unit.rightClick(enemy, im);
						}
					}
				}
			}
			if(!target.isNotUnit()){
				
				// Si batiment pris alors arreter et ne rien faire ...
				if(target.getGameTeam()==this.getPlayer().id){
					if(target.getName()==ObjetsList.Mill && !missionRogerLaventurierDone ){
						unit.stop(im);
						unit.rightClick(unit.getNearestNeutralorEnnemy(ObjetsList.Mill), im);
						this.sendMessage("La mission secrète de Roger commence");
						missionRogerLaventurierDone = true;
					}else{
						unit.stop(im);
					}
				}
				if(target.getName()==ObjetsList.Tower && target.getGameTeam()==0){
					hasTakenTower = true;
					unit.stop(im);
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
					unit.produce(o, im);
				}
			}
		}
		// Réagir à une situation d'urgence (exemple ennemi trop proche)
		Vector<IAUnit> enemiesAttackingBarrack = enemiesAttacking(ObjetsList.Barracks);
		if(enemiesAttackingBarrack.size()>0){
			// TAKE FIRST WIZARD TO DESTROY THEM AHAHAHA
			if(!hasSendMessagePhilippe){
				this.sendMessage("Tu n'auras jamais ma caserne !");
				this.sendMessage("/Philippe");
				hasSendMessagePhilippe = true;
			}
			Vector<IAAllyObject> inquisitors = this.getUnits(ObjetsList.Inquisitor);
			for(IAAllyObject u : inquisitors){
				// TODO : Find first lancier ?
				if(u.getTarget()!=enemiesAttackingBarrack.get(0)){
					u.rightClick(enemiesAttackingBarrack.get(0), im);
				}
			}
		}
		Vector<IAUnit> enemiesAttackingFarm = enemiesAttacking(ObjetsList.Mill);
		if(enemiesAttackingFarm.size()>0){
			// TAKE FIRST WIZARD TO DESTROY THEM AHAHAHA
			if(!hasSendMessagePhilippe){
				this.sendMessage("Tu n'auras jamais ma caserne !");
				this.sendMessage("/Philippe");
				hasSendMessagePhilippe = true;
			}
			Vector<IAAllyObject> inquisitors = this.getUnits(ObjetsList.Inquisitor);
			for(IAAllyObject u : inquisitors){
				// TODO : Find first lancier ?
				if(u.getTarget()!=enemiesAttackingFarm.get(0)){
					u.rightClick(enemiesAttackingFarm.get(0), im);
				}	
			}
		}
		// Pour mes buildings, prévenir du danger
		// Pour mes characters attaquer s'ils se font target
		// Aller en fourbe chercher une ferme
		for(IAAllyObject u: getUnits(ObjetsList.Spearman)){
			
		}
		round++;
		return im;	
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
		for(IAAllyObject u : getMyUnits()){
			if(ObjetsList.getBuildings().contains(u.getName())){
				result.add(u);
			}
		}
		return result;
	}
	private Vector<IAAllyObject> getMyCharacters(){
		Vector<IAAllyObject> result = new Vector<IAAllyObject>();
		for(IAAllyObject u : getMyUnits()){
			if(ObjetsList.getUnits().contains(u.getName())){
				result.add(u);
			}
		}
		return result;
	}
	private Vector<IAAllyObject> getUnits(ObjetsList o){
		Vector<IAAllyObject> result = new Vector<IAAllyObject>();
		for(IAAllyObject u: getMyUnits()){
			if(u.getName()==o){
				result.add(u);
			}
		}
		return result;
	}

	private boolean isAlreadyTargeted(ObjetsList o){
		for(IAAllyObject u : getMyUnits()){
			if(!u.getTarget().isNull() && u.getTarget().getName()==o){
				return true;
			}
		}
		return false;
	}
	private Vector<IAUnit> enemiesAttacking(ObjetsList o){
		Vector<IAUnit> units = new Vector<IAUnit>();
		for(IAUnit u : getEnemies()){
			if(!u.isNull() && !u.getTarget().isNull() && u.getTarget().getName()==o && u.getTarget().getGameTeam()==this.getPlayer().id){
				units.add(u);
			}
		}
		return units;
	}
	
	public Vector<IAUnit> convert(Vector<IAAllyObject> units){
		Vector<IAUnit> result = new Vector<IAUnit>();
		for(IAUnit unit : units){
			result.add(unit);
		}
		return result;
	}


	@Override
	public InputObject select(InputObject im) throws Exception {
		// SELECT MY UNITS 
		return null;
	}
	
	
	
}
