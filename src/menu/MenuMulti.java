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
import multiplaying.MultiReceiver;
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
	float startY;
	float stepY;
	float ratioReso;

	public Vector<Menu_MapChoice> gamesList;
	public Vector<OpenGames> openGames;
	public int Nplayer;
	public int gameSelected = -1;

	public MenuMulti(Game game){
		try {
			this.backGround = new Image("pics/fondMenu.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.game = game;
		this.items = new Vector<Menu_Item>();
		this.gamesList = new Vector<Menu_MapChoice>();
		this.openGames = new  Vector<OpenGames>();
		startY = 0.39f*this.game.resY;
		stepY = 0.10f*this.game.resY;
		startXGames = 3f*this.game.resX/8;
		sizeXGames = this.game.resX/2;
		startYGames = 0.37f*this.game.resY;
		sizeYGames = this.game.resY*(0.95f-0.37f);
		ratioReso = this.game.resX/2800f;
		try {
			this.host = new Image("pics/menu/host.png").getScaledCopy(ratioReso);
			this.hostSelected = new Image("pics/menu/hostselected.png").getScaledCopy(ratioReso);
			this.join= new Image("pics/menu/join.png").getScaledCopy(ratioReso);
			this.joinSelected= new Image("pics/menu/joinselected.png").getScaledCopy(ratioReso);
			this.back = new Image("pics/menu/back.png").getScaledCopy(ratioReso);
			this.backSelected = new Image("pics/menu/backselected.png").getScaledCopy(ratioReso);
			this.marbre = new Image("pics/menu/marbre.png").getScaledCopy((int)sizeXGames, (int)sizeYGames);
			this.title = new Image("pics/menu/title01.png").getScaledCopy(0.35f*this.game.resY/650);
			float startX = 2f*this.game.resX/8-this.host.getWidth()/2;
			this.items.addElement(new Menu_Item(startX,startY+1*stepY,this.host,this.hostSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+2*stepY,this.join,this.joinSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+3*stepY,this.back,this.backSelected,this.game));
			
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		this.sounds = game.sounds;
	}

	public void callItem(int i){
		switch(i){
		case 0:
			game.host = true;
			game.inMultiplayer = true;
			try {
				game.addressHost = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				
				e.printStackTrace();
			}
			game.plateau.clearPlayer();
			game.plateau.addPlayer(game.options.nickname,this.game.addressHost,(int)this.game.resX,(int) this.game.resY);
			game.plateau.currentPlayer = game.plateau.players.get(1);
			game.setMenu(game.menuMapChoice);
			break;
		case 1:
			if(gameSelected!=-1){
				game.host = false;
				game.inMultiplayer = true;
				game.plateau.clearPlayer();
				game.toSendConnexions.clear();
				OpenGames opengame = openGames.get(gameSelected);
				this.game.connexionSender = new MultiSender(opengame.hostAddress, this.game.portConnexion, this.game.toSendConnexions, game);
				this.game.connexionSender.start();
				this.game.addressHost = opengame.hostAddress;
				this.game.clock.start();
				for(int j=1; j<opengame.nPlayers; j++){
					game.plateau.addPlayer("unknown",null,1,1);
				}
				this.game.getPlayerById(Game.ID_HOST).address=opengame.hostAddress;
				game.plateau.addPlayer(this.game.options.nickname,null,(int)game.resX,(int)game.resY);
				game.plateau.currentPlayer = game.plateau.players.lastElement();
				game.plateau.currentPlayer.setTeam(game.plateau.players.get(1).getTeam()%2+1);
				try {
					this.game.plateau.currentPlayer.address = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {}
				game.setMenu(game.menuMapChoice);
			}
			break;
		case 2:
			this.game.setMenu(this.game.menuIntro);
			break;
		default:		
		}
	}

	public void draw(Graphics g){
		g.drawImage(this.backGround, 0,0,this.game.resX,this.game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		g.drawImage(this.title, this.game.resX/2f-this.title.getWidth()/2, 10f);
		g.setColor(Color.white);
		g.drawString("Parties disponibles: ", this.startXGames+70f, startY+50f);
		g.fillRect(this.startXGames+80f, startY+80f+this.game.font.getHeight("R"),2, sizeYGames/2f - 60f+this.game.font.getHeight("R"));
		for(Menu_MapChoice s : this.gamesList)
			s.draw(g);
	}

	public void update(Input i){
		if(this.game.connexions.size()>0){
			String s = this.game.connexions.remove(0);
			HashMap<String, String> hashmap = Objet.preParse(s);
			if(hashmap.containsKey("ip") && hashmap.containsKey("hst") && hashmap.containsKey("npl")){
				try {
					OpenGames o = null;
					for(OpenGames g : this.openGames)
						if(g.hostName.equals(hashmap.get("hst")))
							o = g;
					if(o==null){
						openGames.add(new OpenGames(hashmap.get("hst"), InetAddress.getByName(hashmap.get("ip")),Integer.parseInt(hashmap.get("npl"))));
						gamesList.add(new Menu_MapChoice(""+openGames.lastElement().hostName +"'s games", startXGames+80f, startY + 50f + 50f*openGames.size(), 200f, 40f));
					} else {
						o.nPlayers = Integer.parseInt(hashmap.get("npl"));
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			callItems(i);
			this.game.sounds.menuItemSelected.play(1f,game.options.soundVolume);
		}
		for(Menu_Item item: this.items){
			item.update(i);
		}
		for(Menu_MapChoice item: this.gamesList){
			item.update(i);
		}			

	} 

	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
		int toselect = -1;
		for(int j=0; j<gamesList.size(); j++){
			if(gamesList.get(j).isClicked(i))
				toselect = j;
		}
		if(toselect!=-1){
			for(int j=0; j<gamesList.size(); j++){
				gamesList.get(j).isSelected = j==toselect;
				if(gamesList.get(j).isSelected)
					gameSelected = j;
			}
		}
	}

	public class OpenGames{

		String hostName;
		InetAddress hostAddress;
		int nPlayers;

		public OpenGames(String hostName, InetAddress hostAddress, int nPlayers) {
			this.hostName = hostName;
			this.nPlayers = nPlayers;
			this.hostAddress = hostAddress;
		}

	}
}
