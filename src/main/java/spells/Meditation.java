package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import main.Main;
import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import utils.ObjetsList;

public strictfp class Meditation extends SpellEffect{

	public float remainingTime;

	
	public boolean active = false;
	public Meditation(Character launcher, Objet t, Plateau plateau){
		super(plateau);
		this.type = 1;
		this.name = ObjetsList.Meditation;
		launcher.etats.add(Etats.Meditating);
		this.x = launcher.getX();
		this.y = launcher.getY()+1f;
		remainingTime = this.getAttribut(Attributs.totalTime);	
		// Calculate step of lifepoints
		this.lifePoints = 1f;
		plateau.addSpell(this);
		owner = launcher.getId();
		this.team = launcher.getTeam();
	}

	public void action(Plateau plateau){
		this.remainingTime-=1f/Main.framerate;
		if(getOwner(plateau)==null)
			this.lifePoints=-1f;
		
		if(this.remainingTime<=0f){
			// Test if special capacity explosion
			if(getOwner(plateau).getAttribut(Attributs.ressourceAfterMeditation)==1f){
				getOwner(plateau).getTeam().food = getOwner(plateau).getTeam().food+20;
			}
			if(getOwner(plateau).getAttribut(Attributs.attackBonusAfterMeditation)==1f){
				getOwner(plateau).attributsChanges.add(new AttributsChange(Attributs.damage,Change.ADD,1,0));
			}
			
			this.lifePoints=-1f;
			getOwner(plateau).canMove = true;
			getOwner(plateau).etats.remove(Etats.Meditating);
		}
	}

//	public Graphics draw(Graphics g){
//		Color color = Color.darkGray;
//		color = new Color(100,150,255,0.4f);
//		Character owner = (Character) getOwner(plateau);
//		if(owner!=null){
//			int direction = (owner.orientation/2-1);
//			// inverser gauche et droite
//			if(direction==1 || direction==2){
//				direction = ((direction-1)*(-1)+2);
//			}
//			Image im;
//			im = Images.getUnit(owner.name, direction, owner.animation, getTeam().id, owner.isAttacking);
//			g.drawImage(im,owner.getX()-im.getWidth()/2,owner.getY()-3*im.getHeight()/4);
//			im.drawFlash(owner.getX()-im.getWidth()/2,owner.getY()-3*im.getHeight()/4,im.getWidth(),im.getHeight(),color);			
//		}
//		return g;
//	}

}
