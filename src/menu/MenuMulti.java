package menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import model.Game;
import model.Objet;
import multiplaying.InputObject;
import multiplaying.MultiSender;

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

	public Vector<Menu_MapChoice> gamesList;
	public Vector<OpenGames> openGames;
	public int Nplayer;
	public int gameSelected = -1;

	public MenuMulti(Game game){
		super(game);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		this.gamesList = new Vector<Menu_MapChoice>();
		this.openGames = new  Vector<OpenGames>();
		startX = 2f*this.game.resX/8;
		startY = 0.39f*this.game.resY;
		stepY = 0.10f*this.game.resY;
		startXGames = 3f*this.game.resX/8;
		sizeXGames = this.game.resX/2;
		startYGames = 0.37f*this.game.resY;
		sizeYGames = this.game.resY*(0.95f-0.37f);
		this.items.addElement(new Menu_Item(startX,startY+1*stepY,"heberger",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+2*stepY,"rejoindre",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+3*stepY,"retour",this.game,true));

	}

	public void callItem(int i){
		switch(i){
		case 0:
			// Heberger
			this.openGames.clear();
			this.gamesList.clear();
			game.host = true;
			game.inMultiplayer = true;
			game.connexionSender = new MultiSender(null, game.portConnexion, this.game.toSendConnexions,game);
			game.connexionSender.start();
			try {
				game.addressHost = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {}
			game.plateau.clearPlayer();
			game.plateau.addPlayer(game.options.nickname,this.game.addressHost,(int)this.game.resX,(int) this.game.resY);
			game.plateau.currentPlayer = game.plateau.players.get(1);
			game.menuMapChoice.initializeMenuPlayer();
			game.setMenu(game.menuMapChoice);
			break;
		case 1:
			// Rejoindre
			if(gameSelected!=-1){
				game.host = false;
				game.inMultiplayer = true;
				game.plateau.clearPlayer();
				game.toSendConnexions.clear();
				OpenGames opengame = openGames.get(gameSelected);
				this.game.connexionSender = new MultiSender(opengame.hostAddress, this.game.portConnexion, this.game.toSendConnexions, game);
				this.game.connexionSender.start();
				this.game.addressHost = opengame.hostAddress;
				for(int j=1; j<opengame.nPlayers; j++){
					game.plateau.addPlayer("unknown",null,1,1);
				}
				this.game.getPlayerById(Game.ID_HOST).address=opengame.hostAddress;
				game.plateau.addPlayer(this.game.options.nickname,null,(int)game.resX,(int)game.resY);
				game.plateau.currentPlayer = game.plateau.players.lastElement();
				game.plateau.currentPlayer.setTeam(opengame.teamFirstPlayer%2+1);
				try {
					this.game.plateau.currentPlayer.address = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {}
				game.menuMapChoice.initializeMenuPlayer();
				this.openGames.clear();
				this.gamesList.clear();
				game.setMenu(game.menuMapChoice);
			}
			break;
		case 2:
			// Retour 
			this.game.setMenu(this.game.menuIntro);
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
		g.fillRect(this.startXGames+40f, startY+80f+this.game.font.getHeight("R"),2, sizeYGames/2f - 60f+this.game.font.getHeight("R"));
		for(Menu_MapChoice s : this.gamesList)
			s.draw(g);
	}

	public void update(InputObject im){
		while(this.game.connexions.size()>0){
			String s = this.game.connexions.remove(0);
			HashMap<String, String> hashmap = Objet.preParse(s);
			if(hashmap.containsKey("ip") && hashmap.containsKey("hst") && hashmap.containsKey("npl")){
				try {
					OpenGames o = null;
					for(OpenGames g : this.openGames)
						if(g.hostName.equals(hashmap.get("hst")))
							o = g;
					if(o==null){
						int t = 1;
						if(hashmap.containsKey("idT")){
							String[] idTeam =hashmap.get("idT").split(",");
							t = Integer.parseInt(idTeam[1]);
						}
						openGames.add(new OpenGames(hashmap.get("hst"), InetAddress.getByName(hashmap.get("ip")),Integer.parseInt(hashmap.get("npl")),t));
						gamesList.add(new Menu_MapChoice(game, ""+openGames.lastElement().hostName +"'s games", startXGames+80f, startY + 50f + 50f*openGames.size(), sizeXGames/2f, 40f));
					} else {
						o.nPlayers = Integer.parseInt(hashmap.get("npl"));
						String[] idTeam =hashmap.get("idT").split(",");
						o.teamFirstPlayer = Integer.parseInt(idTeam[1]);
						o.messageDropped=0;
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		Vector<Integer> toRemove= new Vector<Integer>();
		for(int i=0; i<this.openGames.size(); i++){
			OpenGames o = this.openGames.get(i);
			o.messageDropped++;
			if(o.messageDropped>25){
				toRemove.add(i);
			}
		}
		for(Integer i : toRemove){
			this.openGames.removeElementAt(i);
			this.gamesList.removeElementAt(i);
		}
		this.updateItems(im);
		for(int i=0; i<this.gamesList.size(); i++){
			Menu_MapChoice item = this.gamesList.get(i);
			item.update(im);
			if(item.mouseOver && im.pressedLeftClick){
				for(Menu_MapChoice it : this.gamesList){
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
