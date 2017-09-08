package plateau;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import data.Attributs;
import events.EventHandler;
import events.EventNames;
import utils.ObjetsList;

public strictfp class SpellImmolation extends Spell{

	
	public SpellImmolation(){
		this.name = ObjetsList.Immolation;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		
		launcher.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.setSpells(new Vector<ObjetsList>());
		launcher.addSpellEffect(new Immolation(launcher,target,plateau));
		EventHandler.addEvent(EventNames.Immolation, launcher, plateau);
		return true;
	}

	
	

	
	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}
}
