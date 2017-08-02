package mybot;

import java.util.List;
import java.util.stream.Collectors;

import bot.IA;
import bot.IAUnit;
import bot.IAUnit.Role;

public class General {

	IA ia;
	public General(IA ia){
		this.ia = ia;
	}
	
	public void assignRoles(){
		// get count for each role
		// Choose a balanced policy
		List<IAUnit> noRoles = ia.getUnits().filter(x-> x.getRole()==Role.noRole).collect(Collectors.toList());
		if(noRoles.size()>0){
			long countBuild = ia.getUnits().filter(x-> x.getRole()==Role.build).count();
			long countWar = ia.getUnits().filter(x-> x.getRole()==Role.war).count();
			long countRecon = ia.getUnits().filter(x-> x.getRole()==Role.recon).count();
			if(countBuild<=countRecon){
				ia.getUnits().filter(x->x.getRole()==Role.noRole).findFirst().orElse(null).setRole(Role.build);
			}else if(countRecon<=countWar &&countRecon <= 2){
				ia.getUnits().filter(x->x.getRole()==Role.noRole).findFirst().orElse(null).setRole(Role.recon);
			}else{
				ia.getUnits().filter(x->x.getRole()==Role.noRole).findFirst().orElse(null).setRole(Role.war);
			}
			
		}
		
	}
}
