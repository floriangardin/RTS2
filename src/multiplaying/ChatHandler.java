package multiplaying;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import main.Main;
import menu.Lobby;
import menuutils.Menu_TextScanner;
import model.Game;
import ressources.GraphicElements;
import ressources.Images;
import system.Debug;

public class ChatHandler {

	public static Vector<ChatMessage> messages;
	public static boolean typingMessage;
	public static Menu_TextScanner textScanner;
	public static float startY;
	public static float remainingTimeNotEnoughRoom = 0f;
	public static float remainingTimeBeingAttacked = 0f;

	public static void init(){
		messages = new Vector<ChatMessage>();
		textScanner = new Menu_TextScanner("", 
				Game.resX/2f, Game.resY-2f*GraphicElements.font_main.getHeight("Pg"), 
				Game.resX/3f, GraphicElements.font_main.getHeight("Pg"));
		textScanner.isSelected = true;
		startY = Game.resY/3f;
	}

	public static void action(Input in, InputObject im){
		if(remainingTimeNotEnoughRoom>0f){
			remainingTimeNotEnoughRoom-=Main.increment;
		}
		if(remainingTimeBeingAttacked>0f){
			remainingTimeNotEnoughRoom-=Main.increment;
		}

		Vector<ChatMessage> toRemove = new Vector<ChatMessage>();
		ChatMessage m;
		int k=0;
		while(k<messages.size()){
			m = messages.get(k);
			m.remainingTime--;
			if(m.remainingTime<0){
				toRemove.add(m);
			}
			k++;
		}
		messages.removeAll(toRemove);
		if(im.isPressed(KeyEnum.Enter)){
			typingMessage = !typingMessage;
		}
		if(!typingMessage){
			sendTypedMessage();
			return;
		}
		textScanner.update(in,im);
	}

	public static void draw(Graphics g){
		// Draw messages
		ChatMessage m;
		Font f = g.getFont();
		float height = f.getHeight("Pg");
		int k=0;
		String nickname = "";
		while(k<messages.size()){
			m = messages.get(k);
			g.setColor(m.color);
			if(m.idPlayer!=0){
				nickname = m.nickname+ " : ";
				g.drawString(nickname, 20f, startY+2f*height*k);
			}
			g.setColor(m.colorBody);
			g.drawString(m.message, 20f+f.getWidth(nickname), startY+2f*height*k);
			k++;
		}
		// Draw Chat Bar
		if(typingMessage){
			textScanner.draw(g);
		}
	}

	public static void sendTypedMessage(){
		if(textScanner.s.length()>0){
			// checking if not adding an ip n menumapchoice
			InetAddress ia = null;
			// activate GdB mode
			if(textScanner.s.equals("/gilles") && Debug.gillesModeEnable){
				if(!Images.gillesModeEnable){
					Images.activateGdBMode();
					messages.addElement(new ChatMessage("Gilles de Bouard mode activé",0));
				} else {
					Images.deactivateGdBMode();
					messages.addElement(new ChatMessage("Gilles de Bouard mode desactivé",0));
				}
				//System.out.println("Gilles de Bouard Mode activé");
				textScanner.s="";
				return;
			}
			if(textScanner.s.equals("/marcopolo") && Debug.marcoPoloModeEnable){
//				game.activateMarcoPoloMode();
//				if(Game.g.marcoPolo)
//					messages.addElement(new ChatMessage("Mode Marco Polo activé",0));
//				else
//					messages.addElement(new ChatMessage("Mode Marco Polo desactivé",0));
//				//System.out.println("Gilles de Bouard Mode activé");
//				textScanner.s="";
//				return;
			}
			if(Game.isInMenu()){
				if(Game.menuSystem.currentMenu == Game.menuSystem.menuMapChoice && textScanner.s.contains(".")){
					try {
						ia = InetAddress.getByName(textScanner.s);
						Lobby.addressesInvites.addElement(ia);
						messages.addElement(new ChatMessage("IP ajoutée : " + ia.getHostName(),0));
						textScanner.s="";
					} catch (IOException e) {}
					return;
				}
				//Communications.sendMessage(new ChatMessage(textScanner.s,0));
				//FIXME : Class communication
				textScanner.s="";
			} else {
				//Communications.sendMessage(new ChatMessage(textScanner.s,0));
				textScanner.s="";
			}
		}
	}

}
