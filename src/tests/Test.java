package tests;

import java.util.Vector;

import control.InputHandler;
import control.InputObject;
import main.Main;
import model.Game;

public class Test {
	
	public static int dernierRoundRecu = 0;
	public static int nbMessageRecu = 0;
	public static long reveilSender = 0L;
	
	public static void testRoundCorrect(Game g, Vector<InputObject> ims) throws FatalGillesError{
		/**
		 * Teste si tous les rounds envoyés dans ims correspondent au bon round dans game
		 * appelé dans l'update de Game avant l'update de plateau
		 */
		
		for(InputObject io : ims){
			if(io.round+Main.nDelay!=g.round){
				throw new FatalGillesError("roundCorrect");
			}
		}
	}
	public static void testNombreInput(Game g, Vector<InputObject> ims) throws FatalGillesError{
		/**
		 * Teste si le nombre d'inputs vaut bien soit 0 soit le nombre de joueurs
		 * appelé dans l'update de Game avant l'update de plateau
		 */
		if(!(ims.size()==g.players.size() || ims.size()==0)){
			throw new FatalGillesError("nombre d'input incorrect");
		}
	}
	public static void testRoundInputHandler(InputHandler ih, Game g) throws FatalGillesError{
		/**
		 * Teste si tous les inputs dans l'inputHandler ne sont pas outdatés
		 * appelé à chaque nouvel input ajouté au handler
		 */
		for(InputObject io : ih.getInputs()){
			if(io.round+Main.nDelay+1<g.round)
				throw new FatalGillesError("messages non supprimés");
		}
	}
	
	public static void testOrderedMessages(int round) throws FatalGillesError{
		/**
		 * Teste si le message reçu est bien de round supérieur au dernier message reçu
		 * appelé à la réception de chaque message en jeu
		 */
		if(dernierRoundRecu!=0 && dernierRoundRecu>=round){
			throw new FatalGillesError("message non ordoné:\n    reçu : " + round+" \n    et dernier : " + dernierRoundRecu);
		}
		dernierRoundRecu = round;
	}
	public static void testNombreMessagesRecus(int round) throws FatalGillesError{
		/**
		 * Teste si tous les messages sont reçus à une tolérance près
		 * appelé à la réception de chaque message en jeu
		 */
		int seuil = 3, diff =Math.abs(round - nbMessageRecu); 
		if(diff>seuil){
			throw new FatalGillesError("messages non reçus : \n     au moins " + diff+" messages perdus");
		}
		nbMessageRecu++;
	}
	public static void testIfInputInMessage(String message) throws FatalGillesError{
		/**
		 * Teste si le message contient bien un input
		 * appelé à la réception de chaque message en jeu
		 */
		if(Game.g.round<5)
			return;
		String[] tab = message.split("\\%");
		boolean ok = false;
		for(int i =0; i<tab.length;i++){
			if(tab[i].length()>0 && tab[i].substring(0,1).equals("1")){
				ok = true;
				break;
			}
		}
		if(!ok){
			throw new FatalGillesError("pas d'input dans le message \n      message : "+message+"\n au round : "+ Game.g.round);
		}
	}
	public static void testReveilSender(long time) throws FatalGillesError{
		if(Game.g.isInMenu){
			return;
		}
		if(reveilSender!=0L && Math.abs(reveilSender - time)>16000L){
			throw new FatalGillesError("Sender en panne de réveil");
		}
		reveilSender = time;
	}
	public static void testSendEmptyMessages(String message) throws FatalGillesError{
		if(message.length()==0)
			throw new FatalGillesError("tentative d'envoi de message vide");
	}
	public static void testReceiveEmptyMessage(String message) throws FatalGillesError{
		boolean b = true;
		for(int i=0; i<message.length()-1; i++){
			b = b && message.charAt(i) == message.charAt(i+1);
			if(!b)
				break;
		}
		if(b)
			throw new FatalGillesError("reception d'un message vide");
	}
	
}
