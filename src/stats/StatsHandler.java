package stats;

import java.util.HashMap;

import data.Attributs;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public strictfp class StatsHandler {
	
	public static boolean isInit;
	
	public static HashMap<Integer, StateGame> states;
	public static HashMap<Integer, HashMap<ObjetsList, Float>> damages;
	public static HashMap<Integer, Integer> nbKills;
	public static HashMap<Integer, Float> nbRessourcesSpent;
	public static HashMap<Integer, Float> nbRessourcesDestroyed;
	
	public static void init(Plateau plateau){
		isInit = true;
		states = new HashMap<Integer, StateGame>();
		damages = new HashMap<Integer, HashMap<ObjetsList, Float>>();
		nbKills = new HashMap<Integer, Integer>();
		nbRessourcesSpent = new HashMap<Integer, Float>();
		nbRessourcesDestroyed = new HashMap<Integer, Float>();
		for(Team team : plateau.getTeams()){
			damages.put(team.id, new HashMap<ObjetsList, Float>());
			nbKills.put(team.id, 0);
			nbRessourcesDestroyed.put(team.id, 0f);
			nbRessourcesSpent.put(team.id, 0f);
		}
	}
	
	public static boolean isInit(){
		return isInit();
	}
	
	public static void pushState(Plateau plateau){
		if(!isInit){
			return;
		}
		StateGame newstate = new StateGame(plateau);
		states.put(plateau.getRound(), newstate);
	}
	
	public static void pushDamage(Plateau plateau, Objet launcher, float damage){
		if(!isInit || launcher==null){
			return;
		}
		if(!damages.get(launcher.team.id).containsKey(launcher.getName())){
			damages.get(launcher.team.id).put(launcher.getName(), 0f);
		}
		damages.get(launcher.team.id).put(launcher.getName(), damages.get(launcher.team.id).get(launcher.getName())+damage);
	}
	
	public static void pushKill(Objet o){
		if(!isInit || o==null){
			return;
		}
		nbKills.put(o.team.id, nbKills.get(o.team.id)+1);
		nbRessourcesDestroyed.put(o.team.id, nbRessourcesDestroyed.get(o.team.id)+o.getAttribut(Attributs.foodCost));
	}
	
	public static void pushUnitCreation(Objet o){
		if(!isInit || o==null){
			return;
		}
		nbRessourcesSpent.put(o.team.id, nbRessourcesSpent.get(o.team.id)+o.getAttribut(Attributs.foodCost));
	}
	
	

}
