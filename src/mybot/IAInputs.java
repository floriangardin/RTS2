package mybot;

import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import bot.IA;
import bot.IAUnit;
import bot.Utils;
import iamission.MissionBuilding;
import utils.ObjetsList;

public class IAInputs extends IA {

	BuildCommander bo = BuildCommander.classicBo();
	ReconCommander recon = ReconCommander.classicRecon();
	WarCommander war = WarCommander.classicWar();
	
	
	General general;
	MissionBuilding currentMission;

	public IAInputs(int teamid) {
		super(teamid);
		this.general = new General(this);
	}

	@Override
	public void update() throws Exception {
		// General attribute roles to everyone
		general.assignRoles();
		if(getUnits().count()==0){
			return;
		}
		if(currentMission==null || currentMission.isFinished()){
			currentMission = bo.getOrder(this);
		}else{
			// Recon or War !!!
		}
	}










}
