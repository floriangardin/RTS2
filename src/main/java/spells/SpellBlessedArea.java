package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public class SpellBlessedArea extends Spell{
	
	protected SpellBlessedArea(){
		this.name = ObjetsList.BlessedArea;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		Objet t = Spell.realTarget(target, launcher, this.getAttribut(Attributs.range),true, plateau);
		BlessedArea ba = new BlessedArea(launcher,(Checkpoint)t,getAttribut(Attributs.size), plateau);
		ba.remainingTime = this.getAttribut(Attributs.totalTime);
		ba.size = this.getAttribut(Attributs.size);
		launcher.stop(plateau);
		return true;
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y,plateau), launcher, this.getAttribut(Attributs.range),true, plateau);
		g.draw(BlessedArea.createShape(launcher, t, getAttribut(Attributs.size)));
		g.setLineWidth(1f);
	}

}
