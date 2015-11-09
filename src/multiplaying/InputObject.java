package multiplaying;

import model.Game;
import model.Player;

public class InputObject {
	// Contient un input
	// Can be either a validation or an input
	//TODO plug this object with multi receiver and multisender
	public String rawInput;
	public InputMailBox mailBox;
	public InputModel input;
	public int id;
	public int round;
	public boolean validated;
	public Player p;
	public Game g;
	
	public InputObject(Game g,String rawInput,boolean myInputs){
		this.g = g;
		this.mailBox = this.g.inputsBox;
		
		// Should check if this is a validation or a real input
		// I for inputs, V for validation
		
		//If this is not my inputs, inputs are automatically validated
		this.validated = !myInputs;
		this.rawInput = rawInput;
		//If input message
		if(this.rawInput.substring(0, 1).equals("I")){
			String[] inMessage = this.rawInput.split("\\|");
			//0 : I, 1:round 2:player, 3: message
			System.out.println(" raw inputs : " +this.rawInput);
			this.round = Integer.parseInt(inMessage[1]);
			//Not sure it works
			this.p = this.g.getPlayerById(Integer.parseInt(inMessage[2]));
			this.input = new InputModel(inMessage[3]);
			// We add the message into the mailbox
			this.mailBox.addInput(this);
			//We send a validation if it is not our inputs
			if(!myInputs){
				this.sendValidation(this.g.inputSender);
			}
		}
		//If validation message
		else if(this.rawInput.substring(0, 1).equals("V")){
			//Get the corresponding round and player
			String[] valMessage = this.rawInput.split("\\|");
			int round = Integer.parseInt(valMessage[1]);
			int idPlayer = Integer.parseInt(valMessage[2]);
			this.mailBox.validate(round, idPlayer);
		}
		
	}
	
	//If it is my own inputs
	public InputObject(Game g,InputModel input,boolean myInputs){
		this.g = g;
		this.mailBox = this.g.inputsBox;
		this.validated = !myInputs;
		this.p = this.g.getPlayerById(input.idPlayer);
		this.round = input.round;
		//Send input to other players
		g.inputSender.depot.addElement(input.toString());
	}
	
	public void sendValidation(MultiSender sender){
		//TODO Check if it works ?
		sender.depot.addElement("3V"+"|"+this.round+"|"+p.id);
	}
	
	public InputModel getInput(){
		return this.input;
	}
	
	public int getRound(){
		return this.round;
	}
	
}
