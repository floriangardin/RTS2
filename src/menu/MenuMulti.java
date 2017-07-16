package menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import menuutils.Menu_Item;
import menuutils.Menu_Map;
import model.Game;
import model.GameClient;
import model.GameServer;
import ressources.GraphicElements;
import system.MenuSystem.MenuNames;

public class MenuMulti extends Menu {

	Image title;
	public Image marbre;
	public Image host;
	public Image join;
	public Image back;

	public Image hostSelected;
	public Image joinSelected;
	public Image backSelected;



	public float startXGames;
	public float sizeXGames;
	public float startYGames;
	public float sizeYGames;
	float startX;
	float startY;
	float stepY;
	float ratioReso;

	
	public Vector<Menu_Map> gamesList;
	public Vector<String> openGames;
	public int Nplayer;
	public int gameSelected = -1;

	public MenuMulti(){
		super();
		this.items = new Vector<Menu_Item>();
		this.gamesList = new Vector<Menu_Map>();
		this.openGames = new  Vector<String>();
		startX = 2f*Game.resX/8;
		startY = 0.39f*Game.resY;
		stepY = 0.10f*Game.resY;
		startXGames = 3f*Game.resX/8;
		sizeXGames = Game.resX/2;
		startYGames = 0.37f*Game.resY;
		sizeYGames = Game.resY*(0.95f-0.37f);
		this.items.addElement(new Menu_Item(startX,startY+1*stepY,"Heberger",true));
		this.items.addElement(new Menu_Item(startX,startY+2*stepY,"Rejoindre",true));
		this.items.addElement(new Menu_Item(startX,startY+3*stepY,"Rafraîchir",true));
		this.items.addElement(new Menu_Item(startX,startY+4*stepY,"Retour",true));

	}

	public void callItem(int i){
		switch(i){
		case 0:
			// Heberger
			this.openGames.clear();
			this.gamesList.clear();
			try {
				GameServer.init();
				String addressHost = InetAddress.getLocalHost().toString();
				GameClient.init(addressHost);
				Lobby.init();
			} catch (UnknownHostException e) {}
			
			
			Game.menuSystem.setMenu(MenuNames.MenuMapChoice);
			break;
		case 1:
			// Rejoindre
//			if(gameSelected!=-1){
//				//System.out.println("connexion au serveur : " + (System.currentTimeMillis())%10000);
//				Game.MenuMapChoice.seconds = 6;
//				Game.MenuMapChoice.messageDropped = 0;
//				game.host = false;
//				game.inMultiplayer = true;
//				game.initializePlayers();
//				Map.updateMap(0, game);
//				game.clearPlayer();
//				OpenGames opengame = openGames.get(gameSelected);
//
//				Game.addressHost = opengame.hostAddress;
//				// and now, we can create a kyonet connection
//				if(Game.g.usingKryonet){
//					Game.g.kryonetClient.connect(game.addressHost, Game.portTCP, Game.portUDPKryonet, Game.portTCPResynchro, Game.portUDPKryonetResynchro);
//				}
//				for(int j=1; j<opengame.nPlayers; j++){
//					game.addPlayer("unknown",null,1,1);
//				}
//				Game.getPlayerById(1).address=opengame.hostAddress;
//				game.addPlayer(Game.options.nickname,null,(int)game.resX,(int)game.resY);
//				game.currentPlayer = game.players.lastElement();
//				game.currentPlayer.setTeam(opengame.teamFirstPlayer%2+1);
//				try {
//					Game.currentPlayer.address = InetAddress.getLocalHost();
//				} catch (UnknownHostException e) {}
//				game.menuMapChoice.initializeMenuPlayer();
//				this.openGames.clear();
//				this.gamesList.clear();
//				game.setMenu(game.menuMapChoice);
//			}
			
			this.openGames.clear();
			this.gamesList.clear();
			
			if(this.openGames.size()>0) {
				//FIXME: ajouter la selection de partie à rejoindre
				String addressHost = this.openGames.get(0);
				GameClient.init(addressHost);
				Lobby.init();
			}
			
			
			Game.menuSystem.setMenu(MenuNames.MenuMapChoice);
			break;
		case 2:
			// Rafraîchir
			Vector<String> existingServers = new Vector<String>();
			existingServers = GameClient.getExistingServerIPS();
			for(String s : existingServers){
				if(openGames.contains(s)){
					continue;
				} else {
					this.openGames.add(s);
				}
			}
			Vector<String> toRemove = new Vector<String>();
			for(String s : openGames){
				if(!existingServers.contains(s)){
					toRemove.add(s);
				}
			}
			openGames.removeAll(toRemove);
			break;
		case 3:
			// Retour 
			Game.menuSystem.setMenu(MenuNames.MenuIntro);
			this.openGames.clear();
			this.gamesList.clear();
			break;
		default:		
		}
	}

	public void draw(Graphics g){
		this.drawItems(g);
		g.setColor(Color.white);
		g.drawString("Parties disponibles: ", this.startXGames+70f, startY+50f);
		g.fillRect(this.startXGames+40f, startY+80f+GraphicElements.font_main.getHeight("R"),2, sizeYGames/2f - 60f+GraphicElements.font_main.getHeight("R"));
		for(String s : this.openGames)
			g.drawString(s, this.startXGames+80f, startY+80f);

	}

	public void update(InputObject im){
		this.updateItems(im);
		for(int i=0; i<this.gamesList.size(); i++){
			Menu_Map item = this.gamesList.get(i);
			item.update(im);
			if(item.mouseOver && im.isPressed(KeyEnum.LeftClick)){
				for(Menu_Map it : this.gamesList){
					it.isSelected = false;
				}
				this.gameSelected = i;
				item.isSelected = true;
			}
		}			

	} 

}
