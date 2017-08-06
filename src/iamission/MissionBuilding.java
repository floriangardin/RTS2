package iamission;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bot.IA;
import bot.IAUnit;
import bot.Utils;
import bot.IAUnit.Role;
import utils.ObjetsList;

public class MissionBuilding {
	
	IA ia;
	ObjetsList objective;
	List<Integer> ressources;
	int target;
	boolean finished;
	boolean hasStarted;
	
	public MissionBuilding(IA ia, ObjetsList objective){
		this.ia =ia;
		this.setRessources();
		this.setObjective(objective);
		ia.selectByIds(ressources);
		List<IAUnit> units = ressources.stream()
				.map(x-> ia.getById(x))
				.collect(Collectors.toList());
		ia.select(units);
		ia.rightClick(ia.getById(target));
	}
	public boolean isFinished(){
		return ia.getById(target)!=null && ia.getById(target).getGameTeam()==ia.getTeamId();
	}
	
	public MissionBuilding setObjective(ObjetsList objective){
		this.objective = objective;
		IAUnit captain = this.ia.getById(ressources.get(0));
		this.target = this.ia.getUnits()
				.filter(x-> x.getName()==objective)
				.filter(x-> x.getGameTeam()!=ia.getTeamId())
				.sorted((x,y) -> Float.compare(Utils.distance2(x, captain), Utils.distance2(y, captain)))
				.findFirst()
				.map(x-> x.getId())
				.orElse(-1);
		
		return this;
	}
	public MissionBuilding setRessources(){
		this.ressources = ia.getUnits()
				.filter(x-> x.isFree())
				.filter(x-> x.getRole()==Role.build)
				.filter(x-> x.getGameTeam()==ia.getTeamId())
				.filter(x -> x.getName()==ObjetsList.Spearman)
				.peek(x -> x.setBusy())
				.map(x -> x.getId())
				.collect(Collectors.toList());
		return this;
	}
	
}
