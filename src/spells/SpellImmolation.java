package spells;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import events.EventNames;
import model.Character;
import model.Game;
import model.Objet;
import utils.ObjetsList;

public class SpellImmolation extends Spell{

	
	public SpellImmolation(){
		this.name = ObjetsList.Immolation;
	}

	public void launch(Objet target, Character launcher){
		
		launcher.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.setSpells(new Vector<ObjetsList>());
		launcher.addSpellEffect(new Immolation(launcher,target));
		Game.g.triggerEvent(EventNames.Immolation, launcher);
	}

	
	

	
	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}
}
