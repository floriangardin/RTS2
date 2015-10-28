package menu;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import bullets.Bullet;
import model.Game;
import model.Objet;
import model.Sounds;
import model.Utils;

public class MenuIntro extends Menu {

	Vector<Objet> trees = new Vector<Objet>();
	Vector<Bullet> bullets = new Vector<Bullet>();
	float timer = 0f;
	float nextBullet = 1f;
	Image title;
	public boolean toGame = false;
	public float timeToGame = 00f;
	public Image newGame;
	public Image multiplayer;
	public Image options;
	public Image exit;

	public Image newGameSelected;
	public Image multiplayerSelected;
	public Image optionsSelected;
	public Image exitSelected;
	public int selected = -1;
	private boolean multiplaying;


	public int cooldown;

	public MenuIntro(Game game){
		this.music = game.musics.menu;
		this.music.loop();
		this.music.setVolume(game.options.musicVolume);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		float startY = 0.37f*this.game.resY;
		float stepY = 0.12f*this.game.resY;
		float ratioReso = this.game.resX/2800f;
		try {
			this.newGame = new Image("pics/menu/newgame.png").getScaledCopy(ratioReso);
			this.newGameSelected = new Image("pics/menu/newgameselected.png").getScaledCopy(ratioReso);
			this.multiplayer= new Image("pics/menu/multiplayer.png").getScaledCopy(ratioReso);
			this.multiplayerSelected= new Image("pics/menu/multiplayerselected.png").getScaledCopy(ratioReso);
			this.options = new Image("pics/menu/options.png").getScaledCopy(ratioReso);
			this.optionsSelected = new Image("pics/menu/optionsselected.png").getScaledCopy(ratioReso);
			this.exit = new Image("pics/menu/exit.png").getScaledCopy(ratioReso);
			this.exitSelected = new Image("pics/menu/exitselected.png").getScaledCopy(ratioReso);
			this.title = new Image("pics/menu/title01.png").getScaledCopy(0.35f*this.game.resY/650);
			float startX = this.game.resX/2-this.newGame.getWidth()/2;
			this.items.addElement(new Menu_Item(startX,startY,this.newGame,this.newGameSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+1*stepY,this.multiplayer,this.multiplayerSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+2*stepY,this.options,this.optionsSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+3*stepY,this.exit,this.exitSelected,this.game));

		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		Utils.triY(trees);
		this.sounds = game.sounds;
	}

	//	public void callItems(Input i){
	//		for(int j=0; j<items.size(); j++){
	//			if(items.get(j).isClicked(i))
	//				callItem(j);
	//		}
	//	}
	//	public void callItems(InputModel im){
	//		for(int j=0; j<items.size(); j++){
	//			if(items.get(j).isClicked(im))
	//				callItem(j);
	//		}
	//	}
	public void callItem(int i){
		switch(i){
		case 0:
			this.game.setMenu(this.game.menuMapChoice);
			break;
		case 1:
			this.game.setMenu(this.game.menuMulti);
			break;
		case 2:
			this.game.setMenu(this.game.menuOptions);
			break;
		case 3: 
			this.game.app.exit();
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
		g.drawImage(this.title, this.game.resX/2-this.title.getWidth()/2, 10f);
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
