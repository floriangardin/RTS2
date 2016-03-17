package multiplaying;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import menu.Menu_TextScanner;
import model.Game;

public class ChatHandler {

	public Vector<ChatMessage> messages;
	public Game game;
	public ReentrantLock mutex;
	public boolean typingMessage;
	public Menu_TextScanner textScanner;
	public float startY;


	public ChatHandler(Game game){
		this.game = game;
		this.messages = new Vector<ChatMessage>();
		this.mutex = new ReentrantLock();
		this.textScanner = new Menu_TextScanner("", game.resX/2f, game.resY-2f*game.font.getHeight("Pg"), game.resX/3f, game.font.getHeight("Pg"), game);
		this.textScanner.isSelected = true;
		this.startY = game.resY/3f;
	}

	public void action(Input in, InputObject im){
		//		for(int i=1; i<256; i++){
		//			if(in.isKeyDown(i)){
		//				System.out.println(i);
		//			}
		//		}
		Vector<ChatMessage> toRemove = new Vector<ChatMessage>();
		mutex.lock();
		ChatMessage m;
		int k=0;
		while(k<this.messages.size()){
			m = this.messages.get(k);
			m.remainingTime--;
			if(m.remainingTime<0){
				toRemove.add(m);
			}
			k++;
		}
		this.messages.removeAll(toRemove);
		mutex.unlock();
		if(im.isPressedENTER){
			this.typingMessage = !this.typingMessage;
		}
		if(!this.typingMessage){
			this.sendTypedMessage();
			return;
		}
		textScanner.update(in,im);
	}

	public void draw(Graphics g){
		// Draw messages
		ChatMessage m;
		Font f = g.getFont();
		float height = f.getHeight("Pg");
		int k=0;
		String nickname = "";
		while(k<this.messages.size()){
			m = this.messages.get(k);
			g.setColor(m.color);
			if(m.idPlayer!=0){
				nickname = game.players.get(m.idPlayer).nickname+ " : ";
				g.drawString(nickname, 20f, startY+2f*height*k);
			}
			g.setColor(Color.white);
			g.drawString(m.message, 20f+f.getWidth(nickname), startY+2f*height*k);
			k++;
		}
		// Draw Chat Bar
		if(typingMessage){
			textScanner.draw(g);
		}
	}

	public void sendTypedMessage(){
		if(textScanner.s.length()>0){
			// checking if not adding an ip n menumapchoice
			InetAddress ia = null;
			try {
				ia = InetAddress.getByName(textScanner.s);
				if(Game.g.menuCurrent == Game.g.menuMapChoice && ia!=null){
					Game.g.menuMapChoice.addressesInvites.addElement(ia);
					this.messages.addElement(new ChatMessage("IP ajoutée : " + ia.getHostName(),0));
					textScanner.s="";
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.game.sendMessage(new ChatMessage(textScanner.s,this.game.currentPlayer.id));
			textScanner.s="";
		}
	}

}
