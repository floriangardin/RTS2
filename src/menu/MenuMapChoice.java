package menu;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import main.Main;
import model.Civilisation;
import model.Game;
import model.Objet;
import model.Player;
import ressources.Map;
import tests.FatalGillesError;
import utils.Utils;

public class MenuMapChoice extends Menu {


	public int selected = 0;
	public int mapSelected = 0;
	public Vector<String> maps = Map.maps();

	public Vector<InetAddress> addressesInvites = new Vector<InetAddress>();

	public Vector<Menu_MapChoice> mapchoices;

	public Vector<Menu_Player> menuPlayers;


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

	public String messageToClient;

	public MenuMapChoice(Game game){
		super(game);

		this.items = new Vector<Menu_Item>();
		this.mapchoices = new Vector<Menu_MapChoice>();
		this.menuPlayers = new Vector<Menu_Player>();

		startY = this.game.resY*0.37f;
		stepY = 0.12f*this.game.resY;

		startXMapChoice = game.resX*(2f/3f);
		startYMapChoice = startY;
		sizeXMapChoice = game.resX*(1f/3f-1f/30f);
		sizeYMapChoice = game.resY*0.80f-startYMapChoice;

		startXPlayers = 1f*game.resX/6f;
		startYPlayers = startY;
		sizeXPlayers = game.resX*(2f/3f)-2*startXPlayers;
		sizeYPlayers = game.resY*0.80f-startYMapChoice;
		this.items.addElement(new Menu_Item(1f/3f*this.game.resX,this.game.resY*0.9f,"Demarrer",this.game,true));
		this.items.addElement(new Menu_Item(2f/3f*this.game.resX,this.game.resY*0.9f,"Retour",this.game,true));
		for(int i=0; i<maps.size(); i++){
			this.mapchoices.addElement(new Menu_MapChoice(game, maps.get(i),startXMapChoice+1f/10f*sizeXMapChoice,startYMapChoice+1f*(i+3)/9f*sizeYMapChoice-this.game.font.getHeight("P")/2,200f,30f));
		}
	}


	public void callItem(int i){
		switch(i){
		case 1:
			// retour
			if(game.inMultiplayer){
				this.game.setMenu(this.game.menuMulti);
				this.game.chatHandler.messages.clear();
			}else{
				this.game.setMenu(this.game.menuIntro);
			}
			break;
		case 0: 
			// demarrer
			if(!game.inMultiplayer){
				this.launchGameSinglePlayer();
			} else {
				this.game.currentPlayer.isReady = true;
			}
			break;
		default:		
		}
	}

	private void launchGameSinglePlayer() {
		this.startGame = game.clock.getCurrentTime()+5000000000L;
		this.deselectItems();
		this.menuPlayers.get(game.currentPlayer.id).isOverColor = false;
	}


