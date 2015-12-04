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
import model.Map;
import model.Objet;
import model.Player;
import multiplaying.InputObject;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class MenuMapChoice extends Menu {

	boolean pingAsked = false;
	boolean secondPingAsked = false;

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
			this.game.connexionSender.interrupt();
			if(game.inMultiplayer)
				this.game.setMenu(this.game.menuMulti);
			else
				this.game.setMenu(this.game.menuIntro);
			break;
		case 1: 
			// démarrer
			if(!game.inMultiplayer){
				Map.updateMap(mapSelected, game);
				game.launchGame();
				break;
			} else {
				this.game.plateau.currentPlayer.isReady = true;
			}
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
		g.fillRect(startXMapChoice + 1f/15f*sizeXMapChoice,startYMapChoice+1f*(3)/9f*sizeYMapChoice-this.game.font.getHeight("P")/2,2f,6f/9f*sizeYMapChoice-this.game.font.getHeight("P")/2);
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
				this.handleSendingConnexions();
				// parsing if received anything
				while(game.connexions.size()>0){
					this.parseForHost(Objet.preParse(game.connexions.remove(0)));
				}
			} else {
				// sending to host
				this.game.toSendConnexions.addElement("2"+this.messageToHost());		
				// parsing if received anything
				while(game.connexions.size()>0){
					this.parseForClient(Objet.preParse(game.connexions.remove(0)));
				}		
			}
			// checking disconnecting players
			int toRemove = -1;
			if(game.host){
				for(int i=2 ; i<this.menuPlayers.size(); i++){
					Menu_Player mp = this.menuPlayers.get(i);
					if(mp!=null && mp.hasBeenUpdated){
						mp.messageDropped=0;
						mp.hasBeenUpdated = false;
					} else {
						mp.messageDropped++;
						if(mp.messageDropped>25){
							System.out.println("disconnecting player:"+i);
							toRemove=i;
						}
					}
				}
			}
			if(toRemove!=-1){
				int k = toRemove;
				this.menuPlayers.remove(k);
				this.game.plateau.removePlayer(k);
				for(int i=0; i<this.menuPlayers.size(); i++){
					this.game.plateau.players.get(i).id = i;
				}
				this.initializeMenuPlayer();
			}
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
		for(int i=0; i<this.mapchoices.size(); i++){
			Menu_MapChoice item = this.mapchoices.get(i);
			item.update(im);
			if(im.pressedLeftClick && item.mouseOver){
				for(Menu_MapChoice mp : this.mapchoices){
					mp.isSelected = false;
				}
				item.isSelected = true;
				this.mapSelected = i;
			}
		}	

	}
	public void handleStartGame(){
		/**
		 * function that checks if the game is about to start
		 * ie. if the startTime has been defined
		 * 
		 * then play sounds each seconds then launches the game
		 */
		if(startGame-this.game.clock.getCurrentTime()<=this.seconds*1000000000L){
			//System.out.println("debut de la partie dans :" + seconds + "heure de la clock" + this.game.clock.getOrigin());
			//System.out.println("Current time: "+this.game.clock.getCurrentTime());
			this.game.sounds.buzz.play();
			if(seconds==5){
				Map.updateMap(mapSelected, game);
				// Create sender and receiver
				for(Player p : this.game.plateau.players){
					this.game.toSendInputs.add(new Vector<String>());
					this.game.inputSender.add(new MultiSender(p.address, this.game.portInput, this.game.toSendInputs.lastElement(),this.game));
					if(p.address!=null)
						this.game.inputSender.lastElement().start();
				}
				this.game.inputReceiver = new MultiReceiver(this.game, this.game.portInput);
			}

			seconds--;
		} else if (startGame<=this.game.clock.getCurrentTime()) {
			this.game.inputReceiver.start();
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
						this.startGame = this.game.clock.getCurrentTime()+10000000000L;
					}
				}
			}
		}
	}
	public void handleSendingConnexions(){
		// sending games to ingame players
		for(Player p : this.game.plateau.players){
			if(p.address != null && p!=this.game.plateau.currentPlayer){
				this.game.connexionSender.address = p.address;
				//						Thread.sleep((long) 0.005);
				this.game.toSendConnexions.addElement("2"+toString());
				try {
					Thread.sleep((long) 0.05);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		// sending games to other ips
		for(int cooldown=0; cooldown<100; cooldown++){
			if(!game.connexionSender.isAlive()){
				game.connexionSender.start();
			}
			String s="";
			try {
				s = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			String[] tab = s.split("\\.");
			s = "";
			for(int k=0; k<tab.length-1;k++){
				s += tab[k]+".";
			}
			String thisAddress;
			try {
				thisAddress = InetAddress.getLocalHost().getHostAddress();
				if(!thisAddress.equals(s+""+cooldown)){
					this.game.connexionSender.address = InetAddress.getByName(s+""+cooldown);
					//							Thread.sleep((long) 0.005);
					this.game.toSendConnexions.addElement("2"+toString());
					Thread.sleep((long) 0.01);
					//							this.game.connexionSender.address = InetAddress.getByName(s+""+((cooldown+1)%255));
				} else {
				}
			} catch (UnknownHostException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void initializeMenuPlayer(){
		this.menuPlayers.clear();
		for(int j=0;j<this.game.plateau.players.size(); j++){
			this.menuPlayers.addElement(new Menu_Player(game.plateau.players.get(j),startXPlayers+ 1f/10f*sizeXPlayers,startYPlayers+1f*(j+1)/6f*sizeYPlayers-this.game.font.getHeight("Pg")/2f,game));
		}
	}

	public String toString(){
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
		//cancelling if the sender is the host
		try{
			if(address.getHostAddress().equals(InetAddress.getLocalHost().getHostAddress())){

			}
		} catch (UnknownHostException e){}
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
			if(civ.length<this.game.plateau.players.size()){
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
				}
				System.out.println("MenuMapChoice line 529: un joueur a été déconnecté");
				return;
				
			}
			///////////
			// parsing
			///////////
			if(hs.containsKey("clk")){
				long clockTime = Long.parseLong(hs.get("clk"));
				if((!this.game.host && !pingAsked) || (seconds<4&& ! secondPingAsked)){
					this.game.clock.getPing();
					pingAsked = true;
					secondPingAsked= true;
				}
				if(!this.game.host && pingAsked){
					this.game.clock.synchro(clockTime);
				}

			}
			// checking the start time
			if(hs.containsKey("stT")){
				this.startGame = Long.parseLong(hs.get("stT"));
				System.out.println("MenuMapChoice line 489 : parsed Start Time");
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
