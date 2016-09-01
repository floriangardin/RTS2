package bot;

import java.util.Vector;
import java.util.stream.Stream;

import buildings.Building;
import model.Game;
import model.GameTeam;
import model.Player;
import units.Character;
public class IA extends Player {
	
	/*
	 * Tous les objectifs sont réalisés en même temps mais ne peut réalisé qu'un objectif à la fois
	 */
	Stream<Character> units;
	Stream<Building> buildings;
		
	public IA(int id, String name, GameTeam gameteam) {
		super(id, name, gameteam);
		
	}

	Vector<Objectif> strategy; // Liste de tous les objectifs
		

	
	/*
	 * Renvoie un objectif à réaliser 
	 */
	public Objectif findObjective(){
		return null;
	}
	
	/*
	 * Method always called
	 */
	public void action(){
		this.units = Game.g.plateau.characters.stream();
		this.buildings = Game.g.plateau.buildings.stream();
		this.update();
	}
	
	public void update(){
		// Model with orders
	}
	
	/*
	 * Assign people to missions
	 */
	public void assign(){
		// Get allies
		Stream<Character> allies = units.filter(c -> c.getGameTeam().id == this.getGameTeam().id);
		Stream<Character> ennemies = units.filter(c -> c.getGameTeam().id != this.getGameTeam().id);
		Stream<Building> ennemyBuildings = buildings.filter(c -> c.getGameTeam().id == this.getGameTeam().id);
		Stream<Building> AllyBuildings= buildings.filter(c -> c.getGameTeam().id != this.getGameTeam().id);
		
		return;
	}
	
	public Vector<Character> getUnits(){
		return Game.g.plateau.characters;
	}
	

	

	
}
