package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import model.Game;

public class Menu_Item {

	public float sizeX;
	public Game game;
	public float sizeY;
	public float x;
	public float y;
	public Image image;
	public Image selectedImage;
	public float animation = 0f;
	public Color color = Color.black;
	public Image toDraw;
	public boolean selectionable = true;
	public boolean mouseOver = false;
	public String name;
	public Menu_Item(){

	}

	public Menu_Item(float x, float y, String name,Image im, Image selectedImage, Game game) {
		this.image = im;
		this.name = name;
		this.selectedImage = selectedImage;
		this.game = game;
		this.toDraw = this.image;
		this.x = x;
		this.y = y;
		if(this.image!=null){
			this.sizeX = this.image.getWidth();
			this.sizeY = this.image.getHeight();
		}
	}

	public Menu_Item(float x, float y, String name, Game game, boolean selectionnable){
		this.game = game;
		this.x = x;
		this.y = y;
		this.selectionable = selectionnable;
		this.image = Game.g.images.get("menu"+name).getScaledCopy(game.ratioResolution);
		if(selectionnable)
			this.selectedImage = Game.g.images.get("menu"+name+"selected").getScaledCopy(game.ratioResolution);
		this.toDraw = this.image;
		this.sizeX = this.image.getWidth();
		this.sizeY = this.image.getHeight();
	}


	public boolean isMouseOver(InputObject im){
		float xMouse = im.x;
		float yMouse = im.y;
		return (x-sizeX/2f<xMouse && xMouse<x+sizeX/2f && y-sizeY/2f<yMouse && yMouse<y+sizeY/2f);
	}


	public void draw(Graphics g){
		if(this.image!=null){
			g.drawImage(this.toDraw,x-this.image.getWidth()/2f, y-this.image.getHeight()/2f);
		}
		else{
			g.setColor(Color.white);
			g.drawString(this.name, x, y-this.game.font.getHeight(this.name)/2f);
		}

	}


	public void update(InputObject im){
		if(this.selectionable){
			if(this.isMouseOver(im)){
				if(!mouseOver){
					this.game.sounds.get("menuMouseOverItem").play(1f,this.game.options.soundVolume);
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
	
	public void setMouseOver(boolean b){
		if(this.selectionable){
			if(b){
				this.toDraw = this.selectedImage;
			} else {
				this.toDraw = this.image;
			}
		}
	}
}
