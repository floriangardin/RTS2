package control;

import java.util.Vector;

import main.Main;



public class InputHandler {

	public static int nbPlayers;

	public static Vector<InputObject> inputs;

	// Gestion du rectangle de selection


	public static void init(int nbPlayer){
		nbPlayers = nbPlayer;
		inputs = new Vector<InputObject>();
		
	}

	public static void validate(int round,int playerToValidate,int playerWhoValidates){
		// 

		int idx = 0;
		while(idx<inputs.size()){
			if(playerToValidate==inputs.get(idx).idplayer && round==inputs.get(idx).round){
				//System.out.println("Input handler line 30 :Validation reussiz for  round "+round+ " "+player);
				inputs.get(idx).validate(playerWhoValidates);
				break;
			}
			idx++;
		}
	}

	public static Vector<InputObject> getInputsForRound(int round, boolean multi){
		
		if(!multi){
			Vector<InputObject> toReturn = new Vector<InputObject>();
			for(InputObject i : inputs){
				toReturn.add(i);
			}
			inputs.clear();
			return toReturn;
		}
		// Check if good round to apply and messages validated
		//Good round if current round = message round +delay
		//If good round but not validated remove the message
		Vector<InputObject> toReturn = new Vector<InputObject>();
		Vector<InputObject> toRemove = new Vector<InputObject>();
		int i = 0;
		while(i<inputs.size()){
			InputObject in = inputs.get(i);
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
		inputs.removeAll(toRemove);
		boolean toPlay;
		for(int k=0; k<nbPlayers; k++){
			toPlay = false;
			for(InputObject io : toReturn){
				if(io.idplayer==k){
					toPlay = true;
				}
			}
			if(!toPlay){
				//				System.out.println("Round drop "+g.round+" à cause du joueur "+k);
				//				Game.g.toDrawDrop = true;
				return new Vector<InputObject>();				
			}
		}

		return toReturn;
	}

	public static Vector<InputObject> getInputs(){
		return inputs;
	}

	public static void addToInputs(InputObject io){
		inputs.addElement(io);
	}

	public static void addToInputs(InputObject io, boolean toValidate){
		addToInputs(io);
		if(toValidate){
			validate(io.round, io.idplayer, io.idplayer);
		}
	}
}
