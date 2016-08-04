package control;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import model.Game;
import tests.Test;


public class InputHandler {

	private Vector<InputObject> inputs;
	public Rectangle selection;
	public Vector<Rectangle> rectangleSelection;
	public Vector<Float> recX;
	public Vector<Float> recY;
	
	Game g;


	public InputHandler(Game g){
		this.inputs = new Vector<InputObject>();
		this.g = g;
	}

	public void validate(int round,int player,int val){
		// 
		
		int idx = 0;
		while(idx<this.inputs.size()){
			if(player==this.inputs.get(idx).idplayer && round==this.inputs.get(idx).round){
				//System.out.println("Input handler line 30 :Validation reussiz for  round "+round+ " "+player);
				this.inputs.get(idx).validate(g.getPlayerById(val));
				break;
			}
			idx++;
		}
	
	}

	public Vector<InputObject> getInputsForRound(int round){
		

		// Check if good round to apply and messages validated
		//Good round if current round = message round +delay
		//If good round but not validated remove the message
		Vector<InputObject> toReturn = new Vector<InputObject>();
		Vector<InputObject> toRemove = new Vector<InputObject>();
		int i = 0;
		while(i<this.inputs.size()){
			InputObject in = this.inputs.get(i);
			//If right round and validated add it to player inputs to play
			if(round==(in.round+Main.nDelay) && in.isValidated()  && in.toPlay){
				//ADD inputs in player
				toReturn.add(in);
				toRemove.add(in);
			} else if (round==(in.round+Main.nDelay) && !in.isValidated()){
				//If right round but not validated, erase the input
				toRemove.add(in);
			} else if (round>(in.round+Main.nDelay)){
				//If too late to play this input, erase the input
				toRemove.add(in);
			}
			i++;
		}
		//Remove mark as treated inputs
		this.inputs.removeAll(toRemove);
		boolean toPlay;
		for(int k=1; k<this.g.players.size(); k++){
			toPlay = false;
			for(InputObject io : toReturn){
				if(io.idplayer==k){
					toPlay = true;
				}
			}
			if(!toPlay){
				
//				System.out.println("Round drop "+this.g.round+" à cause du joueur "+k);
				this.g.toDrawDrop = true;
				return new Vector<InputObject>();				
			}
		}

		return toReturn;
	}
	
	public Vector<InputObject> getInputs(){
		return this.inputs;
	}
	
	public void addToInputs(InputObject io) throws SlickException{
		this.inputs.addElement(io);
		if(Game.tests) Test.testRoundInputHandler(this, g);
	}
}
