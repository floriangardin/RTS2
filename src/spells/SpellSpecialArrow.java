package spells;

import org.newdawn.slick.Graphics;

import bullets.Arrow;
import control.InputObject;
import data.Attributs;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellSpecialArrow extends Spell{

	public float remainingTime;
	
	public SpellSpecialArrow(){
		this.name = SpellsList.SpecialArrow;
	}

	public void launch(Objet target, Character launcher){
		new Arrow(launcher,target.getX()-launcher.getX(),target.getY()-launcher.getY(),this.getAttribut(Attributs.damage),-1);
		
		launcher.stop();
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}
	
}
