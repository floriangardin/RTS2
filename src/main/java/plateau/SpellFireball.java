package plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.Player;
import data.Attributs;
import main.Main;
import utils.ObjetsList;

public strictfp class SpellFireball extends Spell{

	public SpellFireball(){
		this.name = ObjetsList.SpellFireball;
	}
	public boolean launch(Objet target, Character launcher, Plateau plateau){
		// Launch on mouseOver
		Objet t = Spell.realTarget(target, launcher, launcher.getAttribut(Attributs.range),true, plateau);
		new Fireball(launcher, t.getX(), t.getY(), target.getX()-launcher.getX(), target.getY()-launcher.getY(), launcher.getAttribut(Attributs.damage), plateau);
		launcher.stop(plateau);
		return true;
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		
//		if(Player.mouseOver==-1){			
		g.setLineWidth(2f*Main.ratioSpace);
		g.setAntiAlias(true);
		Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, launcher.getAttribut(Attributs.range),true, plateau);
		g.setColor(Color.red);
		g.drawOval(t.getX()-this.getAttribut(Attributs.size)/2,t.getY()-this.getAttribut(Attributs.size)/2, this.getAttribut(Attributs.size), this.getAttribut(Attributs.size));
		g.setAntiAlias(false);
		
//		}
	}

}
