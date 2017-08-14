package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import events.Event;
import main.Main;
import plateau.Objet;
import plateau.Plateau;
import ressources.GraphicElements;
import ressources.Images;


public class DisplayRessources extends Event{

	private Image image;
	private Color color;
	private String string;
	private float x,y;
	private int totalRemainingTime = (int) (1.2*Main.framerate);
	private int remainingTime = totalRemainingTime;
	public static int taille = 24;
	
	private float vx, vy;
	
	public DisplayRessources(Objet parent, Plateau plateau, float price, String ressource){
		super(parent, plateau);
		this.topLayer = true;
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
		this.x = parent.x;
		this.y = parent.y;
		this.vy = (float) (-5f+Math.random()-0.5);
		this.vx = (float) (2f*Math.random()-1f);
	}
	
	

	@Override
	public boolean play(Graphics g, Plateau plateau) {
		this.vy += 6f/Main.framerate;
		this.y+=vy;
		this.x+=vx;
		this.remainingTime--;
		Color c = new Color(color.r,color.g,color.b,2f*this.remainingTime/totalRemainingTime);
		g.setColor(c);
		g.drawString(string, x-(GraphicElements.font_main.getWidth(string)+image.getWidth())/2, y-GraphicElements.font_main.getHeight(string)/2);
		this.image.setAlpha(2f*this.remainingTime/totalRemainingTime);
		g.drawImage(image, x+(GraphicElements.font_main.getWidth(string)-image.getWidth())/2, y - image.getHeight()/2);
		this.image.setAlpha(1f);
		return this.remainingTime>0;
	}
}
