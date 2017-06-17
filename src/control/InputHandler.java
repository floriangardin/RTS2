package control;

import java.util.Vector;

import main.Main;
import model.Game;
import model.Player;
import plateau.Objet;

import org.newdawn.slick.SlickException;

import tests.Test;


public class InputHandler {

	private static Vector<InputObject> inputs;
	
	public static Vector<Selection> selection;
	// Gestion du rectangle de selection


	public static void init(){
		inputs = new Vector<InputObject>();
		selection = new Vector<Selection>();
	}
	
	public static void initSelection(){
		for(Player p : Game.g.players){
			selection.addElement(new Selection(p.id));
		}
	}
	
	public static Selection getSelection(int player){
		for(Selection s  : selection){
			if(s.player==player){
				return s;
			}
		}
		return null;
	}

	public static void validate(int round,int player,int val){
		// 
		
		int idx = 0;
		while(idx<inputs.size()){
			if(player==inputs.get(idx).idplayer && round==inputs.get(idx).round){
				//System.out.println("Input handler line 30 :Validation reussiz for  round "+round+ " "+player);
				inputs.get(idx).validate(Game.g.getPlayerById(val));
				break;
			}
			idx++;
		}
	
	}

	public static Vector<InputObject> getInputsForRound(int round){
		

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
		for(int k=1; k<Game.g.players.size(); k++){
			toPlay = false;
			for(InputObject io : toReturn){
				if(io.idplayer==k){
					toPlay = true;
				}
			}
			if(!toPlay){
				
//				System.out.println("Round drop "+g.round+" à cause du joueur "+k);
				Game.g.toDrawDrop = true;
				return new Vector<InputObject>();				
			}
		}

		return toReturn;
	}
	
	public static Vector<InputObject> getInputs(){
		return inputs;
	}
	
	public static void addToInputs(InputObject io) throws SlickException{
		inputs.addElement(io);
		if(Game.tests) Test.testRoundInputHandler(this, Game.g);
	}

	public static void updateSelection(Vector<InputObject> ims) {
		
		for(InputObject im : ims){
			if(im.idplayer!=Game.g.currentPlayer.id){	
				setSelection(im.idplayer, im.selection);
			}
		}
	}
	
	public static void updateSelection(InputObject im){
		
		getSelection(im.idplayer).handleSelection(im);
		/// Put it in input model
		for(Objet o :getSelection(im.idplayer).selection){
			im.selection.add(o.id);
		}

	}
	
	public static void setSelection(int idPlayer, Vector<Integer> selection){
		for(Integer s: selection){
			if(idPlayer == s.player){
				s.selection.clear();
				for(Integer i : selection){
					s.selection.addElement(Game.gameSystem.plateau.getById(i));
				}
			}
		}
	}
}
