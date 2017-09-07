package spells;

import org.newdawn.slick.Graphics;

import events.EventHandler;
import events.EventNames;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public strictfp class SpellMeditation extends Spell{

	
	public SpellMeditation(){
		this.name = ObjetsList.Meditation;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		// Check if already meditating
		if(launcher.etats.contains(Etats.Meditating)){
			return false;
		}
		launcher.addSpellEffect(new Meditation(launcher,target, plateau));
		launcher.canMove = false;
		EventHandler.addEvent(EventNames.Meditation, launcher, plateau);
		return true;
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}
}
