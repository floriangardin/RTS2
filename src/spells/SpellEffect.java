package spells;

import model.ActionObjet;
import model.Plateau;
import multiplaying.OutputModel.OutputSpell;
import units.Character;

public class SpellEffect extends ActionObjet{
	
	public int type;

	public void collision(Character c){
		
	}
	
	
	
	public SpellEffect(){}
	
	public SpellEffect(Plateau p, OutputSpell s){
		System.out.println("creation");
		switch(s.type){
		case 1: new Firewall(p, s);type=1;break;
		case 2: new BlessedArea(p, s);type=2; break;
		default:		
		}
	}
	
}
