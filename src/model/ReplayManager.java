package model;

import plateau.Plateau;

public class ReplayManager {
	Replay replay;
	
	public void init(Plateau plateau){
		replay = new Replay(plateau);
	}
	
	
}
