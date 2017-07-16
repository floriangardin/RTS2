package menu;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.InputObject;
import control.Player;
import control.KeyMapper.KeyEnum;
import display.Camera;
import main.Main;
import menuutils.Menu_Item;
import menuutils.Menu_Map;
import menuutils.Menu_Player;
import model.Game;
import model.WholeGame;
import multiplaying.ChatHandler;
import multiplaying.Communications;
import ressources.GraphicElements;
import ressources.Musics;
import system.MenuSystem.MenuNames;
import tests.FatalGillesError;

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
	public int seconds = 4;

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
			if(!Lobby.multiplayer && Lobby.checkStartGame()){
				this.launchGameSinglePlayer();
			} else {
				
			}
			break;
		case 1:
			// retour
			if(Lobby.multiplayer){
				Game.menuSystem.setMenu(MenuNames.MenuMulti);
				ChatHandler.messages.clear();
			}else{
				Game.menuSystem.setMenu(MenuNames.MenuIntro);
			}
			break;
		default:		
		}
	}

	private void launchGameSinglePlayer() {
		this.startGame = Communications.clock.getCurrentTime()+5000000000L;
		this.deselectItems();
		//		this.menuPlayers.get(game.currentPlayer.id).isOverColor = false;
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
		for(int i=0;i<Lobby.players.size();i++){
			Lobby.players.get(i).draw(g);
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
		// drawing local ip
		if(Lobby.multiplayer){
			g.setColor(Color.white);
			g.drawString("IP Locale : "+Communications.addressLocal.getHostAddress(), 15f, Game.resY-15f-GraphicElements.font_main.getHeight("IP"));
		}
		// drawing title
		if(startGame!=0){
			float f = Math.max(0f, Math.min(1f,1f-(1f*((float)(startGame-Communications.clock.getCurrentTime())/2000000000f))));
			//			System.out.println(startGame-Game.clock.getCurrentTime());
			g.setColor(new Color(0f,0f,0f,f));
			g.fillRect(0, 0, Game.resX, Game.resY);
			// draw title
			g.drawImage(this.title, Game.resX/2-this.title.getWidth()/2, 10f+(Game.resY/2-this.title.getHeight()/2-10f)*f);
			// drawing countdown to launch game
			g.setColor(Color.white);
			int sec = (int) ((startGame-Communications.clock.getCurrentTime())/1000000000L);
			String s = "Début de la partie dans "+(sec+1)+" s.";
			String s1 = "Début de la partie dans 5 s.";
			g.drawString(s,Game.resX-GraphicElements.font_main.getWidth(s1)-15f,Game.resY-GraphicElements.font_main.getHeight(s1)-15f);
		} else {
			// draw title
			g.drawImage(this.title, Game.resX/2-this.title.getWidth()/2, 10f);
		}
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
		//Checking starting of the game
		if(Lobby.checkStartGame()){
			if(startGame==0){
				this.startGame = Communications.clock.getCurrentTime()+5000000000L;
				this.deselectItems();
			}
		}
		if(startGame!=0){
			this.handleStartGame();
			return;
		}
		// Checking if all players are ready then launch the game
		// Updating items
		for(Menu_Player mp : Lobby.players){
			if(mp.id==Player.getID()){
				mp.update(im);
			}
		}
		this.updateItems(im);
		// Updating map choices
		if(Lobby.host){
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


	public void handleStartGame(){
		/**
		 * function that checks if the game is about to start
		 * ie. if the startTime has been defined
		 * 
		 * then plays sounds each seconds then launches the game
		 */
		if(startGame-Communications.clock.getCurrentTime()<=this.seconds*1000000000L && seconds!=0){
			if(seconds==2){
				Musics.getPlayingMusic().fade(3000, 0, true);
			}
			//System.out.println("debut de la partie dans :" + seconds + "heure de la clock" + Game.clock.getOrigin());
			//System.out.println("Current time: "+Game.clock.getCurrentTime());
			//Game.sounds.get("menuItemSelected").play();
			seconds--;
		} else if (startGame<=Communications.clock.getCurrentTime()) {
			launchGame();
		}
	}

	public void launchGame(){
		// Init gameSystem
		Game.gameSystem = new WholeGame();
		Game.system = Game.gameSystem;
//		Camera.maxX = (int) MaxX;
//		Camera.maxY = (int) MaxY;
//		Camera.minX = 0;
//		Camera.minY = 0;
		
	}


	public void init(){
		this.seconds = 3;
		this.startGame = 0;
		this.selected = 0;
	}

}
