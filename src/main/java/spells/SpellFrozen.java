package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public strictfp class SpellFrozen extends Spell{


	public SpellFrozen(){
		this.name = ObjetsList.Frozen;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		Objet t = Spell.realTarget(target, launcher, getAttribut(Attributs.range),true, plateau);
		Frozen f = new Frozen(launcher,t,getAttribut(Attributs.size), plateau);
		f.remainingTime = this.getAttribut(Attributs.totalTime);
		return true;

	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y, plateau), launcher, this.getAttribut(Attributs.range),true, plateau);
		g.draw(Frozen.createShape(launcher, t, getAttribut(Attributs.size)));
	}

}
