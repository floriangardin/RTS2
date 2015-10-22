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

	float startXPlayers;
	float startYPlayers;



	public int cooldown;

	public MenuMapChoice(Game game){
		this.music = game.musics.menu;
		this.music.loop();
		this.music.setVolume(game.options.musicVolume);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		this.mapchoices = new Vector<Menu_MapChoice>();
		this.players = new Vector<Menu_Player>();

		startY = 100f;
		stepY = 0.13f*this.game.resY;

		startXMapChoice = game.resX*2f/3f;
		startYMapChoice = startY+1f*stepY;

		startXPlayers = 25f;
		startYPlayers = startY+1f*stepY;

		float ratioReso = this.game.resX/2400f;
		try {
			this.title = new Image("pics/menu/goldtitle.png");
			this.play = new Image("pics/menu/play.png").getScaledCopy(ratioReso);
			this.playSelected = new Image("pics/menu/playselected.png").getScaledCopy(ratioReso);
			this.back= new Image("pics/menu/back.png").getScaledCopy(ratioReso);
			this.backSelected= new Image("pics/menu/backselected.png").getScaledCopy(ratioReso);
			this.marbre= new Image("pics/menu/marbre.png").getScaledCopy(1.5f*ratioReso);
			this.marbre2= new Image("pics/menu/marbre2.png").getScaledCopy(1.5f*ratioReso);
			float startX = this.game.resX/2-this.play.getWidth()/2;
			this.items.addElement(new Menu_Item(startXPlayers+5f,startYPlayers+45f,this.marbre,this.marbre,this.game));
			this.items.lastElement().selectionable = false;
			this.items.addElement(new Menu_Item(startXMapChoice+15f,startYMapChoice+45f,this.marbre2,this.marbre2,this.game));
			this.items.lastElement().selectionable = false;
			this.items.addElement(new Menu_Item(startX-100f-this.play.getWidth(),this.game.resY-1.5f*stepY,this.back,this.backSelected,this.game));
			this.items.addElement(new Menu_Item(startX-60f,this.game.resY-1.5f*stepY,this.play,this.playSelected,this.game));
			for(int i=0; i<maps.size(); i++){
				this.mapchoices.addElement(new Menu_MapChoice(maps.get(i),startXMapChoice+100f,startYMapChoice+150f+50f*i,200f,30f));
			}
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		this.sounds = game.sounds;
	}


	public void callItem(int i){
		switch(i){
		case 2:
			if(game.inMultiplayer)
				this.game.setMenu(this.game.menuMulti);
			else
				this.game.setMenu(this.game.menuIntro);
			break;
		case 3: 
			if(!game.inMultiplayer){
				Map.updateMap(mapSelected, game);
				this.music = game.musics.imperial;
				this.music.loop();
				this.music.setVolume(game.options.musicVolume);
				//this.game.newGame();
				this.game.quitMenu();
				break;
			} else {
				this.players.get(game.plateau.currentPlayer.id).isReady = !this.players.get(game.plateau.currentPlayer.id).isReady;
			}
		default:		
		}
	}

	public void draw(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, this.game.resX, this.game.resY);
		g.drawImage(this.title, this.game.resX/2f-this.title.getWidth()/2, 50f+0.05f*this.game.resY-this.title.getHeight()/2);

		for(Menu_Item item: this.items){
			item.draw(g);
		}
		g.setColor(Color.black);
		for(Menu_Item item: this.mapchoices){
			item.draw(g);
		}
		g.setColor(Color.black);
		g.drawString("Players :" , startXPlayers+100f, startYPlayers+100f);
		g.drawString("Map :" , startXMapChoice+60f, startYMapChoice+100f);
		for(int i=1;i<this.players.size();i++){
			players.get(i).draw(g);
		}

	}

	public void update(Input i){
		this.players.clear();
		for(int j=0;j<this.game.plateau.players.size(); j++){
			this.players.addElement(new Menu_Player(game.plateau.players.get(j),startXPlayers+130f,startYPlayers+100f+80*j,game));
			this.players.get(j).update(i);
		}
		//Checking if all players are ready then launch the game
		if(game.inMultiplayer){
			boolean toGame = true;
			// checking if all players are ready
			for(Menu_Player m:this.players)
				if(!m.isReady)
					toGame = false;
			// Checking if at least one player is present by team
			if(toGame){
				// TODO check previous condition
			}
			if (toGame){
				//TODO Launch Game

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
							Thread.sleep((long) 0.5);
							this.game.toSendConnexions.addElement("2"+toString());
							Thread.sleep((long) 0.5);
							this.game.connexionSender.address = InetAddress.getByName(s+""+((cooldown+1)%255));
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
				if(this.game.connexions.size()>0){

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
		if(game.host){
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
			s+="ip:"+thisAddress+";hostname:"+this.game.options.nickname+";nplayers:"+this.game.plateau.players.size()+";";
		} catch (UnknownHostException e) {}	
		s+="idExp:"+this.game.plateau.currentPlayer.id+";";
		s+="map:"+this.mapSelected+";";
		s+="civSelected:";
		//Civ for all players
		for(Menu_Player p : this.players){
			s+=p.p.gameteam.civ;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		//id for all players
		s+="idTeam:";
		for(Menu_Player p : this.players){
			s+=p.p.gameteam.id;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";
		s+="nickname:";
		//Nickname
		for(Menu_Player p : this.players){
			s+=p.p.nickname;
			s+=",";
		}
		s = s.substring(0,s.length()-1);
		s+= ";";

		s+="isReady:";
		for(Menu_Player p : this.players){
			s+=p.isReady?"1":"0";
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
			}
		}
		if(hs.containsKey("civSelected")){
			String[] civ =hs.get("civSelected").split(",");
			String[] nickname =hs.get("nickname").split(",");
			String[] idTeam =hs.get("idTeam").split(",");
			String[] isReady =hs.get("isReady").split(",");
			for(int i = 0;i<civ.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).p.gameteam.civ =  Integer.parseInt(civ[i]);
				}
			}

			for(int i = 0;i<nickname.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).p.nickname =  nickname[i];
				}
			}

			for(int i = 0;i<idTeam.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).p.gameteam.id = Integer.parseInt(idTeam[i]);
				}

			}

			for(int i = 0;i<isReady.length;i++){
				if(this.game.plateau.currentPlayer.id!=i){
					this.players.get(i).isReady = idTeam[i].equals("1");
				}
			}

		}
	}


}
