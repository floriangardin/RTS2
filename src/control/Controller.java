package control;

import org.newdawn.slick.Input;

import model.Game;

public class Controller {

	/*
	 * Handle selection and all things control related
	 */
	InputModel[] inputs;
	KeyMapper keymapper;
	
	
	public Controller(int nPlayers){
		keymapper = new KeyMapper();
		inputs = new InputModel[nPlayers];
		for(int i = 0; i< inputs.length;i++){
			inputs[i] = new InputModel(i);
		}
	}
	public void getInputs(Input i){
		
		inputs[Game.g.currentPlayer.id].update(i, keymapper);
		for(int idx = 0 ; idx<inputs.length; idx++){
			if(idx!=Game.g.currentPlayer.id){
				/*
				 * Insert multiplayer inputs here or AI
				 */
			}
		}
	}
	public InputModel getCurrentPlayerInputModel(){
		return inputs[Game.g.currentPlayer.id];
	}
	
	
}
