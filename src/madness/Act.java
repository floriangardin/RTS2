package madness;


public enum Act implements java.io.Serializable{


	HAINE(new ActRule[]{ActRule.no_attack_building,ActRule.no_tower_attack},10f,"La Haine"),
	GUERRE(new ActRule[]{},180f,"La Guerre"),
	DESTRUCTION(new ActRule[]{ActRule.no_defense_building},180f,"La Destruction"),
	MORT(new ActRule[]{ActRule.no_building_production, ActRule.sudden_death},-1f,"La Mort");

	Act(ActRule[] rules, float time, String displayName){
		this.rules = rules;
		this.time = time;
		this.displayName = displayName;
	}

	public ActRule[] rules;
	String displayName;
	float time;
	
	public boolean isRuleActive(ActRule rule){
		for(ActRule r : this.rules){
			if(rule==r)
				return true;
		}
		return false;
	}
	
	public float getTime(){
		return this.time;
	}

	public String getDisplayName(){
		return displayName;
	}

}
