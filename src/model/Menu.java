package model;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public abstract class Menu {

	protected Vector<Menu_Item> items;
	public Game game;

	public void callItems(Input i){
		
	}

	public void callItem(int i){

	}

	public Vector<Menu_Item> createHorizontalCentered(int i, float sizeX, float sizeY){
		Vector<Menu_Item> items = new Vector<Menu_Item>();
		float unitY = sizeY/(5f+2f*i);
		for(int j=0;j<i;j++){
			items.add(new Menu_Item(sizeX/3f,3*unitY+2f*j*unitY,sizeX/3f,unitY,""));
		}
		return items;
	}

	public void draw(Graphics g){
		for(int i=0; i<this.items.size(); i++){
			this.items.get(i).draw(g);
		}
	}

	public void printDebug(){
		for(int i=0; i<this.items.size(); i++)
			this.items.get(i).printDebug();
		;
	}
}
