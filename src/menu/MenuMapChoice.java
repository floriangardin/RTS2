package menu;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import main.Main;
import model.Game;
import model.Map;
import model.Objet;
import model.Player;
import multiplaying.InputObject;
import multiplaying.MultiReceiverChat;
import multiplaying.MultiReceiverChecksum;
import multiplaying.MultiReceiverInput;
import multiplaying.MultiReceiverPing;
import multiplaying.MultiReceiverResynchro;
import multiplaying.MultiReceiverValidation;
import multiplaying.MultiSender;

public class MenuMapChoice extends Menu {


	public int selected = -1;
	public int mapSelected = 0;
	public Vector<String> maps = Map.maps();

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
	public int seconds = 6;

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

		startXPlayers = 1f*game.resX/5f;
		startYPlayers = startY;
		sizeXPlayers = game.resX*(2f/3f)-2*startXPlayers;
		sizeYPlayers = game.resY*0.80f-startYMapChoice;
		this.items.addElement(new Menu_Item(1f/3f*this.game.resX,this.game.resY*0.9f,"retour",this.game,true));
		this.items.addElement(new Menu_Item(2f/3f*this.game.resX,this.game.resY*0.9f,"demarrer",this.game,true));
		for(int i=0; i<maps.size(); i++){
			this.mapchoices.addElement(new Menu_MapChoice(game, maps.get(i),startXMapChoice+1f/10f*sizeXMapChoice,startYMapChoice+1f*(i+3)/9f*sizeYMapChoice-this.game.font.getHeight("P")/2,200f,30f));
		}
	}


	public void callItem(int i){
		switch(i){
		case 0:
			// retour
			if(game.inMultiplayer){
				this.game.connexionSender.shutdown();
				this.shutdownNetwork();
				this.game.setMenu(this.game.menuMulti);
			}else{
				this.game.setMenu(this.game.menuIntro);
			}
			break;
		case 1: 
			// demarrer
			if(!game.inMultiplayer){
				Map.updateMap(mapSelected, game);
				game.launchGame();
			} else {
				this.game.plateau.currentPlayer.isReady = true;
			}
			break;
		default:		
		}
	}

	public void draw(Graphics g){
		this.drawItems(g);
		g.setColor(Color.white);
		for(Menu_Item item: this.mapchoices){
			item.draw(g);
		}
		g.setColor(Color.white);
		g.drawString("Players :" , startXPlayers + 1f/30f*sizeXPlayers,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXPlayers+ 1f/15f*sizeXPlayers,startYPlayers+2f/6f*sizeYPlayers-this.game.font.getHeight("P")/2f,2f,3f/6f*sizeYPlayers+this.game.font.getHeight("P")/2f);
		g.drawString("Map :" , startXMapChoice + 1f/30f*sizeXMapChoice,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXMapChoice + 1f/15f*sizeXMapChoice,startYMapChoice+1f*(3f)/9f*sizeYMapChoice-this.game.font.getHeight("P")/2,2f,6f/9f*sizeYMapChoice-this.game.font.getHeight("P")/2);
		for(int i=1;i<this.menuPlayers.size();i++){
			menuPlayers.get(i).draw(g);
		}

	}

	public void update(InputObject im){
		// Handling current player according to input
		this.menuPlayers.get(game.plateau.currentPlayer.id).update(im);
		// handling connexions
		if(game.inMultiplayer){
			if(game.host){
				// sending to all players
				this.messageToClient = this.messageToClient();
				this.handleSendingConnexions();
				// parsing if received anything
				while(game.receivedConnexion.size()>0){
					this.parseForHost(Objet.preParse(game.receivedConnexion.remove(0)));
				}
			} else {
				// sending to host
				this.game.toSendConnexion.addElement(this.messageToHost());	
				messageDropped++;	
				// parsing if received anything
				while(game.receivedConnexion.size()>0){
					messageDropped=0;
					this.parseForClient(Objet.preParse(game.receivedConnexion.remove(0)));
				}		
				//checking if game still exists
				if(this.startGame!=0 && messageDropped>1f*Main.framerate){
					//this.callItem(0);
				}
				// requete de ping
				if(roundForPingRequest==0){
					this.game.pingRequest();
				}else {
					roundForPingRequest++;
					roundForPingRequest%=10;
				}
			}
// 			// checking disconnecting players
//			int toRemove = -1;
//			if(game.host){
//				for(int i=2 ; i<this.menuPlayers.size(); i++){
//					Menu_Player mp = this.menuPlayers.get(i);
//					if(mp!=null && mp.hasBeenUpdated){
//						mp.messageDropped=0;
//						mp.hasBeenUpdated = false;
//					} else {
//						mp.messageDropped++;
//						if(mp.messageDropped>1f*Main.framerate){
//							System.out.println("disconnecting player:"+i);
//							//toRemove=i;
//						}
//					}
//				}
//			}
//			if(toRemove!=-1){
//				int k = toRemove;
//				this.game.plateau.removePlayer(k);
//				for(int i=0; i<this.game.plateau.players.size(); i++){
//					this.game.plateau.players.get(i).id = i;
//				}
//				this.initializeMenuPlayer();
//			}
			
			//Checking starting of the game
			if(startGame!=0){
				this.handleStartGame();
				return;
			}
		}
		// Checking if all players are ready then launch the game
		this.checkStartGame();
		// Updating items
		this.updateItems(im);
		// Updating map choices
		if(this.game.host || !this.game.inMultiplayer){
			for(int i=0; i<this.mapchoices.size(); i++){
				Menu_MapChoice item = this.mapchoices.get(i);
				item.update(im);
				if(im.pressedLeftClick && item.mouseOver){
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
		if(startGame-this.game.clock.getCurrentTime()<=this.seconds*1000000000L){
			//System.out.println("debut de la partie dans :" + seconds + "heure de la clock" + this.game.clock.getOrigin());
			//System.out.println("Current time: "+this.game.clock.getCurrentTime());
			this.game.sounds.buzz.play();
			seconds--;
		} else if (startGame<=this.game.clock.getCurrentTime()) {
			game.launchGame();
		}
	}
	
	public void initializeNetwork() {
		// initializing toSend depots
		this.game.toSendInput.clear();
		this.game.toSendValidation.clear();
		this.game.toSendChecksum.clear();
		this.game.toSendPing.clear();
		this.game.toSendResynchro.clear();
		this.game.toSendChat.clear();
		// initializing received depots
		this.game.receivedInputs.clear();
		this.game.receivedValidation.clear();
		this.game.receivedChecksum.clear();
		this.game.receivedPing.clear();
		this.game.receivedResynchro.clear();
		this.game.receivedChat.clear();
		
		// intializing senders
		this.game.validationSender = new MultiSender(null,this.game.portValidation, this.game.toSendValidation,this.game);
		this.game.inputSender = new MultiSender(this.game.portInput, this.game.toSendInput,this.game);
		this.game.resynchroSender = new MultiSender(this.game.portResynchro, this.game.toSendResynchro, this.game);
		this.game.pingSender = new MultiSender(this.game.addressHost, this.game.portPing, this.game.toSendPing, this.game);
		this.game.checksumSender = new MultiSender(this.game.addressHost, this.game.portChecksum, this.game.toSendChecksum, this.game);
		this.game.chatSender = new MultiSender(this.game.portChat,this.game.toSendChat, this.game);
		this.game.inputSender.start();
		this.game.resynchroSender.start();
		this.game.pingSender.start();
		this.game.checksumSender.start();
		this.game.chatSender.start();
		this.game.validationSender.start();
		
		// initializing receivers
		this.game.inputReceiver = new MultiReceiverInput(this.game);
		this.game.validationReceiver = new MultiReceiverValidation(this.game);
		this.game.resynchroReceiver = new MultiReceiverResynchro(this.game);
		this.game.pingReceiver = new MultiReceiverPing(this.game);
		this.game.checksumReceiver = new MultiReceiverChecksum(this.game);
		this.game.chatReceiver = new MultiReceiverChat(this.game);
		this.game.inputReceiver.start();
		this.game.resynchroReceiver.start();
		this.game.pingReceiver.start();
		this.game.checksumReceiver.start();
		this.game.chatReceiver.start();
		this.game.validationReceiver.start();
	}

	public void shutdownNetwork(){
		this.game.inputReceiver.shutdown();
		this.game.resynchroReceiver.shutdown();
		this.game.pingReceiver.shutdown();
		this.game.checksumReceiver.shutdown();
		this.game.chatReceiver.shutdown();
		this.game.validationReceiver.shutdown();
		this.game.inputSender.shutdown();
		this.game.resynchroSender.shutdown();
		this.game.pingSender.shutdown();
		this.game.checksumSender.shutdown();
		this.game.chatSender.shutdown();
		this.game.validationSender.shutdown();
		
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
				for(Player p : this.game.plateau.players){
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
						this.seconds = 6;
						this.startGame = this.game.clock.getCurrentTime()+10000000000L;
					}
				}
			}
		}
	}

	public void handleSendingConnexions(){
		this.game.toSendConnexion.addElement(messageToClient);
	}

	public void initializeMenuPlayer(){
		this.menuPlayers.clear();
		for(int j=0;j<this.game.plateau.players.size(); j++){
			this.menuPlayers.addElement(new Menu_Player(game.plateau.players.get(j),startXPlayers+ 1f/10f*sizeXPlayers,startYPlayers+1f*(j+1)/6f*sizeYPlayers-this.game.font.getHeight("Pg")/2f,game));
		}
	}
	

	public String messageToClient(){
		String s = "";
		String thisAddress;
		try {
			thisAddress = InetAddress.getLocalHost().getHostAddress();
			s+="idJ:"+this.game.plateau.currentPlayer.id+";ip:"+thisAddress+";";
			if(this.game.host)
				s+="hst:"+this.game.options.nickname+";npl:"+this.game.plateau.players.size()+";map:"+this.mapSelected+";";
		} catch (UnknownHostException e) {}	
		s+="cvS:";
		//Civ for all players
		for(Menu_Player p : this.menuPlayers){
			s+=p.p.getGameTeam().civ;
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
			s+=p.p.bottomBar.resX;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		s+="resY:";
		for(Menu_Player p : this.menuPlayers){
			s+=p.p.bottomBar.resY;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";


		return s;
	}

	public String messageToHost(){
		String s ="";
		String thisAddress;
		Player current = this.game.plateau.currentPlayer;
		try {
			thisAddress = InetAddress.getLocalHost().getHostAddress();
			s+="idJ:"+this.game.plateau.currentPlayer.id+";ip:"+thisAddress+";";
		} catch (UnknownHostException e) {}
		// civ
		s+="cvS:"+current.getGameTeam().civ+";";
		// id team
		s+="idT:"+current.getGameTeam().id+";";
		// nickname
		s+="nckn:"+current.nickname+";";
		// is ready
		s+="isR:"+current.isReady+";";
		// resX and resY
		s+="resX:"+current.bottomBar.resX+";";
		s+="resY:"+current.bottomBar.resY+";";
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
		for(Player p: this.game.plateau.players){
			if(p.address!=null && p.address.equals(address)){
				if(p.id!=idJ){
					return;
				}
			}
		}
		// checking if the player is a new player
		while(this.game.plateau.players.size()<=idJ){
			System.out.println("MenuMapChoice line 442 : new player added");
			this.game.plateau.addPlayer("???", address,1,1);
			this.menuPlayers.add(new Menu_Player(this.game.plateau.players.lastElement(),
					startXPlayers+ 1f/10f*sizeXPlayers,
					startYPlayers+1f*(this.menuPlayers.size()+1)/6f*sizeYPlayers-this.game.font.getHeight("Pg")/2f,game));
		}
		Player playerToChange = this.game.plateau.players.get(idJ);
		if(!playerToChange.address.equals(address)){
			System.out.println("menumpchoice line 426 : error over the ip addresses");
			return;
		}
		if(hs.containsKey("cvS")){
			playerToChange.getGameTeam().civ = Integer.parseInt(hs.get("cvS"));
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
		if(hs.containsKey("resX")){
			playerToChange.bottomBar.resX = Integer.parseInt(hs.get("resX"));
		}
		if(hs.containsKey("resY")){
			playerToChange.bottomBar.resY = Integer.parseInt(hs.get("resY"));
		}
		this.menuPlayers.get(idJ).hasBeenUpdated = true;
	}

	public void parseForClient(HashMap<String,String> hs){
		if(hs.containsKey("map")){
			if(this.mapSelected!=Integer.parseInt(hs.get("map"))){
				Map.updateMap(mapSelected, game);
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
				System.out.println("MenuMapChoice line 496: return due to missing player");
				return;
			}
			// checking if there is a missing player
			if(civ.length<this.game.plateau.players.size()){
				// we reparse all informations and set again the currentPlayer to the right location
				this.game.plateau.clearPlayer();
				Player p;
				for(int i = 1;i<civ.length;i++){
					this.game.plateau.addPlayer("philippe", null, 1, 1);
					p = this.game.plateau.players.get(i);
					p.getGameTeam().civ =  Integer.parseInt(civ[i]);
					p.nickname =  nickname[i];
					p.setTeam(Integer.parseInt(idTeam[i]));
					p.bottomBar.update((int) Float.parseFloat(resX[i]),(int) Float.parseFloat(resY[i]));
					p.isReady = Boolean.parseBoolean(isReady[i]);
					try {
						this.game.getPlayerById(i).address= InetAddress.getByName(ips[i]);
					} catch (UnknownHostException e) {}
					if(p.address.getHostAddress().equals(this.game.plateau.currentPlayer.address.getHostAddress())){
						this.game.plateau.currentPlayer = p;
					}
					this.initializeMenuPlayer();
				}
				System.out.println("MenuMapChoice line 529: un joueur a �t� d�connect�");
				return;

			}
			///////////
			// parsing
			///////////
			if(hs.containsKey("clk")){
				long clockTime = Long.parseLong(hs.get("clk"));
				this.game.clock.synchro(clockTime);
			}
			// checking the start time
			if(hs.containsKey("stT")){
				this.startGame = Long.parseLong(hs.get("stT"));
			}
			// adding new player if needed
			if(civ.length>this.game.plateau.players.size()){
				try {
					this.game.plateau.addPlayer("Philippe", InetAddress.getByName(hs.get("ip")),1,1);
				} catch (UnknownHostException e) {}
				this.menuPlayers.add(new Menu_Player(this.game.plateau.players.lastElement(),
						startXPlayers+ 1f/10f*sizeXPlayers,
						startYPlayers+1f*(this.menuPlayers.size()+1)/6f*sizeYPlayers-this.game.font.getHeight("Pg")/2f,game));
			}
			//checking if changes about currentPlayer
			if(!ips[this.game.plateau.currentPlayer.id].equals(this.game.plateau.currentPlayer.address.getHostAddress())){
				// changes among the player
				System.out.println("MenuMapChoice line 482: error player unidentified");
				return;
			}
			for(int i = 0;i<civ.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.menuPlayers.get(i).p.getGameTeam().civ =  Integer.parseInt(civ[i]);
					this.menuPlayers.get(i).p.nickname =  nickname[i];
					this.menuPlayers.get(i).p.setTeam(Integer.parseInt(idTeam[i]));
					this.menuPlayers.get(i).p.bottomBar.update((int) Float.parseFloat(resX[i]),(int) Float.parseFloat(resY[i]));
					this.game.plateau.players.get(i).isReady = Boolean.parseBoolean(isReady[i]);
					try {
						this.game.getPlayerById(i).address= InetAddress.getByName(ips[i]);
					} catch (UnknownHostException e) {}
				}
			}
		}
	}

}
