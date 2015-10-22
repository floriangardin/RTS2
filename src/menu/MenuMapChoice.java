package menu;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import model.Game;
import model.Map;
import model.Player;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class MenuMapChoice extends Menu {

	public Image gamemode;
	public Image players;
	public Image map;
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
			this.players = new Image("pics/menu/players.png").getScaledCopy(ratioReso);
			this.map = new Image("pics/menu/map.png").getScaledCopy(ratioReso);
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
			this.game.setMenu(this.game.menuIntro);
			break;
		case 3: 
			Map.updateMap(mapSelected, game);
			this.music = game.musics.imperial;
			this.music.loop();
			this.music.setVolume(game.options.musicVolume);
			//this.game.newGame();
			this.game.quitMenu();
			break;
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
		for(int i=1;i<this.game.players.size();i++){
			game.players.get(i).drawMapChoice(g,startXPlayers+130f,startYPlayers+100f+80*i);
		}

	}

	public void update(Input i){
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
				if(cooldown==0){
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
						for(int ip=0; ip<255;ip++){
							if(!thisAddress.equals(s+""+ip)){
								this.game.connexionSender.address = InetAddress.getByName(s+""+ip);
								this.game.toSendConnexions.addElement("2"+thisAddress+","+this.game.options.nickname);
							}
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					cooldown++;
				} else {
					cooldown=cooldown<=60 ? cooldown+1:0;				
				}
				if(this.game.connexions.size()>0){
//					try {
//						this.game.addressClient = InetAddress.getByName(this.game.connexions.get(0));
//						this.game.outputSender = new MultiSender(this.game.addressClient,this.game.portOutput,this.game.toSendOutputs,this.game);
//						this.game.inputReceiver = new MultiReceiver(this.game,this.game.portInput);
//						this.game.outputSender.start();
//						this.game.inputReceiver.start();
//						this.music = game.musics.imperial;
//						this.music.loop();
//						this.music.setVolume(game.options.musicVolume);
//						this.game.newGame();
//						this.game.quitMenu();
//					} catch (UnknownHostException e1) {
//						e1.printStackTrace();
//					}
				}
			} else {
				// updating info
				
			}
		}
	}

	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
		for(int j=0; j<mapchoices.size(); j++){
			mapchoices.get(j).isSelected = mapchoices.get(j).isClicked(i);
			if(mapchoices.get(j).isSelected)
				mapSelected = j;
		}
	}
}
