package control;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import plateau.Plateau;

public abstract strictfp class Bar {

	public float sizeX;
	public float sizeY;
	public float x;
	public float y;


	public Graphics draw(Graphics g){
		return g;
	}
}
