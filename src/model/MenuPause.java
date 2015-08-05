package model;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class MenuPause extends Menu {
	
	public MenuPause(Game game){
		this.game = game;
		this.items = this.createHorizontalCentered(3, this.game.resX, this.game.resY);
		this.items.get(0).name = "Continuer";
		this.items.get(1).name = "Recommencer";
		this.items.get(2).name = "Quitter";
	}
	
	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
	}
	public void callItem(int i){
		switch(i){
		case 0:
			this.game.quitMenu();
			break;
		case 1: 
			System.out.println("Vous essayer de recommencer, \nmais je sais pas encore comment on va faire ça.\ndésolé");
			break;
		case 2:
			System.out.println("Vous essayer de quitter, \nmais je sais pas encore comment on va faire ça.\ndésolé");
			break;
		default:		
		}
	}
}
