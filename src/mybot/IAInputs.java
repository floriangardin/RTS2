package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import plateau.Team;
import utils.ObjetsList;

public class IAInputs extends IA {

	
	public IAInputs(int teamid) {
		super(teamid);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector<IAAllyObject> select() throws Exception {
		// TODO Auto-generated method stub
		// SELECT ALL UNITS
		return this.getMyUnits(ObjetsList.Spearman);
	}

	@Override
	public void update(Vector<IAAllyObject> selection) throws Exception {
		this.rightClick(selection.get(0).getNearestNeutral(ObjetsList.Barracks));
	}

}
