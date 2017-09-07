package mybot;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import bot.IA;
import iamission.MissionBuilding;
import utils.ObjetsList;

public strictfp class BuildCommander extends Commander{

	public List<ObjetsList> orders;
	public MissionBuilding mission;
	public List<Boolean> ok;
	public int cursor;
	public BuildCommander(IA ia){
		super(ia);
	}
	
	
	public static BuildCommander classicBo(IA ia){
		Vector<ObjetsList> bo = new Vector<ObjetsList>();
		bo.add(ObjetsList.Barracks);
		bo.add(ObjetsList.Mill);
		bo.add(ObjetsList.Mine);
		bo.add(ObjetsList.Tower);
		bo.add(ObjetsList.Tower);
		bo.add(ObjetsList.Headquarters);
		BuildCommander b = new BuildCommander(ia);
		b.orders = bo;
		return b;
	}
	
	public void play(){
		if(cursor>=orders.size()){
			return;
		}
		if(mission==null || mission.isFinished()){
			mission = new MissionBuilding(this.ia, orders.get(cursor));
			cursor++;
		}
		
		
	}
}
