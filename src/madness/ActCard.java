package madness;

import java.util.Vector;

import org.newdawn.slick.Font;

import model.Game;
import model.GameTeam;
import utils.ObjetsList;

public abstract class ActCard implements java.io.Serializable{

	private static final long serialVersionUID = -1568324219869262030L;

	public int team;
	public ObjetsList objet;
	public String description = "";
	public String displayName = "";

	public abstract void applyEffect();
	public GameTeam getGameTeam(){
		return Game.g.teams.get(this.team);
	}

	public ActCard(ObjetsList o, int gameteam, String displayName, String description){
		this.objet = o;
		this.team = gameteam;	
		this.displayName = displayName;
		this.description = description;
	}
	public String getName(){
		return this.displayName;
	}

	public String getIcon(){
		return this.objet.name();
	}

	public static ActCard actCard(ObjetsList o, int gameteam){
		switch(o){
		case AgeIa:
			return new ActCard(o, gameteam, "Age I a", "Il est des pays ou les gens au creux des lits font des rêves"){

				@Override
				public void applyEffect() {

				}

			};
		case AgeIb:
			return new ActCard(o, gameteam, "Age I b", "Ici, nous vois-tu, nous on marche, nous on tue, nous on crève"){

				@Override
				public void applyEffect() {

				}

			};
		case AgeIm:
			return new ActCard(o, gameteam, "Age I madness", "Bonjour bonjour"){

				@Override
				public void applyEffect() {

				}

			};
		case AgeIr:
			return new ActCard(o, gameteam, "Age I reason", "Bonjour bonjour"){

				@Override
				public void applyEffect() {

				}

			};
		case AgeIIa:
			return new ActCard(o, gameteam, "Age II a", "Bonjour bonjour"){

				@Override
				public void applyEffect() {

				}

			};
		case AgeIIb:
			return new ActCard(o, gameteam, "Age II b", "Bonjour bonjour"){

				@Override
				public void applyEffect() {

				}

			};
		case AgeIIm:
			return new ActCard(o, gameteam, "Age II madness", "Bonjour bonjour"){

				@Override
				public void applyEffect() {

				}

			};
		case AgeIIr:
			return new ActCard(o, gameteam, "Age II reason", "Bonjour bonjour"){

				@Override
				public void applyEffect() {

				}

			};
		default: return null;
		}

	}
	public Vector<String> getTexte(float length, Font font) {
		Vector<String> v = new Vector<String>();
		v.add(this.getName());
		String temp = "";
		String[] tab = this.description.split(" ");
		int imot = 0;
		while(imot<tab.length){
			while(imot<tab.length && font.getWidth(temp+" "+tab[imot])<length-10){
				temp+="  "+tab[imot];
				imot+=1;
			}
			v.add(temp);
			temp = "";
		}

		return v;
	}
}