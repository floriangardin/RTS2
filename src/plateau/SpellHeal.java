package plateau;

import org.newdawn.slick.Graphics;

import data.Attributs;
import utils.ObjetsList;

//TODO : sort qui use heal
public strictfp class SpellHeal extends Spell{


	public SpellHeal(){
		this.name = ObjetsList.Heal;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		Spell.realTarget(target, launcher, getAttribut(Attributs.range),true, plateau);
		Heal f = new Heal(launcher,target, plateau);
		f.remainingTime = this.getAttribut(Attributs.totalTime);
		return true;
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}

}
