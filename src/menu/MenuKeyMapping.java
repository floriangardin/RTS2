package menu;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import control.InputObject;
import control.KeyMapper;
import control.KeyMapper.KeyEnum;
import main.Main;
import menuutils.Menu_Item;
import model.Game;
import ressources.GraphicElements;
import system.MenuSystem.MenuNames;

public strictfp class MenuKeyMapping extends Menu {



	Image title;

	final int nbKey = KeyEnum.values().length;
	
	public boolean isSelecting;
	public KeyEnum selected;
	public int remainingTime;
	public int totalRemainingTime=2*Main.framerate;



	public MenuKeyMapping(){
		super();
		this.items = new Vector<Menu_Item>();
		//this.itemsSelected = new Vector<Menu_Item>();
		float startY = 2*Game.resY/5+20f;
		float sizeY = 5.5f*Game.resY/10-50f;
		float startX = Game.resX/6+20f;
		float sizeX = 2*Game.resX/3-40f;
		int nbPerLine = 10;
		int nbColonne = (KeyEnum.values().length-1)/nbPerLine+1;
		float stepY = sizeY / (nbPerLine+1);
		float stepX = sizeX / nbColonne;
		int i=0, j=0;
		Menu_Item mi;
		for(KeyEnum ke : KeyEnum.values()){
			mi = new Menu_Item(startX + (i+0.5f)*stepX, startY + j*stepY, ke.name(), true);
			mi.font = GraphicElements.font_menu_small_button;
			mi.font_selected = GraphicElements.font_menu_small_button_selected;
			mi.font_current = mi.font;
			this.items.add(mi);
			j+=1;
			if(j>=nbPerLine){
				j = 0;
				i+=1;
			}
		}
		this.items.addElement(new Menu_Item(Game.resX*3f/7f,startY+(nbPerLine+0.5f)*stepY," ",false));
		this.items.addElement(new Menu_Item(Game.resX*5f/7f,startY+(nbPerLine+0.5f)*stepY,"Retour",true));
	}


	public void callItem(int i){
		System.out.println(i);
		if(i==nbKey+1){ 
			// retour
			Game.menuSystem.setMenu(MenuNames.MenuOptions);
			selected = null;
			isSelecting = false;
			KeyMapper.saveMapping();
		} else if(!isSelecting && i<nbKey){
			isSelecting = true;
			selected = KeyEnum.values()[i];
			try{
				this.items.get(nbKey).name = selected.name()+" - "+Input.getKeyName(KeyMapper.mapping.get(selected));
			} catch(Exception e) {
				this.items.get(nbKey).name = selected.name()+" - Non assigné";
			}
		}
		
	}

	public void update(Input in){
		if(isSelecting){
			for(int i = 0; i<300; i++){
				try{
					if(in.isKeyPressed(i)){
						for(KeyEnum ke : KeyMapper.mapping.keySet()){
							if(KeyMapper.mapping.get(ke)==i){
								KeyMapper.mapping.put(ke,-1);
							}
						}
						KeyMapper.mapping.put(selected, i);
						selected = null;
						isSelecting = false;
						this.items.get(nbKey).name = " ";
					}
				} catch(Exception e){}
			}
		} else {
			InputObject im = new InputObject(in);
			for(KeyEnum ke : KeyEnum.values()){
				for(Menu_Item mi : this.items.subList(0, this.items.size()-2)){
					if(mi.name.equals(ke.name())){
						if(KeyMapper.mapping.get(ke)==-1){
							mi.font = GraphicElements.font_red;
							mi.font_current = mi.font;
						} else {
							mi.font = GraphicElements.font_menu_small_button;
							mi.font_current = mi.font;
						}
					}
				}
			}
			for(KeyEnum ke : im.pressed){
				if(ke!=KeyEnum.LeftClick){
					this.items.get(nbKey).name = ke.name()+" - "+Input.getKeyName(KeyMapper.mapping.get(ke));
					remainingTime = totalRemainingTime;
				}
			}
			if(im.pressed.size()==0){
				remainingTime--;
				if(remainingTime<=0){
					this.items.get(nbKey).name = " ";
				}
			}
			this.updateItems(im, false);
		}
	}
	

	public void draw(Graphics g){
		this.drawItems(g);
	}
}
