package spells;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import model.Game;
import model.Objet;
import units.Character;
import utils.ObjetsList;

// TODO : Sort
public class SpellEclair extends Spell{

	

	public SpellEclair(){
		this.name = ObjetsList.Eclair;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		Objet h = target;
		for(Character c : Game.g.plateau.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()!=team){
			((Character)h).isBolted = true;
			//TODO add a sound
		}else{
			this.getGameTeam().special+=this.getAttribut(Attributs.faithCost);
		}
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		if(target instanceof Character){
			this.drawTargetUnit(g, (Character)target);
		}
	}

}


