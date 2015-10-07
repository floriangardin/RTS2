package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import multiplaying.InputModel;

public class Menu_Item {

	public float sizeX;
	public float sizeY;
	public float x;
	public float y;
	public Image image;
	public String name;
	public Image selectedImage;
	public boolean isMouseOnIt = false;
	public float animation = 0f;
	public Color color = Color.black;
	public Image toDraw;
	public boolean colorAnimation = true;

	public Menu_Item(float x, float y, Image im, Image selectedImage,String name) {
		this.image = im;
		this.selectedImage = selectedImage;
		this.toDraw = this.image;
		this.x = x;
		this.y = y;
		this.name = name;
		this.sizeX = this.image.getWidth();
		this.sizeY = this.image.getHeight();
	}

	public boolean isClicked(Input i){
		float xMouse = i.getAbsoluteMouseX();
		float yMouse = i.getAbsoluteMouseY();
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}
	public boolean isClicked(InputModel im){
		float xMouse = im.xMouse;
		float yMouse = im.yMouse;
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}


	public void draw(Graphics g){
		
		g.drawImage(this.toDraw,x, y);
	}

	public void printDebug(){
		System.out.println(this.name+" "+this.x+" "+this.y+" " +this.sizeX+" " +this.sizeY);
	}

	public void update(Input i){
		if(this.colorAnimation){
			if(this.isClicked(i)){
				this.toDraw = this.selectedImage;
			} else {
				this.toDraw = this.image;
			}
		}
	}

}
