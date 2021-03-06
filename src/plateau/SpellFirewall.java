package plateau;

import org.newdawn.slick.Graphics;

import data.Attributs;
import utils.ObjetsList;

public strictfp class SpellFirewall extends Spell{

	
	public SpellFirewall(){
		this.name = ObjetsList.Firewall;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		Objet t = Spell.realTarget(target, launcher, this.getAttribut(Attributs.range),true, plateau);
		Firewall f = new Firewall(launcher,t, getAttribut(Attributs.width), plateau);
		f.damage = this.getAttribut(Attributs.damage);
		f.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.stop(plateau);
		return true;
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y, plateau), launcher, this.getAttribut(Attributs.range),true, plateau);
		g.draw(Firewall.createShape(launcher, t, getAttribut(Attributs.width)));
	}

}
