package render;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import data.Attributs;
import main.Main;
import model.Colors;
import plateau.Building;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;

public class RenderCharacter {
	
	public static void render(Character character, Graphics g, Plateau plateau){
		
		//g.setColor(Color.blue); // Draw selection box : 
		//g.drawRect(character.selectionBox.getX(), character.selectionBox.getY(),character.selectionBox.getWidth(), character.selectionBox.getHeight());
		float r = 60f*Main.ratioSpace;
		int direction = (character.orientation/2-1);
		// inverser gauche et droite
		if(direction==1 || direction==2){
			direction = ((direction-1)*(-1)+2);
		}
		Image im;
		im = Images.getUnit(character.name, direction, character.animation, character.getTeam().id, character.isAttacking);
		if(character.mouseOver && character.frozen<=0f){
			Color color = Color.darkGray;
			if(character.getTeam().id==1){
				color = new Color(0,0,205,0.4f);
			}
			else{
				color = new Color(250,0,0,0.4f);
			}
			g.drawImage(im,character.x-im.getWidth()/2,character.y-3*im.getHeight()/4);
			drawFlash(g, color, character, plateau);
		}
		else{
			g.drawImage(im,character.x-im.getWidth()/2,character.y-3*im.getHeight()/4);
		}
		if(character.frozen>0f){
			Color color = Color.darkGray;
			color = new Color(100,150,255,0.4f);
			g.drawImage(im,character.x-im.getWidth()/2,character.y-3*im.getHeight()/4);
			drawFlash(g, color, character, plateau);
			
		}
		if(character.isBolted){
			Color color = new Color(44,117,255,0.8f);
			g.drawImage(im,character.x-im.getWidth()/2,character.y-3*im.getHeight()/4);
			drawFlash(g, color, character, plateau);
		}

		// Drawing the health bar
		if(character.lifePoints<character.getAttribut(Attributs.maxLifepoints)){
			drawLifePoints(g,r, character, plateau);
		}
	}
	
	//// GRAPHISMS
	public static void drawLifePoints(Graphics g,float r, Character character, Plateau plateau){
		//Draw lifepoints

		g.setColor(Color.black);
		g.fillRect(character.getX()-r/2-1f,-47f+character.getY()-r,r+2f,8f);
		float x = character.lifePoints/character.getAttribut(Attributs.maxLifepoints);
		g.setColor(new Color((int)(255*(1f-x)),(int)(255*x),0));
		g.fillRect(character.getX()-r/2,-46f+character.getY()-r,x*r,6f);
	}
	
	public static void drawFlash(Graphics g, Color color, Character character, Plateau plateau){
		int direction = (character.orientation/2-1);
		if(direction==1 || direction==2){
			direction = ((direction-1)*(-1)+2);
		}
		Image im;
		im = Images.getUnit(character.name, direction, character.animation, character.getTeam().id, character.isAttacking);
		im.drawFlash(character.x-im.getWidth()/2,character.y-3*im.getHeight()/4,im.getWidth(),im.getHeight(),color);
	}

	public static void renderSelection(Graphics g, Character character, Plateau plateau){

		g.setColor(Colors.selection);
		g.setLineWidth(2f*Main.ratioSpace);
		g.setAntiAlias(true);
		g.draw(character.collisionBox);
		Objet target = character.getTarget(plateau);
		
		if(target !=null && target instanceof Checkpoint){
			RenderCheckpoint.render((Checkpoint) target, g, plateau);
		}
		
		if(target !=null && target instanceof Building){
			RenderCheckpoint.render(((Building) target).marker, g, plateau);
		}
		if(character.mode==Character.MOVE || character.mode==Character.NORMAL){
			g.setColor(Colors.team0);
		}
		else if(character.mode==Character.AGGRESSIVE){
			g.setColor(Colors.aggressive);
		}
		else if(character.mode==Character.TAKE_BUILDING){
			g.setColor(Colors.buildingTaking);
		}
		g.setLineWidth(2f);
		if(character.getTarget(plateau) instanceof Character){
			g.setLineWidth(2f*Main.ratioSpace);
			g.setColor(Colors.aggressive);
			g.draw(character.getTarget(plateau).collisionBox);
		}
		if(character.getTarget(plateau) instanceof Checkpoint){
			((Checkpoint) character.getTarget(plateau)).toDraw = true;
			//character.target.draw(g);
		}
		if(character.getTarget(plateau) instanceof Building){
			((Building) character.getTarget(plateau)).marker.toDraw = true;
			//character.target.draw(g);
		}
		//		//Draw the building which is being conquered
		//		if(character.target !=null && character.target instanceof Building && character.mode==Character.TAKE_BUILDING){
		//			g.setLineWidth(2f*Main.ratioSpace);
		//			g.setColor(Colors.buildingTaking);
		//			Building target = (Building) character.target;
		//			g.draw(target.collisionBox);
		//		}

		g.setLineWidth(1f*Main.ratioSpace);
		g.setAntiAlias(false);
	}	

}
