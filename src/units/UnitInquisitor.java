package units;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import bullets.Fireball;
import data.Attributs;
import data.Data;
import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;
import utils.UnitsList;

public class UnitInquisitor extends Character {

	
	public UnitInquisitor(float x, float y,int team) {
		super(x,y,UnitsList.Inquisitor, team);	
	}
	
	public void action(){
		if(this.mode==TAKE_BUILDING){
			this.mode = NORMAL;
		}
		mainAction();
	}
	
	
	public void useWeapon(){
		new Fireball(this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.getAttribut(Attributs.damage),-1);
		this.state = 0f;
		this.isAttacking = false;
	}

}
