package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import model.Game;
import multiplaying.InputObject;

public class Menu_Item {

	public float sizeX;
	public Game game;
	public float sizeY;
	public float x;
	public float y;
	public Image image;
	public Image selectedImage;
	public boolean isMouseOnIt = false;
	public float animation = 0f;
	public Color color = Color.black;
	public Image toDraw;
	public boolean selectionable = true;
	public boolean mouseOver = false;
	
	public Menu_Item(){
		
	}

	public Menu_Item(float x, float y, Image im, Image selectedImage, Game game) {
		this.image = im;
		this.selectedImage = selectedImage;
		this.game = game;
		this.toDraw = this.image;
		this.x = x;
		this.y = y;
		this.sizeX = this.image.getWidth();
		this.sizeY = this.image.getHeight();
	}

	public boolean isClicked(Input i){
		float xMouse = i.getAbsoluteMouseX();
		float yMouse = i.getAbsoluteMouseY();
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}
	public boolean isClicked(InputObject im){
		float xMouse = im.xMouse;
		float yMouse = im.yMouse;
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}


	public void draw(Graphics g){
		
		g.drawImage(this.toDraw,x, y);
	}


	public void update(Input i){
		if(this.selectionable){
			if(this.isClicked(i)){
				if(!mouseOver){
					this.game.sounds.menuMouseOverItem.play(1f,this.game.options.soundVolume);
					mouseOver = true;
				}
				this.toDraw = this.selectedImage;
			} else {
				if(mouseOver)
					mouseOver = false;
				this.toDraw = this.image;
			}
		}
	}

}
