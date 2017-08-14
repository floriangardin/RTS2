package menu;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import bot.IA;
import control.InputObject;
import control.Player;
import main.Main;
import menuutils.Menu_Item;
import menuutils.Menu_Map;
import menuutils.Menu_Player;
import model.Game;
import model.GameClient;
import model.GameServer;
import model.WholeGame;
import mybot.IAInputs;
import ressources.GraphicElements;
import system.MenuSystem.MenuNames;

public class MenuMapChoice extends Menu {


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
	public float seconds = 4;

	public int cooldown;
	public int messageDropped;
	public int roundForPingRequest;

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
			mapchoices.addElement(new Menu_Map(Lobby.maps.get(i),startXMapChoice+1f/10f*sizeXMapChoice,startYMapChoice+1f*(i+3)/9f*sizeYMapChoice-GraphicElements.font_main.getHeight("P")/2,200f,30f));
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
					if(mp.id==Player.getID()){
						mp.isReady = !mp.isReady;
						this.seconds = 3f;
					}
				}
			}
			break;
		case 1:
			// retour
			Game.menuSystem.setMenu(MenuNames.MenuIntro);
			GameServer.close();
			break;
		default:		
		}
	}

	public void addIA(){
		IA.addIA(new IAInputs(1));
	}
	public void drawItems(Graphics g){
		// draw background
		g.drawImage(this.backGround, 0,0,Game.resX,Game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		// drawing structure
		g.setColor(Color.white);
		g.drawString("Joueurs :" , startXPlayers + 1f/30f*sizeXPlayers,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXPlayers+ 1f/15f*sizeXPlayers,startYPlayers+2f/6f*sizeYPlayers-GraphicElements.font_main.getHeight("P")/2f,2f,3f/6f*sizeYPlayers+GraphicElements.font_main.getHeight("P")/2f);
		g.drawString("Terrain :" , startXMapChoice + 1f/30f*sizeXMapChoice,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXMapChoice + 1f/15f*sizeXMapChoice,startYMapChoice+1f*(3f)/9f*sizeYMapChoice-GraphicElements.font_main.getHeight("P")/2,2f,6f/9f*sizeYMapChoice-GraphicElements.font_main.getHeight("P")/2);
		synchronized (Lobby.players) {
			for(Menu_Player player : Lobby.players){
				player.draw(g);
			}
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
	}

	public void update(InputObject im){
		// handling connexions
		
			// checking disconnecting players
			//			if(Game.gameSystem.host && this.startGame==0 && seconds>2){
			//				int toRemove = -1;
			//				for(int i=2 ; i<this.Lobby.players.size(); i++){
			//					Menu_Player mp = this.Lobby.players.get(i);
			//					if(mp!=null && mp.hasBeenUpdated){
			//						mp.messageDropped=0;
			//						mp.hasBeenUpdated = false;
			//					} else {
			//						mp.messageDropped++;
			//						if(mp.messageDropped>2f*Main.framerate){
			//							//System.out.println("disconnecting player:"+i);
			//							toRemove=i;
			//						}
			//					}
			//				}
			//				if(toRemove!=-1){
			//					int k = toRemove;
			//					Lobby.removePlayer(k);
			//					for(int i=0; i<Game.players.size(); i++){
			//						Game.players.get(i).id = i;
			//					}
			//					this.initializeMenuPlayer();
			//				}
			//			}
		// Checking if all players are ready then launch the game
		// Updating items
		synchronized(Lobby.players){
			for(Menu_Player mp : Lobby.players){
				if(mp.id==Player.getID()){
					mp.update(im);
					Player.setTeam(mp.team);
					if(GameServer.hasLaunched){
						mp.isHost = true;
						mp.idMap = Lobby.idCurrentMap;
					}
					GameClient.send(mp);
				}
			}
		}
		this.updateItems(im);
		//Checking starting of the game
		if(Lobby.checkStartGame()){
			this.seconds-=60f/Main.framerate;
			if(this.seconds<=0){
				launchGame();
			}
		}
		// Updating map choices
		if(GameServer.hasLaunched){
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
	}



	public void launchGame(){
		// Init gameSystem
		Game.gameSystem = new WholeGame();
		Game.system = Game.gameSystem;
		
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
