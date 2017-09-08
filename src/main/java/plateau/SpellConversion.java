package plateau;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import main.Main;
import model.Game;
import multiplaying.ChatMessage;
import utils.ObjetsList;

public strictfp class SpellConversion extends Spell{


	public SpellConversion(){
		this.name = ObjetsList.Conversion;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		Objet t = plateau.findTarget(target.getX(), target.getY(),launcher.team.id);
		if(t instanceof Character && t.getTeam()!=launcher.getTeam()){
			((Character)t).team = launcher.getTeam();
			return true;
		}
		return false;
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		if(target instanceof Character){
			this.drawTargetUnit(g, (Character)target);
		}
	}

}