	public void draw(Graphics g){
		this.drawItems(g);
		g.setColor(Color.white);
		for(Menu_Item item: this.mapchoices){
			item.draw(g);
		}
		g.setColor(Color.white);
		g.drawString("Joueurs :" , startXPlayers + 1f/30f*sizeXPlayers,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXPlayers+ 1f/15f*sizeXPlayers,startYPlayers+2f/6f*sizeYPlayers-this.game.font.getHeight("P")/2f,2f,3f/6f*sizeYPlayers+this.game.font.getHeight("P")/2f);
		g.drawString("Terrain :" , startXMapChoice + 1f/30f*sizeXMapChoice,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXMapChoice + 1f/15f*sizeXMapChoice,startYMapChoice+1f*(3f)/9f*sizeYMapChoice-this.game.font.getHeight("P")/2,2f,6f/9f*sizeYMapChoice-this.game.font.getHeight("P")/2);
		for(int i=1;i<this.menuPlayers.size();i++){
			menuPlayers.get(i).draw(g);
		}
		// drawing local ip
		if(game.inMultiplayer){
			g.setColor(Color.white);
			g.drawString("IP Locale : "+game.addressLocal.getHostAddress(), 15f, game.resY-15f-game.font.getHeight("IP"));
		}
		
		if(startGame!=0){
			float f = Math.max(0f, Math.min(1f,1f-(1f*((float)(startGame-this.game.clock.getCurrentTime())/2000000000f))));
//			System.out.println(startGame-this.game.clock.getCurrentTime());
			g.setColor(new Color(0f,0f,0f,f));
			g.fillRect(0, 0, game.resX, game.resY);
			// draw title
			g.drawImage(this.title, this.game.resX/2-this.title.getWidth()/2, 10f+(game.resY/2-this.title.getHeight()/2-10f)*f);
			// drawing countdown to launch game
			g.setColor(Color.white);
			int sec = (int) ((startGame-this.game.clock.getCurrentTime())/1000000000L);
			String s = "Début de la partie dans "+(sec+1)+" s.";
			String s1 = "Début de la partie dans 5 s.";
			g.drawString(s,game.resX-game.font.getWidth(s1)-15f,game.resY-game.font.getHeight(s1)-15f);
		} else {
			// draw title
			g.drawImage(this.title, this.game.resX/2-this.title.getWidth()/2, 10f);
		}

	}
	
