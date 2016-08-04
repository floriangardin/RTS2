package spells;

import java.util.HashMap;

import model.Checkpoint;
import model.Game;
import model.Objet;
import units.Character;
public class SpellEffect extends Objet{

	public int type;

	public void collision(Character c){

	}

	public SpellEffect(){

	}

	public String toStringSpellEffect(){
		String s= "";
		s+="typeSpell:"+type+";";
		return s;
	}
	public void parse(HashMap<String, String> hs) {
		this.parseObjet(hs);
		this.parseActionObjet(hs);

	}

	public static SpellEffect createNewSpell(HashMap<String, String> hs) {
		SpellEffect sp = null;
		int type = Integer.parseInt(hs.get("typeSpell"));
		Character launcher = Game.g.plateau.getCharacterById(Integer.parseInt(hs.get("idLauncher")));
		switch(type){
			case 1: sp = new Firewall(launcher,new Checkpoint(Float.parseFloat(hs.get("x2")),Float.parseFloat(hs.get("y2"))),Integer.parseInt(hs.get("id")));
			break;
			case 2: sp = new BlessedArea(launcher,new Checkpoint(Float.parseFloat(hs.get("x")),Float.parseFloat(hs.get("y"))),Integer.parseInt(hs.get("id")));
			break;
		}
		return sp;
	}
}
