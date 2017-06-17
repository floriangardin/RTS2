package display;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import model.Player;
import plateau.Plateau;

public abstract class Bar {

	public float sizeX;
	public float sizeY;
	public float x;
	public float y;
	public Player player;


	public Graphics draw(Graphics g){
		return g;
	}
}
