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
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class MenuMapChoice extends Menu {

	boolean pingAsked = false;
	boolean secondPingAsked = false;
	
	public Image back;
	public Image play;
	public Image marbre;
	public Image marbre2;
	Image title;

	public Image playSelected;
	public Image backSelected;
	public int selected = -1;
	public int mapSelected = 0;
	public Vector<String> maps = Map.maps();

	public Vector<Menu_MapChoice> mapchoices;

	public Vector<Menu_Player> players;


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
		try {
			this.backGround = new Image("pics/fondMenu.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.music = game.musics.menu;
		this.game = game;
		this.items = new Vector<Menu_Item>();
		this.mapchoices = new Vector<Menu_MapChoice>();
		this.players = new Vector<Menu_Player>();

		startY = this.game.resY*0.37f;
		stepY = 0.12f*this.game.resY;

		startXMapChoice = game.resX*(2f/3f+1f/60f);
		startYMapChoice = startY;
		sizeXMapChoice = game.resX*(1f/3f-1f/30f);
		sizeYMapChoice = game.resY*39f/40f-startYMapChoice;

		startXPlayers = 5f*game.resX/60f;;
		startYPlayers = startY;
		sizeXPlayers = game.resX*(2f/3f)-2*startXPlayers;
		sizeYPlayers = game.resY*0.80f-startYMapChoice;

		float ratioReso = this.game.resX/2800f;
		try {
			this.title = new Image("pics/menu/title01.png").getScaledCopy(0.35f*this.game.resY/650);
			this.play = new Image("pics/menu/play.png").getScaledCopy(ratioReso);
			this.playSelected = new Image("pics/menu/playselected.png").getScaledCopy(ratioReso);
			this.back= new Image("pics/menu/back.png").getScaledCopy(ratioReso);
			this.backSelected= new Image("pics/menu/backselected.png").getScaledCopy(ratioReso);
			this.marbre= new Image("pics/menu/marbre.png").getScaledCopy((int)sizeXPlayers, (int)sizeYPlayers);
			this.marbre2= new Image("pics/menu/marbre2.png").getScaledCopy((int)sizeXMapChoice,(int)sizeYMapChoice);
			float startX = this.game.resX/2-this.play.getWidth()/2;
//			this.items.addElement(new Menu_Item(startXPlayers,startYPlayers,this.marbre,this.marbre,this.game));
//			this.items.lastElement().selectionable = false;
//			this.items.addElement(new Menu_Item(startXMapChoice,startYMapChoice,this.marbre2,this.marbre2,this.game));
//			this.items.lastElement().selectionable = false;
			this.items.addElement(new Menu_Item(1f/6f*this.game.resX-this.back.getWidth()/2f,this.game.resY*0.9f-this.back.getHeight()/2f,this.back,this.backSelected,this.game));
			this.items.addElement(new Menu_Item(1f/2f*this.game.resX-this.back.getWidth()/2f,this.game.resY*0.9f-this.back.getHeight()/2f,this.play,this.playSelected,this.game));
			for(int i=0; i<maps.size(); i++){
				this.mapchoices.addElement(new Menu_MapChoice(maps.get(i),startXMapChoice+1f/10f*sizeXMapChoice,startYMapChoice+1f*(i+2)/12f*sizeYMapChoice-35f/2,200f,30f));
			}
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		this.sounds = game.sounds;
	}


	public void callItem(int i){
		switch(i){
		case 0:
			if(game.inMultiplayer)
				this.game.setMenu(this.game.menuMulti);
			else
				this.game.setMenu(this.game.menuIntro);
			break;
		case 1: 
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
		g.setDrawMode(Graphics.MODE_NORMAL);
		g.drawImage(this.backGround, 0,0,this.game.resX,this.game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		g.drawImage(this.title, this.game.resX/2f-this.title.getWidth()/2, 10f);

		for(Menu_Item item: this.items){
			g.setColor(Color.white);
			item.draw(g);
		}
		g.setColor(Color.white);
		for(Menu_Item item: this.mapchoices){
			item.draw(g);
		}
		g.setColor(Color.white);
		g.drawString("Players :" , startXPlayers + 1f/30f*sizeXPlayers,startYPlayers+1f/6f*sizeYPlayers-g.getFont().getHeight("P")/2f);
		g.fillRect(startXPlayers + 1f/15f*sizeXPlayers, startYPlayers+1f/6f*sizeYPlayers+g.getFont().getHeight("P")/2f+15f, 2f, sizeYPlayers-(1f/6f*sizeYPlayers+g.getFont().getHeight("P")/2f+5f));
		g.drawString("Map :" , startXMapChoice + 1f/30f*sizeXMapChoice,startYMapChoice+1f/12f*sizeYMapChoice-g.getFont().getHeight("P")/2f);
		for(int i=1;i<this.players.size();i++){
			players.get(i).draw(g);
		}

	}

	public void update(Input i){
		this.players.clear();
		for(int j=0;j<this.game.plateau.players.size(); j++){
			this.players.addElement(new Menu_Player(game.plateau.players.get(j),startXPlayers+ 1f/10f*sizeXPlayers,startYPlayers+1f*(j+1)/6f*sizeYPlayers-35f/2f,game));
			this.players.get(j).update(i);
		}
		//Checking starting of the game
		if(startGame!=0){
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
			return;
		}
		//Checking if all players are ready then launch the game
		if(game.inMultiplayer && game.host){
			boolean toGame = true;
			// checking if all players are ready
			for(int j=1;j<this.players.size(); j++){
				if(!this.players.get(j).isReady){
					toGame = false;
				}
			}
			// Checking if at least one player is present by team
			boolean present1 = false;
			boolean present2 = false;
			if(toGame){
				//  check previous condition
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
		if(i!=null){
			if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				this.callItems(i);
				this.game.sounds.menuItemSelected.play(1f,game.options.soundVolume);
			}
			for(Menu_Item item: this.items){
				item.update(i);
			}	
			for(Menu_MapChoice item: this.mapchoices){
				item.update(i);
			}	
		}
		if(game.inMultiplayer){
			if(game.host){
				// sending games
				for(Player p : this.game.plateau.players){
					if(p.address != null){
						this.game.connexionSender.address = p.address;
						//						Thread.sleep((long) 0.005);
						this.game.toSendConnexions.addElement("2"+toString());
						try {
							Thread.sleep((long) 0.005);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(cooldown<=255){
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
							Thread.sleep((long) 0.005);
							//							this.game.connexionSender.address = InetAddress.getByName(s+""+((cooldown+1)%255));
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cooldown++;
				} else {
					cooldown=0;				
				}
			}
			while(game.connexions.size()>0){
				this.parse(Objet.preParse(game.connexions.remove(0)));
			}
			if(!game.host)
				this.game.toSendConnexions.addElement("2"+this.toString());
		}
	}

	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
		for(int j=0; j<players.size(); j++){
			if(j==game.plateau.currentPlayer.id){
				players.get(j).callItem(i);
			}
		}
		int toselect = -1;
		if(game.host || !game.inMultiplayer){
			for(int j=0; j<mapchoices.size(); j++){
				if(mapchoices.get(j).isClicked(i))
					toselect = j;
			}
			if(toselect!=-1){
				for(int j=0; j<mapchoices.size(); j++){
					mapchoices.get(j).isSelected = j==toselect;
					if(mapchoices.get(j).isSelected)
						mapSelected = j;
				}
			}
		}
	}

	public String toString(){
		String s = "";
		String thisAddress;
		try {
			thisAddress = InetAddress.getLocalHost().getHostAddress();
			s+="ip:"+thisAddress+";hst:"+this.game.options.nickname+";npl:"+this.game.plateau.players.size()+";";
		} catch (UnknownHostException e) {}	
		s+="map:"+this.mapSelected+";";
		s+="cvS:";
		//Civ for all players
		for(Menu_Player p : this.players){
			s+=p.p.getGameTeam().civ;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		//id for all players
		s+="idT:";
		for(Menu_Player p : this.players){
			s+=p.p.getGameTeam().id;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";
		s+="nckn:";
		//Nickname
		for(Menu_Player p : this.players){
			s+=p.p.nickname;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		s+="isR:";
		for(Menu_Player p : this.players){
			s+=p.isReady?"1":"0";
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";
		//Send time if isHost
		if(this.game.host){
			s+="clk:"+this.game.clock.getCurrentTime();
			s+=";";
		}
		//Send starttime if isHost and is about to launch game
		if(this.game.host && this.startGame!=0){
			s+="stT:"+this.startGame;
			s+=";";
		}

		//Send all ip for everyone
		s+="ips:";
		for(Menu_Player p : this.players){
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
		for(Menu_Player p : this.players){
			s+=p.p.bottomBar.resX;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";
		
		s+="resY:";
		for(Menu_Player p : this.players){
			s+=p.p.bottomBar.resY;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";


		return s;
	}
	

	//TODO put parse in update of this menu
	public void parse(HashMap<String,String> hs){
		if(hs.containsKey("map")){
			if(!this.game.host){
				this.mapSelected = Integer.parseInt(hs.get("map"));
				for(int j = 0; j<mapchoices.size(); j++){
					mapchoices.get(j).isSelected = j==this.mapSelected;
				}
			}
		}


		if(hs.containsKey("cvS")){
			String[] civ =hs.get("cvS").split(",");
			String[] nickname =hs.get("nckn").split(",");
			String[] idTeam =hs.get("idT").split(",");
			String[] isReady =hs.get("isR").split(",");
			String[] resX = hs.get("resX").split(",");
			String[] resY = hs.get("resY").split(",");
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
			if(hs.containsKey("stT")){
				this.startGame = Long.parseLong(hs.get("stT"));
			}
			//
			if(civ.length>this.game.plateau.players.size()){
				try {
					this.game.plateau.addPlayer("Philippe", InetAddress.getByName(hs.get("ip")),1,1);
				} catch (UnknownHostException e) {}
				this.players.add(new Menu_Player(this.game.plateau.players.lastElement(),startXPlayers, startYPlayers,game));
			}

			for(int i = 0;i<civ.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).p.getGameTeam().civ =  Integer.parseInt(civ[i]);
				}
			}

			for(int i = 0;i<nickname.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).p.nickname =  nickname[i];
				}
			}

			for(int i = 0;i<idTeam.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).p.setTeam(Integer.parseInt(idTeam[i]));
				}

			}

			for(int i = 0;i<resX.length;i++){
				if(this.game.plateau.currentPlayer.id!=i && this.players.get(i).p.bottomBar.resX==1){
					this.players.get(i).p.bottomBar.update((int) Float.parseFloat(resX[i]),(int) Float.parseFloat(resY[i]));
				}

			}

			for(int i = 0;i<isReady.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).isReady = isReady[i].equals("1");
					this.game.plateau.players.get(i).isReady = isReady[i].equals("1");
				}
			}



		}
		//Handle ip
		if(hs.containsKey("ips")){
			String[] ips =hs.get("ips").split(",");
			for(int i = 0;i<ips.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					if(!ips[i].equals("!")){
						try {
							this.game.getPlayerById(i).address= InetAddress.getByName(ips[i]);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}
		}
	}


}
