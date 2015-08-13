package model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import org.newdawn.slick.Graphics;

import multiplaying.InputModel;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class MenuMulti extends Menu {

	Vector<Objet> trees = new Vector<Objet>();
	Vector<Bullet> bullets = new Vector<Bullet>();
	float timer = 0f;
	float nextBullet = 1f;
	public boolean inHost;
	public boolean inJoin;
	Vector<Menu_Item> mainItems;
	Vector<Menu_Item> hostItems;
	Vector<Menu_Item> joinItems;
	String IP = "";
	InetAddress addressEnemy;
	private float timerMessage = 0f;
	private boolean lock = false;
	private float timeoutConnexion;


	public MenuMulti(MenuIntro m) {
		this.game = m.game;
		this.mainItems = this.createHorizontalCentered(3, this.game.resX, this.game.resY);
		this.mainItems.get(0).name = "Host Game";
		this.mainItems.get(1).name = "Join Game";
		this.mainItems.get(2).name = "Back";
		this.hostItems = this.createHorizontalCentered(4, this.game.resX, this.game.resY);
		this.hostItems.get(0).name = "Host Game";
		this.hostItems.get(1).name = "Your IP: 127.0.0.1 (héhé)"; // TODO
		this.hostItems.get(2).name = "*** no client ***";
		this.hostItems.get(3).name = "Back";
		this.hostItems.get(0).colorAnimation = false;
		this.hostItems.get(1).colorAnimation = false;
		this.hostItems.get(2).colorAnimation = false;
		this.joinItems = this.createHorizontalCentered(4, this.game.resX, this.game.resY);
		this.joinItems.get(0).name = "Join Game";
		this.joinItems.get(1).name = "IP: "+IP;
		this.joinItems.get(2).name = "*** no game found ***";
		this.joinItems.get(3).name = "Back";
		this.joinItems.get(0).colorAnimation = false;
		this.joinItems.get(1).colorAnimation = false;
		this.joinItems.get(2).colorAnimation = false;
		this.items = this.mainItems;
		this.trees = m.trees;
		this.bullets = m.bullets;
		this.sounds = m.sounds;

	}

	public void callItem(int i){
		if(inHost){
			switch(i){
			case 2:
				this.items = this.mainItems;
				this.inHost = false;
			default:
			}
		} else if(inJoin){
			switch(i){
			case 2:
				this.items = this.mainItems;
				this.inJoin = false;
			default:
			}
		} else {
			switch(i){
			case 0:

				//				this.game.inMultiplayer = true;
				//				this.game.isHost = true;
				//				this.game.startTime = System.currentTimeMillis();
				//				this.game.inputReceiver.start();
				//				this.game.outputSender.start();
				//				this.game.newGame();
				//				this.game.quitMenu();
				this.setInHost(true);
				break;
			case 1: 
				//				this.game.inMultiplayer = true;
				//				this.game.startTime = System.currentTimeMillis();
				//				this.game.inputSender.start();
				//				this.game.outputReceiver.start();
				//				this.game.currentPlayer = 1;
				//				this.game.newGame();
				//				this.game.quitMenu();
				this.setInJoin(true);
				break;
			case 2:
				this.game.menuCurrent = this.game.menuIntro;
				break;
			default:		
			}
		}
	}

	public void draw(Graphics g){
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
	}

	public void update(InputModel im){
		this.timerMessage=Math.max(this.timerMessage-1f, 0f);
		this.timeoutConnexion=Math.max(this.timeoutConnexion-1f, 0f);
		if(timerMessage==0f && timeoutConnexion==0f){
			lock = false;
			this.joinItems.get(2).name = "*** no game found ***";
		}
		if(timeoutConnexion>0f){
			if(this.game.connexions.size()>0){
				// TODO connexion established
				this.joinItems.get(2).name = this.game.connexions.get(0);
			}
		}
		if(im!=null){
			for(Menu_Item item: this.items)
				item.update(im);
			if(!lock){

				this.joinItems.get(1).name = "IP: " + IP;
				if(im.isPressedLeftClick)
					callItems(im);
				if(im.isPressedESC){
					if(inJoin){
						this.setInJoin(false);
					} else if (inHost){
						this.setInHost(false);
					} else {
						this.game.menuCurrent = this.game.menuIntro;					
					}
				}
				if(inJoin){
					for(int i=0; i<10; i++){
						if(im.isPressedNumPad[i] && this.IP.length()<16){
							if(i==9)
								this.IP += "0";
							else
								this.IP += (i+1);
						}
					}
					if(im.isPressedENTER){
						this.sendRequest(IP);
					}
					if(im.isPressedBACK && this.IP.length()>0){
						this.IP = this.IP.substring(0, IP.length()-1);
					}
					if(im.isPressedDOT && this.IP.length()<16){
						this.IP += ".";
					}
				}
			}
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
	}

	public void setInHost(boolean inHost){
		this.inHost = inHost;
		if(inHost){
			this.game.connexionReceiver.start();
			this.items = this.hostItems;
		} else {
			this.game.connexionReceiver.server.disconnect();
			this.game.connexionReceiver.server.close();
			this.game.connexionReceiver.stop();
			this.items = this.mainItems;
		}
	}
	public void setInJoin(boolean inJoin){
		this.inJoin = inJoin;
		if(inJoin){
			this.items = this.joinItems;
		} else {
			if(this.game.connexionSender!=null && this.game.connexionSender.isAlive())
				this.game.connexionSender.interrupt();
			this.game.connexionReceiver.server.disconnect();
			this.game.connexionReceiver.server.close();
			this.game.connexionReceiver.stop();
			this.items = this.mainItems;
		}
	}
	public void sendRequest(String IP){
		try {
			InetAddress ia = InetAddress.getByName("localhost");
			String s = "2connexion?";
			this.game.connexionSender = new MultiSender(this.game.app,this.game,ia,2344,this.game.toSendConnexions);
			this.game.connexionReceiver.start();
			this.game.connexionSender.start();
			this.game.toSendConnexions.add(s);
			while(this.game.toSendConnexions.size()>0){try{Thread.sleep(100);}catch(InterruptedException e){}}
			this.game.connexionSender.interrupt();
			this.joinItems.get(2).name = "Request sent";
			this.timeoutConnexion = 600f;
			this.lock = true;
		} catch (UnknownHostException e) {
			this.joinItems.get(1).name = "Invalid IP";
			this.timerMessage  = 120f;
			this.IP = "";
			this.lock = true;
			System.out.println("invalid ip");
		}
	}
}
