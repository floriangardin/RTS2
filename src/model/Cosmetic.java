package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import control.InputObject;
import control.KeyMapper.KeyEnum;
// Handle cosmetic for lan game to avoid feeling of latency
public class Cosmetic {
	
	Rectangle selection;
	float recX;
	float recY;
	
	
	
	public Cosmetic(){

	}
	
	public void update(InputObject im){
		//SELECTION RECTANGLE
		if (im.idplayer == Game.g.currentPlayer.id) {
			Game.g.plateau.handleMouseHover(im);
		}
		if (im.isDown(KeyEnum.LeftClick)) {


			if (im.isOnMiniMap && selection==null) {
				return;
			}
			if (selection == null|| im.isPressed(KeyEnum.ToutSelection)) {
				recX= (float) im.x;
				recY= (float) im.y;
				selection = new Rectangle(recX, recX, 0.1f, 0.1f);
			}
			selection.setBounds((float) Math.min(recX, im.x),
					(float) Math.min(recY, im.y), (float) Math.abs(im.x - recX) + 0.1f,
					(float) Math.abs(im.y - recY) + 0.1f);
		}else{
			selection = null;
		}
		// Spell visualisation
		
	}
	
	public Graphics draw(Graphics g){
		g.setLineWidth(1f);
		g.draw(selection);
		
//		System.out.println("v1 : "+selection.getX()+" "+selection.getY());
		// Draw Input handler for debug
	

		return g;
	}
	
	
	
	
}
