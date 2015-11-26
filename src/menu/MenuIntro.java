package menu;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import bullets.Bullet;
import model.Game;
import model.Objet;
import model.Utils;
import multiplaying.InputObject;

public class MenuIntro extends Menu {

	public Image newGame;
	public Image multiplayer;
	public Image options;
	public Image exit;
	public Image newGameSelected;
	public Image multiplayerSelected;
	public Image optionsSelected;
	public Image exitSelected;
	public int selected = -1;


	public MenuIntro(Game game){
		super(game);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		float startY = 0.50f*this.game.resY;
		float stepY = 0.09f*this.game.resY;
		float startX = this.game.resX/2;
		
		// handling items
		this.items.addElement(new Menu_Item(startX,startY,"unjoueur",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+1f*stepY,"multijoueur",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+2f*stepY,"options",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+3f*stepY,"quitter",this.game,true));

		//		}
	}

	public void callItem(int i){
		switch(i){
		case 0:
			this.game.setMenu(this.game.menuMapChoice);
			break;
		case 1:
			this.game.setMenu(this.game.menuMulti);
			break;
		case 2:
			this.game.setMenu(this.game.menuOptions);
			break;
		case 3: 
			this.game.app.exit();
			break;
		default:		
		}
	}

}
