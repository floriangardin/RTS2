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
		try {
			this.title = new Image("pics/menu/title01.png").getScaledCopy(0.35f*this.game.resY/650);
			this.backGround = new Image("pics/fondMenu.png").getScaledCopy(0.35f*this.game.resY/650);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if(!game.musics.menu.playing()){
			game.musics.menu.play();
			game.musics.menu.setVolume(game.options.musicVolume);
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
					this.game.sounds.menuItemSelected.play(1f,game.options.soundVolume);
				}
			}			
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
