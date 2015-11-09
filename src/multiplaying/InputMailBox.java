package multiplaying;

import java.util.Vector;

import model.Game;

public class InputMailBox {

	Vector<InputObject> inputs;
	Game g;
	
	public InputMailBox(Game g){
		this.inputs = new Vector<InputObject>();
	}
	
	public void validate(int round,int player){
		for(InputObject in : inputs){
			if(in.p.id==player && round==in.round){
				in.validated = true;
			}
		}
	}
	
	public void applyInputs(){
		//TODO :
		// Check if good round to apply and messages validated
		//Good round if current round = message round +2
		//If good round but not validated remove the message
		Vector<InputObject> toRemove = new Vector<InputObject>();
		for(InputObject in: inputs){
			//If good round and validated add it to player inputs to play
			if(this.g.round==(in.round+2) && in.validated  ){
				//ADD inputs in player
				in.p.addToPlay(in.input);
				toRemove.add(in);
			}
			//If good round but not validated, erase the input
			else if(this.g.round==(in.round+2) && !in.validated){
				toRemove.add(in);
			}
			//If too late to play this input, erase the input
			else if(this.g.round>(in.round+2)){
				toRemove.add(in);
			}
		}
		
		//Remove mark as treated inputs
		this.inputs.removeAll(toRemove);
	}
	
	public void addInput(InputObject input){
		this.inputs.addElement(input);
	}
}
