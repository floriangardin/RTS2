package multiplaying;

import java.util.Vector;

import model.Game;
import model.Player;

public class InputHandler {

	private Vector<InputObject> inputs;
	Game g;

	public InputHandler(Game g){
		this.inputs = new Vector<InputObject>();
		this.g = g;
	}

	public void validate(int round,Player player){
		// 
		
		for(InputObject in : inputs){
			if(player.equals(in.player) && round==in.round){
				in.validate(player);
			}
		}
	}

	public Vector<InputObject> getInputsForRound(int round){
		//TODO :
		// Check if good round to apply and messages validated
		//Good round if current round = message round +2
		//If good round but not validated remove the message
		Vector<InputObject> toReturn = new Vector<InputObject>();
		Vector<InputObject> toRemove = new Vector<InputObject>();
		int i = 0;
		while(i<this.inputs.size()){
			InputObject in = this.inputs.get(i);
			//If right round and validated add it to player inputs to play
			if(round==(in.round+2) && in.isValidated()  ){
				//ADD inputs in player
				toReturn.add(in);
				toRemove.add(in);
			}
			//If right round but not validated, erase the input
			else if(this.g.round==(in.round+2) && !in.isValidated()){
				toRemove.add(in);
			}
			//If too late to play this input, erase the input
			else if(this.g.round>(in.round+2)){
				toRemove.add(in);
			}
			i++;
		}
		//Remove mark as treated inputs
		this.inputs.removeAll(toRemove);
		if(toReturn.size()==this.g.plateau.players.size()-1){
			System.out.println("InputHandler line 57: inputs to play in round "+this.g.round);
			return toReturn;
		} else {
			System.out.println("InputHandler line 60: invalid or missing inputs");
			return new Vector<InputObject>();
		}
	}
	
	public Vector<InputObject> getInputs(){
		return this.inputs;
	}
	
	public void addToInputs(InputObject io){
		this.inputs.addElement(io);
	}
}
