package multiplaying;

import model.Game;

public class MultiReceiverInput extends MultiReceiver {

	public MultiReceiverInput(Game g) {
		super(g, g.portInput);

	}
	@Override
	public void action(String msg) {
		System.out.println(msg);
		InputObject io = new InputObject(msg,g);
		if(Game.debugValidation){
			System.out.println("MultiReceiver line 63 input received at round "+ this.g.round);
		}
		//Send the validation for other players if the round is still ok
		if(this.g.round<io.round+InputHandler.nDelay){
			this.g.validationSender.address = this.g.getPlayerById(io.player.id).address;
			this.g.toSendValidation.addElement(io.getMessageValidationToSend());
			this.g.inputsHandler.addToInputs(io);
			io.validate();
		}
	}
}
