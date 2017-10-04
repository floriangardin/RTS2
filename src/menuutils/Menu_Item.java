package menuutils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;

import control.InputObject;
import model.Game;
import ressources.GraphicElements;
import ressources.Images;
import ressources.Sounds;

public strictfp class Menu_Item {

	public float sizeX;
	public float sizeY;
	public float x;
	public float y;
	public float animation = 0f;
	public Color color = Color.black;
	public boolean selectionable = true;
	public boolean mouseOver = false;
	public String name;
	public UnicodeFont font;
	public UnicodeFont font_selected;
	public UnicodeFont font_current;
	
	public Menu_Item(){

	}

	public Menu_Item(float x, float y, String name, boolean selectionnable){
		this.x = x;
		this.y = y;
		this.name = name;
		this.selectionable = selectionnable;
		this.font = GraphicElements.font_menu_button;
		this.font_selected = GraphicElements.font_menu_button_selected;
		this.font_current = this.font;
		this.sizeX = this.font.getWidth(name);
		this.sizeY = this.font.getHeight(name);
	}


	public boolean isMouseOver(InputObject im){
		float xMouse = im.xOnScreen;
		float yMouse = im.yOnScreen;
		return (x-sizeX/2f<xMouse && xMouse<x+sizeX/2f && y-sizeY/2f<yMouse && yMouse<y+sizeY/2f);
	}


	public void draw(Graphics g){
		font_current.drawString(x-font_current.getWidth(this.name)/2, y-font_current.getHeight(this.name)/2f,this.name);
	}
	public void draw(Graphics g, float x, float y){
		font_current.drawString(x-font_current.getWidth(this.name)/2, y-font_current.getHeight(this.name)/2f,this.name);
	}

	public void update(InputObject im){
		if(this.selectionable){
			if(this.isMouseOver(im)){
				if(!mouseOver){
					Sounds.playSound("menuMouseOverItem");
					mouseOver = true;
				}
				this.font_current = this.font_selected;
			} else {
				if(mouseOver)
					mouseOver = false;
				this.font_current = this.font;
			}
		}
	}

	public void setMouseOver(boolean b){
		if(this.selectionable){
			if(b){
				this.font_current = this.font_selected;
			} else {
				this.font_current = this.font;
			}
		}
	}
	
	public void setY(float y){
		this.y = y;
	}
}
