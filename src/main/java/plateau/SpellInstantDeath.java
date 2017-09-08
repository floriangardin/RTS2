package plateau;

import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Game;
import utils.ObjetsList;
import utils.Utils;

public strictfp class SpellInstantDeath extends Spell{


	public SpellInstantDeath(){
		this.name = ObjetsList.InstantDeath;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		// Check if target intersect an ennemy
		Objet h = target;
		
		for(Character c : plateau.getCharacters()){
			if(c.getCollisionBox().contains(target.getCollisionBox())){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()!=launcher.getTeam() && this.getAttribut(Attributs.range)>=Utils.distance(h, launcher)){
			((Character)h).isBolted = true;
			return true;
			
		}
		return false;
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		if(target instanceof Character){
			this.drawTargetUnit(g, (Character)target);
		}
	}

}


