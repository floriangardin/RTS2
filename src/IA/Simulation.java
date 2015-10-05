package IA;

import java.util.Vector;

import units.Character;
import model.Constants;
import model.Game;
import model.Plateau;
public class Simulation {

	
	Vector<Vector<Character>> armies;

	Plateau p;
	boolean end;
	int victory;
	Report report;
	Game game;
	int framerate ;
	float sizeX = 200;
	float sizeY = 200;
	
	int sizeArmy1;
	int sizeArmy2;
	
	
	public Simulation(){
		//INIT SIZE ARMY
		this.sizeArmy1= (int)Math.random()*10;
		this.sizeArmy2 = (int)((float)Math.random()-0.5f)*10+this.sizeArmy2;
		
		// INIT GAME AND PLATEAU
		this.framerate = 60;
		game = new Game();
		game.setParams(new Constants(framerate),800,600);
		end = false;
		this.p = new Plateau(new Constants(60),sizeX,sizeY,2,new Game());
		//INIT ARMIES
		armies = new Vector<Vector<Character>>();
		armies.add(new Vector<Character>());
		armies.add(new Vector<Character>());
		
		// GENERATE RANDOM ARMY
		for(int i=0 ;i<this.sizeArmy1;i++){
			
		}
		for(int i=0 ;i<this.sizeArmy2;i++){
			
		}


		
		// ADD ARMY IN PLATEAU
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				this.p.addCharacterObjets(new Character(c,(float)Math.random()*this.sizeX,(float)Math.random()*this.sizeY));
				
			}
		}
		
		//LAUNCH SIMULATION
		
		simulate();
		
	}
	
	public void simulate(){
		//Call update until victory or timeout
		while(!end){
			update();
		}
		
		// if victory call report function
		if(end){
			report = new Report(victory,armies.get(victory-1).size());
		}
		
	}
	public void update(){
		//Call action on each character until end of fight
		
		//Update end
		if(this.armies.get(0).size()==0){
			end = true;
			victory = 2;
		}
		else if(this.armies.get(1).size()==0){
			end = true;
			victory = 1;
		}
		
	}
	
	public void report(){
		//Call action on each character until end of fight
	}
	
	public Character generateRandomUnit(){
		
	}
	
	
}
