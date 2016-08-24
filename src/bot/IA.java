package bot;

import java.util.Vector;

import model.Game;
import model.GameTeam;
import model.Objet;
import model.Player;
import units.*;
import units.Character;
public class IA extends Player {
	
	/*
	 * Tous les objectifs sont réalisés en même temps mais ne peut réalisé qu'un objectif à la fois
	 */
		
	public IA(int id, String name, GameTeam gameteam) {
		super(id, name, gameteam);
		
	}

	Vector<Objectif> strategy; // Liste de tous les objectifs
		
	public int team;
	
	/*
	 * Renvoie un objectif à réaliser 
	 */
	public Objectif findObjective(){
		return null;
	}
	
	/*
	 * Assign people to missions
	 */
	public void assign(){
		
		return;
	}
	
	public Vector<Character> getUnits(){
		return Game.g.plateau.characters;
	}
	
	public Vector<Objet> get(String query){
		/*
		 * Create A Json query 
		 */
		Vector<Objet> result = new Vector<Objet>();
		
		
		return null;
		
	}
	
}
