package plateau;

import java.io.Serializable;
import java.util.Vector;

public abstract strictfp class EndCondition implements Serializable{

	int team;
	private EndConditions type;

	public enum EndConditions{
		buildings,
		units;
	};

	public EndCondition(int team) {
		this.team = team;
	}

	public abstract boolean hasLost(Plateau p);

	public static EndCondition getEndConditionFromString(String s, Plateau plateau){
		String[] tab = s.split(" ");
		Team team = plateau.getTeams().get(Integer.parseInt(tab[0]));
		EndConditions ec = EndConditions.valueOf(tab[1]);
		switch(ec){
		case buildings : 
			return new EndConditionBuilding(team.id, tab[2]);
		case units :
			return new EndConditionUnits(team.id);
		default: 
			throw new RuntimeException("Erreur : condition de victoire non reconnue : "+ec.name());
		}
	}

	public static EndCondition getEndCondition(EndConditions ed, int team){
		switch(ed){
		case units:
			return new EndConditionUnits(team);
		case buildings:
			return new EndConditionBuilding(team);
		default:
			throw new RuntimeException("Erreur : condition de victoire non reconnue : "+ed.name());
		}
	}

	public abstract String nameForList();

	public EndConditions getType() {
		return type;
	}

	public void setType(EndConditions type) {
		this.type = type;
	}


}
