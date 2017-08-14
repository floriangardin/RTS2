package spells;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import main.Main;
import model.Game;
import multiplaying.ChatMessage;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;

public class SpellConversion extends Spell{


	public SpellConversion(){
		this.name = ObjetsList.Conversion;
	}

	public void launch(Objet target, Character launcher, Plateau plateau){
		Objet t = plateau.findTarget(target.x, target.y,launcher.team.id);
		if(t instanceof Character && t.getTeam()!=launcher.getTeam()){
			((Character)t).team = launcher.getTeam();
		}
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		if(target instanceof Character){
			this.drawTargetUnit(g, (Character)target);
		}
	}

}