	public void drawItems(Graphics g){
		// draw background
		g.drawImage(this.backGround, 0,0,this.game.resX,this.game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		// draw items
		for(Menu_Item item: this.items){
			item.draw(g);
		}
	}

	public void update(InputObject im){
		// handling connexions
		if(game.inMultiplayer){
			if(game.host){
				// sending to all players only if the game isn't about to start
				this.messageToClient = this.messageToClient();
				if(this.startGame==0 || this.seconds>2){
					try {
						this.game.sendFromMenu(messageToClient);
					} catch (FatalGillesError e) {
						e.printStackTrace();
					}
				}
				// parsing if received anything
				while(game.receivedConnexion.size()>0){
					this.parseForHost(Utils.preParse(game.receivedConnexion.remove(0)));
				}
			} else {
				// sending to host only if the game isn't about to start
				if(this.startGame==0 || this.seconds>2){
					try {
						this.game.sendFromMenu(this.messageToHost());	
					} catch (FatalGillesError e) {
						e.printStackTrace();
					}
				}
				messageDropped++;	
				// parsing if received anything
				if(game.receivedConnexion.size()>0){
					messageDropped=0;
					this.parseForClient(Utils.preParse(game.receivedConnexion.lastElement()));
					game.receivedConnexion.clear();
				}		
				//checking if game still exists
				if(this.startGame==0 && messageDropped>2f*Main.framerate && seconds>2){
					this.callItem(0);
				}
				// requete de ping
				if(roundForPingRequest==0){
					this.game.pingRequest();
//					System.out.println("MMC 204 ping");
					roundForPingRequest++;
				}else {
					roundForPingRequest++;
					roundForPingRequest%=Main.framerate/2;
				}
			}
			// checking disconnecting players
			if(game.host && this.startGame==0 && seconds>2){
				int toRemove = -1;
				for(int i=2 ; i<this.menuPlayers.size(); i++){
					Menu_Player mp = this.menuPlayers.get(i);
					if(mp!=null && mp.hasBeenUpdated){
						mp.messageDropped=0;
						mp.hasBeenUpdated = false;
					} else {
						mp.messageDropped++;
						if(mp.messageDropped>2f*Main.framerate){
							//System.out.println("disconnecting player:"+i);
							toRemove=i;
						}
					}
				}
				if(toRemove!=-1){
					int k = toRemove;
					this.game.removePlayer(k);
					for(int i=0; i<this.game.players.size(); i++){
						this.game.players.get(i).id = i;
					}
					this.initializeMenuPlayer();
				}
			}
			//Checking starting of the game
			if(startGame!=0){
				this.handleStartGame();
				return;
			}
		} else {
			if(startGame!=0){
				this.handleStartGame();
				return;
			}
		}
		// Checking if all players are ready then launch the game
		this.checkStartGame();
		// Updating items
		this.menuPlayers.get(game.currentPlayer.id).update(im);
		this.updateItems(im);
		// Updating map choices
		if(this.game.host || !this.game.inMultiplayer){
			for(int i=0; i<this.mapchoices.size(); i++){
				Menu_MapChoice item = this.mapchoices.get(i);
				item.update(im);
				if(im.isPressed(KeyEnum.LeftClick) && item.mouseOver){
					for(Menu_MapChoice mp : this.mapchoices){
						mp.isSelected = false;
					}
					item.isSelected = true;
					this.mapSelected = i;
					Map.updateMap(mapSelected, game);
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
		if(startGame-this.game.clock.getCurrentTime()<=this.seconds*1000000000L && seconds!=0){
			if(seconds==2){
				this.game.musicPlaying.fade(3000, 0, true);
			}
			//System.out.println("debut de la partie dans :" + seconds + "heure de la clock" + this.game.clock.getOrigin());
			//System.out.println("Current time: "+this.game.clock.getCurrentTime());
			//this.game.sounds.get("menuItemSelected").play();
			seconds--;
		} else if (startGame<=this.game.clock.getCurrentTime()) {
			game.launchGame();
		}
	}


	public void checkStartGame(){
		if(game.inMultiplayer && game.host){
			boolean toGame = true;
			// checking if all players are ready
			for(int j=1;j<this.menuPlayers.size(); j++){
				if(!this.menuPlayers.get(j).p.isReady){
					toGame = false;
				}
			}
			// Checking if at least one player is present by team
			boolean present1 = false;
			boolean present2 = false;
			if(toGame){
				for(Player p : this.game.players){
					if(p.getTeam()==1){
						present1 = true;
					}
					if(p.getTeam()==2){
						present2 = true;
					}
				}
				if (present1 && present2){
					// Launch Game
					if(startGame==0){
						this.startGame = this.game.clock.getCurrentTime()+5000000000L;
						this.deselectItems();
						this.menuPlayers.get(game.currentPlayer.id).isOverColor = false;
					}
				}
			}
		}
	}


	public void initializeMenuPlayer(){
		this.menuPlayers.clear();
		for(int j=0;j<this.game.players.size(); j++){
			this.menuPlayers.addElement(new Menu_Player(game.players.get(j),startXPlayers+ 1f/10f*sizeXPlayers,startYPlayers+1f*(j+1)/6f*sizeYPlayers-this.game.font.getHeight("Pg")/2f,game));
		}
	}


	public String messageToClient(){
		String s = "";
		String thisAddress;
		try {
			thisAddress = InetAddress.getLocalHost().getHostAddress();
			s+="idJ:"+this.game.currentPlayer.id+";ip:"+thisAddress+";";
			if(this.game.host)
				s+="hst:"+this.game.options.nickname+";npl:"+this.game.players.size()+";map:"+this.mapSelected+";";
		} catch (UnknownHostException e) {}	
		s+="cvS:";
		//Civ for all players
		for(Menu_Player p : this.menuPlayers){
			s+=p.p.getGameTeam().civ.name;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		//id for all players
		s+="idT:";
		for(Menu_Player p : this.menuPlayers){
			s+=p.p.getGameTeam().id;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";
		s+="nckn:";
		//Nickname
		for(Menu_Player p : this.menuPlayers){
			s+=p.p.nickname;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		s+="isR:";
		for(Menu_Player p : this.menuPlayers){
			s+=p.p.isReady;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";
		//Send time 
		s+="clk:"+this.game.clock.getCurrentTime();
		s+=";";
		//Send starttime if isHost and is about to launch game
		if(this.startGame!=0){
			s+="stT:"+this.startGame;
			s+=";";
		}

		//Send all ip for everyone
		s+="ips:";
		for(Menu_Player p : this.menuPlayers){
			if(p.p.address==null){
				s+="!";
			}
			else{
				s+=p.p.address.getHostAddress();
			}

			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		//RESOLUTION
		s+="resX:";
		for(Menu_Player p : this.menuPlayers){
			s+=Game.g.bottomBar.resX;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		s+="resY:";
		for(Menu_Player p : this.menuPlayers){
			s+=Game.g.bottomBar.resY;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";


		return s;
	}

	public String messageToHost(){
		String s ="";
		String thisAddress;
		Player current = this.game.currentPlayer;
		try {
			thisAddress = InetAddress.getLocalHost().getHostAddress();
			s+="idJ:"+this.game.currentPlayer.id+";ip:"+thisAddress+";";
		} catch (UnknownHostException e) {}
		// civ
		s+="cvS:"+current.getGameTeam().civ.name+";";
		// id team
		s+="idT:"+current.getGameTeam().id+";";
		// nickname
		s+="nckn:"+current.nickname+";";
		// is ready
		s+="isR:"+current.isReady+";";
		// resX and resY
		s+="resX:"+Game.g.resX+";";
		s+="resY:"+Game.g.resY+";";
		return s;
	}

	public void parseForHost(HashMap<String,String> hs){
		if(!hs.containsKey("idJ"))
			return;
		int idJ = Integer.parseInt(hs.get("idJ"));
		if(idJ==0 || idJ==1){
			return;
		}
		InetAddress address = null;
		try {
			address = InetAddress.getByName(hs.get("ip"));
		} catch (UnknownHostException e){}
		// if player already there but id is wrong do nothing
		for(Player p: this.game.players){
			if(p.address!=null && p.address.equals(address)){
				if(p.id!=idJ){
					return;
				}
			}
		}
		// checking if the player is a new player
		while(this.game.players.size()<=idJ){
			//			System.out.println("MenuMapChoice line 442 : new player added");
			this.game.addPlayer("???", address,1,1);
			this.menuPlayers.add(new Menu_Player(this.game.players.lastElement(),
					startXPlayers+ 1f/10f*sizeXPlayers,
					startYPlayers+1f*(this.menuPlayers.size()+1)/6f*sizeYPlayers-this.game.font.getHeight("Pg")/2f,game));
		}
		Player playerToChange = this.game.players.get(idJ);
		if(!playerToChange.address.equals(address)){
			//			System.out.println("menumpchoice line 426 : error over the ip addresses");
			return;
		}
		if(hs.containsKey("cvS")){
			playerToChange.getGameTeam().civ = new Civilisation(hs.get("cvS"), playerToChange.getGameTeam());
		}
		if(hs.containsKey("idT")){
			playerToChange.setTeam(Integer.parseInt(hs.get("idT")));
		}
		if(hs.containsKey("nckn")){
			playerToChange.nickname = hs.get("nckn");
		}
		if(hs.containsKey("isR")){
			playerToChange.isReady = Boolean.parseBoolean(hs.get("isR"));
		}

		this.menuPlayers.get(idJ).hasBeenUpdated = true;
	}

	public void parseForClient(HashMap<String,String> hs){
		if(hs.containsKey("map")){
			if(this.mapSelected!=Integer.parseInt(hs.get("map"))){
				Map.updateMap(Integer.parseInt(hs.get("map")), game);
			}
			this.mapSelected = Integer.parseInt(hs.get("map"));
			for(int j = 0; j<mapchoices.size(); j++){
				mapchoices.get(j).isSelected = j==this.mapSelected;
			}
		}
		if(hs.containsKey("cvS")){
			///////////
			// initializing
			///////////
			String[] civ =hs.get("cvS").split(",");
			String[] nickname =hs.get("nckn").split(",");
			String[] idTeam =hs.get("idT").split(",");
			String[] isReady =hs.get("isR").split(",");
			String[] resX = hs.get("resX").split(",");
			String[] resY = hs.get("resY").split(",");
			String[] ips =hs.get("ips").split(",");
			// drop the parsings without the player
			// (this is the first parsing where the host doesn't know yet that the player is here
			boolean toStop = true;
			String localhost = "";
			try {
				localhost = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {}
			for(int i=0; i<ips.length; i++){
				if(ips[i].equals(localhost)){
					toStop = false;
				}
			}
			if(toStop){
				return;
			}
			// checking if there is a missing player
			if(civ.length<this.game.players.size()){
				//System.out.println("MMC line 485: missing player");
				// we reparse all informations and set again the currentPlayer to the right location
				this.game.clearPlayer();
				Player p;
				for(int i = 1;i<civ.length;i++){
					this.game.addPlayer("philippe", null, 1, 1);
					p = this.game.players.get(i);
					p.getGameTeam().civ = new Civilisation(hs.get("cvS"), p.getGameTeam());
					p.nickname =  nickname[i];
					p.setTeam(Integer.parseInt(idTeam[i]));

					p.isReady = Boolean.parseBoolean(isReady[i]);
					try {
						this.game.getPlayerById(i).address= InetAddress.getByName(ips[i]);
					} catch (UnknownHostException e) {}
					if(p.address.getHostAddress().equals(this.game.currentPlayer.address.getHostAddress())){
						this.game.currentPlayer = p;
					}
					this.initializeMenuPlayer();
				}
				//				System.out.println("MenuMapChoice line 529: un joueur a ï¿½tï¿½ dï¿½connectï¿½");
				return;

			}
			// adding new player if needed
			if(civ.length>this.game.players.size()){
//				System.out.println("MMC line 511: new player");
				try {
					this.game.addPlayer("Philippe", InetAddress.getByName(hs.get("ip")),1,1);
				} catch (UnknownHostException e) {}
				this.menuPlayers.add(new Menu_Player(this.game.players.lastElement(),
						startXPlayers+ 1f/10f*sizeXPlayers,
						startYPlayers+1f*(this.menuPlayers.size()+1)/6f*sizeYPlayers-this.game.font.getHeight("Pg")/2f,game));
			}
			//checking if changes about currentPlayer
			if(!ips[this.game.currentPlayer.id].equals(this.game.currentPlayer.address.getHostAddress())){
				// changes among the player
				return;
			}
			for(int i = 0;i<civ.length;i++){
				if(this.game.currentPlayer.id!=i){
					this.menuPlayers.get(i).p.getGameTeam().civ =  new Civilisation(civ[i], this.menuPlayers.get(i).p.getGameTeam());
					this.menuPlayers.get(i).p.nickname =  nickname[i];
					this.menuPlayers.get(i).p.setTeam(Integer.parseInt(idTeam[i]));

					this.game.players.get(i).isReady = Boolean.parseBoolean(isReady[i]);
					try {
						if(i==1)
							this.game.getPlayerById(i).address = game.addressHost;
						else if(i>1)
							this.game.getPlayerById(i).address= InetAddress.getByName(ips[i]);
					} catch (UnknownHostException e) {}
				}
			}
		}
		///////////
		// parsing startTime
		///////////
		if(hs.containsKey("clk")){
			long clockTime = Long.parseLong(hs.get("clk"));
			this.game.clock.synchro(clockTime);
		}
		// checking the start time
		if(hs.containsKey("stT")){
			this.startGame = Long.parseLong(hs.get("stT"));
		}

	}

	public void initialize(){
		this.seconds = 3;
		this.startGame = 0;
		this.mapSelected = 0;
		for(Menu_MapChoice mp : this.mapchoices){
			mp.isSelected = false;
		}
		if(this.mapchoices.size()>0){
			this.mapchoices.get(0).isSelected = true;
		}
		this.selected = 0;
		maps = Map.maps();
	}

}
