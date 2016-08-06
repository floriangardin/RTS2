package units;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import utils.Utils;
import data.Data;
import model.Game;
import model.GameTeam;
import model.Plateau;
import model.Player;


public class UnitArchange extends Character {

	public static float radiusCollisionBox = 30f;
	
	public UnitArchange(GameTeam gameteam) {
		super(gameteam);
		this.name = "archange";
		this.type = UnitsList.Archange;
		this.unitType = ARCHANGE;
		this.maxLifePoints = 200f*gameteam.data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f*Main.ratioSpace;
		this.collisionBox = new Circle(0f,0f,radiusCollisionBox);
		this.selectionBox = new Rectangle(-1.5f*radiusCollisionBox,-2.5f*radiusCollisionBox,3*radiusCollisionBox,3*radiusCollisionBox);
		this.maxVelocity = 60f*Main.ratioSpace*gameteam.data.speedFactor;
		this.armor = 5f;
		this.damage = 20f*gameteam.data.damageFactor;
		this.chargeTime = 12f;
		this.weapon = "sword";
		this.civ = gameteam.civ;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f*Main.ratioSpace;
		this.explosionWhenImmolate = gameteam.data.explosionWhenImmolate;
		this.spells.add(gameteam.data.instantDeath);
		this.spells.add(gameteam.data.instantHealth);
	}

	public UnitArchange(UnitArchange archange, float x, float y,int id) {
		super(archange,x,y,id);
	}

//	public Graphics draw(Graphics g){
//		float r = collisionBox.getBoundingCircleRadius();
//		float direction = 0f;
//		direction = (float)(orientation/2-1);
//		int imageWidth = this.image.getWidth()/4;
//		int imageHeight = this.image.getHeight()/4;
//		float drawWidth = r*imageWidth/Math.min(imageWidth,imageHeight);
//		float drawHeight = r*imageHeight/Math.min(imageWidth,imageHeight);
//		float x1 = this.getX() - drawWidth*2f;
//		float y1 = this.getY() + (drawWidth - 2*drawHeight)*3f;
//		float x2 = this.getX() + drawWidth*2f;
//		float y2 = this.getY() + drawWidth;
//		g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
//		// Drawing the health bar
//		if(!isImmolating && this.lifePoints<this.maxLifePoints){
//			drawLifePoints(g,r);
//		}
//		//Draw the immolation
//		if(isImmolating){
//			drawImmolation(g,r);
//		}
//		return g;
//	}
	
	

	public void useWeapon(){
		Character c = (Character) this.target;
		
		// Attack sound
		float damage = this.damage;
	
		if(Game.g.sounds!=null)
			Game.g.sounds.get(this.weapon).play(1f,Game.g.options.soundVolume);

		if(c.armor<damage){
			c.setLifePoints(c.lifePoints+c.armor-damage);
		}
		// Reset the state
		this.state = 0f;
		c.isAttacked();
		this.isAttacking = false;
		
	}


}
