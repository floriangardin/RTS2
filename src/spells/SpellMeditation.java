package spells;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import events.EventNames;
import model.Game;
import plateau.Character;
import plateau.Objet;
import utils.ObjetsList;

public class SpellMeditation extends Spell{

	
	public SpellMeditation(){
		this.name = ObjetsList.Meditation;
	}

	public void launch(Objet target, Character launcher){
		// Check if already meditating
		if(launcher.etats.contains(Etats.Meditating)){
			return;
		}
		launcher.addSpellEffect(new Meditation(launcher,target));
		launcher.canMove = false;
		Game.g.triggerEvent(EventNames.Meditation, launcher);
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}
}
