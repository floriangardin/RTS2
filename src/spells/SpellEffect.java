package spells;

import java.util.HashMap;

import model.Character;
import model.Checkpoint;
import model.Game;
import model.Objet;
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
		return Game.g.plateau.getById(owner);
	}

	
}
