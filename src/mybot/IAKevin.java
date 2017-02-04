package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import bot.IAUnit;
import data.Attributs;
import model.Game;
import model.Player;
import utils.ObjetsList;

public class IAKevin extends IA {


	public boolean hasAllyCaserne = false;
	public boolean hasAllyTour = true;
	public boolean hasAllyFarm = false;
	public boolean hasAllyMine = false;
	public boolean hasEnnemyCaserne = false;
	public boolean hasEnnemyFarm = false;
	public boolean hasEnnemyMine = false;
	public boolean hasEnnemyTour = true;

	public Vector<Integer> defFarm = new Vector<Integer>();
	public Vector<Integer> defMine = new Vector<Integer>();
	public Vector<Integer> defCaserne = new Vector<Integer>();
	public Vector<Integer> attFarm = new Vector<Integer>();
	public Vector<Integer> attMine = new Vector<Integer>();
	public Vector<Integer> attCaserne = new Vector<Integer>();
	public Vector<Integer> attTour = new Vector<Integer>();
	public Vector<Integer> attHQ = new Vector<Integer>();
	public Vector<Integer> def = new Vector<Integer>();
	public Vector<Vector<Integer>> vectors = new Vector<Vector<Integer>>();
	
	//	public Vector<IAAllyObject> employed = new Vector<IAAllyObject>();
	public Vector<IAAllyObject> unemployed = new Vector<IAAllyObject>();

	public IAUnit allyCaserne;
	public IAUnit allyFarm;
	public IAUnit allyMine;
	public IAUnit allyTour;
	public IAUnit allyHQ;
	public IAUnit enemyCaserne;
	public IAUnit enemyFarm;
	public IAUnit enemyMine;
	public IAUnit enemyTour;
	public IAUnit enemyHQ;


	public IAKevin(Player p) {
		super(p);
		vectors.add(defCaserne);
		vectors.add(defFarm);
		vectors.add(defMine);
		vectors.add(attCaserne);
		vectors.add(attFarm);
		vectors.add(attMine);
		vectors.add(attTour);
		vectors.add(attHQ);
	}
	
	public void initBuildings(){
		IAUnit iau = this.getUnits().get(0);
		allyCaserne = iau.getNearestNeutral(ObjetsList.Barracks);
		allyMine = iau.getNearestNeutral(ObjetsList.Mine);
		allyFarm = iau.getNearestNeutral(ObjetsList.Mill);
		for(IAUnit iau2 : this.getNature()){
			if(iau2.getName().toString().equals("Barracks") && !iau2.equals(allyCaserne)){
				enemyCaserne = iau2;
			}
			if(iau2.getName().toString().equals("Mill") && !iau2.equals(allyFarm)){
				enemyFarm = iau2;
			}
			if(iau2.getName().toString().equals("Mine") && !iau2.equals(allyMine)){
				enemyMine = iau2;
			}
		}
		enemyTour = iau.getNearestNeutral(ObjetsList.Tower);
		enemyHQ = iau.getNearestNeutral(ObjetsList.Headquarters);
		allyTour = iau.getNearestAlly(ObjetsList.Tower);
		allyHQ = iau.getNearestAlly(ObjetsList.Headquarters);
	}

	public void refreshBuildings(){
		if(allyCaserne!=null){
			hasAllyCaserne = allyCaserne.getGameTeam()==this.getPlayer().id;
		}
		if(allyFarm!=null){
			hasAllyFarm = allyFarm.getGameTeam()==this.getPlayer().id;
		}
		if(allyMine!=null){
			hasAllyMine = allyMine.getGameTeam()==this.getPlayer().id;
		}
		if(allyTour!=null){
			hasAllyTour = allyTour.getGameTeam()==this.getPlayer().id;
		}
		if(enemyCaserne!=null){
			hasEnnemyCaserne = enemyCaserne.getGameTeam()!=this.getPlayer().id;
		}
		if(enemyFarm!=null){
			hasEnnemyFarm = enemyFarm.getGameTeam()!=this.getPlayer().id;
		}
		if(enemyMine!=null){
			hasEnnemyMine = enemyMine.getGameTeam()!=this.getPlayer().id;
		}
		
		//		System.out.println("hasAllyFarm "+hasAllyFarm);
		//		System.out.println("hasAllyCaserne "+hasAllyCaserne);
		//		System.out.println("hasAllyMine "+hasAllyMine);
		//		System.out.println("hasAllyTour "+hasAllyTour);
		//		System.out.println("hasEnnemyCaserne "+hasEnnemyCaserne);
		//		System.out.println("hasEnnemyFarm "+hasEnnemyFarm);
		//		System.out.println("hasEnnemyTour "+hasEnnemyTour);
		//		System.out.println("hasEnnemyMine "+hasEnnemyMine);
	}

	
	public float minDist = 300f;
	public IAUnit minLancier = null;
	
