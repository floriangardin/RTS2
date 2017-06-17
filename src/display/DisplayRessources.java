package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.Main;
import model.Game;


public class DisplayRessources {

	private Image image;
	private Color color;
	private String string;
	private float x,y;
	private int totalRemainingTime = (int) (1.2*Main.framerate);
	private int remainingTime = totalRemainingTime;
	public static int taille = 24;
	
	private float vx, vy;
	
	public DisplayRessources(float price, String ressource, float x2, float y2){
		switch(ressource){
		case "gold" : this.image = Images.get("imageGolddisplayressources");break;
		case "food" : this.image = Images.get("imageFooddisplayressources");break;
		case "faith" : this.image = Images.get("imageSpecial");break;
		default:
		}
		if(price>0){
			color = Color.green;
			string = "+ "+(int)Math.abs(price)+" ";
		} else {
			color = Color.red;
			string = "- "+(int)Math.abs(price)+" ";
		}
		this.x = x2;
		this.y = y2;
		this.vy = (float) (-5f+Math.random()-0.5);
		this.vx = (float) (2f*Math.random()-1f);
	}
	
	public void update(){
		this.vy += 6f/Main.framerate;
		this.y+=vy;
		this.x+=vx;
		this.remainingTime--;
	}
	
	public void draw(Graphics g){
//		Utils.drawNiceRect(g, color, x-(Game.g.font.getWidth(string)+image.getWidth())/2-2, y-Game.g.font.getHeight(string)/2-2, (Game.g.font.getWidth(string)+image.getWidth())+4, Game.g.font.getHeight(string)+4);
		Color c = new Color(color.r,color.g,color.b,2f*this.remainingTime/totalRemainingTime);
		g.setColor(c);
		g.drawString(string, x-(Game.g.font.getWidth(string)+image.getWidth())/2, y-Game.g.font.getHeight(string)/2);
		this.image.setAlpha(2f*this.remainingTime/totalRemainingTime);
		g.drawImage(image, x+(Game.g.font.getWidth(string)-image.getWidth())/2, y - image.getHeight()/2);
		this.image.setAlpha(1f);
	}
	
	public boolean isDead(){
		return this.remainingTime<=0;
	}
}
