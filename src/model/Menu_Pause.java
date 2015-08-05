package model;

import java.util.Vector;

import org.newdawn.slick.Graphics;

public class Menu_Pause extends Menu {
	
	public Menu_Pause(){
		this.items = this.createHorizontalCentered(3, this.game.resX, this.game.resY);
		this.items.get(0).name = "Continuer";
		this.items.get(1).name = "Recommencer";
		this.items.get(2).name = "Quitter";
	}
	
	
}
