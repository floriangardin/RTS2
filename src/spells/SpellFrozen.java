package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Character;
import model.Checkpoint;
import model.Objet;
import utils.ObjetsList;

public class SpellFrozen extends Spell{


	public SpellFrozen(){
		this.name = ObjetsList.Frozen;
	}

	public void launch(Objet target, Character launcher){
		Objet t = Spell.realTarget(target, launcher, getAttribut(Attributs.range),true);
		Frozen f = new Frozen(launcher,t,getAttribut(Attributs.size));
		f.remainingTime = this.getAttribut(Attributs.totalTime);

	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, this.getAttribut(Attributs.range),true);
		g.draw(Frozen.createShape(launcher, t, getAttribut(Attributs.size)));
	}

}
