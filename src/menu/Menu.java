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

	public float xMouseTemp, yMouseTemp;
	public boolean mouseControl = true;
	public int itemSelected = -1;
	public boolean arrowPressed = false;
	public boolean escPressed = false;

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
		//		if(this instanceof MenuIntro)
		//			g.drawImage(((MenuIntro)this).im, 20, 20);

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
			// if you move your mouse more than a certain distance you able it
			float distanceThreshold = 250f;
			if(! mouseControl && (xMouseTemp-im.xMouse)*(xMouseTemp-im.xMouse)+(yMouseTemp-im.yMouse)*(yMouseTemp-im.yMouse) > distanceThreshold){
				mouseControl = true;
			}
			// Handling passage to mouseControl or not mouse Control
			// if you press up or down you disable mouse control
			if(arrowPressed && !im.isPressedUP && !im.isPressedDOWN){
				arrowPressed = false;
			}
			if(!arrowPressed && (im.isPressedUP || im.isPressedDOWN)){
				this.mouseControl = false;
				this.xMouseTemp = im.xMouse;
				this.yMouseTemp = im.yMouse;
			}
			// handling key control
			if(! mouseControl){
				boolean flag = !arrowPressed && (im.isPressedUP || im.isPressedDOWN);
				if(!arrowPressed && im.isPressedUP){
					do{
						this.itemSelected-=1;
						this.itemSelected = this.itemSelected % this.items.size() + (this.itemSelected<0 ? this.items.size() : 0);
					} while(!this.items.get(itemSelected).selectionable || this.items.get(itemSelected) instanceof Menu_Curseur);
				}
				if(!arrowPressed && im.isPressedDOWN){
					do{
						this.itemSelected+=1;
						this.itemSelected = this.itemSelected % this.items.size() + (this.itemSelected<0 ? this.items.size() : 0);
					} while(!this.items.get(itemSelected).selectionable || this.items.get(itemSelected) instanceof Menu_Curseur);
				}
				for(int i=0; i<this.items.size(); i++){
					if(i==this.itemSelected){
						if(flag)
							this.game.sounds.get("menuMouseOverItem").play(1f,this.game.options.soundVolume);
						item = this.items.get(i);
						item.setMouseOver(true);
						if(im.isPressedENTER){
							this.game.sounds.get("menuMouseOverItem").play(1f,this.game.options.soundVolume);
							this.callItem(i);
						}
					} else {
						item = this.items.get(i);
						item.setMouseOver(false);						
					}
				}
			}
			// handling mouse control
			if(mouseControl){
				for(int i=0; i<this.items.size(); i++){
					item = this.items.get(i);
					item.update(im);
					if(item.mouseOver){
						this.itemSelected = i;
					}
					if((im.isPressedENTER || im.pressedLeftClick) && item.mouseOver){
						this.callItem(i);
						this.game.sounds.get("menuItemSelected").play(1f,game.options.soundVolume);
					}
				}			
			}
			if(im.isPressedUP || im.isPressedDOWN){
				arrowPressed = true;
			}
			if(im.isPressedESC)
				this.escPressed = true;
			if(escPressed && !im.isPressedESC){
				this.escPressed = false;
				this.callItem(this.items.size()-1);
				this.game.sounds.get("menuItemSelected").play(1f,game.options.soundVolume);
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
