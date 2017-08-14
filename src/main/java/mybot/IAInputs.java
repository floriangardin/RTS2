package mybot;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import bot.IA;
import bot.IAUnit;
import bot.IAUnit.Role;
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
		general.assignRoles();
		
		if(getUnits().count()==0){
			return;
		}
		int roleToPlay = general.roleToPlay();
		switch(roleToPlay){
		case General.BUILD:
			bo.play();
			break;
		case General.RECON:
			recon.play();
			break;
		case General.WAR:
			war.play();
			break;
		case General.PROD:
			prod.play();
			break;
		}
	}
	
	public void draw(Graphics g){
		
		// Mark units given there role
		HashMap<Role, Color> colors = new HashMap<Role,Color>();
		colors.put(Role.build, Color.green);
		colors.put(Role.recon, Color.blue);
		colors.put(Role.war, Color.black);
		colors.put(Role.product, Color.pink);
		
		g.setColor(Color.green);
		for(Role role : colors.keySet()){
			getUnits()
			.filter(x-> x.getRole()==role)
			.peek(x-> g.setColor(colors.get(role)))
			.forEach(x-> g.fillRect(x.getX(),x.getY(), 10, 10 ));
			
		}	
		g.setColor(Color.black);
		getUnits()
		.filter(x-> x.hasTarget())
		.forEach(x-> g.drawLine(x.getX(), x.getY(), x.getTarget().getX(), x.getTarget().getY()));
	}

}
