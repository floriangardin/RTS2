package plateau;

import plateau.EndCondition.EndConditions;

public strictfp class EndConditionUnits extends EndCondition {
	
	int team;
	
	public EndConditionUnits(int team){
		super(team);
		this.setType(EndConditions.units);
	}
	
	@Override
	public boolean hasLost(Plateau p) {
		// TODO Auto-generated method stub
		return p.get()
				.filter(x-> x instanceof Character)
				.filter(x-> x.getTeam().id==team)
				.count()==0;
	}
	
	@Override
	public String nameForList() {
		// TODO Auto-generated method stub
		return "Destroy all units";
	}

}
