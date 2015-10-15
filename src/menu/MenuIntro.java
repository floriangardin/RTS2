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
	boolean toGame = false;
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

	public MenuIntro(Game game){
		try {
			this.music = new Music("music/menuTheme.ogg");
			this.music.setVolume(0.5f);
			this.music.loop();
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.game = game;
		this.game.connexionReceiver.start();
		this.game.connexionSender.start();
		this.items = new Vector<Menu_Item>();
		this.itemsSelected = new Vector<Menu_Item>();
		float startY = 100f+0.1f*this.game.resX;
		float stepY = 0.15f*this.game.resY;

		try {
			this.newGameSelected = new Image("pics/menu/newgameselected.png").getScaledCopy(this.game.resX/1680);
			this.multiplayerSelected= new Image("pics/menu/multiplayerselected.png").getScaledCopy(this.game.resX/1680);
			this.optionsSelected = new Image("pics/menu/optionsselected.png").getScaledCopy(this.game.resX/1680);
			this.exitSelected = new Image("pics/menu/exitselected.png").getScaledCopy(this.game.resX/1680);
			this.newGame = new Image("pics/menu/newgame.png").getScaledCopy(this.game.resX/1680);
			float startX = this.game.resX/2-this.newGame.getWidth()/2;
			this.items.addElement(new Menu_Item(startX,startY,this.newGame,this.newGameSelected,"New Game"));
			this.multiplayer= new Image("pics/menu/multiplayer.png").getScaledCopy(this.game.resX/1680);
			this.items.addElement(new Menu_Item(startX,startY+1*stepY,this.multiplayer,this.multiplayerSelected,"Multiplayer"));
			this.options = new Image("pics/menu/options.png").getScaledCopy(this.game.resX/1680);
			this.items.addElement(new Menu_Item(startX,startY+2*stepY,this.options,this.optionsSelected,"Options"));
			this.exit = new Image("pics/menu/exit.png").getScaledCopy(this.game.resX/1680);
			this.items.addElement(new Menu_Item(startX,startY+3*stepY,this.exit,this.exitSelected ,"Exit"));

		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		float x1,x2,y1,y2;
		float sizeX = this.game.resX;
		float sizeY = this.game.resY;
		//		for(int i=0; i<4; i++){
		//			x1 = sizeX/18f+(float)Math.random()*2f*sizeX/9f;
		//			x2 = 13f*sizeX/18f+(float)Math.random()*2f*sizeX/9f;
		//			y1 = sizeY/5f+(float)Math.random()*3f*sizeY/5f;
		//			y2 = sizeY/5f+(float)Math.random()*3f*sizeY/5f;
		//			this.trees.add(new Tree(x1,y1,(int)(Math.random()*4f+1f)));
		//			this.trees.add(new Tree(x2,y2,(int)(Math.random()*4f+1f)));
		//		}
		Utils.triY(trees);
		try{
			this.sounds = new Sounds();
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
			this.multiplaying = true;
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
				
			} else {
				
			}
		}else if(!toGame){
			if(i!=null){
				if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON))
					callItems(i);
				for(Menu_Item item: this.items)
					item.update(i);				
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
				this.game.newGame();
				this.game.quitMenu();
			}
		}
	}
}
