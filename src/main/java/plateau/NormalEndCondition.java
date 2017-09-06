package plateau;

public class NormalEndCondition extends EndCondition {

	@Override
	public boolean hasLost(Plateau plateau, Team t) {
		// TODO Auto-generated method stub
		return (plateau.getHQ(t)).constructionPoints <= 0|| t.hasGaveUp;
	}

}
