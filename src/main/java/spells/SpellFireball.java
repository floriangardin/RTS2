package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import data.Attributs;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Fireball;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public class SpellFireball extends Spell{

	public SpellFireball(){
		this.name = ObjetsList.SpellFireball;
	}

	public void launch(Objet target, Character launcher, Plateau plateau){
		Objet t = Spell.realTarget(target, launcher, this.getAttribut(Attributs.range),true, plateau);
		new Fireball(launcher, t.getX(), t.getY(), target.getX()-launcher.getX(), target.getY()-launcher.getY(), this.getAttribut(Attributs.damage), plateau);
		launcher.stop(plateau);
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y, plateau), launcher, this.getAttribut(Attributs.range),true, plateau);
		g.setColor(Color.red);
		g.drawOval(t.x-this.getAttribut(Attributs.size)/2,t.y-this.getAttribut(Attributs.size)/2, this.getAttribut(Attributs.size), this.getAttribut(Attributs.size));
	}

}
