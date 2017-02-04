package bot;

import java.util.Vector;

import model.Building;
import model.Character;
import model.Checkpoint;
import model.Game;
import model.Objet;
import multiplaying.ChatMessage;
import spells.Spell;
import utils.ObjetsList;

public class IAAllyObject extends IAUnit{

	

	
	public IAAllyObject(Objet o, IA ia) {
		super(o, ia);
		// TODO Auto-generated constructor stub
	}


	public void rightClick(float x, float y){
		if(objet instanceof Building){
			((Building)this.objet).setRallyPoint(x, y);
			return;
		}
		IAUnit u = this.getIA().findUnit(x,y);
		if(u!=null){
			this.rightClick(u);
		}else{
			if(objet instanceof Character){
				((Character)this.objet).setTarget(new Checkpoint(x,y),null,Character.NORMAL);
			}

		}
	}; // simulate right click
	
	public void stop(){
		this.objet.setTarget(null);
	}
	public void rightClick(IAUnit u){
		if(u==null || u.isNull()){
			return;
		}
		if(objet instanceof Character){
			((Character)this.objet).setTarget(u.getObjet(),null,(u.objet instanceof Building)? Character.TAKE_BUILDING:Character.NORMAL);
		}
		if(objet instanceof Building){
			((Building)this.objet).setTarget(u.getObjet());
		}
	}; // simulate right click on unit

	
	public  void move(float x , float y){
		if(objet instanceof Character){			
			rightClick(x,y);
			objet.mode = Character.MOVE;
		}else{
			Game.g.sendMessage(new ChatMessage("1|Warning : Tried to move a building ..."));
		}
	}; // classic move
	public  void attack(float x , float y){
		rightClick(x,y);
		objet.mode = Character.AGGRESSIVE;
	}; // attack move
	public  void produceUnit(ObjetsList production){
		// Check price of unit first
		if(objet instanceof Building){
			if(this.getProductionList().contains(production)){
				((Building) objet).product(this.getProductionList().indexOf(production));
			}
		}else{
			Game.g.sendMessage(new ChatMessage("1|Warning : Tried to produce not in a building ..."));
		}
	}; // Produce unit
	public  void produceTechnology(ObjetsList tech){
		if(objet instanceof Building){
			
		}else{
			Game.g.sendMessage(new ChatMessage("1|Warning : Tried to research not in a building ..."));
		}
	}; // Produce research
	
	public void produce(ObjetsList o){
		produceUnit(o);
		produceTechnology(o);
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
	
	public void launchSpell(float x , float y,ObjetsList spell){
		
		if(this.getObjet() instanceof Character ){
			Character c = (Character) getObjet();
			if(c.getSpellsName().contains(spell)){
				c.launchSpell(new Checkpoint(x,y),spell);
				
			}
			
		}
	}
	
	public void launchSpell(IAUnit target,ObjetsList spell){
		
		if(this.getObjet() instanceof Character ){
			Character c = (Character) getObjet();
			if(c.getSpellsName().contains(spell)){
				c.getSpell(spell).launch(target.getObjet(), c);
			}
			
		}
	}
	
	public Vector<ObjetsList> getQueue(){
		Vector<ObjetsList> res = new Vector<ObjetsList>();
		
		if(getObjet() instanceof Building){
			Building b = (Building) getObjet();
			res.addAll(b.getQueue());
		}
		
		return res;
	}
}
