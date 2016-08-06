package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Checkpoint;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellFrozen extends Spell{


	public SpellFrozen(){
		this.name = SpellsList.Frozen;
	}

	public void launch(Objet target, Character launcher){
		Objet t = Spell.realTarget(target, launcher, getAttribut(Attributs.range));
		Frozen f = new Frozen(launcher,t,-1,getAttribut(Attributs.size));
		f.remainingTime = this.getAttribut(Attributs.totalTime);

	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, this.getAttribut(Attributs.range));
		g.draw(Frozen.createShape(launcher, t, getAttribut(Attributs.size)));
	}

}
