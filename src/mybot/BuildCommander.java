package mybot;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import bot.IA;
import iamission.MissionBuilding;
import utils.ObjetsList;

public class BuildCommander {

	public List<ObjetsList> orders;
	public List<Boolean> ok;
	public int cursor;
	public BuildCommander(){
		
	}
	public BuildCommander(List<ObjetsList> orders){
		this.orders = orders;
		this.ok = orders.stream()
				.map(x-> false)
				.collect(Collectors.toList());
	}
	
	public static BuildCommander classicBo(){
		Vector<ObjetsList> bo = new Vector<ObjetsList>();
		bo.add(ObjetsList.Barracks);
		bo.add(ObjetsList.Mill);
		bo.add(ObjetsList.Mine);
		return new BuildCommander(bo);
	}
	
	public  MissionBuilding getOrder(IA ia){
		if(cursor>=orders.size()){
			return null;
		}
		MissionBuilding res = new MissionBuilding(ia, orders.get(cursor));
		cursor++;
		return res;
	}
}
