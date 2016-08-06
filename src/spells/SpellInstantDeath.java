package spells;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Objet;
import units.Character;
import utils.SpellsList;
import utils.Utils;

public class SpellInstantDeath extends Spell{


	public SpellInstantDeath(){
		this.name = SpellsList.InstantDeath;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		Objet h = target;
		
		for(Character c : Game.g.plateau.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()!=launcher.getTeam() && this.getAttribut(Attributs.range)>=Utils.distance(h, launcher)){
			((Character)h).isBolted = true;
			
		}
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		if(target instanceof Character){
			this.drawTargetUnit(g, (Character)target);
		}
	}

}


