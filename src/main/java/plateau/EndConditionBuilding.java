package plateau;

import java.util.Vector;

public class EndConditionBuilding extends EndCondition{

	private String label;

	public EndConditionBuilding(int team, String label){
		super(team);
		this.setLabel(label);
		this.setType(EndConditions.buildings);
	}

	public EndConditionBuilding(int team) {
		super(team);
		this.setType(EndConditions.buildings);
		this.setLabel(null);
	}

	@Override
	public boolean hasLost(Plateau p) {
		// TODO Auto-generated method stub
		if(getLabel()==null){
			return false;
		}
		int compt = 0;
		for(Building b : p.getBuildings()){
			if(b.hasLabel(getLabel())){
				if(b.constructionPoints >0){
					return false;
				} else {
					compt += 1;
				}
			}
		}
		if(compt == 0){
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String nameForList() {
		// TODO Auto-generated method stub
		return "Destroy buildings";
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return type+" "+label;
	}

}
