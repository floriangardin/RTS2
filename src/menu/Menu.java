package menu;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import menuutils.Menu_Curseur;
import menuutils.Menu_Item;
import model.Game;
import ressources.Images;
import ressources.Sounds;

public abstract strictfp class Menu {

	public Image backGround ;
	public Vector<Menu_Item> items;
	public Image title;

	public float xMouseTemp, yMouseTemp;
	public boolean mouseControl = true;
	public int itemSelected = -1;
	public boolean arrowPressed = false;
	public boolean escPressed = false;

	public Menu(){
		this.items = new Vector<Menu_Item>();
		this.title = Images.get("menuTitle01").getScaledCopy(0.35f*Game.resY/650);
		this.backGround = Images.get("backgroundMenu").getScaledCopy(0.35f*Game.resY/650);
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
		this.updateItems(im);
	}
	
	public void init(){
		
	}

	public void updateItems(InputObject im){
		updateItems(im, true);
	}
	
	public void updateItems(InputObject im, boolean updateKeys){
		// Checking each item : 
		// if mouse is click we apply the effect
		// if mouse is over we call the related function
		if(im!=null){
			Menu_Item item;
			// if you move your mouse more than a certain distance you able it
			float distanceThreshold = 250f;
			if(! mouseControl && (xMouseTemp-im.xOnScreen)*(xMouseTemp-im.xOnScreen)+(yMouseTemp-im.yOnScreen)*(yMouseTemp-im.yOnScreen) > distanceThreshold){
				mouseControl = true;
			}
			// Handling passage to mouseControl or not mouse Control
			// if you press up or down you disable mouse control
			if(arrowPressed && !im.isPressed(KeyEnum.Up) && !im.isPressed(KeyEnum.Down)){
				arrowPressed = false;
			}
			if(updateKeys && !arrowPressed && (im.isPressed(KeyEnum.Up) || im.isPressed(KeyEnum.Down))){
				this.mouseControl = false;
				this.xMouseTemp = im.xOnScreen;
				this.yMouseTemp = im.yOnScreen;
			}
			// handling key control
			if(! mouseControl){
				boolean flag = !arrowPressed && (im.isPressed(KeyEnum.Up) || im.isPressed(KeyEnum.Down));
				if(!arrowPressed && im.isPressed(KeyEnum.Up)){
					do{
						this.itemSelected-=1;
						this.itemSelected = this.itemSelected % this.items.size() + (this.itemSelected<0 ? this.items.size() : 0);
					} while(!this.items.get(itemSelected).selectionable || this.items.get(itemSelected) instanceof Menu_Curseur);
				}
				if(!arrowPressed && im.isPressed(KeyEnum.Down)){
					do{
						this.itemSelected+=1;
						this.itemSelected = this.itemSelected % this.items.size() + (this.itemSelected<0 ? this.items.size() : 0);
					} while(!this.items.get(itemSelected).selectionable || this.items.get(itemSelected) instanceof Menu_Curseur);
				}
				for(int i=0; i<this.items.size(); i++){
					if(i==this.itemSelected){
						if(flag)
							Sounds.playSound("menuMouseOverItem");
						item = this.items.get(i);
						item.setMouseOver(true);
						if(im.isPressed(KeyEnum.Enter)){
							Sounds.playSound("menuMouseOverItem");
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
					if((im.isPressed(KeyEnum.Enter) || im.isPressed(KeyEnum.LeftClick)) && item.mouseOver){
						this.callItem(i);
						Sounds.playSound("menuItemSelected");
					}
				}
				
				
			}
			if(updateKeys && (im.isPressed(KeyEnum.Up) || im.isPressed(KeyEnum.Down))){
				arrowPressed = true;
			}
			if(updateKeys && im.isPressed(KeyEnum.Escape))
				this.escPressed = true;
			if(updateKeys && escPressed && !im.isPressed(KeyEnum.Escape)){
				this.escPressed = false;
				this.callItem(this.items.size()-1);
				Sounds.playSound("menuItemSelected");
			}
		}
	}
	
	public void deselectItems(){
		for(int i=0; i<this.items.size(); i++){
			this.items.get(i).font_current  = this.items.get(i).font;
		}	
	}

	public void drawItems(Graphics g){
		// draw background
//		g.drawImage(this.backGround, 0,0,Game.resX,Game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		// draw items
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		// draw title
		g.drawImage(this.title, Game.resX/2-this.title.getWidth()/2, 10f);
	}
}
