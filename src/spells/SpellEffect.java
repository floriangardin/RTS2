package spells;

import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
public class SpellEffect extends Objet{

	public int type;
	public String image;
	public Integer owner;

	public void collision(Character c, Plateau plateau){

	}

	public SpellEffect(){

	}
	
	public Objet getOwner(){
		return Game.gameSystem.plateau.getById(owner);
	}

	
}
