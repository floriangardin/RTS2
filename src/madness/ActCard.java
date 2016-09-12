package madness;

import data.Attributs;
import model.Game;
import model.GameTeam;
import utils.ObjetsList;

public abstract class ActCard implements java.io.Serializable{

	private static final long serialVersionUID = -1568324219869262030L;

	public int team;
	public ObjetsList objet;

	public abstract void applyEffect();
	public GameTeam getGameTeam(){
		return Game.g.teams.get(this.team);
	}

	public ActCard(ObjetsList o, int gameteam){
		this.objet = o;
		this.team = gameteam;		
	}
	public String getName(){
		return this.objet.name();
	}

	public String getIcon(){
		return this.getGameTeam().data.getAttributString(objet, Attributs.nameIcon);
	}

	public static ActCard actCard(ObjetsList o, int gameteam){
		switch(o){
		case AgeIa:
			return new ActCard(o, gameteam){

				 @Override
				 public void applyEffect() {
					 
				 }

			};
		case AgeIb:
			return new ActCard(o, gameteam){
				
				@Override
				public void applyEffect() {
					
				}
				
			};
		case AgeIm:
			return new ActCard(o, gameteam){
				
				@Override
				public void applyEffect() {
					
				}
				
			};
		case AgeIr:
			return new ActCard(o, gameteam){
				
				@Override
				public void applyEffect() {
					
				}
				
			};
		default: return null;
		}

	}
}