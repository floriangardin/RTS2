package spells;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import model.Objet;
import units.Character;
import utils.SpellsList;
import utils.Utils;

//TODO : sort qui use heal
public class SpellHeal extends Spell{


	public SpellHeal(){
		this.name = SpellsList.Heal;
	}

	public void launch(Objet target, Character launcher){
		Spell.realTarget(target, launcher, getAttribut(Attributs.range),true);
		Heal f = new Heal(launcher,target,-1);
		f.remainingTime = this.getAttribut(Attributs.totalTime);
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}

}
