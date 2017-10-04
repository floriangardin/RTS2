package stats;

import java.util.HashMap;

import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public strictfp class StateGame {
	
	public HashMap<Integer, HashMap<ObjetsList, Integer>> characterComp;
	public HashMap<Integer, Integer> pop, maxPop, food, towerCount;
	
	public StateGame(Plateau plateau){
		characterComp = new HashMap<Integer, HashMap<ObjetsList, Integer>>();
		pop = new HashMap<Integer, Integer>();
		maxPop = new HashMap<Integer, Integer>();
		food = new HashMap<Integer, Integer>();
		towerCount = new HashMap<Integer, Integer>();
		for(Team team : plateau.getTeams()){
			characterComp.put(team.id, new HashMap<ObjetsList, Integer>());
			pop.put(team.id, team.getPop(plateau));
			maxPop.put(team.id, team.getMaxPop(plateau));
			food.put(team.id, team.food);
			towerCount.put(team.id, 0);
		}
		for(Objet o : plateau.getObjets().values()){
			if(!characterComp.get(o.team.id).containsKey(o.getName())){
				characterComp.get(o.team.id).put(o.getName(), 0);
			}
			characterComp.get(o.team.id).put(o.getName(), characterComp.get(o.team.id).get(o.getName())+1);
		}
	}
	

}
