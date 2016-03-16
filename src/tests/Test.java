package tests;

import java.util.Vector;

import main.Main;
import model.Game;
import multiplaying.InputHandler;
import multiplaying.InputObject;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class Test {
	
	public static void testRoundCorrect(Game g, Vector<InputObject> ims) throws FatalGillesError{
		for(InputObject io : ims){
			if(io.round+Main.nDelay!=g.round){
				throw new FatalGillesError("roundCorrect");
			}
		}
	}
	public static void testNombreInput(Game g, Vector<InputObject> ims) throws FatalGillesError{
		if(!(ims.size()==g.players.size() || ims.size()==0)){
			throw new FatalGillesError("nombre d'input incorrect");
		}
	}
	public static void testRoundInputHandler(InputHandler ih, Game g) throws FatalGillesError{
		for(InputObject io : ih.getInputs()){
			if(io.round+Main.nDelay+1<g.round)
				throw new FatalGillesError("messages non supprimés");
		}
	}
	public static void testSizeSender(MultiSender ms) throws FatalGillesError{
		if(ms.depot.size()>2)
			throw new FatalGillesError("sender saturé");
	}
	public static void testDelayReceiver(MultiReceiver mr) throws FatalGillesError{
		if(!mr.g.isInMenu && System.currentTimeMillis()-mr.tempsReception>(Main.nDelay*1000f/Main.framerate) && mr.nbReception>10){
			throw new FatalGillesError("délai d'attente dépassé");
		}
	}
}
