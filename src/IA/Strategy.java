package IA;

import java.util.Vector;

public class Strategy {
	
	static final int NORMAL=0;
	static final int GREEDY = 1;
	static final int AGGRESSIVE = 2;
	
	static final int NO_ACTION = -1;
	//Exprime une strategie high level
	static final int MAKE_CROSSBOWMAN=3;
	static final int MAKE_SPEARMAN=4;
	static final int MAKE_TECH=5;

	static final int GET_GOLD=6;
	static final int GET_FOOD=7;
	static final int GET_BARRACK = 8;
	
	static final int CONTROL_CENTER = 8;
	static final int CONQUER_ENNEMY_BARRACK = 9;
	static final int CONQUER_ENNEMY_MILL = 10;
	static final int CONQUER_ENNEMY_MINE = 11;

	private Vector<Integer> actions;

	public Strategy(int type){
		actions = new Vector<Integer>();
		switch(type){
		case NORMAL:
			actions.addElement(GET_FOOD);
			actions.addElement(GET_BARRACK);
			actions.addElement(MAKE_SPEARMAN);
			actions.addElement(GET_GOLD);
			actions.addElement(GET_FOOD);
			actions.addElement(MAKE_SPEARMAN);
			actions.addElement(MAKE_CROSSBOWMAN);
			actions.addElement(CONQUER_ENNEMY_BARRACK);
			break;
		}
	}
	
	public int getNextAction(){
		if(actions.size()>0){
			int action = actions.get(0);
			actions.remove(0);
			return action;
		}
		
		return NO_ACTION;

		
	}

}
