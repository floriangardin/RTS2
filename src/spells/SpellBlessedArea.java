package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Checkpoint;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellBlessedArea extends Spell{
	
	protected SpellBlessedArea(){
		this.name = SpellsList.BlessedArea;
	}

	public void launch(Objet target, Character launcher){
		Objet t = Spell.realTarget(target, launcher, this.getAttribut(Attributs.range),true);
		BlessedArea ba = new BlessedArea(launcher,(Checkpoint)t,-1,getAttribut(Attributs.size));
		ba.remainingTime = this.getAttribut(Attributs.totalTime);
		ba.size = this.getAttribut(Attributs.size);
		launcher.stop();
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, this.getAttribut(Attributs.range),true);
		g.draw(BlessedArea.createShape(launcher, t, getAttribut(Attributs.size)));
		g.setLineWidth(1f);
	}




	
	
}
