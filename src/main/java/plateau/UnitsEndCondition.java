package plateau;

public class UnitsEndCondition extends EndCondition {

	@Override
	public boolean hasLost(Plateau p, Team t) {
		// TODO Auto-generated method stub
		return p.get()
				.filter(x-> x instanceof Character)
				.filter(x-> x.getTeam().id==t.id)
				.count()==0;
	}

}
