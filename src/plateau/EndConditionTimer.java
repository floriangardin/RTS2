package plateau;


public class EndConditionTimer extends EndCondition{
	
	private int nbRound;

	public EndConditionTimer(int team) {
		super(team);
		this.setType(EndConditions.timer);
	}
	
	public EndConditionTimer(int team, int nbRounds) {
		super(team);
		this.setNbRound(nbRounds);
		this.setType(EndConditions.timer);
	}

	@Override
	public boolean hasLost(Plateau p) {
		if(getNbRound()>0 && p.getRound()>=getNbRound()) {
			return true;
		}
		return false;
	}

	@Override
	public String nameForList() {
		// TODO Auto-generated method stub
		return "Timer condition";
	}

	public int getNbRound() {
		return nbRound;
	}

	public void setNbRound(int nbRound) {
		this.nbRound = nbRound;
	}
	
	@Override
	public String toString() {
		return type+" "+nbRound;
	}

}
