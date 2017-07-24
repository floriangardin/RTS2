package bot;

import java.util.Vector;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import plateau.Building;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import utils.ObjetsList;

public class IAAllyObject extends IAUnit{

	public IAAllyObject(Objet o, IA ia) {
		super(o, ia, ia.plateau);
	}

	// SPELLS
	public boolean canLaunch(ObjetsList spell){
		if(getObjet() instanceof Character){
			Character c = (Character) getObjet();
			int index = c.getSpellsName().indexOf(spell);
			if(index>=0){
				return c.canLaunch(index);
			}
			
		}

		return false;
	}

	public Vector<ObjetsList> getQueue(){
		Vector<ObjetsList> res = new Vector<ObjetsList>();
		
		if(getObjet() instanceof Building){
			Building b = (Building) getObjet();
			res.addAll(b.getQueue());
		}
		
		return res;
	}
	public int roundsSinceLastAttack(){
		return this.getObjet().roundSinceLastAttack(plateau.getRound());
	}
}
