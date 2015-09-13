package menu;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;

import model.Game;
import model.Sounds;
import multiplaying.InputModel;

public abstract class Menu {

	protected Vector<Menu_Item> items;
	public Game game;
	public Sounds sounds;
	public Music music;
	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
	}
	public void callItems(InputModel im){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(im))
				callItem(j);
		}
	}

	public void callItem(int i){

	}

	public Vector<Menu_Item> createHorizontalCentered(int i, int sizeX, int sizeY){
		Vector<Menu_Item> items = new Vector<Menu_Item>();
		float unitY = sizeY/(9f+3f*i);
		for(int j=0;j<i;j++){
			items.add(new Menu_Item(sizeX/3f,5*unitY+3f*j*unitY,sizeX/3f,2*unitY,""));
		}
		return items;
	}

	public void draw(Graphics g){
		//g.translate(-game.Xcam,-game.Ycam);
		for(int i=0; i<this.items.size(); i++){
			this.items.get(i).draw(g);
		}
	}

	public void printDebug(){
		for(int i=0; i<this.items.size(); i++)
			this.items.get(i).printDebug();
		;
	}
	
	public void update(Input i){
		
	}
	public void update(InputModel im){
		
	}
}
