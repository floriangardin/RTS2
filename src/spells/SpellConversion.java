package spells;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import main.Main;
import model.Character;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import multiplaying.ChatMessage;
import utils.ObjetsList;

public class SpellConversion extends Spell{


	public SpellConversion(){
		this.name = ObjetsList.Conversion;
	}

	public void launch(Objet target, Character launcher){
		Objet t = Game.g.plateau.findTarget(target.x, target.y,launcher.getTeam());
		if(t instanceof Character && t.getTeam()!=launcher.getTeam()){
			if(launcher.getGameTeam().special>=this.getAttribut(Attributs.faithCost)){
				((Character)t).changeTeam(launcher.getTeam());
				launcher.getGameTeam().special-=this.getAttribut(Attributs.faithCost);
			} else {
				// Messages
				if(this.team==Game.g.currentPlayer.getTeam()){
					Game.g.sendMessage(ChatMessage.getById("faith"));
				}
			}
		}
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		if(target instanceof Character){
			this.drawTargetUnit(g, (Character)target);
		}
	}

}