	public void refreshUnemployed(){
		unemployed = new Vector<IAAllyObject>();
		// declarer etat durgence
		minDist = 300f;
		minLancier = null;
		float d = minDist+1;
		try{
			d = IAUnit.distance(allyHQ.getNearestEnemy(ObjetsList.Spearman),allyHQ);
		}catch(Exception e){
			
		}
		if(d<minDist){
			minDist = d;
			minLancier = allyHQ.getNearestEnemy(ObjetsList.Spearman);
		}
		//handle dead people
		Vector<Integer> alive = new Vector<Integer>();
		for(IAAllyObject iao : this.getUnits()){
			alive.add(iao.getId());
		}
		Vector<Integer> toRemove = new Vector<Integer>();
		for(Vector<Integer> v : vectors){
			toRemove = new Vector<Integer>();
			for(Integer i : v){
				if(!alive.contains(i) || minLancier!=null)
					toRemove.add(i);
			}
			for(Integer i : toRemove){
				v.removeElement(i);
			}
		}
		for(IAAllyObject iao : this.getUnits()){
			if(iao.getName()!=ObjetsList.Spearman){
				unemployed.add(iao);
				continue;
			}
			if(defFarm.contains(iao.getId())){
				if(hasAllyFarm){
					defFarm.removeElement(iao.getId());
					unemployed.add(iao);
				}
			} else if(defMine.contains(iao.getId())){
				if(hasAllyMine){
					defMine.removeElement(iao.getId());
					unemployed.add(iao);
				}
			} else if(defCaserne.contains(iao.getId())){
				if(hasAllyCaserne){
					defCaserne.removeElement(iao.getId());
					unemployed.add(iao);
				}
			}  else if(attCaserne.contains(iao.getId())){
				if(!hasEnnemyCaserne){
					attCaserne.removeElement(iao.getId());
					unemployed.add(iao);
				}
			}  else if(attFarm.contains(iao.getId())){
				if(!hasEnnemyFarm){
					attFarm.removeElement(iao.getId());
					unemployed.add(iao);
				}
			}  else if(attMine.contains(iao.getId())){
				if(!hasEnnemyMine){
					attMine.removeElement(iao.getId());
					unemployed.add(iao);
				}
			}  else if(attTour.contains(iao.getId())){
				if(!hasEnnemyTour || IAUnit.distance(iao, enemyTour)<iao.getAttribut(Attributs.sight) && enemyTour.getGameTeam()==0){
					attTour.removeElement(iao.getId());
					unemployed.add(iao);
					hasEnnemyTour = false;
				}
			}  else{
				unemployed.add(iao);
			}
		}
	}

	boolean initedBuilding=false;

	@Override
	public void update() {
		if(getUnits().size()==0){
			return;
		}
		Vector<IAAllyObject> v = this.getUnits();
		if(!initedBuilding){
			initedBuilding = true;
			initBuildings();
		}
		refreshUnemployed();
		refreshBuildings();
		int compt1 = 0;
		for(IAAllyObject iao : unemployed){
			if(iao.getName()==ObjetsList.Tower || iao.getName()==ObjetsList.Headquarters){
				continue;
			}
			if(iao.getName()==ObjetsList.Inquisitor || iao.getName()==ObjetsList.Crossbowman){
				iao.rightClick(iao.getNearest(ObjetsList.Spearman));
				continue;
			}
			compt1+=1;
			if(iao.getName()==ObjetsList.Barracks){
				ObjetsList ol = null;
				if(getUnits().size()>getEnemies().size()+2)
					ol = ObjetsList.Spearman;
				else
					ol = ObjetsList.Inquisitor;
				if(this.getAttribut(ol, Attributs.foodCost)<=this.getFood())
					iao.produceUnit(ol);
			}
			if(defFarm.size()==0 && !hasAllyFarm){
				defFarm.add(iao.getId());
			}else if(defCaserne.size()==0 && !hasAllyCaserne){
				defCaserne.add(iao.getId());
//			}else if(defMine.size()==0 && !hasAllyMine){
//				defMine.add(iao.getId());
			}else if(attTour.size()<2 && hasEnnemyTour){
				attTour.add(iao.getId());
			}else if(true){
				attHQ.add(iao.getId());
			}
//			System.out.println("defFarm:"+defFarm.size());
//			System.out.println("defCaserne:"+defCaserne.size());
//			System.out.println("defMine:"+defMine.size());
		}
//		System.out.println(compt1);
		// on vérifie que les ordres sont obéis
		for(IAAllyObject iao : getUnits()){
			if(minLancier!=null){
				iao.rightClick(minLancier);
				continue;
			}
			if(defFarm.contains(iao.getId())){
				iao.rightClick(allyFarm);
			}
			if(defCaserne.contains(iao.getId())){
				iao.rightClick(allyCaserne);
			}
			if(defMine.contains(iao.getId())){
				iao.rightClick(allyMine);
			}
			if(attFarm.contains(iao.getId())){
				iao.rightClick(enemyFarm);
			}
			if(attCaserne.contains(iao.getId())){
				iao.rightClick(enemyCaserne);
			}
			if(attMine.contains(iao.getId())){
				iao.rightClick(enemyMine);
			}
			if(attTour.contains(iao.getId())){
				iao.rightClick(enemyTour);
			}
			if(attHQ.contains(iao.getId())){
				iao.rightClick(enemyHQ);
			}
		}
	}

}
