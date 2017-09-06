package spells;

import org.newdawn.slick.Graphics;

import data.Attributs;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

//TODO : sort
public class SpellProduct extends Spell{


	public SpellProduct(){
		this.name = ObjetsList.Product;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		// Check if target intersect an ennemy
		if(target instanceof Building && ((Building) target).getQueue().size()>0){
			Building p = (Building) target;
			if(p.getQueue().size()>0){
				 Float prodTime = p.getTeam().data.getAttribut(p.getQueue().get(0), Attributs.printName);
				p.charge=prodTime;
			}
		}else if(target instanceof Building && ((Building) target).getQueueTechnologie()!=null){
			Building p = (Building) target;
			p.techTerminate(p.getQueueTechnologie(), plateau);
			return true;
		}
		return false;
		
		
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		// TODO Auto-generated method stub
		
	}

}


