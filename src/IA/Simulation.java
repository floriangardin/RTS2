package IA;

import java.util.Vector;

import buildings.Building;
import bullets.Bullet;
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
import model.Utils;
import spells.SpellEffect;
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
		this.sizeArmy1= (int)(Math.random()*10);
		this.sizeArmy2 = (int)(((float)Math.random()-0.5f)*10)+this.sizeArmy1;
		
		// INIT GAME AND PLATEAU
		this.framerate = 60;
		this.game = game;

		end = false;
		this.p = new Plateau(new Constants(this.framerate),sizeX,sizeY,2,this.game);
		//INIT ARMIES
		armies = new Vector<Vector<Character>>();
		armies.add(new Vector<Character>());
		armies.add(new Vector<Character>());
		
		// GENERATE RANDOM ARMY
		for(int i=0 ;i<this.sizeArmy1;i++){
			this.armies.get(0).add(generateRandomUnit(1));
			this.armies.get(0).lastElement().x = (float)Math.random()*this.sizeX;
			this.armies.get(0).lastElement().y = (float)Math.random()*this.sizeY;
		}
		for(int i=0 ;i<this.sizeArmy2;i++){
			this.armies.get(1).add(generateRandomUnit(2));
			this.armies.get(1).lastElement().x = (float)Math.random()*this.sizeX;
			this.armies.get(1).lastElement().y = (float)Math.random()*this.sizeY;
		}


		
		// ADD ARMY IN PLATEAU
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				new Character(c,c.x,c.y);
				
			}
		}

		System.out.println("new configuration:");
		System.out.println("army 1: "+armies.get(0).size());
		System.out.println("army 2: "+armies.get(1).size());
		//Utils.printCurrentState(p);
		
		
		
	}
	
	public void simulate(){
		//Call update until victory or timeout
		end = false;
		while(!end){
			update();
			Utils.printCurrentState(p);
		}
		
		// if victory call report function
		if(end){
			System.out.println(victory+" "+armies.get(victory-1).size());
			report = new Report(victory,armies.get(victory-1).size());
		}
		
	}
	
	public void update(){
		//Call action on each character until end of fight
		this.p.collision();
		this.p.clean();
		this.p.action();

		// 4 - Update the visibility
		for(Character c:this.p.characters)
			c.visibleByCurrentPlayer = this.p.isVisibleByPlayer(1, c);
		for(Building b:this.p.buildings)
			b.visibleByCurrentPlayer = this.p.isVisibleByPlayer(1, b);
		for(Bullet b:this.p.bullets)
			b.visibleByCurrentPlayer = this.p.isVisibleByPlayer(1, b);
		for(SpellEffect b:this.p.spells)
			b.visibleByCurrentPlayer = this.p.isVisibleByPlayer(1, b);


		// Remove characters if death
		Vector<Character> toRemove = new Vector<Character>();
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				if(c.lifePoints<=0){
					toRemove.add(c);
					System.out.println("death: "+c.team);
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
