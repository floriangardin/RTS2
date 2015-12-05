package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

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
		try {
			this.image = new Image("pics/menu/"+name+".png").getScaledCopy(game.ratioResolution);
			if(selectionnable){
				this.selectedImage = new Image("pics/menu/"+name+"selected.png").getScaledCopy(game.ratioResolution);
			}
		} catch (SlickException e) {
			System.out.println("Error Menu_Item line 63 : image manquante");
		}
		this.toDraw = this.image;
		this.sizeX = this.image.getWidth();
		this.sizeY = this.image.getHeight();
	}

	
	public boolean isMouseOver(InputObject im){
		float xMouse = im.xMouse;
		float yMouse = im.yMouse;
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
