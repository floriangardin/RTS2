package ressources;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import control.Player;
import plateau.Building;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;


public class SoundManager {
	public static boolean firstRound = true;
	public static boolean isInit = false;
	public static HashMap<Integer, ObjetsList> selection = new HashMap<Integer, ObjetsList>();
	public static HashMap<Integer, ObjetsList> units = new HashMap<Integer, ObjetsList>();
	public HashMap<SoundList, Integer> timeRemaining = new HashMap<SoundList, Integer>(); // Time between successive sound
	
	public enum SoundList{
		etrange(1),
		heureux(1),
		mystere_decouverte(1),
		triste(1),
		peur(100),
		reve(1);
		
		int timeBetween ;
		private SoundList(int timeBetween){
			this.timeBetween = timeBetween;
		}
	}
	
	public static void init(){
		isInit = true;
	}
	public static void update(Plateau plateau){
		if(!isInit){
			return;
		}
		if(!firstRound){
			
			List<ObjetsList> newDistinctObjects = newDistinctObjects(plateau);
			// Deal with new units or building
		}else{
			firstRound = false;
		}
		updateHashMap(plateau);
	}
	
	public static void updateHashMap(Plateau plateau){
		List<Objet> state = plateau.get()
				.filter(x -> x.team.id==Player.team)
				.collect(Collectors.toList());
		units.clear();
		for(Objet s : state){
			units.put(s.id, s.name);
		}
	}
	public static List<ObjetsList> newDistinctObjects(Plateau plateau){
		// Get differentiation with old hashmap
		return plateau.get()
				.filter(x -> !units.containsKey(x.id) && x.team.id==Player.team)
				.map(x -> x.name)
				.distinct()
				.collect(Collectors.toList());
	}
	
	public static List<ObjetsList> buildingTaking(Plateau plateau){
		// Get differentiation with old hashmap
		return plateau.get()
				.filter(x -> x.getTeam().id != Player.getTeamId())
				.filter(x -> x instanceof Building)
				.map(x -> (Building) x)
				.filter(x -> x.isUnderAttack())
				.map(x -> x.name)
				.distinct()
				.collect(Collectors.toList());
	}
	
}
