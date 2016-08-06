package spells;

import data.Attributs;
import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import multiplaying.ChatMessage;
import units.Character;
import utils.SpellsList;

public class SpellConversion extends Spell{


	public SpellConversion(){
		this.name = SpellsList.Conversion;
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

	
}
