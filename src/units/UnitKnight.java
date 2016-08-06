package units;

import data.Attributs;
import model.Game;
import utils.UnitsList;

public class UnitKnight extends Character {

	
	
	public UnitKnight(float x, float y,int team) {
		super(x,y,UnitsList.Knight,team);
	}

	
	public void useWeapon(){
		if(! (this.target instanceof Character)){
			return ;
		}
		Character c = (Character) this.target;
		// Attack sound
		float damage = this.getAttribut(Attributs.damage);
		if(Game.g.sounds!=null)
			Game.g.sounds.get(this.getAttributString(Attributs.weapon)).play(1f,Game.g.options.soundVolume);
		if(c.getAttributString(Attributs.weapon)=="bow"){
			damage = damage*this.getGameTeam().data.bonusSwordBow;
		}
		if(c.getAttribut(Attributs.armor)<damage){
			c.setLifePoints(c.lifePoints+c.getAttribut(Attributs.armor)-damage);
		}
		// Reset the state
		this.state = 0f;
		this.isAttacking = false;
		c.isAttacked();
	}
	


	
	


//	public Graphics draw(Graphics g){
//
//		float r = collisionBox.getBoundingCircleRadius()*1.9f;
//		float direction = 0f;
//		//Adapted to spearman TODO : Genericity
//		
//		if(this.isImmolating){
//			this.animation = 0;
//			this.orientation = 2;
//		}
//
//		direction = (float)(orientation/2-1);
//		int imageWidth = this.image.getWidth()/5;
//		int imageHeight = this.image.getHeight()/4;
//		float drawWidth = r*imageWidth/Math.min(imageWidth,imageHeight);
//		float drawHeight = r*imageHeight/Math.min(imageWidth,imageHeight);
//		float x1 = this.getX() - drawWidth;
//		float y1 = this.getY() + drawWidth - 2*drawHeight;
//		float x2 = this.getX() + drawWidth;
//		float y2 = this.getY() + drawWidth;
//		y1-=40f*Main.ratioSpace;
//		y2-=40f*Main.ratioSpace;
//		x1+=5f*Main.ratioSpace;
//		x2+=5f*Main.ratioSpace;
//		if(mouseOver && frozen<=0f){
//			Color color = new Color(this.gameteam.color.getRed(),this.gameteam.color.getGreen(),this.gameteam.color.getBlue(),0.4f);
//			Image i = this.image.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
//			i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));
//
//			g.drawImage(i,x1,y1);
//			i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
//			//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
//		}
//		else if(frozen<=0f){
//			g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
//		}else{
//		
//				Color color = Color.darkGray;
//				color = new Color(100,150,255,0.4f);
//				Image i = this.image.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
//				i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));
//
//				g.drawImage(i,x1,y1);
//				i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
//				//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
//		}
//
//		// Drawing the health bar
//		if(!isImmolating && this.lifePoints<this.maxLifePoints){
//			drawLifePoints(g,r);
//
//		}
//		//Draw the immolation
//		if(isImmolating){
//			drawImmolation(g,r);
//		}
//		return g;
//	
//	}

}




