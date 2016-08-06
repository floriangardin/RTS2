package spells;

import org.newdawn.slick.Graphics;

import buildings.BuildingProduction;
import buildings.BuildingTech;
import control.InputObject;
import data.Attributs;
import model.Objet;
import units.Character;
import utils.SpellsList;

//TODO : sort
public class SpellProduct extends Spell{


	public SpellProduct(){
		this.name = SpellsList.Product;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		if(target instanceof BuildingProduction && ((BuildingProduction) target).queue.size()>0){
			BuildingProduction p = (BuildingProduction) target;
			if(p.queue.size()>0){
				p.charge=p.productionList.get(p.queue.get(0)).time;
			}
		}else if(target instanceof BuildingTech && ((BuildingTech) target).queue!=null){
			BuildingTech p = (BuildingTech) target;
			p.techTerminate(p.queue);
		}else{
			this.getGameTeam().special+=this.getAttribut(Attributs.faithCost);
		}
		
		
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}

}


