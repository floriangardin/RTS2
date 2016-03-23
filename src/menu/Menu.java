package menu;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Game;
import multiplaying.InputObject;

public abstract class Menu {

	public Image backGround ;
	public Vector<Menu_Item> items;
	public Game game;
	public Image title;

	public Menu(Game g){
		this.game = g;
		this.items = new Vector<Menu_Item>();
		this.title = game.images.get("menuTitle01").getScaledCopy(0.35f*this.game.resY/650);
		this.backGround = game.images.get("backgroundMenu").getScaledCopy(0.35f*this.game.resY/650);

	}

	public void callItem(int i){
		/**
		 * function called when clicked on the item i
		 */

	}

	public void draw(Graphics g){
		/**
		 * render function
		 */
		this.drawItems(g);
	}


	public void update(InputObject im){
		/**
		 * function called on each game loop
		 */
		if(!game.musics.get("themeMenu").playing()){
			game.musics.get("themeMenu").play();
			game.musics.get("themeMenu").setVolume(game.options.musicVolume);
		}
		this.updateItems(im);
	}

	public void updateItems(InputObject im){
		// Checking each item : 
		// if mouse is click we apply the effect
		// if mouse is over we call the related function
		if(im!=null){
			Menu_Item item;
			for(int i=0; i<this.items.size(); i++){
				item = this.items.get(i);
				item.update(im);
				if(im.pressedLeftClick && item.mouseOver){
					this.callItem(i);
					this.game.sounds.get("menuItemSelected").play(1f,game.options.soundVolume);
				}
			}			
		}
	}
	public void deselectItems(){
		for(int i=0; i<this.items.size(); i++){
			this.items.get(i).toDraw = this.items.get(i).image;
		}	
	}

	public void drawItems(Graphics g){
		// draw background
		g.drawImage(this.backGround, 0,0,this.game.resX,this.game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		// draw items
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		// draw title
		g.drawImage(this.title, this.game.resX/2-this.title.getWidth()/2, 10f);
	}
}
