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
	boolean hasplay = false;

	BuildCommander bo = BuildCommander.classicBo(this);
	ReconCommander recon = new ReconCommander(this);
	WarCommander war = new WarCommander(this);
	ProductionCommander prod = new ProductionCommander(this);

	General general;
	MissionBuilding currentBuilding;

	public IAInputs(int teamid) {
		super(teamid);
		this.general = new General(this);
	}

	@Override
	public void update() throws Exception {
		// General attribute roles to everyone
		//general.assignRoles();
		
		if(getUnits().count()==0){
			return;
		}
		IAUnit selected = getUnits()
				.filter(x->x.getId()==77)
				.findFirst()
				.orElse(null);
		this.select(selected);
		this.rightClick(getUnits()
				.filter(x->x.getId()==63)
				.findFirst()
				.orElse(null));
		int roleToPlay = general.roleToPlay();
		switch(roleToPlay){
		case General.BUILD:
			//bo.play();
			break;
//		case General.RECON:
//			recon.play();
//			break;
//		case General.WAR:
//			war.play();
//			break;
//		case General.PROD:
//			prod.play();
//			break;
		}


	}

}
