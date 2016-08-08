package technologies;

import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.ObjetsList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import data.Attributs;

public class DualistExplosion extends Technologie {

	public DualistExplosion(int team) {
		this.id = 12;
		this.tech = Technologies.DualistExplosion;
		this.initialize(team, tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		for(ObjetsList o : ObjetsList.getUnits()){
			this.getGameTeam().data.setAttribut(o, Attributs.explosionWhenImmolate, 1f);
		}
	}

	
}
