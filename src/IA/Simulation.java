package IA;

import java.util.Vector;

import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;
import units.UnitTest;
import model.Constants;
import model.Game;
import model.Plateau;
public class Simulation {

	
	public Vector<Vector<Character>> armies;
	
	public Vector<Vector<Character>> armiesInitial;
	public Plateau p;
	public boolean end;
	public int victory;
	public Report report;
	public Game game;
	public int framerate ;
	public float sizeX = 200;
	public float sizeY = 200;
	
	public int sizeArmy1;
	public int sizeArmy2;
	public int timeout;
	
	public Simulation(Game game){
		//INIT SIZE ARMY
		this.sizeArmy1= (int)Math.random()*10;
		this.sizeArmy2 = (int)((float)Math.random()-0.5f)*10+this.sizeArmy2;
		
		// INIT GAME AND PLATEAU
		this.framerate = 60;
		this.game = game;

		end = false;
		this.p = new Plateau(new Constants(60),sizeX,sizeY,2,this.game);
		//INIT ARMIES
		armies = new Vector<Vector<Character>>();
		armies.add(new Vector<Character>());
		armies.add(new Vector<Character>());
		
		// GENERATE RANDOM ARMY
		for(int i=0 ;i<this.sizeArmy1;i++){
			this.armies.get(0).add(generateRandomUnit(1));
		}
		for(int i=0 ;i<this.sizeArmy2;i++){
			this.armies.get(0).add(generateRandomUnit(2));
		}


		
		// ADD ARMY IN PLATEAU
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				this.p.addCharacterObjets(new Character(c,(float)Math.random()*this.sizeX,(float)Math.random()*this.sizeY));
				
			}
		}
		
		
		
		
		
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
		for(Character c : this.p.characters){
			c.action();
		}
		// Remove characters if death
		Vector<Character> toRemove = new Vector<Character>();
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				if(c.lifePoints<=0){
					toRemove.add(c);
				}
				
			}
		}
		
		for(Character c : toRemove){
			if(armies.get(0).contains(c)){
				armies.get(0).remove(c);
			}
			if(armies.get(1).contains(c)){
				armies.get(1).remove(c);
			}
		}
		
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
	
	public Character generateRandomUnit(int team){
		int n_units = 5;
		Character c;
		int i = (int) Math.random()*n_units;
		switch(i){
		case 0:
			c =  new UnitSpearman(this.p,this.game.players.get(team),this.game.players.get(team).data);	
			
			break;
		case 1:
			c = new UnitKnight(this.p,this.game.players.get(team),this.game.players.get(team).data);	
			
			break;
		case 2:
			c =  new UnitPriest(this.p,this.game.players.get(team),this.game.players.get(team).data);
			
			break;	
		case 3:
			c =  new UnitCrossbowman(this.p,this.game.players.get(team),this.game.players.get(team).data);
			
			break;	
		case 4:
			c =  new UnitInquisitor(this.p,this.game.players.get(team),this.game.players.get(team).data);
			
			break;
		case 5:
			c = new UnitArchange(this.p,this.game.players.get(team),this.game.players.get(team).data);
			
			break;
		
		default:
			c = new UnitSpearman(this.p,this.game.players.get(team),this.game.players.get(team).data);	
		}
		return c;
		
	}
	
	
	
}
