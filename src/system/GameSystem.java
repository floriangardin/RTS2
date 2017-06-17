package system;

import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import control.InputHandler;
import display.DisplayHandler;
import display.Interface;
import events.EventNames;
import model.Player;
import plateau.Objet;
import plateau.Plateau;

public class GameSystem extends ClassSystem{
	
	public int round;
	public Vector<Player> players;
	public Plateau plateau;
	public Interface bottombar;
	public int currentPlayer;
	public DisplayHandler events;
	public int startTime;
	

	
	public GameSystem(){
		this.round = 0;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		// TODO Auto-generated method stub

		this.handleInterface(im);


		this.handleMinimap(im, player);
		

		InputHandler.updateSelection(ims);
		

		// 3 - handling visibility
		this.updateVisibility();
	}
	
	public void setPlateau(Plateau plateau){
		this.plateau = plateau;
		this.bottombar = new Interface(plateau);
	}
	
	public Player getCurrentPlayer(){
		return this.players.get(currentPlayer);
	}
	
	public int getCurrentTeam(){
		return this.players.get(currentPlayer).getGameTeam().id;
	}
	
	public void triggerEvent(EventNames name,Objet o){
		events.addEvent(name, o);
	}
	public DisplayHandler getEvents(){
		return events;
	}

}
