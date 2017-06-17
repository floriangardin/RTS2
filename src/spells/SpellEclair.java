package spells;

import org.newdawn.slick.Graphics;

import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

// TODO : Sort
public class SpellEclair extends Spell{

	

	public SpellEclair(){
		this.name = ObjetsList.Eclair;
	}

	public void launch(Objet target, Character launcher, Plateau plateau){
		// Check if target intersect an ennemy
		Objet h = target;
		for(Character c : Game.gameSystem.plateau.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()!=team){
			((Character)h).isBolted = true;
			//TODO add a sound
		}
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		if(target instanceof Character){
			this.drawTargetUnit(g, (Character)target);
		}
	}

}


