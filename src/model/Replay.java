package model;

import java.util.Vector;

import control.InputObject;
import plateau.Plateau;

public class Replay {
	Vector<InputObject> ims;
	Plateau plateau; // First plateau of the game
	public Replay(Plateau plateau){
		this.plateau = plateau;
	}
	public void save(String file){
		byte[] serializedReplay = Serializer.serialize(this);
		// TODO : Open a binary file and put serializedReplay
	}
}
