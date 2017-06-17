package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

//TODO : sort qui use heal
public class SpellHeal extends Spell{


	public SpellHeal(){
		this.name = ObjetsList.Heal;
	}

	public void launch(Objet target, Character launcher, Plateau plateau){
		Spell.realTarget(target, launcher, getAttribut(Attributs.range),true);
		Heal f = new Heal(launcher,target);
		f.remainingTime = this.getAttribut(Attributs.totalTime);
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}

}
