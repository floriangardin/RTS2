package spells;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import data.Attributs;
import events.EventHandler;
import events.EventNames;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public class SpellImmolation extends Spell{

	
	public SpellImmolation(){
		this.name = ObjetsList.Immolation;
	}

	public void launch(Objet target, Character launcher, Plateau plateau){
		
		launcher.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.setSpells(new Vector<ObjetsList>());
		launcher.addSpellEffect(new Immolation(launcher,target,plateau));
		EventHandler.addEvent(EventNames.Immolation, launcher, plateau);
	}

	
	

	
	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}
}
