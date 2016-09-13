package madness;


public enum Act implements java.io.Serializable{


	HAINE(new ActRule[]{ActRule.no_attack_building},15f,"La Haine"),
	GUERRE(new ActRule[]{},18f,"La Guerre"),
	DESTRUCTION(new ActRule[]{ActRule.no_defense_building},18f,"La Destruction"),
	MORT(new ActRule[]{ActRule.no_building_production, ActRule.sudden_death},15f,"La Mort");

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
