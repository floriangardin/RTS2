package spells;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import model.Building;
import model.Character;
import model.Objet;
import utils.ObjetsList;

//TODO : sort
public class SpellProduct extends Spell{


	public SpellProduct(){
		this.name = ObjetsList.Product;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		if(target instanceof Building && ((Building) target).getQueue().size()>0){
			Building p = (Building) target;
			if(p.getQueue().size()>0){
				 Float prodTime = p.getGameTeam().data.getAttribut(p.getQueue().get(0), Attributs.printName);
				p.charge=prodTime;
			}
		}else if(target instanceof Building && ((Building) target).getQueueTechnologie()!=null){
			Building p = (Building) target;
			p.techTerminate(p.getQueueTechnologie());
		}
		
		
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}

}


