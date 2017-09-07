package spells;

import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
public strictfp class SpellEffect extends Objet{

	public SpellEffect(Plateau plateau) {
		super(plateau);
		// TODO Auto-generated constructor stub
	}



	public int type;
	public String image;
	public Integer owner;

	public void collision(Character c, Plateau plateau){

	}


	
	public Objet getOwner(Plateau plateau){
		return plateau.getById(owner);
	}

	
}
