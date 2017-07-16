package menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import org.newdawn.slick.Image;

import menuutils.Menu_Item;
import model.Game;
import model.GameClient;
import model.GameServer;
import system.MenuSystem.MenuNames;

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


	public MenuIntro(){
		super();
		this.items = new Vector<Menu_Item>();
		float startY = 0.45f*Game.resY;
		float stepY = 0.09f*Game.resY;
		float startX = Game.resX/2;
		
		// handling items
		this.items.addElement(new Menu_Item(startX,startY,"Unjoueur",true));
		this.items.addElement(new Menu_Item(startX,startY+1f*stepY,"Multijoueur",true));
		this.items.addElement(new Menu_Item(startX,startY+2f*stepY,"Editeur",true));
		this.items.addElement(new Menu_Item(startX,startY+3f*stepY,"Options",true));
		this.items.addElement(new Menu_Item(startX,startY+4f*stepY,"Credits",true));
		this.items.addElement(new Menu_Item(startX,startY+5f*stepY,"Quitter",true));

		//		}
		
		     
	}
	
	//to delete too
	public Image im;

	public void callItem(int i){
		switch(i){
		case 0:
			Game.menuSystem.setMenu(MenuNames.MenuMapChoice);
			try {
				GameServer.init();
				String addressHost = InetAddress.getLocalHost().getHostAddress();
				GameClient.init(addressHost);
				Lobby.init();
			} catch (UnknownHostException e) {}
			break;
		case 1:
			Game.menuSystem.setMenu(MenuNames.MenuMulti);
			break;
		case 2:
			break;
		case 3:
			Game.menuSystem.setMenu(MenuNames.MenuOptions);
			break;
		case 4:
			Game.menuSystem.setMenu(MenuNames.Credits);
			break;
		case 5: 
			Game.app.exit();
			break;
		default:		
		}
	}

}
