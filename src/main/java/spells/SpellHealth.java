package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Game;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;
import utils.Utils;

// TODO : Sort
public class SpellHealth extends Spell{

	public SpellHealth(){
		this.name = ObjetsList.Health;
	}

	public void launch(Objet target, Character launcher, Plateau plateau){
		// Check if target intersect an ennemy
		Objet h = target;
		for(Character c : plateau.getCharacters()){
			if(Utils.distance(c, target)< c.getAttribut(Attributs.size) && c.getTeam().id==launcher.getTeam().id && c.lifePoints<c.getAttribut(Attributs.maxLifepoints) ){
				h = c;
				break;
			}
		}
		if(h instanceof Character && h.getTeam()==team){
			h.setLifePoints(h.lifePoints+(h.getAttribut(Attributs.maxLifepoints)-h.lifePoints)/2, plateau);
			//TODO add a sound
		}
	}
	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(3f);
		Objet t = Spell.realTarget(new Checkpoint(x,y, plateau), launcher, this.getAttribut(Attributs.range),true, plateau);
		g.setColor(Color.red);
		g.drawOval(t.x-this.getAttribut(Attributs.size)/2,t.y-this.getAttribut(Attributs.size)/2, this.getAttribut(Attributs.size), this.getAttribut(Attributs.size));
	}

}


