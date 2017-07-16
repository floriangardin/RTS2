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
	public Vector<OpenGames> openGames;
	public int Nplayer;
	public int gameSelected = -1;

	public MenuMulti(){
		super();
		this.items = new Vector<Menu_Item>();
		this.gamesList = new Vector<Menu_Map>();
		this.openGames = new  Vector<OpenGames>();
		startX = 2f*Game.resX/8;
		startY = 0.39f*Game.resY;
		stepY = 0.10f*Game.resY;
		startXGames = 3f*Game.resX/8;
		sizeXGames = Game.resX/2;
		startYGames = 0.37f*Game.resY;
		sizeYGames = Game.resY*(0.95f-0.37f);
		this.items.addElement(new Menu_Item(startX,startY+1*stepY,"Heberger",true));
		this.items.addElement(new Menu_Item(startX,startY+2*stepY,"Rejoindre",true));
		this.items.addElement(new Menu_Item(startX,startY+3*stepY,"Retour",true));

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
		for(Menu_Map s : this.gamesList)
			s.draw(g);

	}

	public void update(InputObject im){
//		while(Communications.receivedConnexion.size()>0){
//			String s = Communications.receivedConnexion.remove(0);
//			HashMap<String, String> hashmap = Utils.preParse(s);
//			if(hashmap.containsKey("ip") && hashmap.containsKey("hst") && hashmap.containsKey("npl")){
//				try {
//					OpenGames o = null;
//					for(OpenGames g : this.openGames)
//						if(g.hostName.equals(hashmap.get("hst")))
//							o = g;
//					if(o==null){
//						int t = 1;
//						if(hashmap.containsKey("idT")){
//							String[] idTeam =hashmap.get("idT").split(",");
//							t = Integer.parseInt(idTeam[1]);
//						}
//						openGames.add(new OpenGames(hashmap.get("hst"), InetAddress.getByName(hashmap.get("ip")),Integer.parseInt(hashmap.get("npl")),t));
//						gamesList.add(new Menu_Map(""+openGames.lastElement().hostName +"'s games", startXGames+80f, startY + 50f + 50f*openGames.size(), sizeXGames/2f, 40f));
//					} else {
//						o.nPlayers = Integer.parseInt(hashmap.get("npl"));
//						String[] idTeam =hashmap.get("idT").split(",");
//						o.teamFirstPlayer = Integer.parseInt(idTeam[1]);
//						o.messageDropped=0;
//					}
//				} catch (UnknownHostException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		Vector<Integer> toRemove= new Vector<Integer>();
//		for(int i=0; i<this.openGames.size(); i++){
//			OpenGames o = this.openGames.get(i);
//			o.messageDropped++;
//			if(o.messageDropped>25){
//				toRemove.add(i);
//			}
//		}
//		for(Integer i : toRemove){
//			this.openGames.removeElementAt(i);
//			this.gamesList.removeElementAt(i);
//		}
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

	public class OpenGames{

		String hostName;
		InetAddress hostAddress;
		int nPlayers;
		int teamFirstPlayer;
		int messageDropped;

		public OpenGames(String hostName, InetAddress hostAddress, int nPlayers, int teamF) {
			this.hostName = hostName;
			this.nPlayers = nPlayers;
			this.hostAddress = hostAddress;
			this.teamFirstPlayer = teamF;
			this.messageDropped = 0;
		}

	}
}
