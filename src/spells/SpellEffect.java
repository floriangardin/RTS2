package spells;

import model.ActionObjet;
import model.Plateau;
import multiplaying.OutputModel.OutputSpell;
import units.Character;

public class SpellEffect extends ActionObjet{

	public void collision(Character c){
		
	}
	
	public int getType(){
		if(this instanceof Firewall)
			return 1;
		else if (this instanceof BlessedArea)
			return 2;
		return 0;
	}
	
	public SpellEffect(){}
	
	public SpellEffect(Plateau p, OutputSpell s){
		switch(s.type){
		case 1: new Firewall(p, s);break;
		case 2: new BlessedArea(p, s); break;
		default:		
		}
	}
	
}
