package multiplaying;

import java.util.Vector;

import model.Game;
import model.Player;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;


public class InputHandler {

	private Vector<InputObject> inputs;
	public static int nDelay=4;
	Game g;
	public Lock mutex;

	public InputHandler(Game g){
		this.inputs = new Vector<InputObject>();
		this.g = g;
		mutex = new ReentrantLock();
	}

	public void validate(int round,int player,int val){
		// 
		mutex.lock();
		int idx = 0;
		while(idx<this.inputs.size()){
			if(player==this.inputs.get(idx).player.id && round==this.inputs.get(idx).round){
				//System.out.println("Input handler line 30 :Validation reussiz for  round "+round+ " "+player);
				this.inputs.get(idx).validate(g.getPlayerById(val));
				break;
			}
			idx++;
		}
		mutex.unlock();
	}

	public Vector<InputObject> getInputsForRound(int round){
		
		this.mutex.lock();
		// Check if good round to apply and messages validated
		//Good round if current round = message round +2
		//If good round but not validated remove the message
		Vector<InputObject> toReturn = new Vector<InputObject>();
		Vector<InputObject> toRemove = new Vector<InputObject>();
		int i = 0;
		while(i<this.inputs.size()){
			InputObject in = this.inputs.get(i);
			//If right round and validated add it to player inputs to play
			if(round==(in.round+nDelay) && in.isValidated()  ){
				//ADD inputs in player
				toReturn.add(in);
				toRemove.add(in);
			} else if (round==(in.round+nDelay) && !in.isValidated()){
				//If right round but not validated, erase the input
				toRemove.add(in);
			} else if (round>(in.round+nDelay)){
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
				if(io.player.id==k && io.toPlay){
					toPlay = true;
				}
			}
			if(!toPlay){
				this.mutex.unlock();
				System.out.println("Round drop "+this.g.round);
				return new Vector<InputObject>();				
			}
		}
		this.mutex.unlock();
		return toReturn;
	}
	
	public Vector<InputObject> getInputs(){
		return this.inputs;
	}
	
	public void addToInputs(InputObject io){
		mutex.lock();
		this.inputs.addElement(io);
		mutex.unlock();
	}
}
