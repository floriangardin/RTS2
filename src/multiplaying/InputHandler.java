package multiplaying;

import java.util.Vector;

import model.Game;
import model.Player;

public class InputHandler {

	private Vector<InputObject> inputs;
	static int nDelay=3;
	Game g;

	public InputHandler(Game g){
		this.inputs = new Vector<InputObject>();
		this.g = g;
	}

	public void validate(int id,Player player){
		// 
		int idx = 0;
		while(idx<this.inputs.size()){
			if(player.equals(this.inputs.get(idx).player) && id==this.inputs.get(idx).id){
				this.inputs.get(idx).validate(player);
				break;
			}
			idx++;
		}
	}

	public Vector<InputObject> getInputsForRound(int round){
		//TODO :
		// Check if good round to apply and messages validated
		//Good round if current round = message round +2
		//If good round but not validated remove the message
		Vector<InputObject> toReturn = new Vector<InputObject>();
		Vector<InputObject> toRemove = new Vector<InputObject>();
		int cause=0;
		int i = 0;
		while(i<this.inputs.size()){
			InputObject in = this.inputs.get(i);
			//If right round and validated add it to player inputs to play
			if(round==(in.round+nDelay) && in.isValidated()  ){
				//ADD inputs in player
				toReturn.add(in);
				toRemove.add(in);
			}
			//If right round but not validated, erase the input
			else if(this.g.round==(in.round+nDelay) && !in.isValidated()){
				this.g.vroundDropped.addElement(in.id);
				cause = 1;
			}
			//If too late to play this input, erase the input
			else if(this.g.round>(in.round+nDelay)){
				toRemove.add(in);
			}

			i++;
		}
		
		//Remove mark as treated inputs
		this.inputs.removeAll(toRemove);
		if(toReturn.size()>=this.g.plateau.players.size()-1){
			System.out.println("InputHandler line 57: inputs to play in round "+this.g.round);
			return toReturn;
		} else if(cause==1) {
			System.out.println("InputHandler line 60: invalid inputs for input round "+(round-2));
			this.g.roundDropped++;
			this.g.roundDroppedValidate++;
			return new Vector<InputObject>();
		}
		else{
			System.out.println("InputHandler line 60: missing inputs for input round "+(round-2));
			this.g.roundDroppedMissing++;
			this.g.vroundMissing.addElement(this.g.round);
			this.g.roundDropped++;
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
