package menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import org.newdawn.slick.Graphics;

import bullets.Bullet;
import model.Objet;
import model.Utils;
import multiplaying.ConnectionModel;
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
	String localIP;
	InetAddress addressEnemy;
	InetAddress addressLocal;
	private float timerMessage = 0f;
	private boolean lock = false;
	private float timeoutConnexion;
	private boolean waitingForReady = false;
	private boolean waitingForStart = false;


	public MenuMulti(MenuIntro m) {
		this.game = m.game;
		try {
			localIP= InetAddress.getLocalHost ().getHostAddress ();
			addressLocal = InetAddress.getLocalHost ();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		this.mainItems = this.createHorizontalCentered(3, this.game.resX, this.game.resY);
		this.mainItems.get(0).name = "Host Game";
		this.mainItems.get(1).name = "Join Game";
		this.mainItems.get(2).name = "Back";
		this.hostItems = this.createHorizontalCentered(4, this.game.resX, this.game.resY);
		this.hostItems.get(0).name = "Host Game";
		this.hostItems.get(1).name = "Your IP: "+localIP; // TODO
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
			case 3:
				if(waitingForStart){
					this.sendMessage("2start");
					this.initMulti(true);
					this.game.inMultiplayer = true;
					this.game.isHost = true;
					this.game.startTime = System.currentTimeMillis();
					this.game.outputReceiver.start();
					this.game.outputSender.start();
					this.game.quitMenu();
				} else {
					this.setInHost(false);
				}
			default:
			}
		} else if(inJoin){
			switch(i){
			case 3:
				if(waitingForReady){
					this.sendMessage("2ready");
					this.joinItems.get(3).name = "Waiting for host";
					this.timeoutConnexion = 600f;
				} else{
					this.setInJoin(false);
				}
			default:
			}
		} else {
			switch(i){
			case 0:
				this.setInHost(true);
				break;
			case 1: 
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
		if(inJoin){
			if(lock && timerMessage==0f && timeoutConnexion==0f && !waitingForReady){
				lock = false;
				this.joinItems.get(2).name = "*** no game found ***";
			}
			if(this.game.connexions.size()>0){
				// TODO connection established
				if(!waitingForReady){
					ConnectionModel cm = new ConnectionModel(this.game.connexions.get(0));
					this.joinItems.get(2).name = cm.ia.getHostAddress();
					this.addressEnemy = cm.ia;
					this.game.newGame(cm);
					this.joinItems.get(3).name = "ready";
					this.waitingForReady = true;
					this.game.connexions.clear();
					this.lock = true;
				} else {
					this.initMulti(false);
					this.game.inMultiplayer = true;
					this.game.startTime = System.currentTimeMillis();
					this.game.outputSender.start();
					this.game.outputReceiver.start();
					this.game.outputReceiver.setLock = true;
					this.game.currentPlayer = 2;
					this.game.quitMenu();
				}
			}
		} else if (inHost){
			if(this.game.connexions.size()>0){
				// TODO connection established
				if(!waitingForReady){
					this.hostItems.get(2).name = "client: " + this.game.connexions.get(0);
					try {
						this.addressEnemy = InetAddress.getByName(this.game.connexions.get(0));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					//System.out.println("client recu: " +addressEnemy.getHostName());
					this.game.connexions.clear();
					this.game.newGame(true);
					this.game.toSendConnexions.clear();
					this.game.connexionSender = new MultiSender(this.game.app, this.game, addressEnemy, this.game.portConnexion, this.game.toSendConnexions);
					this.game.connexionSender.start();
					ConnectionModel cm = new ConnectionModel(addressLocal,0);
					this.game.toSendConnexions.add(cm.toString());
					try{Thread.sleep(100);}catch(InterruptedException e){}
					this.game.connexionSender.interrupt();
					this.waitingForReady  = true;
					this.lock = true;
					this.hostItems.get(3).name = "Waiting for the client";
				} else {
					this.hostItems.get(3).name = "Start";
					this.waitingForStart = true;
					this.lock = false;
				}
			}
		}
		if(im!=null){
			for(Menu_Item item: this.items)
				item.update(im);
			if(!lock){

				this.joinItems.get(1).name = "IP: " + IP;
				if(im.isPressedLeftClick)
					callItems(im);
				if(im.isPressedESC && !waitingForStart){
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
					if(im.isPressedBACK && this.IP.length()>0){
						this.IP = this.IP.substring(0, IP.length()-1);
					}
					if(im.isPressedDOT && this.IP.length()<16){
						this.IP += ".";
					}
					if(im.isPressedENTER){
						this.sendRequest(IP);
					}
				}
			}
			if(inJoin && waitingForReady){
				if(im.isPressedLeftClick)
					callItems(im);
				if(im.isPressedENTER){
					callItem(3);
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
			this.game.connexionReceiver = new MultiReceiver(this.game, this.game.portConnexion);
			this.game.connexionReceiver.start();
			this.items = this.hostItems;
		} else {
			this.game.connexionReceiver.server.close();
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
			if(this.game.connexionReceiver.isAlive()){
				this.game.connexionReceiver.server.disconnect();
				this.game.connexionReceiver.server.close();
			}
			this.items = this.mainItems;
		}
	}
	public void sendRequest(String IP){
		try {
			InetAddress ia = InetAddress.getByName(IP);
			this.game.connexionSender = new MultiSender(this.game.app,this.game,ia,this.game.portConnexion,this.game.toSendConnexions);
			this.game.connexionReceiver = new MultiReceiver(this.game, this.game.portConnexion);
			this.game.connexionReceiver.start();
			this.game.connexionSender.start();
			this.game.toSendConnexions.add("2"+localIP);
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
		}
	}
	public void sendMessage(String msg){
		InetAddress ia = addressEnemy;
		this.game.connexionSender = new MultiSender(this.game.app,this.game,ia,this.game.portConnexion,this.game.toSendConnexions);
		this.game.connexionSender.start();
		this.game.toSendConnexions.add("2"+msg);
		while(this.game.toSendConnexions.size()>0){try{Thread.sleep(100);}catch(InterruptedException e){}}
		this.game.connexionSender.interrupt();
		this.lock = true;
	}
	public void initMulti(boolean isHost){
		if(isHost){
			this.game.addressClient = addressEnemy;
			this.game.addressHost = addressLocal;
		} else {
			this.game.addressClient = addressLocal;
			this.game.addressHost = addressEnemy;			
		}
		this.game.outputSender = new MultiSender(this.game.app,this.game,game.addressClient, this.game.portOutput, this.game.toSendOutputs);
	}
}
