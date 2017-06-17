package spells;

import java.util.HashMap;

import model.Game;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import utils.Utils;
import data.Data;
public class SpellEffect extends Objet{

	public int type;
	public String image;
	public Integer owner;

	public void collision(Character c){

	}

	public SpellEffect(){

	}
	
	public Objet getOwner(){
		return Game.gameSystem.plateau.getById(owner);
	}

	
}
