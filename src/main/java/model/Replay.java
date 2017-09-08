package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import control.InputObject;
import plateau.Plateau;

public strictfp class Replay implements Serializable{
	private final HashMap<Integer, Vector<InputObject>> ims = new HashMap<Integer, Vector<InputObject>>();
	private byte[] plateau; // First plateau of the game
	private Plateau currentPlateau;
	
	public static void handleReplay(Replay replay, Vector<InputObject> ims, Plateau currentPlateau){
		if(replay.plateau==null){
			replay.plateau = Serializer.serialize(currentPlateau);
		}
		replay.add(currentPlateau.getRound(), ims);
	}
	
	// CREATE REPLAY PART
	public Replay(){
		
	}
	private void add(int round, Vector<InputObject> inputs){
		ims.put(round, inputs);
	}
	
	public void save() throws FileNotFoundException{
		save("lastReplay.rtsreplay");
	}
	public void save(String file) throws FileNotFoundException{
	    FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+"/replays/"+file);
			System.out.println("Saving at : "+System.getProperty("user.dir")+"/replays/"+file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
	}
	
	
	// LOAD REPLAY PART
	public static Replay load(String file) throws FileNotFoundException, ClassNotFoundException{
		FileInputStream fos = new FileInputStream(System.getProperty("user.dir")+"/replays/"+file);
		try {
			ObjectInputStream iis = new ObjectInputStream(fos);
			Replay replay = (Replay) iis.readObject();
			iis.close();
			replay.currentPlateau = (Plateau)Serializer.deserialize(replay.plateau);
			return replay;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Plateau update(){
		// Read plateau if serialized
		if(currentPlateau==null){
			this.currentPlateau = (Plateau) Serializer.deserialize(plateau);
		}
		// Play inputs and return current plateau
		Vector<InputObject> toPlayThisTurn = ims.get(currentPlateau.getRound());
		currentPlateau.update(toPlayThisTurn);
		return currentPlateau;
	}

	public Plateau getCurrentPlateau() {
		// TODO Auto-generated method stub
		return currentPlateau;
	}
	
}
