package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import multiplaying.InputModel;

public class MenuIntro extends Menu {

	Vector<Objet> trees = new Vector<Objet>();
	Vector<Bullet> bullets = new Vector<Bullet>();
	float timer = 0f;
	float nextBullet = 1f;
	Image title;

	boolean toGame = false;
	public float timeToGame = 200f;

	public MenuIntro(Game game){
		this.game = game;
		this.items = this.createHorizontalCentered(4, this.game.resX, this.game.resY);
		this.items.get(0).name = "Nouvelle Partie";
		this.items.get(1).name = "Multiplayer";
		this.items.get(2).name = "Options";
		this.items.get(3).name = "Quitter";
		float x1,x2,y1,y2;
		float sizeX = this.game.resX;
		float sizeY = this.game.resY;
		for(int i=0; i<4; i++){
			x1 = sizeX/18f+(float)Math.random()*2f*sizeX/9f;
			x2 = 13f*sizeX/18f+(float)Math.random()*2f*sizeX/9f;
			y1 = sizeY/5f+(float)Math.random()*3f*sizeY/5f;
			y2 = sizeY/5f+(float)Math.random()*3f*sizeY/5f;
			this.trees.add(new Tree(x1,y1,(int)(Math.random()*4f+1f)));
			this.trees.add(new Tree(x2,y2,(int)(Math.random()*4f+1f)));
		}
		Utils.triY(trees);
		try{
			this.sounds = new Sounds();
			title = new Image("pics/Title.png");
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
			this.game.musicMenu.fade(300,0f,true);
			break;
		case 1:
			this.game.menuCurrent = new MenuMulti(this);
			break;
		case 3: 
			this.game.app.exit();
			break;
		default:		
		}
	}

	public void draw(Graphics g){
		if(toGame){
			if(this.timeToGame>80f){
				g.setColor(new Color(0f,0f,0f,0.05f));
			} else {
				if(this.timeToGame>50f){
					g.setColor(new Color(0f,0f,0f,0.5f));
				} else {
					g.setColor(new Color(0f,0f,0f,0.9f));
					
				}
			}
			g.fillRect(0, 0, this.game.resX, this.game.resY);
		} else {
			for(int i=0;i<3;i++){
				for(int j=0;j<2;j++){
					g.drawImage(this.game.background, i*512, j*512);
				}
			}
			Vector<Objet> toDraw = new Vector<Objet>();
			for(Objet o: this.trees)
				toDraw.add(o);
			for(Bullet b: this.bullets)
				toDraw.add(b);
			Utils.triY(toDraw);
			for(Objet o: toDraw){
				o.draw(g);
			}
			for(int i=0; i<this.items.size(); i++){
				this.items.get(i).draw(g);
			}
			g.drawImage(this.title, this.game.resX/2f-200f, 20f);
			g.setColor(Color.black);
			String copyright = "           Copyright 2015           \n- GdB Production / Welcome's Games -";
			g.drawString(copyright, 6.6f*this.game.resX/18f, 4f*this.game.resY/5f);
			//            abcdefghijklmn  abcdefghijklmnopqrstuvwxyabcdefghijk
		}
	}

	public void update(Input i){
		InputModel im = new InputModel(0,0,i);
		this.update(im);
	}
	public void update(InputModel im){
		if(!toGame){
			if(im!=null){
				if(im.isPressedLeftClick)
					callItems(im);
				for(Menu_Item item: this.items)
					item.update(im);				
			}
			this.timer += 0.1f;
			for(Bullet b : this.bullets)
				b.action();
			if(this.timer>this.nextBullet){
				this.timer = 0f;
				float x1 = (float)(Math.random()*this.game.resX), x2 = (float)(Math.random()*this.game.resX);
				float y1 = (float)(Math.random()*this.game.resY), y2 = (float)(Math.random()*this.game.resY);
				boolean arrow = Math.random()>0.5;
				if(x1/this.game.resX>y1/this.game.resY){
					if(x1/this.game.resX+y1/this.game.resY>1f){
						y1 = this.game.resY;
					} else {
						x1 = 0f;
					}
				} else {
					if(x1/this.game.resX+y1/this.game.resY>1f){
						x1 = this.game.resX;
					} else {
						y1 = 0f;
					}
				}
				if(arrow){
					if(x2/this.game.resX>y2/this.game.resY){
						if(x2/this.game.resX+y2/this.game.resY>1f){
							y2 = this.game.resY;
						} else {
							x2 = 0f;
						}
					} else {
						if(x2/this.game.resX+y2/this.game.resY>1f){
							x2 = this.game.resX;
						} else {
							y2 = 0f;
						}
					}
					this.bullets.addElement(new MenuArrow(x1,x2,y1,y2,this));
				} else {
					this.bullets.addElement(new MenuFireball(x1,y1,x2,y2,this));
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
				this.game.newGame();
				this.game.quitMenu();
			}
		}
	}
}
