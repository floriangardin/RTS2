package mybot;

import java.util.HashMap;
import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import bot.IAUnit;
import data.Attributs;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public class IAKevin extends IA {


	public boolean hasAllyCaserne = false;
	public boolean hasAllyTour = true;
	public boolean hasAllyFarm = false;
	public boolean hasAllyMine = false;
	public boolean hasEnnemyCaserne = true;
	public boolean hasEnnemyFarm = true;
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


	public IAKevin(Team team, Plateau plateau) {
		super(team, plateau);
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

	public boolean updateHasEnemyBuilding(IAUnit building){
		return building.getGameTeam()==2-allyHQ.getGameTeam();
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
//		if(enemyCaserne!=null){
//			hasEnnemyCaserne = enemyCaserne.getGameTeam()==2-allyHQ.getGameTeam();
//		}
		if(enemyFarm!=null){
			hasEnnemyFarm = updateHasEnemyBuilding(enemyFarm);
		}
		if(enemyMine!=null){
			hasEnnemyMine = updateHasEnemyBuilding(enemyMine);
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


	public float minDist = 600f;
	public IAUnit minLancier = null;

	public void refreshUnemployed(){
		unemployed = new Vector<IAAllyObject>();
		// declarer etat durgence
		minDist = 600f;
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
			} else if(attCaserne.contains(iao.getId())){
				if(!hasEnnemyCaserne || IAUnit.distance(iao, enemyCaserne)<iao.getAttribut(Attributs.sight) && enemyCaserne.getGameTeam()==allyHQ.getGameTeam()){
					attCaserne.removeElement(iao.getId());
					unemployed.add(iao);
					hasEnnemyCaserne = false;
					this.launchTaunt("alafraiche");
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
	
	boolean tauntPhilippe = false;
	boolean tauntVerdi = false;
	boolean tauntAlaFraiche = false;
	int round = 0;
	
	public void launchTaunt(String taunt){
		switch(taunt){
		case "philippe":
			if(!tauntPhilippe){
				tauntPhilippe=true;
				this.sendMessage("/"+taunt);				
			}
			break;
		case "verdi":
			if(!tauntVerdi){
				tauntVerdi=true;
				this.sendMessage("/"+taunt);				
			}
			break;
		case "alafraiche":
			if(!tauntAlaFraiche){
				tauntAlaFraiche=true;
				this.sendMessage("/"+taunt);				
			}
			break;
		default:
		}
	}
	
	float memLifepoints = 0;
	int memNbUnit = 0;

	boolean initedBuilding=false;
	int nbSpearman = 0;
	int nbInquisitor = 0;
	Vector<Integer> isOnFire = new Vector<Integer>();
	HashMap<IAAllyObject, Integer> temp = new HashMap<IAAllyObject, Integer>();

	public void refreshIsOnFire(){
		isOnFire.clear();
		for(IAAllyObject iao : getUnits()){
			if(iao.getName()==ObjetsList.Inquisitor){
				if(temp.containsKey(iao.getId())){
					if(temp.get(iao.getId())!=iao.getLifepoints()){
						isOnFire.add(iao.getId());
					}
					temp.put(iao, (int)(iao.getLifepoints()));
				}
			}
		}
	}

	@Override
	public void update() {
		round++;
		if(getUnits().size()==0){
			return;
		}
		nbInquisitor=0;
		nbSpearman=0;
		for(IAAllyObject iao : getUnits()){
			if(iao.getName()==ObjetsList.Spearman)
				nbSpearman+=1;
			if(iao.getName()==ObjetsList.Inquisitor)
				nbInquisitor+=1;
		}
		int curNbUnit=0;
		float curLifepoints=0f;
		for(IAUnit iau : getEnemies()){
			if(ObjetsList.getUnits().contains(iau.getName())){
				curNbUnit+=1;
				curLifepoints+=iau.getLifepoints();
			}
		}
		if(memNbUnit==curNbUnit){
			if(curLifepoints<memLifepoints-15f){
				this.launchTaunt("verdi");
			}
		} else {
			memNbUnit=curNbUnit;
		}
		if(enemyHQ!=null && enemyHQ.getLifepoints()<10){
			this.launchTaunt("philippe");
		}
		Vector<IAAllyObject> v = this.getUnits();
		if(!initedBuilding){
			initedBuilding = true;
			initBuildings();
		}
		refreshUnemployed();
		refreshBuildings();
		int compt1 = 0;
		for(IAAllyObject iao : getUnits()){
			if(iao.getName()==ObjetsList.Tower || iao.getName()==ObjetsList.Headquarters){
				continue;
			}
			if(iao.getName()==ObjetsList.Inquisitor || iao.getName()==ObjetsList.Crossbowman){
//				iao.rightClick(iao.getNearestEnemyCharacter());
				continue;
			}
			compt1+=1;
			if(iao.getName()==ObjetsList.Barracks){
				ObjetsList ol = null;
				if(nbInquisitor<2)
					ol = ObjetsList.Inquisitor;
				else
					ol = ObjetsList.Spearman;
				if(this.getAttribut(ol, Attributs.foodCost)<=this.getFood())
					iao.produceUnit(ol);
			}
			if(defFarm.size()==0 && !hasAllyFarm){
				defFarm.add(iao.getId());
			}else if(defCaserne.size()==0 && !hasAllyCaserne){
				defCaserne.add(iao.getId());
			}else if(attCaserne.size()>-1 && hasEnnemyCaserne){
				attCaserne.add(iao.getId());
			}else if(getUnits().size()<4 && hasEnnemyTour){
				attTour.add(iao.getId());
			}else if(getUnits().size()<4){
				attHQ.add(iao.getId());
			}
			//			System.out.println("defFarm:"+defFarm.size());
			//			System.out.println("defCaserne:"+defCaserne.size());
			//			System.out.println("defMine:"+defMine.size());
		}
		//		System.out.println(compt1);
		// on vérifie que les ordres sont obéis
		for(IAAllyObject iao : getUnits()){
			if(iao.getName()==ObjetsList.Inquisitor){
//				if(isOnFire.contains(iao.getId())&&round%100!=0){
//					iao.rightClick(650,1000);
//					continue;
//				}
				if(minLancier!=null){
					iao.rightClick(minLancier);
					continue;
				}
				iao.rightClick(allyHQ.getNearestEnemyCharacter());
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
