package menu;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import bullets.Bullet;
import model.Game;
import model.Objet;

public class MenuMapChoice extends Menu {

	public Image gamemode;
	public Image players;
	public Image map;
	public Image back;
	public Image play;
	public Image marbre;

	public Image playSelected;
	public Image backSelected;
	public int selected = -1;
	
	private boolean multiplaying;
	
	//TODO upgrading multiplayer
	public int cooldown;

	public MenuMapChoice(Game game){
		this.music = game.musics.menu;
		this.music.loop();
		this.music.setVolume(game.options.musicVolume);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		this.multiplaying = game.inMultiplayer;
		float startY = 100f;
		float stepY = 0.13f*this.game.resY;
		float ratioReso = this.game.resX/2400f;
		try {
			this.play = new Image("pics/menu/play.png").getScaledCopy(ratioReso);
			this.playSelected = new Image("pics/menu/playselected.png").getScaledCopy(ratioReso);
			this.back= new Image("pics/menu/back.png").getScaledCopy(ratioReso);
			this.backSelected= new Image("pics/menu/backselected.png").getScaledCopy(ratioReso);
			this.marbre= new Image("pics/menu/marbre.png").getScaledCopy(1.5f*ratioReso);
			if(multiplaying)
				this.gamemode = new Image("pics/menu/multiplayer.png").getScaledCopy(ratioReso);
			else
				this.gamemode = new Image("pics/menu/singleplayer.png").getScaledCopy(ratioReso);
			this.players = new Image("pics/menu/players.png").getScaledCopy(ratioReso);
			this.map = new Image("pics/menu/map.png").getScaledCopy(ratioReso);
			float startX = this.game.resX/2-this.gamemode.getWidth()/2;
			this.items.addElement(new Menu_Item(30f,startY+1f*stepY+45f,this.marbre,this.marbre,this.game));
			this.items.lastElement().selectionable = false;
			this.items.addElement(new Menu_Item(startX,startY,this.gamemode,this.gamemode,this.game));
			this.items.lastElement().selectionable = false;
			this.items.addElement(new Menu_Item(25f,startY+1f*stepY,this.players,this.players,this.game));
			this.items.lastElement().selectionable = false;
			this.items.addElement(new Menu_Item(game.resX*2f/3f,startY+1f*stepY,this.map,this.map,this.game));
			this.items.lastElement().selectionable = false;
			this.items.addElement(new Menu_Item(startX-80f-this.play.getWidth(),this.game.resY-1.5f*stepY,this.back,this.backSelected,this.game));
			this.items.addElement(new Menu_Item(startX-40f,this.game.resY-1.5f*stepY,this.play,this.playSelected,this.game));
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		this.sounds = game.sounds;
	}

	
	public void callItem(int i){
		switch(i){
		case 3:
			this.game.setMenu(this.game.menuIntro);
			break;
		case 4: 
			this.music = game.musics.imperial;
			this.music.loop();
			this.music.setVolume(game.options.musicVolume);
			this.game.newGame();
			this.game.quitMenu();
			break;
		default:		
		}
	}

	public void draw(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, this.game.resX, this.game.resY);
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		g.setColor(Color.white);
		if(multiplaying)
			g.drawString("waiting for someone", this.game.resX/2f-g.getFont().getWidth("waiting for someone")/2f, this.game.resY-g.getFont().getHeight("waiting for someone")-2f);
	}

	public void update(Input i){
		if(i!=null){
			if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				callItems(i);
				this.game.sounds.menuItemSelected.play(1f,game.options.soundVolume);
			}
			for(Menu_Item item: this.items){
				item.update(i);
			}			
		}
	}
}
