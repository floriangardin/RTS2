package menu;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import bot.IA;
import control.InputObject;
import control.Player;
import main.Main;
import menuutils.Menu_IA_Button;
import menuutils.Menu_Item;
import menuutils.Menu_Map;
import menuutils.Menu_Player;
import model.Game;
import model.GameClient;
import model.WholeGame;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import mybot.IAPython;
import ressources.GraphicElements;
import ressources.Musics;
import system.MenuSystem.MenuNames;

public strictfp class MenuMapChoice extends Menu {

	public int selected = 0;
	float startY;
	float stepY;

	float startXMapChoice;
	float startYMapChoice;
	float sizeXMapChoice;
	float sizeYMapChoice;
	float startXPlayers;
	float startYPlayers;
	float sizeXPlayers;
	float sizeYPlayers;
	long startGame = 0;
	public float seconds = 3f;

	public int cooldown;
	public int messageDropped;
	public int roundForPingRequest;
	
	//IA 
	public Menu_IA_Button iaButton = new Menu_IA_Button(-1, 0, "IA");

	Vector<Menu_Map> mapchoices = new Vector<Menu_Map>();

	public MenuMapChoice(){

		Lobby.init();
		this.items = new Vector<Menu_Item>();

		startY = Game.resY*0.37f;
		stepY = 0.12f*Game.resY;

		startXMapChoice = Game.resX*(2f/3f);
		startYMapChoice = startY;
		sizeXMapChoice = Game.resX*(1f/3f-1f/30f);
		sizeYMapChoice = Game.resY*0.80f-startYMapChoice;

		startXPlayers = 1f*Game.resX/6f;
		startYPlayers = startY;
		sizeXPlayers = Game.resX*(2f/3f)-2*startXPlayers;
		sizeYPlayers = Game.resY*0.80f-startYMapChoice;

		this.items.addElement(new Menu_Item(1f/3f*Game.resX,Game.resY*0.9f,"Demarrer",true));
		this.items.addElement(new Menu_Item(2f/3f*Game.resX,Game.resY*0.9f,"Retour",true));

		for(int i=0; i<Lobby.maps.size(); i++){
			mapchoices.addElement(new Menu_Map(Lobby.maps.get(i),
					startXMapChoice+1f/10f*sizeXMapChoice,
					startYMapChoice+1f*(i+3)/9f*sizeYMapChoice-GraphicElements.font_main.getHeight("P")/2,
					4f/10f*sizeXMapChoice,
					0.5f/9f*sizeYMapChoice));
			if(mapchoices.get(i).name.equals(Lobby.idCurrentMap)){
				mapchoices.get(i).isSelected = true;
			}
		}

	}


	public void callItem(int i){
		switch(i){
		case 0: 
			// demarrer
			synchronized(Lobby.players){
				for(Menu_Player mp : Lobby.players){
					if(mp.id==Player.getID() && this.seconds>2.5f){
						mp.isReady = !mp.isReady;
						this.seconds = 3f;
					}
				}
			}
			break;
		case 1:
			// retour
			if(GameClient.isHost){
				System.out.println("Retour menu avec touche");
				Game.menuSystem.setMenu(MenuNames.MenuIntro);
				GameClient.isHost = false;
			} else {
				Game.menuSystem.setMenu(MenuNames.MenuMulti);
				GameClient.isHost = false;
			}
			System.out.println("==> OOPS");
			GameClient.close();
			break;
		default:		
		}
	}

	public void addIA(int team){
		IA.addIA(new IAPython(team));
	}
	public void drawItems(Graphics g){
		// draw background
		//		g.drawImage(this.backGround, 0,0,Game.resX,Game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		// drawing structure
		g.setColor(Color.white);
		g.drawString("Joueurs :" , startXPlayers + 1f/30f*sizeXPlayers,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXPlayers+ 1f/15f*sizeXPlayers,startYPlayers+2f/6f*sizeYPlayers-GraphicElements.font_main.getHeight("P")/2f,2f,3f/6f*sizeYPlayers+GraphicElements.font_main.getHeight("P")/2f);
		g.drawString("Terrain :" , startXMapChoice + 1f/30f*sizeXMapChoice,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXMapChoice + 1f/15f*sizeXMapChoice,startYMapChoice+1f*(3f)/9f*sizeYMapChoice-GraphicElements.font_main.getHeight("P")/2,2f,6f/9f*sizeYMapChoice-GraphicElements.font_main.getHeight("P")/2);
		synchronized (Lobby.players) {
			int i = 1;
			int minid = -1;
			for(Menu_Player player : Lobby.players){
				int mintemp = 50;
				int minpos = -1;
				int j = 0;
				for(Menu_Player player2 : Lobby.players){
					if(player2.id>minid && player2.id<mintemp){
						mintemp = player2.id;
						minpos = j;
					}
					j++;
				}
				minid = Lobby.players.get(minpos).id;
				Lobby.players.get(minpos).draw(g, i);
				i++;
			}
			iaButton.draw(g, i);
		}
		// draw items
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		// drawing maps
		g.setColor(Color.white);
		for(Menu_Item item: mapchoices){
			item.draw(g);
		}
		// draw title
		g.drawImage(this.title, Game.resX/2-this.title.getWidth()/2, 10f);

		// draw black fading
		if(Lobby.checkStartGame()){
			g.setColor(new Color(0,0,0,1f-3*this.seconds));
			g.fillRect(0, 0, Game.resX, Game.resY);
		}
	}

	public void update(InputObject im){
		// handling connexions

		// checking disconnecting players
		int toRemove = -1;
		synchronized(Lobby.players){
			for(Menu_Player mp : Lobby.players){
				if(mp.id!=Player.getID() && mp.canTimeout){
					if(mp.hasBeenUpdated){
						mp.messageDropped=0;
						mp.hasBeenUpdated = false;
					} else {
						mp.messageDropped++;
						if(mp.messageDropped>10){
							toRemove=mp.id;
							if(mp.isHost){
								ChatHandler.addMessage(new ChatMessage("Host déconnecté"));
								//Game.menuSystem.setMenu(MenuNames.MenuIntro);
								
							} else {
								ChatHandler.addMessage(new ChatMessage("Joueur deconnecté : "+mp.nickname));
							}
							break;
						}
					}
				}
			}
			if(toRemove!=-1){
				Lobby.removePlayer(toRemove);
			}
		}
		// Checking if all players are ready then launch the game
		// Updating items
		synchronized(Lobby.players){
			for(Menu_Player mp : Lobby.players){
				if(mp.id==Player.getID() || mp.isIA){
					if(!mp.isReady || mp.isIA){
						mp.update(im);
						if(!mp.isIA){							
							Player.setTeam(mp.team);
						}
					}
					if(GameClient.isHost){
						mp.isHost = true;
						mp.idMap = Lobby.idCurrentMap;
					}
					GameClient.send(mp);
				}
			}
			iaButton.update(im);
			if(iaButton.createNewIA){
				// Create new menu player
				// GET MAX ID 
				int maxId = Lobby.players.stream().map(x -> x.id).max((x,y)-> Integer.compare(x, y)).orElse(-1);
				Lobby.players.add(new Menu_Player(maxId+1, 1,"IA", false, true, true));
			}
		}
		this.updateItems(im, false);
		//Checking starting of the game
		if(Lobby.checkStartGame()){
			this.seconds-=1f/Main.framerate;
			this.seconds-=1f/Main.framerate;
			if(this.seconds<=0){
				launchGame();
			}
		}
		// Updating map choices
		for(Menu_Player mp : Lobby.players){
			if(mp.id==Player.getID() && !mp.isReady){
				if(GameClient.isHost){
					for(int i=0; i<Lobby.maps.size(); i++){
						Menu_Map item = mapchoices.get(i);
						item.update(im);
						if(item.isSelected){
							Lobby.idCurrentMap = Lobby.maps.get(i);
							for(int j=0; j<Lobby.maps.size(); j++){
								if(j!=i){
									mapchoices.get(j).isSelected = false;
								}
							}
						}
					}
				}
			} else {
				for(int j=0; j<Lobby.maps.size(); j++){
					mapchoices.get(j).isSelected = Lobby.maps.get(j).equals(Lobby.idCurrentMap);
				}
			}
		}
	}
	public void launchGame(){
		// Init gameSystem
		Game.gameSystem = new WholeGame(false);
		Musics.stopMusic();
		Game.system = Game.gameSystem;
		
		
		// INIT IA
		boolean isHost = false;
		for(Menu_Player mp : Lobby.players){
			if(mp.isHost && mp.id==Player.getID()){
				isHost = true;
			}
		}if(isHost){			
			for(Menu_Player mp : Lobby.players){
				if(mp.isIA){
					this.addIA(mp.team);
				}
			}
		}
		// Send Plateau to all

		//		Camera.maxX = (int) MaxX;
		//		Camera.maxY = (int) MaxY;
		//		Camera.minX = 0;
		//		Camera.minY = 0;

	}


	public void init(){
		this.seconds = 3f;
		this.startGame = 0;
		this.selected = 0;
	}

}
