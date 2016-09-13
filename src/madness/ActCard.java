package madness;

import java.util.Vector;

import org.newdawn.slick.Font;

import data.Attributs;
import model.Building;
import model.Character;
import model.Game;
import model.GameTeam;
import model.Technologie;
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
		// Choice madness
		case AgeICrossbowman:
			return new ActCard(o, gameteam, "Archer gratuit", "Produit un archer dans votre centre ville"){
				
				@Override
				public void applyEffect() {
					// get headquarters
					Building  b = Game.g.teams.get(gameteam).hq;
					System.out.println("gt" + gameteam);
					System.out.println("building " +b);
					new Character(b.getX()+b.getAttribut(Attributs.sizeX),b.getY(),ObjetsList.Crossbowman,gameteam);
				}

			};
		case AgeISpearman:
			return new ActCard(o, gameteam, "Lancier gratuit", "Produit un lancier dans votre centre ville"){

				@Override
				public void applyEffect() {
					// get headquarters
					Building  b = Game.g.teams.get(gameteam).hq;
					new Character(b.getX()+b.getAttribut(Attributs.sizeX),b.getY(),ObjetsList.Spearman,gameteam);
				}

			};
			


		case AgeIExplosion:
			return new ActCard(o, gameteam, "Immolation explosive", "Vos unités qui s'immolent explosent désormais. Cela leur confère un dégât de zone au moment de leur mort"){

				@Override
				public void applyEffect() {
					Technologie.technologie(ObjetsList.DualistExplosion, gameteam);
				}

			};
			
		case AgeIImmolationAuto:
			return new ActCard(o, gameteam, "Immolation automatique", "Vos unités s'immoleront désormais automatiquement quand leur mort est sur le point d'arriver"){
				
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