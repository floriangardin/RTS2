package menu;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Game;
import ressources.Map;

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
		float startY = 0.45f*this.game.resY;
		float stepY = 0.09f*this.game.resY;
		float startX = this.game.resX/2;
		
		// handling items
		this.items.addElement(new Menu_Item(startX,startY,"Unjoueur",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+1f*stepY,"Multijoueur",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+2f*stepY,"Editeur",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+3f*stepY,"Options",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+4f*stepY,"Credits",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+5f*stepY,"Quitter",this.game,true));

		//		}
		
		//TODO : Delete
		// Bonus - to Delete

		Image image = this.game.images.getSand("A212");
		Image image2 = this.game.images.getSand("B221");
		Image image3 = this.game.images.getSand("C101");
		Image image4 = this.game.images.getSand("D112");
        int w = 2*image.getWidth();
        int h = 2*image.getHeight();
        Image combined;
		try {
			im = new Image(w, h);
			Graphics g = im.getGraphics();
			g.drawImage(image, 0, 0);
			g.drawImage(image2, w/2, 0);
			g.drawImage(image4, 0, h/2);
			g.drawImage(image3, w/2, h/2);
			g.flush();
		} catch (SlickException e) {
			e.printStackTrace();
		}        
	}
	
	//to delete too
	public Image im;

	public void callItem(int i){
		switch(i){
		case 0:
			this.game.inMultiplayer = false;
			this.game.initializePlayers();
			game.menuMapChoice.initializeMenuPlayer();
			Map.updateMap(0, game);
			this.game.setMenu(this.game.menuMapChoice);
			break;
		case 1:
			this.game.setMenu(this.game.menuMulti);
			break;
		case 2:
			this.game.inEditor = true;
			this.game.editor.plateau = null;
			this.game.isInMenu = false;
			break;
		case 3:
			this.game.setMenu(this.game.menuOptions);
			break;
		case 4:
			this.game.credits.initialize();
			this.game.setMenu(this.game.credits);
			break;
		case 5: 
			this.game.app.exit();
			break;
		default:		
		}
	}

}
