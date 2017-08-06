package mybot;

import java.util.List;
import java.util.stream.Collectors;

import bot.IA;
import bot.IAUnit;
import bot.IAUnit.Role;
import bot.Utils;
import plateau.Character;
public class WarCommander extends Commander{

	public WarCommander(IA ia) {
		super(ia);
		// TODO Auto-generated constructor stub
	}

	public static WarCommander classicWar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		
		List<IAUnit> units = ia.getUnits()
				.filter(x-> x.getGameTeam()==ia.getTeamId())
				.filter(x-> x.getRole()==Role.war)
				.collect(Collectors.toList());
		if(units.size()==0){
			return;
		}
		IAUnit leader = units.get(0);
		// Do something with units
		// Get enemy characters at sight else attack neutral/enemy headquarter
		List<IAUnit> enemies = ia.getUnits()
				.filter(x->x.getGameTeam()!=ia.getTeamId())
				.filter(x-> x.getGameTeam()==0)
				.filter(x-> x.getType().isInstance(Character.class))
				.collect(Collectors.toList());
		
		// Attack nearest ennemy from team leader
		ia.select(units);
		ia.rightClick(getNearestEnemy(leader, enemies));
		
	}
	
	public static IAUnit getNearestEnemy(IAUnit leader, List<IAUnit> enemies){
		return enemies.stream()
				.sorted((x,y)-> Float.compare(Utils.distance2(x, leader), Utils.distance2(y, leader)))
				.findFirst()
				.orElse(null);
	}

}
