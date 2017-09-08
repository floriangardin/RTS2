package mybot;

import java.util.List;
import java.util.stream.Collectors;

import bot.IA;
import bot.IAUnit;
import bot.IAUnit.Role;
import utils.ObjetsList;

public strictfp class ReconCommander extends Commander{

	public ReconCommander(IA ia) {
		super(ia);
		// TODO Auto-generated constructor stub
	}

	public static ReconCommander classicRecon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void play() {
		List<IAUnit> recons = ia.getUnits()
				.filter(x->x.getGameTeam()==ia.getTeamId())
				.filter(x-> x.getRole()==Role.recon)
				.collect(Collectors.toList());
		ia.select(recons);
		
		IAUnit toRecon = ia.getUnits()
				.filter(x-> x.getGameTeam()!=ia.getTeamId())
				.filter(x-> x.getName()==ObjetsList.Headquarters)
				.findFirst()
				.orElse(null);
		
		ia.rightClick(toRecon.getX(), toRecon.getY());
		
	}

}
