package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import display.DisplayRessources;
import main.Main;
import model.Character;
import model.Game;
import model.Objet;
import utils.ObjetsList;
import utils.Utils;

public class Meditation extends SpellEffect{

	public float remainingTime;

	
	public boolean active = false;
	public Meditation(Character launcher, Objet t){
		
		this.type = 1;
		this.name = ObjetsList.Meditation;
		launcher.etats.add(Etats.Meditating);
		this.x = launcher.getX();
		this.y = launcher.getY()+1f;
		remainingTime = this.getAttribut(Attributs.totalTime);	
		// Calculate step of lifepoints
		this.lifePoints = 1f;
		Game.g.plateau.addSpell(this);
		owner = launcher.id;
		this.setTeam(launcher.getTeam());
	}

	public void action(){
		this.remainingTime-=1f/Main.framerate;
		
		
		if(this.remainingTime<=0f){
			// Test if special capacity explosion
			if(getOwner().getAttribut(Attributs.ressourceAfterMeditation)==1f){
				getOwner().getGameTeam().food = getOwner().getGameTeam().food+20;
			}
			if(getOwner().getAttribut(Attributs.attackBonusAfterMeditation)==1f){
				getOwner().attributsChanges.add(new AttributsChange(Attributs.damage,Change.ADD,1,0));
			}
			
			this.lifePoints=-1f;
			getOwner().canMove = true;
			getOwner().etats.remove(Etats.Meditating);
		}
	}

	public Graphics draw(Graphics g){
		Color color = Color.darkGray;
		color = new Color(100,150,255,0.4f);
		Character owner = (Character) getOwner();
		int direction = (owner.orientation/2-1);
		// inverser gauche et droite
		if(direction==1 || direction==2){
			direction = ((direction-1)*(-1)+2);
		}
		Image im;
		im = Game.g.images.getUnit(owner.name, direction, owner.animation, getGameTeam().id, owner.isAttacking);
		g.drawImage(im,owner.getX()-im.getWidth()/2,owner.getY()-3*im.getHeight()/4);
		im.drawFlash(owner.getX()-im.getWidth()/2,owner.getY()-3*im.getHeight()/4,im.getWidth(),im.getHeight(),color);
		return g;
	}

}
