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
		this.getGameTeam().data.setAttribut(ObjetsList.Spearman.name, Attributs.explosionWhenImmolate, 1f);
		this.getGameTeam().data.setAttribut(ObjetsList.Crossbowman.name, Attributs.explosionWhenImmolate, 1f);
		this.getGameTeam().data.setAttribut(ObjetsList.Knight.name, Attributs.explosionWhenImmolate, 1f);
		this.getGameTeam().data.setAttribut(ObjetsList.Priest.name, Attributs.explosionWhenImmolate, 1f);
		this.getGameTeam().data.setAttribut(ObjetsList.Inquisitor.name, Attributs.explosionWhenImmolate, 1f);
		this.getGameTeam().data.setAttribut(ObjetsList.Archange.name, Attributs.explosionWhenImmolate, 1f);
	}

	
}
