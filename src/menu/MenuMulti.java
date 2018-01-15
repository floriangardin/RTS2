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

import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import ressources.GraphicElements;
import system.MenuSystem.MenuNames;

public strictfp class MenuMulti extends Menu {

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
	public Vector<OpenGame> openGames;
	public int Nplayer;
	public int gameSelected = -1;

	public MenuMulti(){
		super();
		init();
	}

	public void init(){
		gameSelected = -1;
		this.items = new Vector<Menu_Item>();
		this.gamesList = new Vector<Menu_Map>();
		this.openGames = new  Vector<OpenGame>();
		startX = 2f*Game.resX/8;
		startY = 0.39f*Game.resY;
		stepY = 0.10f*Game.resY;
		startXGames = 3f*Game.resX/8;
		sizeXGames = Game.resX/2;
		startYGames = 0.37f*Game.resY;
		sizeYGames = Game.resY*(0.95f-0.37f);
		this.items.addElement(new Menu_Item(startX,startY+1.5f*stepY,"Rafraîchir",true));
		this.items.addElement(new Menu_Item(startX,startY+2.5f*stepY,"Rejoindre",true));
		this.items.addElement(new Menu_Item(startX,startY+3.5f*stepY,"Retour",true));
	}

	public void callItem(int i){
		switch(i){
		case 0:
			// Rafraîchir
			Vector<OpenGame> existingServers = GameClient.getExistingServerIPS();
			for(OpenGame s : existingServers){
				if(openGames.contains(s)){
					continue;
				} else {
					this.openGames.add(s);
				}
			}
			Vector<OpenGame> toRemove = new Vector<OpenGame>();
			for(OpenGame s : openGames){
				if(!existingServers.contains(s)){
					toRemove.add(s);
				}
			}
			this.gamesList.clear();
			int j = 0;
			float x = this.startXGames+80f;
			float y;
			float sizeX = this.sizeXGames/2f;
			float sizeY = this.sizeYGames/8f;
			for(OpenGame s : openGames){
				y = startY+80f+j*sizeY;
				this.gamesList.add(new Menu_Map(s.addressHost+" - "+s.nicknameHost,x,y,sizeX,sizeY/2f));
				j++;
			}
			openGames.removeAll(toRemove);
			break;
		case 1:
			// Rejoindre
			if(gameSelected!=-1 && this.openGames.size()>gameSelected) {
				//FIXME: ajouter la selection de partie à rejoindre
				String addressHost = this.openGames.get(gameSelected).addressHost;
				try{
					GameClient.init(addressHost);
					Lobby.init();
					Game.menuSystem.setMenu(MenuNames.MenuMapChoice);
				}catch(Exception e){
					ChatHandler.addMessage(new ChatMessage("Unable to join game!"));
				}
			}


			break;
		case 2:
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
		for(Menu_Map mm : this.gamesList){
			mm.draw(g);
		}
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

	public static class OpenGame{
		public String addressHost;
		public String nicknameHost;

		public OpenGame(String a, String n){
			this.addressHost = a;
			this.nicknameHost = n;
		}
	}

}
