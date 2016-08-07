package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Checkpoint;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellFirewall extends Spell{

	
	public SpellFirewall(){
		this.name = SpellsList.Firewall;
	}

	public void launch(Objet target, Character launcher){
		Objet t = Spell.realTarget(target, launcher, this.getAttribut(Attributs.range),true);
		Firewall f = new Firewall(launcher,t, getAttribut(Attributs.width),-1);
		f.damage = this.getAttribut(Attributs.damage);
		f.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.stop();
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, this.getAttribut(Attributs.range),true);
		g.draw(Firewall.createShape(launcher, t, getAttribut(Attributs.width)));
	}

}
