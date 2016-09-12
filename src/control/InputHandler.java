package control;

import java.util.Vector;

import main.Main;
import model.Game;
import model.Objet;
import model.Player;

import org.newdawn.slick.SlickException;

import tests.Test;


public class InputHandler {

	private Vector<InputObject> inputs;
	
	public Vector<Selection> selection;
	// Gestion du rectangle de selection


	public InputHandler(){
		this.inputs = new Vector<InputObject>();
		this.selection = new Vector<Selection>();
	}
	
	public void initSelection(){
		for(Player p : Game.g.players){
			this.selection.addElement(new Selection(p.id));
		}
	}
	
	public Selection getSelection(int player){
		for(Selection s  : selection){
			if(s.player==player){
				return s;
			}
		}
		return null;
	}

	public void validate(int round,int player,int val){
		// 
		
		int idx = 0;
		while(idx<this.inputs.size()){
			if(player==this.inputs.get(idx).idplayer && round==this.inputs.get(idx).round){
				//System.out.println("Input handler line 30 :Validation reussiz for  round "+round+ " "+player);
				this.inputs.get(idx).validate(Game.g.getPlayerById(val));
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
		for(int k=1; k<Game.g.players.size(); k++){
			toPlay = false;
			for(InputObject io : toReturn){
				if(io.idplayer==k){
					toPlay = true;
				}
			}
			if(!toPlay){
				
//				System.out.println("Round drop "+this.g.round+" à cause du joueur "+k);
				Game.g.toDrawDrop = true;
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
		if(Game.tests) Test.testRoundInputHandler(this, Game.g);
	}

	public void updateSelection(Vector<InputObject> ims) {
		
		for(InputObject im : ims){
			updateSelection(im);
		}
	}
	
	public void updateSelection(InputObject im){
		
		this.getSelection(im.idplayer).handleSelection(im);
		/// Put it in input model
		for(Objet o :this.getSelection(im.idplayer).selection){
			im.selection.add(o.id);
		}

	}
	
	public void setSelection(int idPlayer, Vector<Integer> selection){
		for(Selection s: this.selection){
			if(idPlayer == s.player){
				s.selection.clear();
				for(Integer i : selection){
					s.selection.addElement(Game.g.plateau.getById(i));
				}
			}
		}
	}
}
