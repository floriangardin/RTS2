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
	
	//TODO upgrading multiplayer
	public int cooldown;

	public MenuIntro(Game game){
		this.music = game.musics.menu;
		this.music.loop();
		this.music.setVolume(game.options.musicVolume);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		float startY = 100f+0.1f*this.game.resX;
		float stepY = 0.15f*this.game.resY;
		float ratioReso = this.game.resX/2400f;
		try {
			this.newGame = new Image("pics/menu/newgame.png").getScaledCopy(ratioReso);
			this.newGameSelected = new Image("pics/menu/newgameselected.png").getScaledCopy(ratioReso);
			this.multiplayer= new Image("pics/menu/multiplayer.png").getScaledCopy(ratioReso);
			this.multiplayerSelected= new Image("pics/menu/multiplayerselected.png").getScaledCopy(ratioReso);
			this.options = new Image("pics/menu/options.png").getScaledCopy(ratioReso);
			this.optionsSelected = new Image("pics/menu/optionsselected.png").getScaledCopy(ratioReso);
			this.exit = new Image("pics/menu/exit.png").getScaledCopy(ratioReso);
			this.exitSelected = new Image("pics/menu/exitselected.png").getScaledCopy(ratioReso);
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
		try{
			this.sounds = game.sounds;
			this.title = new Image("pics/menu/goldtitle.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
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
			this.toGame = true;
			this.music.fade(300,0f, true);
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
		g.drawImage(this.title, this.game.resX/2f-this.title.getWidth()/2, 50f+0.05f*this.game.resY-this.title.getHeight()/2);
		g.setColor(Color.white);
		if(multiplaying)
			g.drawString("waiting for someone", this.game.resX/2f-g.getFont().getWidth("waiting for someone")/2f, this.game.resY-g.getFont().getHeight("waiting for someone")-2f);
	}

	public void update(Input i){
		if(multiplaying){
			if(i.isKeyPressed(Input.KEY_ESCAPE))
				multiplaying = false;
			if(this.game.host){
				if(cooldown<=0){
					this.game.toSendConnexions.addElement("2mythe");
					cooldown+=50;
				}else
					cooldown-=1;
				if(this.game.connexions.size()>0){
					game.inMultiplayer = true;
					callItem(0);
					multiplaying = false;
				}
			} else {
				if(this.game.connexions.size()>0){
					this.game.toSendConnexions.addElement("2mythe");
					this.game.toSendConnexions.addElement("2mythe");
					this.game.toSendConnexions.addElement("2mythe");
					try{
						Thread.sleep(5);
					} catch(InterruptedException e) { }
					game.inMultiplayer = true;
					game.currentPlayer = 2;
					callItem(0);
					multiplaying = false;
				}
			}
		}else if(!toGame){
			if(i!=null){
				if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
					callItems(i);
					this.game.sounds.menuItemSelected.play(1f,game.options.soundVolume);
				}
				for(Menu_Item item: this.items){
					item.update(i);
				}			
			}
			Vector<Bullet> toremove = new Vector<Bullet>();
			for(Bullet b: this.bullets){
				if(b.lifePoints<=0)
					toremove.add(b);
			}
			for(Bullet b: toremove)
				this.bullets.remove(b);
		} else {
			this.timeToGame -= 1f;
			if(timeToGame<0f){
				this.music = game.musics.imperial;
				this.music.loop();
				this.music.setVolume(game.options.musicVolume);
				this.game.newGame();
				this.game.quitMenu();
			}
		}
	}
}
