package multiplaying;

import model.Game;

public class MultiReceiverValidation extends MultiReceiver {

	public MultiReceiverValidation(Game g) {
		super(g, g.portValidation);
	}

	@Override
	public void action(String msg) {
		//Get the corresponding round and player
		String rawInput = msg.substring(1);

		if(Game.debugValidation){
			System.out.println("MultiReceiver line 69 validation received for round "+ this.g.round);	
		}
		String[] valMessage = rawInput.split("\\|");
		int round = Integer.parseInt(valMessage[1]);
		int idPlayer = Integer.parseInt(valMessage[2]);
		// Ressources partag� le vecteur d'inputs de la mailbox..
		this.g.inputsHandler.validate(round, g.getPlayerById(idPlayer));

	}

}
